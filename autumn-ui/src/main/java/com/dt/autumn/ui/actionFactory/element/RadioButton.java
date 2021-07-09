package com.dt.autumn.ui.actionFactory.element;

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

import com.dt.autumn.reporting.assertions.CustomAssert;
import com.dt.autumn.ui.driver.DriverManager;
import com.dt.autumn.reporting.extentReport.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RadioButton extends UIElement {

	@Deprecated
	public RadioButton(By by, String pageName) {
		super(by, pageName);
	}

	public RadioButton(By by, String pageName, String elementName) {
		super(by, pageName, elementName);
	}

	public RadioButton(WebElement webElement, String pageName, String elementName) {
		super(webElement, pageName, elementName);
	}

	public void select() {
		Logger.logInfo("Select [" + getElementName() + "] on [" + getPageName() + "]");
		if (!isSelected()) {
			click();
		}
	}

	public boolean isSelected() {
		return getWrappedElement().isSelected();
	}

	public void assertSelected() {
		CustomAssert.assertTrue(getWrappedElement().isSelected(),"Assert [" + getElementName() + "] is selected on [" + getPageName() + "]");
	}

	public void assertDeSelected() {
		CustomAssert.assertFalse(getWrappedElement().isSelected(),"Assert [" + getElementName() + "] is not selected on [" + getPageName() + "]");
	}

	public void waitUntilSelected() {
		Logger.logInfo("Wait until [" + getElementName() + "] is selected on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), true));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

	public void waitUntilDeSelected() {
		Logger.logInfo("Wait until [" + getElementName() + "] is not selected on [" + getPageName() + "]");
		try {
			DriverManager.getWebDriverElementWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), false));
		} catch (TimeoutException e) {
			// swallowing the exception as this function is meant to be used as pre-step to
			// a main-step, so no need to fail it if we get TimeOutException
		}
	}

}
