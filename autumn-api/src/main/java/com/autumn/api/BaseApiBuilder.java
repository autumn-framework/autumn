package com.autumn.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BaseApiBuilder {

    public RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    public MethodType method;
    public Object body;
    public ContentType contentType;
    public String baseUri;
    public Map<String, Object> pathParams = new HashMap<>();
    public Map<String, Object> queryParams = new HashMap<>();
    public Map<String, Object> formURLEncoded = new HashMap<>();
    public Map<String, Object> params = new HashMap<>();
    public String basePath;
    public String cookie;
    public Map<String, Object> headers = new HashMap<>();
    public Boolean captureAPIDetails = true;
    public Boolean redirectFlag = true;
    public KeyStore keyStore;
    public String certificatePassphrase;
    public String certificateName;
    public Boolean mtlsCertificateflag = false;
    public String jsonResponseSchema = "";



}
