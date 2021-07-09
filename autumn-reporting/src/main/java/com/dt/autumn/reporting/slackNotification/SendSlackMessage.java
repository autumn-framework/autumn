package com.dt.autumn.reporting.slackNotification;

/*-
 * #%L
 * autumn-reporting
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

import com.dt.autumn.reporting.emailReporting.EmailSummary;
import com.dt.autumn.reporting.exceptions.MicrosoftTeamSendException;
import com.dt.autumn.reporting.globals.GlobalVariables;
import com.dt.autumn.reporting.jacksonProcessor.JacksonJsonProcessor;
import com.dt.autumn.reporting.slackNotification.dataObjects.WebhookPostMessageDTO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class SendSlackMessage {
    private String jsonBody;

    public void webHookPostMessage(String... urls) {
        for (String url : urls) {
            RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
            requestSpecBuilder.setBaseUri(url);
            requestSpecBuilder.setContentType(ContentType.JSON);
            requestSpecBuilder.setBody(jsonBody);
            RequestSpecification requestSpecification = requestSpecBuilder.addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter()).build();
            Response response = given().spec(requestSpecification).post();
            if (response.getStatusCode() != 200) {
                throw new MicrosoftTeamSendException("Failure in sending message");
            }
        }
    }

    public void createBody(String summary, String environment, String reportPath,String klovServerUrl) {
        String defaultFailureBody = "{\"username\":\"AutomationStatus\",\"icon_emoji\":\":twice:\",\"attachments\":[{\"color\":\"#3AA3E3\",\"title\":\"\",\"text\":\"\"},{\"color\":\"#3AA3E3\",\"fields\":[{\"title\":\" \",\"value\":\"*Overall Report Status*\n*Overall Test Cases*\n*Overall Pass Percentage*\",\"short\":true},{\"title\":\" \",\"value\":\"\",\"short\":true}]},{\"color\":\"#3AA3E3\",\"title\":\"DPS Based Failure Summary\",\"fields\":[{\"title\":\"Microservice\",\"value\":\"\",\"short\":true},{\"title\":\"Failures\",\"value\":\"\",\"short\":true}]},{\"color\":\"#3AA3E3\",\"text\":\"\",\"fields\":[{\"title\":\"UserName\",\"value\":\"\",\"short\":true},{\"title\":\"Password\",\"value\":\"\",\"short\":true}]}]}";
        String defaultPassBody="{\"username\":\"AutomationStatus\",\"icon_emoji\":\":twice:\",\"attachments\":[{\"color\":\"#3AA3E3\",\"title\":\"\",\"text\":\"\"},{\"color\":\"#3AA3E3\",\"fields\":[{\"title\":\" \",\"value\":\"*Overall Report Status*\n*Overall Test Cases*\n*Overall Pass Percentage*\",\"short\":true},{\"title\":\" \",\"value\":\"\",\"short\":true}]},{\"color\":\"#3AA3E3\",\"text\":\"\",\"fields\":[{\"title\":\"UserName\",\"value\":\"\",\"short\":true},{\"title\":\"Password\",\"value\":\"\",\"short\":true}]}]}";
        String defaultBody;
        try {
            int totalFailures= GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalFail();
            int totalTestCases=GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalTest();
            int totalProdBugs=GlobalVariables.SUITE_TEST_STATUS_DTO.getProductionBugs();
            int totalPassCases=GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass();
            float passPercentage=(totalPassCases*100)/(totalPassCases+totalFailures+totalProdBugs);

            String ReportStatus=getStatus(totalFailures);
            if(ReportStatus.equals("FAIL"))
                defaultBody=defaultFailureBody;
            else defaultBody=defaultPassBody;

            WebhookPostMessageDTO webhookPostMessageDTO = JacksonJsonProcessor.getInstance().fromJson(defaultBody, WebhookPostMessageDTO.class);
            webhookPostMessageDTO.getAttachments().get(0).setTitle(summary).setText("On Environment : " + environment);
            webhookPostMessageDTO.getAttachments().get(1).getFields().get(1).setValue(ReportStatus+"\n"+totalTestCases+"\n"+passPercentage+ "%");
            if(ReportStatus.equals("FAIL")){
                HashMap<String ,String > dpsFailures = new HashMap<>();
                String failedPackageNames="";
                String  failedPackageCount="";
                for (String packageName : GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().keySet()) {
                    if (GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalFail() > 0) {
                        dpsFailures.put(packageName,String.valueOf(GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalFail()));
                        failedPackageNames=failedPackageNames+packageName+"\n";
                        failedPackageCount=failedPackageCount+String.valueOf(GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalFail())+"\n";
                    }
                }
                webhookPostMessageDTO.getAttachments().get(2).getFields().get(0).setValue(failedPackageNames);
                webhookPostMessageDTO.getAttachments().get(2).getFields().get(1).setValue(failedPackageCount);
                if(!klovServerUrl.equals(""))
                webhookPostMessageDTO.getAttachments().get(3).setText("<"+reportPath+"|View Complete Report>       <"+klovServerUrl+"|View Klov Report>\n\nNote: Use below credentials to open report")
                .getFields().get(0).setValue(EmailSummary.getReportLoginUser());
                else
                    webhookPostMessageDTO.getAttachments().get(3).setText("<"+reportPath+"|View Complete Report>\n\nNote: Use below credentials to open report")
                            .getFields().get(0).setValue(EmailSummary.getReportLoginUser());
                webhookPostMessageDTO.getAttachments().get(3).getFields().get(1).setValue(EmailSummary.getReportLoginPassword());

            }
            else {
                if(!klovServerUrl.equals(""))
                webhookPostMessageDTO.getAttachments().get(2).setText("<"+reportPath+"|View Complete Report>       <"+klovServerUrl+"|View Klov Report>\n\nNote: Use below credentials to open report")
                        .getFields().get(0).setValue(EmailSummary.getReportLoginUser());
                else
                    webhookPostMessageDTO.getAttachments().get(2).setText("<"+reportPath+"|View Complete Report>\n\nNote: Use below credentials to open report")
                            .getFields().get(0).setValue(EmailSummary.getReportLoginUser());
                webhookPostMessageDTO.getAttachments().get(2).getFields().get(1).setValue(EmailSummary.getReportLoginPassword());
                            }
            this.jsonBody = JacksonJsonProcessor.getInstance().toJSon(webhookPostMessageDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getStatus(int FailCount) {
        if (FailCount > 0) {
            return "FAIL";
        }
        return "PASS";
    }

}
