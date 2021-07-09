package com.dt.autumn.ui.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

class WebDriverThread {

    private WebDriver webDriver;
    private DesiredCapabilities capabilities;
    private String browser;
    private String platform;
    private String mobileEmulation;
    private String userAgent;
    public static String EXECUTION_ENVIRONMENT = System.getProperty("EXECUTION_ENVIRONMENT", "local");

    private void setPlatform() {
        if (EXECUTION_ENVIRONMENT.equalsIgnoreCase("remote")) {
            switch (platform.toUpperCase()) {
                case "LINUX":
                    capabilities.setPlatform(Platform.LINUX);
                    break;
                case "MAC":
                    capabilities.setPlatform(Platform.MAC);
                    break;
                case "WINDOWS":
                    capabilities.setPlatform(Platform.WINDOWS);
                    break;
                default:
                    throw new RuntimeException("Invalid execution environment: " + platform);
            }
        }
    }

    public WebDriver getDriver() {

        if (null == webDriver || ((RemoteWebDriver) webDriver).getSessionId() == null) {
            browser =DriverManager.getBrowserName();
            platform = DriverManager.getPlatformName();
            mobileEmulation = DriverManager.getMobileEmulation();
            userAgent = DriverManager.getUserAgent();

            switch (browser.toUpperCase()) {

                case "CHROME":
                    capabilities = DesiredCapabilities.chrome();
                    setPlatform();
                    LoggingPreferences logPrefs = new LoggingPreferences();
                    logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
                    capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
                    capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
                    HashMap<String, String> chromePreferences = new HashMap<String, String>();
                    chromePreferences.put("profile.password_manager_enabled", "false");
                    chromePreferences.put("profile.default_content_settings.popups", "0");
                    chromePreferences.put("download.prompt_for_download", "false");
                    chromePreferences.put("download.default_directory", System.getProperty("user.dir"));
                    capabilities.setCapability("chrome.prefs", chromePreferences);
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--test-type");
                    options.addArguments("start-maximized");
                    options.addArguments("--disable-web-security");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-extensions");
                    options.addArguments("--allow-running-insecure-content");
                    HashMap<String, Object> chromePrefs = new HashMap<>();
                    chromePrefs.put("profile.default_content_settings.popups", 0);
                    chromePrefs.put("intl.accept_languages", "English");
                    options.setExperimentalOption("prefs", chromePrefs);
                    options.addArguments("enable-automation");
                    options.addArguments("--disable-notifications");
                    options.addArguments("--dns-prefetch-disable");
                    options.addArguments("disable-infobars");
                    options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    if(DriverManager.getHeadlessMode().equalsIgnoreCase("true")){
                        options.addArguments("--headless");
                        options.addArguments("--window-size=1920,1200");
                        options.addArguments("--disable-gpu");
                    }
                    // options.addArguments("disable-infobars");
                    if (!mobileEmulation.isEmpty()) {
                        Map<String, String> mobileEmulationMap = new HashMap<String, String>();
                        mobileEmulationMap.put("deviceName", mobileEmulation);
                        options.setExperimentalOption("mobileEmulation", mobileEmulationMap);
                        options.addArguments("--user-agent=" + userAgent);
                    }
                    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                    if (!EXECUTION_ENVIRONMENT.equalsIgnoreCase("remote")) {
                        WebDriverManager.chromedriver().setup();
                        webDriver = new ChromeDriver(options);
                    }

                    break;

            }
            webDriver.manage().timeouts().pageLoadTimeout(120000L, TimeUnit.MILLISECONDS);
            webDriver.manage().timeouts().setScriptTimeout(10000L, TimeUnit.MILLISECONDS);
            webDriver.manage().timeouts().implicitlyWait(30000L, TimeUnit.MILLISECONDS);
            webDriver.manage().window().maximize();
            DriverManager.setWebDriverElementWait(DriverManager.MAX_ELEMENT_LOAD_WAIT_TIME);
        }
        return webDriver;
    }

    public void quitDriver() {
        if (null != webDriver) {
            webDriver.quit();
            webDriver = null;
        }
    }

    public void closeDriver() {
        if (null != webDriver) {
            webDriver.close();
        }
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public String getBrowser() {
        return browser;
    }

    public String getPlatform() {
        return platform;
    }

    public String getMobileEmulation() {
        return mobileEmulation;
    }

    public String getUserAgent() {
        return userAgent;
    }
}