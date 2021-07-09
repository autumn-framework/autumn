package com.dt.autumn.utils.messageQueueUtils;

import com.dt.autumn.reporting.extentReport.Logger;
import com.dt.autumn.reporting.jacksonProcessor.JacksonJsonProcessor;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.avro.Schema;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SchemaRegistry {

    private static volatile SchemaRegistry instance;

    private SchemaRegistry() {
    }

    public static SchemaRegistry getInstance() {
        if (instance == null) {
            synchronized (SchemaRegistry.class) {
                if (instance == null) {
                    instance = new SchemaRegistry();
                }
            }
        }
        return instance;
    }

    private String executeRequest(String url,String schemaId, String schemaVersion) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(url);
        requestSpecBuilder.addParam("id",schemaId);
        RequestSpecification requestSpecification = requestSpecBuilder.addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter()).build();
        Response response = given().spec(requestSpecification).get();
        Logger.logInfoInLogger("Status Code:: Getting Schema from cable library : " +response.getStatusCode());
        return response.asString();
    }


    private static Schema schemaTextToAvro(String apiResponse) {
        Map<String, String> map = null;
        Schema avroSchemaText = null;
        try {
            map = JacksonJsonProcessor.getInstance().fromJson(apiResponse,Map.class);
            String schemaText = map.get("schemaText");
            Schema.Parser parser = new Schema.Parser();
            avroSchemaText = parser.parse(schemaText);
        } catch (Exception e) {
            Logger.logInfoInLogger("error:: error while converting from schema text to avro schema: "+ e);
        }
        return avroSchemaText;
    }

    public Schema getAvroFormatSchema(String cableLibraryUrl, String schemaId, String schemaVersion){
        String apiResponse = executeRequest(cableLibraryUrl,schemaId ,schemaVersion);
        Schema schema = schemaTextToAvro(apiResponse);
        return schema;
    }

}
