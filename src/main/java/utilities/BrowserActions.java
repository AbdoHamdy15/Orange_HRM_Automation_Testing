package utilities;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class BrowserActions {
    private static WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigating to URL: {url}")
    public void navigateToURL(String url) {
        driver.get(url);
        LogsUtil.info("Navigated to URL: ", url);
    }

    @Step("Getting current URL")
    public String getCurrentURL() {
        LogsUtil.info("Current URL: ", driver.getCurrentUrl());
        return driver.getCurrentUrl();

    }

    @Step("Getting page title")
    public String getPageTitle() {
        LogsUtil.info("Page title: ", driver.getTitle());
        return driver.getTitle();
    }

    @Step("Refreshing the page")
    public void refreshPage() {
        LogsUtil.info("Refreshing the page");
        driver.navigate().refresh();
    }

    @Step("Closing the browser")
    public void closeBrowser() {
        LogsUtil.info("Closing the browser");
        if (driver != null) {
            driver.quit();
        }
    }
}