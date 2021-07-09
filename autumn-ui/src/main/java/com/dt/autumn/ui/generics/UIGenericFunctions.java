package com.dt.autumn.ui.generics;

import com.dt.autumn.ui.driver.DriverManager;

public class UIGenericFunctions {

    public void setBrowserDetails(String browser, String mobileEmulation, String userAgent, String platform, String browserHeadless, String environment){
        DriverManager.setBrowserName(browser);
        DriverManager.setMobileEmulation(mobileEmulation);
        DriverManager.setUserAgent(userAgent);
        DriverManager.setPlatformName(platform);
        DriverManager.setHeadlessMode(browserHeadless);
    }


}
