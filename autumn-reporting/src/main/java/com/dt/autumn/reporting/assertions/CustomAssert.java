package com.dt.autumn.reporting.assertions;

import com.dt.autumn.reporting.extentReport.Logger;
import com.dt.autumn.reporting.jacksonProcessor.JacksonJsonProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class CustomAssert {
    private List<Throwable> m_errors;

    private CustomAssert() {
    }

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
        Logger.logPass(message + " = <b>Pass</b>");
    }

    public static void assertNotNull(Object condition, String message) {
        Assert.assertNotNull(condition, message);
        Logger.logPass(message + " = <b>Pass</b>");
    }

    public static void assertFalse(boolean condition, String message) {
        Assert.assertTrue(!condition, message);
        Logger.logPass(message + " = <b>Pass</b>");
    }

    public static void assertEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertEquals(Boolean actual, Boolean expected, String message) {
        Assert.assertEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertEquals(double actual, double expected, String message) {
        Assert.assertEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertEquals(int actual, int expected, String message) {
        Assert.assertEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertNotEquals(String actual, String expected, String message) {
        Assert.assertNotEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertNotEquals(Boolean actual, Boolean expected, String message) {
        Assert.assertNotEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertNotEquals(Object actual, Object expected, String message) {
        Assert.assertNotEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertNotEquals(double actual, double expected, String message) {
        Assert.assertNotEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertNotEquals(int actual, int expected, String message) {
        Assert.assertNotEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertContainsIgnoreCase(String completeString, String subString, String message) {
        String str = message + "<br><b>Complete String : </b>" + completeString + "<br><b>Substring : </b>" + subString;
        String errorMessage = message + " -- \nComplete String : " + completeString + "\nSubstring : " + subString + "\n\n";
        Assert.assertTrue(completeString.toLowerCase().contains(subString.toLowerCase()), errorMessage);
        Logger.logPass(str);
    }


    public static void assertEqualsIgnoreCase(String actual, String expected, String message) {
        Assert.assertEquals(actual.toLowerCase(), expected.toLowerCase(), message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertContains(String completeString, String subString, String message) {
        String str = message + "<br><b>Complete String : </b>" + completeString + "<br><b>Substring : </b>" + subString;
        String errorMessage = message + " -- \nComplete String : " + completeString + "\nSubstring : " + subString + "\n\n";
        Assert.assertTrue(completeString.contains(subString), errorMessage);
        Logger.logPass(str);
    }

    public static void assertNotContains(String completeString, String subString, String message) {
        String str = message + "<br><b>Complete String : </b>" + completeString + "<br><b>Substring : </b>" + subString;
        String errorMessage = message + " -- \nComplete String : " + completeString + "\nSubstring : " + subString + "\n\n";
        Assert.assertTrue(!completeString.contains(subString), errorMessage);
        Logger.logPass(str);
    }

    public static void assertFail(String message) {
        Assert.fail(message);
        Logger.logFail(message + " = <b>Fail</b>");
    }

    public static void compareList(List<String> actual, List<String> expected, String message) {
        assertTrue(compareList(actual, expected), message);
    }

    public static void compareJsonNode(String actualJsonString, String expectedJsonString, String message) {
        try {
            JsonNode actualJsonNode = JacksonJsonProcessor.getInstance().toJsonNode(actualJsonString);
            JsonNode expectedJsonNode = JacksonJsonProcessor.getInstance().toJsonNode(expectedJsonString);
            CustomAssert.assertEquals(actualJsonNode,expectedJsonNode,message);
        }catch (Exception e){
            Logger.logInfoInLogger("Exception occur in json node parsing: "+e);
        }
    }

    private static Boolean compareList(List<String> actual, List<String> expected) {
        List<String> expectedEntryL = new ArrayList<>(expected);
        List<String> actualentryL = new ArrayList<>(actual);
        if(actualentryL.size()==0 && expectedEntryL.size()==0){
            return true;
        }
        if (actualentryL.size() == expectedEntryL.size()) {
            Boolean flag = false;
            for (String expectedEntry : expectedEntryL) {
                List<String> actualEntryCopy = new ArrayList<>(actualentryL);
                for (String actualEntry : actualEntryCopy) {
                    if (actualEntry.equalsIgnoreCase(expectedEntry)) {
                        flag = true;
                        actualentryL.remove(actualEntry);
                        break;
                    } else {
                        flag = false;
                    }
                }
                if (flag == false) {
                    break;
                }
            }
            return flag;
        } else {
            return false;
        }
    }

}
