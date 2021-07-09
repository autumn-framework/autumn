package com.dt.autumn.ui.actionFactory.element;

import com.dt.autumn.ui.actionFactory.MoreExpectedConditions;
import com.dt.autumn.ui.driver.DriverManager;
import com.dt.autumn.reporting.extentReport.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class Select extends UIElement {

	public Select(By by, String pageName, String elementName) {
		super(by, pageName, elementName);
	}

	
	public boolean isMultiple() {
		return new org.openqa.selenium.support.ui.Select(getWrappedElement()).isMultiple();
	}

	
	public void deselectByIndex(int index) {
		Logger.logInfo(
				"De-select [" + getElementName() + "] option at position [" + index + "] on [" + getPageName() + "]");
		new org.openqa.selenium.support.ui.Select(getWrappedElement()).deselectByIndex(index);
	}

	
	public void selectByValue(String value) {
		Logger.logInfo(
				"Select [" + getElementName() + "] option with value [" + value + "] on [" + getPageName() + "]");
		new org.openqa.selenium.support.ui.Select(getWrappedElement()).selectByValue(value);
	}

	
	public WebElement getFirstSelectedOption() {
		return new org.openqa.selenium.support.ui.Select(getWrappedElement()).getFirstSelectedOption();
	}

	
    public void selectByVisibleText(String text) {
        Logger.logInfo("Select [" + getElementName() + "] option with text [" + text + "] on [" + getPageName() + "]");
        new org.openqa.selenium.support.ui.Select(getWrappedElement()).selectByVisibleText(text);
    }

	
	public void deselectByValue(String value) {
		Logger.logInfo(
				"De-Select [" + getElementName() + "] option with value [" + value + "] on [" + getPageName() + "]");
		new org.openqa.selenium.support.ui.Select(getWrappedElement()).deselectByValue(value);
	}

	
	public void deselectAll() {
		Logger.logInfo("De-select [" + getElementName() + "] all options on [" + getPageName() + "]");
		new org.openqa.selenium.support.ui.Select(getWrappedElement()).deselectAll();
	}

	
	public List<WebElement> getAllSelectedOptions() {
		return new org.openqa.selenium.support.ui.Select(getWrappedElement()).getAllSelectedOptions();
	}

	
	public List<WebElement> getOptions() {
		return new org.openqa.selenium.support.ui.Select(getWrappedElement()).getOptions();
	}

	
	public void deselectByVisibleText(String text) {
		Logger.logInfo(
				"De-select [" + getElementName() + "] option with text [" + text + "] on [" + getPageName() + "]");
		new org.openqa.selenium.support.ui.Select(getWrappedElement()).deselectByVisibleText(text);
	}

	
	public void selectByIndex(int index) {
		Logger.logInfo(
				"Select [" + getElementName() + "] option at position [" + index + "] on [" + getPageName() + "]");
		new org.openqa.selenium.support.ui.Select(getWrappedElement()).selectByIndex(index);
	}

	
	public void waitUntilSelected() {
		Logger.logInfo("Wait until [" + getElementName() + "] option is selected on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), true));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	
	public void waitUntilDeSelected() {
		Logger.logInfo("Wait until [" + getElementName() + "] option is de-selected on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), false));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	
	public void waitUntilOptionToBeSelectedByVisibeText(String optionText) {
		Logger.logInfo("Wait until [" + getElementName() + "] option with text [" + optionText
				+ "] is selected on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait().until(MoreExpectedConditions.optionToBeSelectedInElement(
					new org.openqa.selenium.support.ui.Select(getWrappedElement()), optionText, true));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	
	public void waitUntilOptionToBeSelectedByValue(String optionValue) {
		Logger.logInfo("Wait until [" + getElementName() + "] option with value [" + optionValue
				+ "] is selected on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait().until(MoreExpectedConditions.optionToBeSelectedInElement(
					new org.openqa.selenium.support.ui.Select(getWrappedElement()), optionValue, false));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}
}