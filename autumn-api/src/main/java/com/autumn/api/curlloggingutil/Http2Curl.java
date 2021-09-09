package com.autumn.api.curlloggingutil;

/*-
 * #%L
 * autumn-api
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.restassured.internal.multipart.RestAssuredMultiPartEntity;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Generates CURL command for a given HTTP request.
 */
public class Http2Curl {

    private static final Logger log = LoggerFactory.getLogger(Http2Curl.class);

    private static final List<String> nonBinaryContentTypes = Arrays.asList("application/x-www-form-urlencoded",
            "application/json");

    /**
     * Generates single-line CURL command for a given HTTP request.
     *
     * @param request HTTP request
     * @return CURL command
     * @throws Exception if failed to generate CURL command
     */
    public static String generateCurl(HttpRequest request) throws Exception {
        return generateCurl(request, false);
    }

    /**
     * Generates CURL command for a given HTTP request.
     *
     * @param request HTTP request
     * @param printMultiliner if {@code true} breaks command into lines for better legibility
     * @return CURL command
     * @throws Exception if failed to generate CURL command
     */
    public static String generateCurl(HttpRequest request, boolean printMultiliner) throws Exception {

        List<List<String>> command = new ArrayList<>();  // Multi-line command
        Set<String> ignoredHeaders = new HashSet<>();
        List<Header> headers = Arrays.asList(request.getAllHeaders());

        String inferredUri = request.getRequestLine().getUri();
        if (!isValidUrl(inferredUri)) { // Missing schema and domain name
            String host = getHost(request);
            String inferredScheme = "http";
            if (host.endsWith(":443")) {
                inferredScheme = "https";
            } else if (request instanceof RequestWrapper) {
                if (getOriginalRequestUri(request).startsWith("https")) {
                    // This is for original URL, so if during redirects we go out of HTTPs, this might be a wrong guess
                    inferredScheme = "https";
                }
            }

            if ("CONNECT".equals(request.getRequestLine().getMethod())) {
                inferredUri = String.format("%s://%s", inferredScheme, host);
            } else {
                inferredUri =
                        String.format("%s://%s/%s", inferredScheme, host, inferredUri)
                                .replaceAll("(?<!http(s)?:)//", "/");
            }
        }

        command.add(line(
                "curl",
                escapeString(inferredUri).replaceAll("[[{}\\\\]]", "\\$&")));

        String inferredMethod = "GET";
        List<String> data = new ArrayList<>();

        Optional<String> requestContentType = tryGetHeaderValue(headers, "Content-Type");
        Optional<String> formData = Optional.empty();
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest requestWithEntity = (HttpEntityEnclosingRequest) request;
            try {
                HttpEntity entity = requestWithEntity.getEntity();
                if (entity != null) {
                    if (requestContentType.get().startsWith("multipart/form")) {
                        ignoredHeaders.add("Content-Type"); // let curl command decide
                        ignoredHeaders.add("Content-Length");
                        handleMultipartEntity(entity, command);
                    } else if ((requestContentType.get().startsWith("multipart/mixed"))) {
                        headers = headers.stream().filter(h -> !h.getName().equals("Content-Type")).collect(Collectors.toList());
                        headers.add(new BasicHeader("Content-Type", "multipart/mixed"));
                        ignoredHeaders.add("Content-Length");
                        handleMultipartEntity(entity, command);
                    } else {
                        formData = Optional.of(EntityUtils.toString(entity));
                    }
                }
            } catch (IOException e) {
                log.error("Failed to consume form data (entity) from HTTP request", e);
                throw e;
            }
        }

        if (requestContentType.isPresent()
                && nonBinaryContentTypes.contains(requestContentType.get())
                && formData.isPresent()) {
            data.add("--data");
            data.add(escapeString(formData.get()));
            ignoredHeaders.add("Content-Length");
            inferredMethod = "POST";
        } else if (formData.isPresent()) {
            data.add("--data-binary");
            data.add(escapeString(formData.get()));
            ignoredHeaders.add("Content-Length");
            inferredMethod = "POST";
        }

        if (!request.getRequestLine().getMethod().equals(inferredMethod)) {
            command.add(line(
                    "-X",
                    request.getRequestLine().getMethod()));
        }


        headers = handleAuthenticationHeader(headers, command);

        // cookies
        headers = handleCookieHeaders(command, headers);


        handleNotIgnoredHeaders(headers, ignoredHeaders, command);

        if (! data.isEmpty()) {
            command.add(data);
        }
        command.add(line("--compressed"));
        command.add(line("--insecure"));
        command.add(line("--verbose"));

        return command.stream()
                .map(line -> line.stream().collect(Collectors.joining(" ")))
                .collect(Collectors.joining(chooseJoiningString(printMultiliner)));
    }

    private static CharSequence chooseJoiningString(boolean printMultiliner) {
        return printMultiliner
                ? String.format(" %s%n  ", commandLineSeparator())
                : " ";
    }

    private static String commandLineSeparator() {
        return isOsWindows() ? "^" : "\\";
    }

    private static List<String> line(String... arguments) {
        return Arrays.asList(arguments);
    }

    private static List<Header> handleCookieHeaders(List<List<String>> command, List<Header> headers) {
        List<Header> cookiesHeaders = headers.stream()
                .filter(h -> h.getName().equals("Cookie"))
                .collect(Collectors.toList());
        cookiesHeaders.forEach(h -> handleCookiesHeader(h, command));
        headers = headers.stream().filter(h -> !h.getName().equals("Cookie")).collect(Collectors.toList());
        return headers;
    }

    private static void handleCookiesHeader(Header header, List<List<String>> command) {
        List<String> cookies = Arrays.asList(header.getValue().split("; "));
        cookies.forEach(c -> handleCookie(c.trim(), command));
    }

    private static void handleCookie(String cookie, List<List<String>> command) {
        // Cookie value may contain "=" as well
        String[] nameAndValue = cookie.split("=", 2);
        command.add(line(
                "-b",
                escapeString(String.format("%s=%s", nameAndValue[0], nameAndValue[1]))));
    }

    private static void handleMultipartEntity(HttpEntity entity, List<List<String>> command) throws NoSuchFieldException, IllegalAccessException, IOException {
        HttpEntity wrappedEntity = (HttpEntity) getFieldValue(entity, "wrappedEntity");
        RestAssuredMultiPartEntity multiPartEntity = (RestAssuredMultiPartEntity) wrappedEntity;
        MultipartEntityBuilder multipartEntityBuilder = (MultipartEntityBuilder) getFieldValue(multiPartEntity, "builder");

        List<FormBodyPart> bodyParts = (List<FormBodyPart>) getFieldValue(multipartEntityBuilder, "bodyParts");

        bodyParts.forEach(p -> handlePart(p, command));
    }

    private static void handlePart(FormBodyPart bodyPart, List<List<String>> command) {
        String contentDisposition = bodyPart.getHeader().getFields().stream()
                .filter(f -> f.getName().equals("Content-Disposition"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Multipart missing Content-Disposition header"))
                .getBody();

        List<String> elements = Arrays.asList(contentDisposition.split(";"));
        Map<String, String> map = elements.stream().map(s -> s.trim().split("="))
                .collect(Collectors.toMap(a -> a[0], a -> a.length == 2 ? a[1] : ""));

        if (map.containsKey("form-data")) {

            StringBuffer part = new StringBuffer();
            part.append(removeQuotes(map.get("name"))).append("=");
            if (map.get("filename") != null) {
                part.append("@").append(removeQuotes(map.get("filename")));
            } else {
                try {
                    part.append(getContent(bodyPart));
                } catch (IOException e) {
                    throw new RuntimeException("Could not read content of the part", e);
                }
            }
            part.append(";type=" + bodyPart.getHeader().getField("Content-Type").getBody());
            command.add(line(
                    "-F",
                    escapeString(part.toString())));
        } else {
            throw new RuntimeException("Unsupported type " + map.entrySet().stream().findFirst().get());
        }
    }

    private static String getContent(FormBodyPart bodyPart) throws IOException {
        ContentBody content = bodyPart.getBody();
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) content.getContentLength());
        content.writeTo(out);
        return out.toString();
    }

    private static String removeQuotes(String s) {
        return s.replaceAll("^\"|\"$", "");
    }

    private static String getBoundary(String contentType) {
        String boundaryPart = contentType.split(";")[1];
        return boundaryPart.split("=")[1];
    }

    private static void handleNotIgnoredHeaders(List<Header> headers, Set<String> ignoredHeaders, List<List<String>> command) {
        headers
                .stream()
                .filter(h -> !ignoredHeaders.contains(h.getName()))
                .forEach(h -> {
                    command.add(line(
                            "-H",
                            escapeString(h.getName() + ": " + h.getValue())));
                });
    }

    private static List<Header> handleAuthenticationHeader(List<Header> headers, List<List<String>> command) {
        headers.stream()
                .filter(h -> isBasicAuthentication(h))
                .forEach(h -> {
                    command.add(line(
                            "--user",
                            escapeString(getBasicAuthCredentials(h.getValue()))));
                });

        headers = headers.stream().filter(h -> !isBasicAuthentication(h)).collect(Collectors.toList());
        return headers;
    }

    private static boolean isBasicAuthentication(Header h) {
        return h.getName().equals("Authorization") && h.getValue().startsWith("Basic");
    }

    private static String getBasicAuthCredentials(String basicAuth) {
        String credentials = basicAuth.replaceAll("Basic ", "");
        return new String(Base64.getDecoder().decode(credentials));
    }


    private static String getOriginalRequestUri(HttpRequest request) {
        if (request instanceof HttpRequestWrapper) {
            return ((HttpRequestWrapper) request).getOriginal().getRequestLine().getUri();
        } else if (request instanceof RequestWrapper) {
            return ((RequestWrapper) request).getOriginal().getRequestLine().getUri();

        } else {
            throw new IllegalArgumentException("Unsupported request class type: " + request.getClass());
        }
    }

    private static String getHost(HttpRequest request) {
        return tryGetHeaderValue(Arrays.asList(request.getAllHeaders()), "Host")
                .orElseGet(() -> URI.create(getOriginalRequestUri(request)).getHost());
    }

    private static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private static Optional<String> tryGetHeaderValue(List<Header> headers, String headerName) {
        return headers
                .stream()
                .filter(h -> h.getName().equals(headerName))
                .map(Header::getValue)
                .findFirst();
    }

    private static boolean isOsWindows() {
        return System.getProperty("os.name") != null && System.getProperty("os.name")
                .startsWith("Windows");
    }

    private static String escapeString(String s) {
        // cURL command is expected to run on the same platform that test run
        return isOsWindows() ? escapeStringWin(s) : escapeStringPosix(s);
    }

    /**
     * Replace quote by double quote (but not by \") because it is recognized by both cmd.exe and MS
     * Crt arguments parser.
     * <p>
     * Replace % by "%" because it could be expanded to an environment variable value. So %% becomes
     * "%""%". Even if an env variable "" (2 doublequotes) is declared, the cmd.exe will not
     * substitute it with its value.
     * <p>
     * Replace each backslash with double backslash to make sure MS Crt arguments parser won't
     * collapse them.
     * <p>
     * Replace new line outside of quotes since cmd.exe doesn't let to do it inside.
     */
    private static String escapeStringWin(String s) {
        return "\""
                + s
                .replaceAll("\"", "\"\"")
                .replaceAll("%", "\"%\"")
                .replaceAll("\\\\", "\\\\")
                .replaceAll("[\r\n]+", "\"^$&\"")
                + "\"";
    }

    private static String escapeStringPosix(String s) {

        if (s.matches("^.([^\\x20-\\x7E]|\').$")) {
            // Use ANSI-C quoting syntax.
            String escaped = s
                    .replaceAll("\\\\", "\\\\")
                    .replaceAll("'", "\\'")
                    .replaceAll("\n", "\\n")
                    .replaceAll("\r", "\\r");

            escaped = escaped.chars()
                    .mapToObj(c -> escapeCharacter((char) c))
                    .collect(Collectors.joining());

            return "$\'" + escaped + "'";
        } else {
            // Use single quote syntax.
            return "'" + s + "'";
        }

    }

    private static String escapeCharacter(char c) {
        int code = (int) c;
        String codeAsHex = Integer.toHexString(code);
        if (code < 256) {
            // Add leading zero when needed to not care about the next character.
            return code < 16 ? "\\x0" + codeAsHex : "\\x" + codeAsHex;
        }
        return "\\u" + ("" + codeAsHex).substring(codeAsHex.length(), 4);
    }


    private static <T> Object getFieldValue(T obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(obj.getClass(), fieldName);
        f.setAccessible(true);
        return f.get(obj);
    }


    private static Field getField(Class clazz, String fieldName)
            throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

}
