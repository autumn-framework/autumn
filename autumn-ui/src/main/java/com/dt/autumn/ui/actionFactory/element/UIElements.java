package com.dt.autumn.ui.actionFactory.element;

import com.dt.autumn.ui.driver.DriverManager;
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