package com.autumn.api;

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


import com.autumn.api.curlloggingutil.CurlLoggingRestAssuredConfigBuilder;
import com.autumn.api.jsonSchemaValidator.JsonSchemaValidation;
import com.autumn.api.mtlsCertificate.MtlsCertificateConfig;
import com.autumn.reporting.extentReport.Logger;
import com.autumn.reporting.perfStatusReport.CreateAPIPerfReport;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class BaseApi {

    private RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    private MethodType method;
    private Object body;
    private ContentType contentType;
    private String baseUri;
    private Map<String, Object> pathParams = new HashMap<>();
    private Map<String, Object> queryParams = new HashMap<>();
    private Map<String, Object> formURLEncoded = new HashMap<>();
    private Map<String, Object> params = new HashMap<>();
    private String basePath;
    private String cookie;
    private Map<String, Object> headers = new HashMap<>();
    private Response response;
    private JsonPath jsonPathevaluator;
    private Boolean captureAPIDetails = true;
    private Boolean redirectFlag = true;
    private KeyStore keyStore;
    private String certificatePassphrase;
    private String certificateName;
    private Boolean mtlsCertificateflag = false;
    private String jsonResponseSchema = "";

    public String getJsonResponseSchema() {
        return jsonResponseSchema;
    }

    public void setJsonResponseSchema(String jsonResponseSchema) {
        this.jsonResponseSchema = jsonResponseSchema;
    }

    public Boolean getMtlsCertificateflag() {
        return mtlsCertificateflag;
    }

    public void setMtlsCertificateflag(Boolean mtlsCertificateflag) {
        this.mtlsCertificateflag = mtlsCertificateflag;
    }

    public void setMtlsCertificate(String mtlsCertificate, String caCertificate,String mtlsCertificatePassword) {
        setMtlsCertificateflag(true);
        MtlsCertificateConfig.getInstance().setKeyStore(mtlsCertificate, mtlsCertificatePassword);
        MtlsCertificateConfig.getInstance().setTrustStore(caCertificate, "testingCertificate");
    }


    public KeyStore getKeyStore() {
        return keyStore;
    }

    public Boolean getRedirectFlag() {
        return redirectFlag;
    }

    public void setRedirectFlag(Boolean redirectFlag) {
        this.redirectFlag = redirectFlag;

    }

    public Boolean getCaptureAPIDetails() {
        return captureAPIDetails;
    }

    public void setCaptureAPIDetails(Boolean captureAPIDetails) {
        this.captureAPIDetails = captureAPIDetails;
    }

    public MethodType getMethod() {
        return method;
    }

    public void setMethod(MethodType method) {
        this.method = method;
    }

    public void setBody(Object obj) {
        this.body = obj;
        requestSpecBuilder.setBody(obj);
    }

    public void setBody(byte[] obj) {
        this.body = obj;
        requestSpecBuilder.setBody(obj);
    }

    public void setBasicAuth(String userName, String password) {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(userName);
        authScheme.setPassword(password);
        requestSpecBuilder.setAuth(authScheme);
    }

    public Object getBody() {
        return this.body;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
        requestSpecBuilder.setContentType(contentType.getContentType());

    }

    public ContentType getContentType() {
        return this.contentType;
    }


    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        requestSpecBuilder.setBaseUri(baseUri);
    }

    public String getBaseUri() {
        return this.baseUri;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
        requestSpecBuilder.addCookie(cookie);
    }

    public String getCookie() {
        return this.cookie;
    }


    public void setBasePath(String basePath) {
        this.basePath = basePath;
        requestSpecBuilder.setBasePath(basePath);
    }

    public String getBasePath() {
        return this.basePath;
    }

    public void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        requestSpecBuilder.addHeaders(headers);
    }

    public void addHeader(String headerKey, String headerValue) {
        this.headers.put(headerKey, headerValue);
        requestSpecBuilder.addHeader(headerKey, headerValue);
    }

    public Map<String, Object> getHeaders() {
        return this.headers;
    }


    public void addQueryParam(String paramKey, Object paramValue) {
        this.queryParams.put(paramKey, paramValue);
        requestSpecBuilder.addQueryParam(paramKey, paramValue);
    }

    public void addQueryParams(Map<String, String> queryParams) {
        this.queryParams.putAll(queryParams);
        requestSpecBuilder.addQueryParams(queryParams);
    }

    public Map<String, Object> getQueryParams() {
        return this.queryParams;
    }


    public void addPathParam(String paramKey, Object paramValue) {
        this.pathParams.put(paramKey, paramValue);
        requestSpecBuilder.addPathParam(paramKey, paramValue);
    }

    public String getKeyValue(String keyvalue) {
        jsonPathevaluator = response.getBody().jsonPath();
        return (jsonPathevaluator.getString(keyvalue));
    }

    public void addPathParams(Map<String, String> pathParams) {
        this.pathParams.putAll(pathParams);
        requestSpecBuilder.addPathParams(pathParams);
    }

    public Map<String, Object> getPathParams() {
        return this.pathParams;
    }


    public void addFormURLEncoded(String paramKey, Object paramValue) {
        this.formURLEncoded.put(paramKey, paramValue);
        requestSpecBuilder.addFormParam(paramKey, paramValue);
    }


    public void addFormURLEncoded(Map<String, Object> formURLEncoded) {
        this.formURLEncoded.putAll(formURLEncoded);
        requestSpecBuilder.addFormParams(formURLEncoded);
    }

    public Map<String, Object> getFormURLEncoded() {
        return this.formURLEncoded;
    }

    public void addParam(String paramKey, Object paramValue) {
        this.params.put(paramKey, paramValue);
        requestSpecBuilder.addParam(paramKey, paramValue);
    }

    public void addParams(Map<String, String> queryParams) {
        this.params.putAll(queryParams);
        requestSpecBuilder.addParams(queryParams);
    }

    public void addMultiPart(String controlName, File file, String mimeType) {
        requestSpecBuilder.addMultiPart(controlName, file, mimeType);
    }

    public void addMultiPart(String controlName, File file) {
        requestSpecBuilder.addMultiPart(controlName, file);
    }

    public void addMultiPart(String controlName, String contentBody) {
        requestSpecBuilder.addMultiPart(controlName, contentBody);
    }

    public void addMultiPart(String controlName, String contentBody, String mimeType) {
        requestSpecBuilder.addMultiPart(controlName, contentBody, mimeType);
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public RequestSpecBuilder getRequestSpecBuilder() {
        return requestSpecBuilder;
    }


    public Response execute() {
        RequestSpecification requestSpecification = requestSpecBuilder.addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter()).build();
        Response response;
        RestAssured.defaultParser = Parser.JSON;
        RestAssuredConfig config = new CurlLoggingRestAssuredConfigBuilder(captureAPIDetails).build();
        if (this.getMtlsCertificateflag()) {
            Logger.logInfoInLogger("MTLS certificate flag is true");
            config = RestAssured.config().sslConfig(MtlsCertificateConfig.getInstance().getSSLconfig());
            Logger.logInfoInLogger("SSL Configs are \n" + config.getSSLConfig());
        }

        switch (method) {
            case GET:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).get();
                break;
            case POST:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).post();
                break;
            case PUT:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).put();
                break;
            case DELETE:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).delete();
                break;
            case PATCH:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).patch();
                break;
            case POSTBYTES:
                response = given().spec(requestSpecification).when().redirects().follow(redirectFlag).post();
                break;
            default:
                throw new RuntimeException("API method not specified");

        }
        this.response = response;
        if (captureAPIDetails) {
            captureAPIDetails();
            CreateAPIPerfReport createAPIPerfReport = new CreateAPIPerfReport();
            createAPIPerfReport.captureAPIStatusTimeDTO(basePath, this.response.getTimeIn(TimeUnit.MILLISECONDS));
        }

        if (getJsonResponseSchema() != "" && Pattern.matches("2..", Integer.toString(response.getStatusCode()))) {
            JsonSchemaValidation.getInstance().validateSchema(response.getBody().asString(), getJsonResponseSchema());
        }
        return response;
    }

    public void captureAPIDetails() {
        if (this.response != null) {
            Logger.logInfo("Response is " + this.response.asString());
            Logger.logInfo("Response Status Code = " + this.response.getStatusCode());
            Logger.logInfo("Response time consumed is = " + this.response.getTimeIn(TimeUnit.MILLISECONDS));
        }
        if (this.response.getHeaders() != null) {
            Logger.logInfo("Response Headers are " + this.response.getHeaders().toString());
        }
    }

}
