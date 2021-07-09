package com.dt.autumn.ui.actionFactory.element;

import org.openqa.selenium.By;


public class UploadFile extends UIElement {

    public UploadFile(By by, String pageName, String elementName) {
        super(by, pageName, elementName);
    }

    public void sendFilePath(String FilePath) {
        sendKeys(FilePath);

    }
}
