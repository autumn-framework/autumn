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

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class TextBox extends UIElement {

	@Deprecated
	public TextBox(By by, String pageName) {
		super(by, pageName);
	}

	public TextBox(By by, String pageName, String elementName) {
		super(by, pageName, elementName);
	}

	public void clearAndType(CharSequence... keysToSend) {
		clear();
		type(keysToSend);
	}

	/*
	 * This method can be used when there is a popup with a mouse click on the text
	 * field. 'Esc' will close the popup and will allow the set the text value.
	 */

	public void clearEscAndType(CharSequence... keysToSend) {
		waitUntilVisible();
		clear();
		getWrappedElement().sendKeys(Keys.ESCAPE);
		type(keysToSend);
	}

	public void type(CharSequence... keysToSend) {
		sendKeys(keysToSend);
	}

	public void sendData(String data) {
		waitUntilVisible();
		clearEscAndType(data);
	}
}
