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
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import static io.restassured.RestAssured.given;

public class BaseApi {


    private static Vector<BaseApiBuilder> baseApiBuilderThreadPool =
            new Vector<BaseApiBuilder>();

    private static ThreadLocal<BaseApiBuilder> baseApiBuildersThreads = new ThreadLocal<BaseApiBuilder>() {
        @Override
        protected BaseApiBuilder initialValue() {
            BaseApiBuilder baseApiBuilderThread = new BaseApiBuilder();
            baseApiBuilderThreadPool.add(baseApiBuilderThread);
            return baseApiBuilderThread;
        }
    };

    public static BaseApiBuilder getBaseApiBuilder(){
        BaseApiBuilder baseApiBuilder = baseApiBuildersThreads.get();
        return baseApiBuilder;
    }

    public String getJsonResponseSchema() {
        return getBaseApiBuilder().jsonResponseSchema;
    }

    public void setJsonResponseSchema(String jsonResponseSchema) {
        getBaseApiBuilder().jsonResponseSchema = jsonResponseSchema;
    }

    public Boolean getMtlsCertificateflag() {
        return getBaseApiBuilder().mtlsCertificateflag;
    }

    public void setMtlsCertificateflag(Boolean mtlsCertificateflag) {
        getBaseApiBuilder().mtlsCertificateflag = mtlsCertificateflag;
    }

    public void setMtlsCertificate(String mtlsCertificate, String caCertificate,String mtlsCertificatePassword) {
        setMtlsCertificateflag(true);
        MtlsCertificateConfig.getInstance().setKeyStore(mtlsCertificate, mtlsCertificatePassword);
        MtlsCertificateConfig.getInstance().setTrustStore(caCertificate, "testingCertificate");
    }


    public KeyStore getKeyStore() {
        return getBaseApiBuilder().keyStore;
    }

    public Boolean getRedirectFlag() {
        return getBaseApiBuilder().redirectFlag;
    }

    public void setRedirectFlag(Boolean redirectFlag) {
        getBaseApiBuilder().redirectFlag = redirectFlag;

    }

    public Boolean getCaptureAPIDetails() {
        return getBaseApiBuilder().captureAPIDetails;
    }

    public void setCaptureAPIDetails(Boolean captureAPIDetails) {
        getBaseApiBuilder().captureAPIDetails = captureAPIDetails;
    }

    public MethodType getMethod() {
        return getBaseApiBuilder().method;
    }

    public void setMethod(MethodType method) {
        getBaseApiBuilder().method = method;
    }

    public void setBody(Object obj) {
        getBaseApiBuilder().body = obj;
        getBaseApiBuilder().requestSpecBuilder.setBody(obj);
    }

    public void setBody(byte[] obj) {
        getBaseApiBuilder().body = obj;
        getBaseApiBuilder().requestSpecBuilder.setBody(obj);
    }

    public void setBasicAuth(String userName, String password) {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(userName);
        authScheme.setPassword(password);
        getBaseApiBuilder().requestSpecBuilder.setAuth(authScheme);
    }

    public Object getBody() {
        return getBaseApiBuilder().body;
    }

    public void setContentType(ContentType contentType) {
        getBaseApiBuilder().contentType = contentType;
        getBaseApiBuilder().requestSpecBuilder.setContentType(contentType.getContentType());

    }

    public ContentType getContentType() {
        return getBaseApiBuilder().contentType;
    }


    public void setBaseUri(String baseUri) {
        getBaseApiBuilder().baseUri = baseUri;
        getBaseApiBuilder().requestSpecBuilder.setBaseUri(baseUri);
    }

    public String getBaseUri() {
        return getBaseApiBuilder().baseUri;
    }

    public void setCookie(String cookie) {
        getBaseApiBuilder().cookie = cookie;
        getBaseApiBuilder().requestSpecBuilder.addCookie(cookie);
    }

    public String getCookie() {
        return getBaseApiBuilder().cookie;
    }


    public void setBasePath(String basePath) {
        getBaseApiBuilder().basePath = basePath;
        getBaseApiBuilder().requestSpecBuilder.setBasePath(basePath);
    }

    public String getBasePath() {
        return getBaseApiBuilder().basePath;
    }

    public void addHeaders(Map<String, String> headers) {
        getBaseApiBuilder().headers.putAll(headers);
        getBaseApiBuilder().requestSpecBuilder.addHeaders(headers);
    }

    public void addHeader(String headerKey, String headerValue) {
        getBaseApiBuilder().headers.put(headerKey, headerValue);
        getBaseApiBuilder().requestSpecBuilder.addHeader(headerKey, headerValue);
    }

    public Map<String, Object> getHeaders() {
        return getBaseApiBuilder().headers;
    }


    public void addQueryParam(String paramKey, Object paramValue) {
        getBaseApiBuilder().queryParams.put(paramKey, paramValue);
        getBaseApiBuilder().requestSpecBuilder.addQueryParam(paramKey, paramValue);
    }

    public void addQueryParams(Map<String, String> queryParams) {
        getBaseApiBuilder().queryParams.putAll(queryParams);
        getBaseApiBuilder().requestSpecBuilder.addQueryParams(queryParams);
    }

    public Map<String, Object> getQueryParams() {
        return getBaseApiBuilder().queryParams;
    }


    public void addPathParam(String paramKey, Object paramValue) {
        getBaseApiBuilder().pathParams.put(paramKey, paramValue);
        getBaseApiBuilder().requestSpecBuilder.addPathParam(paramKey, paramValue);
    }

    public String getKeyValue(Response response, String keyvalue) {
         JsonPath jsonPathevaluator = response.getBody().jsonPath();
        return (jsonPathevaluator.getString(keyvalue));
    }

    public void addPathParams(Map<String, String> pathParams) {
        getBaseApiBuilder().pathParams.putAll(pathParams);
        getBaseApiBuilder().requestSpecBuilder.addPathParams(pathParams);
    }

    public Map<String, Object> getPathParams() {
        return getBaseApiBuilder().pathParams;
    }


    public void addFormURLEncoded(String paramKey, Object paramValue) {
        getBaseApiBuilder().formURLEncoded.put(paramKey, paramValue);
        getBaseApiBuilder().requestSpecBuilder.addFormParam(paramKey, paramValue);
    }


    public void addFormURLEncoded(Map<String, Object> formURLEncoded) {
        getBaseApiBuilder().formURLEncoded.putAll(formURLEncoded);
        getBaseApiBuilder().requestSpecBuilder.addFormParams(formURLEncoded);
    }

    public Map<String, Object> getFormURLEncoded() {
        return getBaseApiBuilder().formURLEncoded;
    }

    public void addParam(String paramKey, Object paramValue) {
        getBaseApiBuilder().params.put(paramKey, paramValue);
        getBaseApiBuilder().requestSpecBuilder.addParam(paramKey, paramValue);
    }

    public void addParams(Map<String, String> queryParams) {
        getBaseApiBuilder().params.putAll(queryParams);
        getBaseApiBuilder().requestSpecBuilder.addParams(queryParams);
    }

    public void addMultiPart(String controlName, File file, String mimeType) {
        getBaseApiBuilder().requestSpecBuilder.addMultiPart(controlName, file, mimeType);
    }

    public void addMultiPart(String controlName, File file) {
        getBaseApiBuilder().requestSpecBuilder.addMultiPart(controlName, file);
    }

    public void addMultiPart(String controlName, String contentBody) {
        getBaseApiBuilder().requestSpecBuilder.addMultiPart(controlName, contentBody);
    }

    public void addMultiPart(String controlName, String contentBody, String mimeType) {
        getBaseApiBuilder().requestSpecBuilder.addMultiPart(controlName, contentBody, mimeType);
    }

    public Map<String, Object> getParams() {
        return getBaseApiBuilder().params;
    }

/*    public RequestSpecBuilder getRequestSpecBuilder() {
        return getRequestSpecBuilder();
    }*/


    public Response execute() {
        try {
            RequestSpecification requestSpecification = getBaseApiBuilder().requestSpecBuilder.addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter()).build();
            Response response;
            RestAssured.defaultParser = Parser.JSON;
            RestAssuredConfig config = new CurlLoggingRestAssuredConfigBuilder(getBaseApiBuilder().captureAPIDetails).build();
            if (this.getMtlsCertificateflag()) {
                Logger.logInfoInLogger("MTLS certificate flag is true");
                config = RestAssured.config().sslConfig(MtlsCertificateConfig.getInstance().getSSLconfig());
                Logger.logInfoInLogger("SSL Configs are \n" + config.getSSLConfig());
            }

            switch (getBaseApiBuilder().method) {
                case GET:
                    response = given().config(config).spec(requestSpecification).when().redirects().follow(getBaseApiBuilder().redirectFlag).get();
                    break;
                case POST:
                    response = given().config(config).spec(requestSpecification).when().redirects().follow(getBaseApiBuilder().redirectFlag).post();
                    break;
                case PUT:
                    response = given().config(config).spec(requestSpecification).when().redirects().follow(getBaseApiBuilder().redirectFlag).put();
                    break;
                case DELETE:
                    response = given().config(config).spec(requestSpecification).when().redirects().follow(getBaseApiBuilder().redirectFlag).delete();
                    break;
                case PATCH:
                    response = given().config(config).spec(requestSpecification).when().redirects().follow(getBaseApiBuilder().redirectFlag).patch();
                    break;
                case POSTBYTES:
                    response = given().spec(requestSpecification).when().redirects().follow(getBaseApiBuilder().redirectFlag).post();
                    break;
                default:
                    baseApiBuildersThreads.remove();
                    throw new RuntimeException("API method not specified");

            }
            if (getBaseApiBuilder().captureAPIDetails) {
                captureAPIDetails(response);
                CreateAPIPerfReport createAPIPerfReport = new CreateAPIPerfReport();
                createAPIPerfReport.captureAPIStatusTimeDTO(getBaseApiBuilder().basePath, response.getTimeIn(TimeUnit.MILLISECONDS));
            }

            if (getJsonResponseSchema() != "" && Pattern.matches("2..", Integer.toString(response.getStatusCode()))) {
                JsonSchemaValidation.getInstance().validateSchema(response.getBody().asString(), getJsonResponseSchema());
            }
            baseApiBuildersThreads.remove();
            return response;
        }catch(Exception e){
            baseApiBuildersThreads.remove();
            throw e;
        }
    }

    public void captureAPIDetails(Response response) {
        if (response != null) {
            Logger.logInfo("Response is " + response.asString());
            Logger.logInfo("Response Status Code = " + response.getStatusCode());
            Logger.logInfo("Response time consumed is = " + response.getTimeIn(TimeUnit.MILLISECONDS));
        }
        if (response.getHeaders() != null) {
            Logger.logInfo("Response Headers are " + response.getHeaders().toString());
        }
    }

}
