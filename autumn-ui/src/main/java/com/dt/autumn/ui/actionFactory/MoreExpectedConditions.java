package com.dt.autumn.ui.actionFactory;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * This class provides the additional methods for expected conditions
 */
public class MoreExpectedConditions {

    /**
     * Expected Condition will be true if the specified element attribute contains the specified element value.
     * @param locator The locator of the element
     * @param attributeName The name of the attribute
     * @param expectedAttributeValue The expected value of the attribute.
     * @return True if condition is true else False
     */
    public static ExpectedCondition<Boolean> attributeValueToBeContainedInElement(
            final By locator, final String attributeName, final String expectedAttributeValue) {

        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String elementAttribute = findElement(locator, driver).getAttribute(attributeName);
                    return elementAttribute.contains(expectedAttributeValue);
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }
        };
    }

    /**
     * Expected Condition will be true if the specified element attribute is not null.
     * @param locator The locator of the element
     * @param attributeName The name of the attribute.
     * @return True if condition is true else False
     */
    public static ExpectedCondition<Boolean> attributeToBeContainedInElement(
            final By locator, final String attributeName) {

        return new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    String elementAttributeValue = findElement(locator, driver).getAttribute(attributeName);
                    return elementAttributeValue != null;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }
        };
    }

    /**
     * Expected Condition will be true if the specified option is present in the Dropdown List.
     * @param selectElement The DropdownList
     * @param optionText The option text that is being looked up in the DropdownList.
     * @param byVisibleText True if comparison is on VisibleText based, False for Value based comparison.
     * @return True if condition is true else False.
     */
    public static ExpectedCondition<Boolean> optionToBeSelectedInElement(
            final Select selectElement, final String optionText, final boolean byVisibleText) {

        return new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement firstSelectedOption = selectElement.getFirstSelectedOption();
                    if (byVisibleText) {
                        return firstSelectedOption.getText().equalsIgnoreCase(optionText);
                    } else {
                        return firstSelectedOption.getAttribute("value").equalsIgnoreCase(optionText);
                    }
                } catch (StaleElementReferenceException e) {
                    return null;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        };
    }

    /**
     * Expected condition to check whether the angular has finished processing.
     * @return True if Angular has finished processing else False.
     */
    public static ExpectedCondition<Boolean> angularHasFinishedProcessing() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return Boolean.valueOf(((JavascriptExecutor)
                        driver).executeScript("return (window.angular !== undefined) && " +
                        "(angular.element(document).injector() !== undefined) && " +
                        "(angular.element(document).injector().get('$http').pendingRequests.length === 0)").toString());
            }
        };
    }

    /**
     * Expected condition to check whether the jQuery or AJAX calls have been completed.
     * @return True if jQuery or AJAX calls have been completed else False.
     */
    public static ExpectedCondition<Boolean> jQueryAJAXCallsHaveCompleted() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((JavascriptExecutor)
                        driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };
    }

    /**
     * Expected condition to check whether the document is ready.
     * @return True if document is ready else False.
     */
    public static ExpectedCondition<Boolean> documentIsReady() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
    }

    /**
     * Expected condition to check whether the document is loading.
     * @return True if document state is loading else False.
     */
    public static ExpectedCondition<Boolean> documentIsLoading() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("loading");
            }
        };
    }

    /**
     * Expected condition to check whether the document is interactive.
     * @return True if document state is interactive else False.
     */
    public static ExpectedCondition<Boolean> documentIsInteractive() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("interactive");
            }
        };
    }

    /**
     * Find Element on currently displayed page.
     * @param by The By object of locator used to uniquely identify the element on WebPage.
     * @param driver The WebDriver object.
     * @return The WebElement corresponding the specified locator(By object).
     */
    private static WebElement findElement(By by, WebDriver driver) {
        try {
            return driver.findElement(by);
        } catch (WebDriverException e) {
            throw e;
        }
    }

    /**
     * Find multiple matching Elements on currently displayed page.
     * @param by The By object of locator used to uniquely identify the element on WebPage.
     * @param driver The WebDriver object.
     * @return The list of web Elements matching the specified locator(By object).
     */
    private static List<WebElement> findElements(By by, WebDriver driver) {
        try {
            return driver.findElements(by);
        } catch (WebDriverException e) {
            throw e;
        }
    }
}
