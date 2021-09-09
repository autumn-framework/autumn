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

import com.autumn.reporting.assertions.CustomAssert;
import com.autumn.ui.driver.DriverManager;
import com.autumn.reporting.extentReport.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckBox extends UIElement {

	@Deprecated
	public CheckBox(By by, String pageName) {
		super(by, pageName);
	}

	public CheckBox(By by, String pageName, String elementName) {
		super(by, pageName, elementName);
	}

	public CheckBox(WebElement webElement, String pageName, String elementName) {
		super(webElement, pageName, elementName);
	}

	public void toggle() {
		Logger.logInfo("Click [" + getElementName() + "] on [" + getPageName() + "]");
		click();
	}

	public void check() {
		Logger.logInfo("Check [" + getElementName() + "] on [" + getPageName() + "]");
		if (!isChecked()) {
			toggle();
		}
	}

	public void unCheck() {
		Logger.logInfo("Uncheck [" + getElementName() + "] on [" + getPageName() + "]");
		if (isChecked()) {
			toggle();
		}
	}

	public boolean isChecked() {
		return getWrappedElement().isSelected();
	}

	public void assertChecked() {
		CustomAssert.assertTrue(isChecked(),"Assert [" + getElementName() + "] is checked on [" + getPageName() + "]");
	}

	public void assertUnChecked() {
		CustomAssert.assertFalse(isChecked(),"Assert [" + getElementName() + "] is un-checked on [" + getPageName() + "]");
	}

	public void waitUntilChecked() {
		Logger.logInfo("Wait until [" + getElementName() + "] is checked on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), true));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilUnChecked() {
		Logger.logInfo("Wait until [" + getElementName() + "] is un-checked on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), false));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}
}
