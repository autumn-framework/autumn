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

import com.autumn.ui.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;

public class UIElements {

	public static List<UIElement> getMultiple(By by, String pageName, String elementName) {
		int index = 0;
		List<UIElement> list = new ArrayList<>();
		WebDriver driver = DriverManager.getDriver();
		List<WebElement> elements = driver.findElements(by);
		for (WebElement ele : elements) {
			list.add(new UIElement(ele, pageName, elementName + "[" + index + "]"));
		}
		return list;
	}

	public static List<Button> getButtons(By by, String pageName, String elementName) {
		int index = 0;
		List<Button> list = new ArrayList<>();
		WebDriver driver = DriverManager.getDriver();
		List<WebElement> elements = driver.findElements(by);
		for (WebElement ele : elements) {
			list.add(new Button(ele, pageName, elementName + "[" + index + "]"));
		}
		return list;
	}

	public static List<CheckBox> getCheckBoxes(By by, String pageName, String elementName) {
		int index = 0;
		List<CheckBox> list = new ArrayList<>();
		WebDriver driver = DriverManager.getDriver();
		List<WebElement> elements = driver.findElements(by);
		for (WebElement ele : elements) {
			list.add(new CheckBox(ele, pageName, elementName + "[" + index + "]"));
		}
		return list;
	}

	public static List<RadioButton> getRadioButton(By by, String pageName, String elementName) {
		int index = 0;
		List<RadioButton> list = new ArrayList<>();
		WebDriver driver = DriverManager.getDriver();
		List<WebElement> elements = driver.findElements(by);
		for (WebElement ele : elements) {
			list.add(new RadioButton(ele, pageName, elementName + "[" + index + "]"));
		}
		return list;
	}

}
