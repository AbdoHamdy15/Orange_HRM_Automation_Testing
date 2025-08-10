package utilities;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class BrowserActions {
    private WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigating to URL: {url}")
    public void navigateToURL(String url) {
        driver.get(url);
        System.out.println("Navigated to URL: " + url);
    }

    @Step("Getting current URL")
    public String getCurrentURL() {
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        return currentUrl;
    }

    @Step("Getting page title")
    public String getPageTitle() {
        String pageTitle = driver.getTitle();
        System.out.println("Page title: " + pageTitle);
        return pageTitle;
    }

    @Step("Refreshing the page")
    public void refreshPage() {
        System.out.println("Refreshing the page");
        driver.navigate().refresh();
    }

    @Step("Closing the browser")
    public void closeBrowser() {
        System.out.println("Closing the browser");
        if (driver != null) {
            driver.quit();
        }
    }
} 