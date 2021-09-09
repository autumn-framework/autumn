package com.autumn.ui.driver;

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
import com.autumn.reporting.extentReport.ExtentManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Vector;

public class DriverManager {

    public static int MAX_PAGE_LOAD_WAIT_TIME = 60;
    public static int MAX_ELEMENT_LOAD_WAIT_TIME = 60;


    private static Vector<WebDriverThread> webDriverThreadPool =
            new Vector<WebDriverThread>();

    private static ThreadLocal<WebDriverThread> driverThread = new ThreadLocal<WebDriverThread>() {
        @Override
        protected WebDriverThread initialValue() {
            WebDriverThread webDriverThread = new WebDriverThread();
            webDriverThreadPool.add(webDriverThread);
            return webDriverThread;
        }
    };

    private static final ThreadLocal<WebDriverWait> webDriverPageWait = new ThreadLocal<WebDriverWait>() {
        @Override
        protected WebDriverWait initialValue() {
            WebDriverWait wait = new WebDriverWait(getDriver(), MAX_PAGE_LOAD_WAIT_TIME);
            return wait;
        }
    };

    private static ThreadLocal<WebDriverWait> webDriverElementWait = new ThreadLocal<WebDriverWait>() {
        @Override
        protected WebDriverWait initialValue() {
            WebDriverWait wait = new WebDriverWait(getDriver(), MAX_ELEMENT_LOAD_WAIT_TIME);
            return wait;
        }
    };

    private static ThreadLocal<Boolean> captureScreenShot = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    };

    private static ThreadLocal<String> platformName = new ThreadLocal<String>();
    private static ThreadLocal<String> browserName = new ThreadLocal<String>();
    private static ThreadLocal<String> mobileEmulation = new ThreadLocal<String>();
    private static ThreadLocal<String> userAgent = new ThreadLocal<String>();
    private static ThreadLocal<String> headless = new ThreadLocal<String>();

    public static WebDriver getDriver() {
        WebDriverThread webDriverThread = driverThread.get();
        if (webDriverThread.getWebDriver() != null) {
            if (!webDriverThread.getBrowser().equalsIgnoreCase(DriverManager.getBrowserName()) ||
                    !webDriverThread.getPlatform().equalsIgnoreCase(DriverManager.getPlatformName()) ||
                    !webDriverThread.getMobileEmulation().equalsIgnoreCase(DriverManager.getMobileEmulation()) ||
                    !webDriverThread.getUserAgent().equalsIgnoreCase(DriverManager.getUserAgent())) {
                webDriverThread.quitDriver();
            }
        }
        ExtentManager.setDriver(driverThread.get().getDriver());
        return driverThread.get().getDriver();
    }


    public static WebDriver getCurrentWebDriver() {
        return driverThread.get().getWebDriver();
    }

    public static String getPlatformName() {
        return platformName.get();
    }

    public static void setPlatformName(String platform) {
        platformName.set(platform);
    }

    public static String getBrowserName() {
        return browserName.get();
    }

    public static void setBrowserName(String browser) {
        browserName.set(browser);
    }

    public static String getMobileEmulation() {
        return mobileEmulation.get();
    }

    public static String getUserAgent() {
        return userAgent.get();
    }

    public static void setMobileEmulation(String mobileEmulationValue) {
        mobileEmulation.set(mobileEmulationValue);
    }

    public static void setUserAgent(String userAgentValue) {
        userAgent.set(userAgentValue);
    }


    public static Boolean getCaptureScreenShot() {
        return captureScreenShot.get();
    }

    public static void setCaptureScreenShot(Boolean captureScreenShot) {
        DriverManager.captureScreenShot.set(captureScreenShot);
    }

    public static WebDriverWait getWebDriverPageWait() {
        return webDriverPageWait.get();
    }

    public static void setWebDriverPageWait(int seconds) {
        webDriverPageWait.set(new WebDriverWait(getDriver(), seconds));
    }

    public static WebDriverWait getWebDriverElementWait() {
        return webDriverElementWait.get();
    }

    public static void setWebDriverElementWait(int seconds) {
        webDriverElementWait.set(new WebDriverWait(getDriver(), seconds));
    }

    public static void resetWebDriverPageWait() {
        if (driverThread.get().getWebDriver() != null)
            webDriverPageWait.set(new WebDriverWait(getDriver(), MAX_PAGE_LOAD_WAIT_TIME));
    }

    public static void resetWebDriverElementWait() {
        if (driverThread.get().getWebDriver() != null)
            webDriverElementWait.set(new WebDriverWait(getDriver(), MAX_ELEMENT_LOAD_WAIT_TIME));
    }

    public static void closeCurrentDriver() {
        driverThread.get().quitDriver();
    }

    public static void closeDriverObjects() {
        for (WebDriverThread webDriverThread : webDriverThreadPool) {
            webDriverThread.quitDriver();
        }
    }

    public static void closeCurrentWindow(){driverThread.get().closeDriver();}

    public static void maximizeWindow(){DriverManager.getDriver().manage().window().maximize();}

    public static void navigateToURL(String pageURL){
        DriverManager.getDriver().get(pageURL);
    }

    public static String getHeadlessMode() {
        return headless.get();
    }

    public static void setHeadlessMode(String headlessValue) {
        headless.set(headlessValue);
    }
}
