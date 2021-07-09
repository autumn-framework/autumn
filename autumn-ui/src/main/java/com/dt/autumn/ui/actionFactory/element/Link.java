package com.dt.autumn.ui.actionFactory.element;

import org.openqa.selenium.By;

public class Link extends UIElement {

    @Deprecated
    public Link(By by, String pageName) {
        super(by, pageName);
    }

    public Link(By by, String pageName, String elementName) {
        super(by, pageName, elementName);
    }

    public void clickLink() {
        waitUntilClickable();
        click();
    }

}
