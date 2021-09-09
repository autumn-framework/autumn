package com.autumn.ui.actionFactory.element;

/*-
 * #%L
 * autumn-ui
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

import com.autumn.ui.actionFactory.MoreExpectedConditions;
import com.autumn.ui.driver.DriverManager;
import com.autumn.reporting.assertions.CustomAssert;
import com.autumn.reporting.extentReport.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.Arrays;
import java.util.List;

public class UIElement implements WebElement, WrapsElement {

	JavascriptExecutor js;
	WebDriver driver;
	private String elementName;
	private String pageName;
	private By by;
	private WebElement webElement;
	private Actions actions = new Actions(DriverManager.getDriver());

	@Deprecated
	public UIElement(By by, String pageName) {
		this.by = by;
		this.pageName = pageName;
		this.elementName = Thread.currentThread().getStackTrace()[3].getMethodName(); // returns the method name where
																						// the element is getting
																						// initialized;
		this.driver = DriverManager.getDriver();
		this.js = (JavascriptExecutor) driver;
	}

	public UIElement(By by, String pageName, String elementName) {
		this.by = by;
		this.pageName = pageName;
		this.elementName = elementName;
		this.driver = DriverManager.getDriver();
		this.js = (JavascriptExecutor) driver;
	}

	public UIElement(WebElement webElement, String pageName, String elementName) {
		this.webElement = webElement;
		this.pageName = pageName;
		this.elementName = elementName;
		this.driver = DriverManager.getDriver();
		this.js = (JavascriptExecutor) driver;
	}

	public String getPageName() {
		return this.pageName;
	}

	public String getElementName() {
		return this.elementName;
	}

	public By getBy() {
		return this.by;
	}

	public void click() {
		Logger.logInfo("Click [" + elementName + "] on [" + pageName + "]");
		try {
			getWrappedElement().click();
		} catch (WebDriverException e) {
			this.actions.moveToElement(getWrappedElement()).click();
		}
	}

	public void waitThenClick() {
		Logger.logInfo("Click [" + elementName + "] on [" + pageName + "]");
		try {
			waitUntilClickable();
			getWrappedElement().click();
		} catch (WebDriverException e) {
			this.actions.moveToElement(getWrappedElement()).click();
		}
	}

	private void highlightElement(WebElement element, int duration) throws InterruptedException {
		String original_style = element.getAttribute("style");
		js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
				"border: 5px solid red; border-style: solid;");
		if (duration > 0) {
			Thread.sleep(duration * 500);
			js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", original_style);
		}
	}

	public void sendKeys(CharSequence... keysToSend) {
		Logger.logInfo(
				"Enter text [" + Arrays.toString(keysToSend) + "] in [" + elementName + "] on [" + pageName + "]");
		getWrappedElement().sendKeys(keysToSend);
	}

	public Point getLocation() {
		return getWrappedElement().getLocation();
	}

	public void submit() {
		Logger.logInfo("Click [" + elementName + "] on [" + pageName + "] to submit");
		getWrappedElement().submit();
	}

	public String getAttribute(String name) {
		return getWrappedElement().getAttribute(name);
	}

	public String getCssValue(String propertyName) {
		return getWrappedElement().getCssValue(propertyName);
	}

	public Dimension getSize() {
		return getWrappedElement().getSize();
	}

	@Override
	public Rectangle getRect() {
		throw new UnsupportedOperationException();
	}

	public List<WebElement> findElements(By by) {
		return getWrappedElement().findElements(by);
	}

	public String getText() {
		return getWrappedElement().getText();
	}

	public String getTagName() {
		return getWrappedElement().getTagName();
	}

	public boolean isSelected() {
		return getWrappedElement().isSelected();
	}

	public WebElement findElement(By by) {
		return getWrappedElement().findElement(by);
	}

	public boolean isEnabled() {
		return getWrappedElement().isEnabled();
	}

	public boolean isDisplayed() {
		return getWrappedElement().isDisplayed();
	}

	public void clear() {
		Logger.logInfo("Clear text in [" + elementName + "] on [" + pageName + "]");
		getWrappedElement().clear();
	}

	public WebElement getWrappedElement() {
		if (this.webElement == null) {
			this.webElement = DriverManager.getDriver().findElement(this.by);
		}
		return this.webElement;
	}

	public boolean elementWired() {
		return (webElement != null);
	}

	public void focus() {
		Logger.logInfo("Focus [" + elementName + "] on [" + pageName + "]");
		new Actions(DriverManager.getDriver()).moveToElement(getWrappedElement()).perform();
	}

	public void assertVisible() {
		Logger.logInfo("Assert [" + elementName + "] is visible on [" + pageName + "]");
		if (!isElementPresent() || ExpectedConditions.visibilityOfElementLocated(this.by).apply(this.driver) == null) {
			throw new AssertionError(elementName + " is expected to be visible but it is not");
		}
	}

	public void assertNotVisible() {
		Logger.logInfo("Assert [" + elementName + "] is not visible on [" + pageName + "]");
		if (isElementPresent()
				&& ExpectedConditions.visibilityOfElementLocated(this.by).apply(this.driver) instanceof WebElement) {
			throw new AssertionError(elementName + " is expected to be not visible but it is");
		}
	}

	public void assertText(String text) {
		Logger.logInfo("Assert [" + elementName + "] text equals [" + text + "] on [" + pageName + "]");
		if (!isElementPresent() || !ExpectedConditions.textToBe(this.by, text).apply(this.driver)) {
			throw new AssertionError(
					"text of " + elementName + " is expected to be equal to " + text + " but it is not");
		}
	}

	public void assertContainsText(String text) {
		Logger.logInfo("Assert [" + elementName + "] contains text [" + text + "] on [" + pageName + "]");
		if (!isElementPresent()
				|| !ExpectedConditions.textToBePresentInElementLocated(this.by, text).apply(this.driver)) {
			throw new AssertionError(elementName + " is expected to contain text - '" + text + "' but it is not");
		}
	}

	public void assertDoesNotContainText(String text) {
		Logger.logInfo("Assert [" + elementName + "] doesn't contain text [" + text + "] on [" + pageName + "]");
		if (isElementPresent()
				&& ExpectedConditions.textToBePresentInElementLocated(this.by, text).apply(this.driver)) {
			throw new AssertionError(elementName + " is expected to not contain text - '" + text + "' but it does");
		}
	}

	public void assertValue(String value) {
		Logger.logInfo("Assert [" + elementName + "] value equals [" + value + "] on [" + pageName + "]");
		if (!isElementPresent() || !ExpectedConditions.attributeToBe(this.by, "value", value).apply(this.driver))
			throw new AssertionError(
					"value of " + elementName + " is expected to be equal to " + value + " but it is not");
	}

	public void assertAttribute(String attribute, String value) {
		Logger.logInfo("Assert [" + elementName + "] value equals [" + value + "] on [" + pageName + "]");
		if (!isElementPresent() || !ExpectedConditions.attributeToBe(this.by, attribute, value).apply(this.driver)) {
			throw new AssertionError("element attribute - " + attribute + " is expected to have value equals to "
					+ value + " but it does not");
		}
	}

	public void assertContainsValue(String value) {
		Logger.logInfo("Assert [" + elementName + "] contains value [" + value + "] on [" + pageName + "]");
		String assertionMsg = "value of " + elementName + " is expected to be contain value - " + value
				+ " but it is does not";
		try {
			WebElement webElement = ExpectedConditions.presenceOfElementLocated(this.by).apply(this.driver);
			CustomAssert.assertEqualsIgnoreCase(webElement.getAttribute("value"),value,assertionMsg);
		} catch (NoSuchElementException e) {
			throw new AssertionError(assertionMsg);
		}
	}

	public void assertDoesNotContainValue(String value) {
		Logger.logInfo(
				"Assert [" + elementName + "] doesn't contain value [" + value + "] on [" + pageName + "]");
		String assertionMsg = "value of " + elementName + " is expected to be not contain value - " + value
				+ " but it is does";
		try {
			WebElement webElement = ExpectedConditions.presenceOfElementLocated(this.by).apply(this.driver);
			CustomAssert.assertNotContains(webElement.getAttribute("value"),value,assertionMsg);
		} catch (NoSuchElementException e) {
			// do nothing as it satisfies the assertion
		}
	}

	private boolean isFocused() {
		return getWrappedElement().equals(DriverManager.getDriver().switchTo().activeElement());
	}

	public void switchToFrame() {
		DriverManager.getDriver().switchTo().frame(getWrappedElement());
	}

	public void assertIsFocused() {
		CustomAssert.assertTrue(isFocused(),elementName + " is expected to be focused but is not");
	}

	public void assertIsNotFocused() {
		CustomAssert.assertFalse(isFocused(),elementName + " is expected to be not focused but is");
	}

	public void waitUntilContainsText(String text) {
		Logger.logInfo("Wait until [" + elementName + "] contains text [" + text + "] on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait()
					.until(ExpectedConditions.textToBePresentInElementLocated(getBy(), text));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}

	}

	public void waitUntilDoesNotContainText(String text) {
		Logger.logInfo("Wait until [" + elementName + "] contains text [" + text + "] on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait()
					.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(getBy(), text)));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilVisible() {
		Logger.logInfo("Wait until [" + elementName + "] is visible " + "on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.visibilityOfElementLocated(getBy()));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilPresent() {
		Logger.logInfo("Wait until [" + elementName + "] is present " + "on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.presenceOfElementLocated(getBy()));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilNotVisible() {
		Logger.logInfo("Wait until [" + elementName + "] is not visible " + "on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.invisibilityOfElementLocated(getBy()));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilEditable() {
		Logger.logInfo("Wait until [" + elementName + "] is editable " + "on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.elementToBeClickable(getBy()));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilNotEditable() {
		Logger.logInfo("Wait until [" + elementName + "] is not editable " + "on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait()
					.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(getBy())));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilContainsAttributeValue(String attributeName, String attributeValue) {
		Logger.logInfo("Wait until [" + elementName + "] attribute [" + attributeName + "] contains value ["
				+ attributeValue + "] on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait().until(MoreExpectedConditions
					.attributeValueToBeContainedInElement(getBy(), attributeName, attributeValue));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilDoesNotContainAttributeValue(String attributeName, String attributeValue) {
		Logger.logInfo("Wait until [" + elementName + "] attribute [" + attributeName
				+ "] does not contains value [" + attributeValue + "] on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.not(MoreExpectedConditions
					.attributeValueToBeContainedInElement(getBy(), attributeName, attributeValue)));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilContainsAttribute(String attributeName) {
		Logger.logInfo(
				"Wait until [" + elementName + "] contains attribute [" + attributeName + "] on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait()
					.until(MoreExpectedConditions.attributeToBeContainedInElement(getBy(), attributeName));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilDoesNotContainAttribute(String attributeName) {
		Logger.logInfo("Wait until [" + elementName + "] does not contains attribute [" + attributeName
				+ "] on [" + pageName + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions
					.not(MoreExpectedConditions.attributeToBeContainedInElement(getBy(), attributeName)));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public UIElement and() {
		return this;
	}

	public void scrollToView() {
		Logger.logInfo("Scroll [" + elementName + "] to view on [" + pageName + "]");
		String script = "arguments[0].scrollIntoView(true);";
		((JavascriptExecutor) DriverManager.getDriver()).executeScript(script, getWrappedElement());
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
		return null;
	}

	public void assertDisabled() {
		Logger.logInfo("Assert [" + elementName + " is disabled on [" + pageName + "]");
		if (isElementPresent()
				&& ExpectedConditions.elementToBeClickable(by).apply(this.driver) instanceof WebElement) {
			throw new AssertionError(elementName + " is expected to be disabled but it is not");
		}
	}

	public void assertEnabled() {
		Logger.logInfo("Assert [" + elementName + " is enabled on [" + pageName + "]");
		if (!isElementPresent() || ExpectedConditions.elementToBeClickable(by).apply(this.driver) == null) {
			throw new AssertionError(elementName + " is expected to be enabled but is not");
		}
	}

	public void assertClickable() {
		Logger.logInfo("Assert [" + getElementName() + "] is clickable on [" + getPageName() + "]");
		if (!isElementPresent() || ExpectedConditions.elementToBeClickable(by).apply(this.driver) == null) {
			throw new AssertionError(elementName + " is expected to be clickable but is not");
		}
	}

	public void assertNotClickable() {
		Logger.logInfo("Assert [" + getElementName() + "] is not clickable on [" + getPageName() + "]");
		if (isElementPresent()
				&& ExpectedConditions.elementToBeClickable(by).apply(this.driver) instanceof WebElement) {
			throw new AssertionError(elementName + " is expected to be not clickable but is");
		}
	}

	public void waitUntilClickable() {
		Logger.logInfo("Wait until [" + getElementName() + "] is clickable on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.elementToBeClickable(getBy()));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilNotClickable() {
		Logger.logInfo("Wait until [" + getElementName() + "] is not clickable on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait()
					.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(getBy())));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	private boolean isElementPresent() {
		try {
			ExpectedConditions.presenceOfElementLocated(this.by).apply(this.driver);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
