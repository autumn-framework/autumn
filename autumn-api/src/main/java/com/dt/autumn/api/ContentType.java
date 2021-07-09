package com.dt.autumn.api;

public enum ContentType {

    JSON("application/json"),
    TEXT("text/plain"),
    XML("application/xml"),
    HTML("text/html"),
    URLENC("application/x-www-form-urlencoded"),
    FORMDATA("multipart/form-data"),
    MSGPACK("application/msgpack; charset=UTF-8");


    private String contentType;

    ContentType(String contentType){
        this.contentType=contentType;
    }

    public String getContentType(){
        return this.contentType;
    }



}
