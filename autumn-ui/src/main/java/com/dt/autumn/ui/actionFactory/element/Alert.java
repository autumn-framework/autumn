package com.dt.autumn.ui.actionFactory.element;

import com.dt.autumn.reporting.extentReport.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * This class provides the methods for handling of Alert related actions.
 */
public class Alert extends UIElement {
	/**
	 * Class constructor sets Element name as "Alert".
	 *
	 * @param by       The By object specifies the locator.
	 * @param pageName The name of the Page.
	 */
	@Deprecated
	public Alert(By by, String pageName) {
		super(by, pageName);
	}

	public Alert(By by, String pageName, String elementName) {
		super(by, pageName, elementName);
	}

	public void accept() {
		Logger.logInfo("Accept alert [" + getElementName() + " on [" + getPageName() + "]");
		WebElement button = getWrappedElement().findElement(By.tagName("button"));

		try {
			button.click();
		} catch (WebDriverException e) {
			if (e.getMessage().contains("unknown error: Element is not clickable at point")) {
				Logger.logInfo("Retrying due to the error: Element is not clickable at point(X, Y)");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				button.click();
			} else {
				throw e;
			}
		}
	}

	public void close() {
		Logger.logInfo("Close alert [" + getElementName() + " on [" + getPageName() + "]");
		WebElement cross = getWrappedElement().findElement(By.cssSelector(".ui-icon.ui-icon-closethick"));
		try {
			cross.click();
		} catch (WebDriverException e) {
			if (e.getMessage().contains("unknown error: Element is not clickable at point")) {
				Logger.logInfo("Retrying due to the error: Element is not clickable at point(X, Y)<br>");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				cross.click();
			} else {
				throw e;
			}
		}
	}

}