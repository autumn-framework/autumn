package com.dt.autumn.reporting.microsoftTeamsNotification;

import com.dt.autumn.reporting.emailReporting.EmailSummary;
import com.dt.autumn.reporting.exceptions.MicrosoftTeamSendException;
import com.dt.autumn.reporting.globals.GlobalVariables;
import com.dt.autumn.reporting.jacksonProcessor.JacksonJsonProcessor;
import com.dt.autumn.reporting.microsoftTeamsNotification.dataObjects.WebhookPostMessageDTO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SendMessage {

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
            if(response.getStatusCode()!=200){
                throw new MicrosoftTeamSendException("Failure in sending message");
            }
        }
    }

    public void createBody(String summary, String environment, String reportPath,String klovServerUrl) {
        String defaultBody = "{\"@type\":\"MessageCard\",\"@context\":\"http://schema.org/extensions\",\"themeColor\":\"0076D7\",\"summary\":\"\",\"sections\":[{\"activityTitle\":\"\",\"activitySubtitle\":\"\",\"facts\":[{\"name\":\"Overall Report Status\",\"value\":\"\"},{\"name\":\"Total Test Cases\",\"value\":\"\"},{\"name\":\"Overall Pass Percentage\",\"value\":\"\"}],\"markdown\":true}],\"potentialAction\":[{\"@type\":\"ViewAction\",\"name\":\"View Complete Report\",\"target\":[\"\"]},{\"@type\":\"ViewAction\",\"name\":\"View Klov Report\",\"target\":[\"\"]}]}";
        try {
            WebhookPostMessageDTO webhookPostMessageDTO = JacksonJsonProcessor.getInstance().fromJson(defaultBody, WebhookPostMessageDTO.class);
            webhookPostMessageDTO.setSummary(summary);
            webhookPostMessageDTO.getSections().get(0).setActivityTitle(summary).setActivitySubtitle("On Environment : " + environment);
            webhookPostMessageDTO.getSections().get(0).getFacts().get(0).setValue(getStatus(GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalFail()));
            webhookPostMessageDTO.getSections().get(0).getFacts().get(1).setValue(String.valueOf(GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalTest()));
            webhookPostMessageDTO.getSections().get(0).getFacts().get(2).setValue((GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass() * 100) / (GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass() + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalFail()) + "%");
            if (GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalFail() > 0) {
                WebhookPostMessageDTO.Section section = new WebhookPostMessageDTO.Section();
                section.setTitle("DPS Based Failure Summary");
                List<WebhookPostMessageDTO.Section.Fact> factList = new LinkedList<>();
                for (String packageName : GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().keySet()) {
                    if (GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalFail() > 0) {
                        WebhookPostMessageDTO.Section.Fact fact = new WebhookPostMessageDTO.Section.Fact();
                        fact.setName(packageName);
                        fact.setValue(String.valueOf(GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalFail()));
                        factList.add(fact);
                    }
                }
                section.setFacts(factList);
                webhookPostMessageDTO.getSections().add(section);
            }
            WebhookPostMessageDTO.Section emailsection = new WebhookPostMessageDTO.Section();
            List<WebhookPostMessageDTO.Section.Fact> emailfactList = new LinkedList<>();
            emailsection.setTitle("Note :Use below credentials to open report");
            if(EmailSummary.getReportLoginUser()!=null) {
                WebhookPostMessageDTO.Section.Fact userIdfact = new WebhookPostMessageDTO.Section.Fact();
                userIdfact.setName("UserName");
                userIdfact.setValue(EmailSummary.getReportLoginUser());
                emailfactList.add(userIdfact);
            }
            if(EmailSummary.getReportLoginPassword()!=null) {
                WebhookPostMessageDTO.Section.Fact passwordfact = new WebhookPostMessageDTO.Section.Fact();
                passwordfact.setName("Password");
                passwordfact.setValue(EmailSummary.getReportLoginPassword());
                emailfactList.add(passwordfact);
            }
            emailsection.setFacts(emailfactList);
            webhookPostMessageDTO.getSections().add(emailsection);
            webhookPostMessageDTO.getPotentialAction().get(0).getTarget().set(0, reportPath);
            if(!klovServerUrl.equals(""))
            webhookPostMessageDTO.getPotentialAction().get(1).getTarget().set(0, klovServerUrl);
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
