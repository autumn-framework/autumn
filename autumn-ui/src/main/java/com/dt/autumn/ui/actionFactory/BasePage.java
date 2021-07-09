package com.dt.autumn.ui.actionFactory;

import com.dt.autumn.reporting.assertions.CustomAssert;
import com.dt.autumn.ui.driver.DriverManager;
import com.dt.autumn.reporting.extentReport.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

public abstract class BasePage {

	protected String pageName;
	protected String pageURL;

	public BasePage(String pageName) {
		this.pageName = pageName;
	}

	public String getPageURL() {
		return this.pageURL;
	}

	public String getPageName() {
		return this.pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}

	public void launch() {
		Logger.logInfo("Launch URL [{}]" + pageURL);
		DriverManager.navigateToURL(this.pageURL);
	}

	public void maximizeWindow(){
		DriverManager.maximizeWindow();
	}

	public void quitDriver(){
		DriverManager.closeCurrentDriver();
	}
	public void quitAllDrivers(){
		DriverManager.closeDriverObjects();
	}
	public void closeDriver(){
		DriverManager.closeCurrentWindow();
	}

	public void assertContainsText(String text) {
		CustomAssert.assertEquals(DriverManager.getDriver().findElement(By.tagName("body")).getText(),text,"Assert [{}] contains " + text + " [{}] for " + pageName);
	}

	public void assertDoesNotContainText(String text) {
		CustomAssert.assertNotEquals(DriverManager.getDriver().findElement(By.tagName("body")).getText(),text,"Assert [{}] doesn't contain " + text + " [{}] for " + pageName);
	}

	public void assertContainsTitle(String title) {
		CustomAssert.assertEqualsIgnoreCase(DriverManager.getDriver().getTitle(),title,"Assert [{}] contains " + title + " [{}] for " + pageName);
	}

	public void waitUntilLoads() {
		Logger.logInfo("Wait until [{}] loads for " + pageName);
		int timeToWaitTillDocumentStartsLoading = 5;
		try {
			// It is important to first wait till the document has started loading otherwise
			// tests fail intermittently
			// when the execution is fast on some browser/environment. e.g. if clicking on a
			// link results in a
			// page load & we have a waitForPageLoad after click then if the document hasn't
			// started to load, it will
			// assume that the document is ready
			DriverManager.setWebDriverPageWait(timeToWaitTillDocumentStartsLoading);
			DriverManager.getWebDriverPageWait().until(MoreExpectedConditions.documentIsLoading());
			DriverManager.setWebDriverPageWait(
					DriverManager.MAX_PAGE_LOAD_WAIT_TIME - timeToWaitTillDocumentStartsLoading);
			DriverManager.getWebDriverPageWait().until(MoreExpectedConditions.documentIsReady());
		} catch (Throwable e) {
			// Do nothing since we don't want to fail a test if the page hasn't loaded
			// completely
		} finally {
			DriverManager.resetWebDriverPageWait();
		}
	}

	public void waitUntilContainsTitle(final String title) {
		Logger.logInfo("Wait until [{}] contains " + title + " [{}] for " + pageName);
		DriverManager.getWebDriverPageWait().until(ExpectedConditions.titleContains(title));
	}

	public void waitUntilContainsText(final String text) {
		Logger.logInfo("Wait until [{}] contains " + text + " [{}] for " + pageName);
		DriverManager.getWebDriverPageWait()
				.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), text));
	}

	public void waitUntilDoesNotContainText(final String text) {
		Logger.logInfo("Wait until [{}] doesn't contain " + text + " [{}] for " + pageName);
		DriverManager.getWebDriverPageWait().until(
				ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), text)));
	}

	public void waitUntilAllAJAXCallsFinish() {
		Logger.logInfo("Wait until all ajax requests complete on [{}]" + pageName);
		WebDriverWait wait = DriverManager.getWebDriverPageWait();
		long timeOutInMilliSeconds = DriverManager.MAX_PAGE_LOAD_WAIT_TIME * 1000;
		long startTime = System.currentTimeMillis();
		try {
			if (timeOutInMilliSeconds > 0) {
				wait.withTimeout(timeOutInMilliSeconds, TimeUnit.MILLISECONDS)
						.until(MoreExpectedConditions.jQueryAJAXCallsHaveCompleted());
			}
			pause(1);

			timeOutInMilliSeconds = timeOutInMilliSeconds - (System.currentTimeMillis() - startTime);
			startTime = System.currentTimeMillis();
			if (timeOutInMilliSeconds > 0) {
				wait.withTimeout(timeOutInMilliSeconds, TimeUnit.MILLISECONDS)
						.until(MoreExpectedConditions.jQueryAJAXCallsHaveCompleted());
			}
			pause(1);

			timeOutInMilliSeconds = timeOutInMilliSeconds - (System.currentTimeMillis() - startTime);
			if (timeOutInMilliSeconds > 0) {
				wait.withTimeout(timeOutInMilliSeconds, TimeUnit.MILLISECONDS)
						.until(MoreExpectedConditions.jQueryAJAXCallsHaveCompleted());
			}

		} catch (Throwable e) {
			// Do nothing since we don't want to fail a test case in case of ongoing request
		}
	}

	public void waitUntilAngularProcessingFinish() {
		Logger.logInfo("Wait until angular js has finished processing on [{}] for " + pageName);
		DriverManager.getWebDriverPageWait().until(MoreExpectedConditions.angularHasFinishedProcessing());
	}

	public void waitUntilFrameAppearsAndSwitchToIt(String frameLocator) {
		Logger.logInfo("Wait until frame with locator [" + frameLocator + "] appears and switch to it on [{}]" + pageName);
		DriverManager.getWebDriverPageWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}

//	public void waitUntilFrameAppearsAndSwitchToIt(UIElement frameElement) {
//		Logger.logInfo(
//				"Wait until frame element [" + frameElement.getElementName() + "] appears and switch to it on [{}]",
//				pageName);
//		DriverManager.getWebDriverPageWait()
//				.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement.getWrappedElement()));
//	}

	public void pause(int seconds) {
		Logger.logInfo("Pause for [" + seconds + "] seconds");
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void assertPageURL(String url) {
		CustomAssert.assertEqualsIgnoreCase(DriverManager.getDriver().getCurrentUrl(),url,"Assert [" + pageName + "] URL [" + url + "]");
	}

	public void assertPageContainsURL(String url) {
		CustomAssert.assertEqualsIgnoreCase(DriverManager.getDriver().getCurrentUrl(),url,"Assert [" + pageName + "] URL contains [" + url + "]");
	}

	public void assertPageURL() {
		String url = DriverManager.getDriver().getCurrentUrl();
		CustomAssert.assertEqualsIgnoreCase(url,getPageURL(),"Assert [" + pageName + "] URL [" + url + "]");
	}

	public void assertPageContainsURL() {
		String url = DriverManager.getDriver().getCurrentUrl();
		CustomAssert.assertEqualsIgnoreCase(url,getPageURL(),"Assert [" + pageName + "] URL contains [" + url + "]");
	}

	public void refresh() {
		Logger.logInfo("Refresh [" + pageName + "]");
		DriverManager.getDriver().navigate().refresh();
		waitUntilLoads();
	}

	public void navigateBack() {
		Logger.logInfo("Navigate back [" + pageName + "]");
		DriverManager.getDriver().navigate().back();
		waitUntilLoads();
	}

	public void navigateForward() {
		Logger.logInfo("Navigate forward [" + pageName + "]");
		DriverManager.getDriver().navigate().forward();
		waitUntilLoads();
	}

}