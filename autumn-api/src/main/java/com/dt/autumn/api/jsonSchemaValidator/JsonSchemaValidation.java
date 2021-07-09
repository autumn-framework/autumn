package com.dt.autumn.api.jsonSchemaValidator;

import com.dt.autumn.api.exceptions.JsonSchemaValidationException;
import com.dt.autumn.api.jsonProcessor.JacksonJsonImpl;
import com.dt.autumn.reporting.assertions.CustomAssert;
import com.dt.autumn.reporting.extentReport.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonSchemaValidation {
    private static volatile JsonSchemaValidation _instance;

    public JsonSchemaValidation(){

    }

    public static JsonSchemaValidation getInstance() {
        if (_instance == null) {
            synchronized (JsonSchemaValidation.class) {
                if (_instance == null) {
                    _instance = new JsonSchemaValidation();
                }
            }
        }
        return _instance;
    }

    public void validateSchema(String response, String schema){
        try{
            if(response.equals("") || response.equals("{}") || response.equals("[]")){
                Logger.logInfo("No content in schema so skipping schema validation");
                return;
            }

            JSONObject jsonObject=new JSONObject();
            JsonNode responseNode= JacksonJsonImpl.getInstance().toJsonNode(response);
            if(responseNode.getNodeType().toString().equals("ARRAY")){
                JSONArray array=new JSONArray(response);
                jsonObject.put("response",array);
            }
            else if(responseNode.getNodeType().toString().equals("OBJECT")){
                jsonObject = new JSONObject(response);
            }
            else{
                Logger.logInfo("Response schema is neither JSONObject nor JSONArray so skipping schema validation");
                return;
            }


         Schema schemaJson = SchemaLoader.load(new JSONObject(schema));
         schemaJson.validate(jsonObject);
            CustomAssert.assertTrue(true,"JSON Schema Validation");
        }
        catch (com.fasterxml.jackson.core.JsonParseException e){
            Logger.logInfo("Response schema is neither JSONObject nor JSONArray so skipping schema validation");
            return;
        }
        catch (Exception e){
            throw new JsonSchemaValidationException("JSON schema validation failed",e);
        }

    }



}
