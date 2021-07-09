package com.dt.autumn.ui.actionFactory.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Button extends UIElement {

	@Deprecated
	public Button(By by, String pageName) {
		super(by, pageName);
	}

	public Button(By by, String pageName, String elementName) {
		super(by, pageName, elementName);
	}

	public Button(WebElement webElement, String pageName, String elementName) {
		super(webElement, pageName, elementName);
	}

	public void clickButton() {
		waitUntilClickable();
		click();
	}

}
