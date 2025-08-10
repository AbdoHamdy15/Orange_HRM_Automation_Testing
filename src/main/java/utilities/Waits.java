package utilities;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waits {
    private WebDriver driver;

    public Waits(WebDriver driver) {
        this.driver = driver;
    }

    // ==================== ESSENTIAL WAIT METHODS ====================

    // Wait for element to be present
    @Step("Waiting for element to be present: {locator}")
    public WebElement waitForElementPresent(By locator) {
        LogsUtil.info("Waiting for element to be present: " + locator.toString());
        return new WebDriverWait(driver, Duration.ofSeconds(6))
                .until(driver1 -> driver1.findElement(locator));
    }

    // Wait for element to be visible
    @Step("Waiting for element to be visible: {locator}")
    public WebElement waitForElementVisible(By locator) {
        LogsUtil.info("Waiting for element to be visible: " + locator.toString());
        return new WebDriverWait(driver, Duration.ofSeconds(6))
                .until(driver1 -> {
                    WebElement element = waitForElementPresent(locator);
                    return element.isDisplayed() ? element : null;
                });
    }

    // Wait for element to be clickable
    @Step("Waiting for element to be clickable: {locator}")
    public WebElement waitForElementClickable(By locator) {
        LogsUtil.info("Waiting for element to be clickable: " + locator.toString());
        return new WebDriverWait(driver, Duration.ofSeconds(6))
                .until(driver1 -> {
                    WebElement element = waitForElementVisible(locator);
                    return element.isEnabled() ? element : null;
                });
    }

    // Wait for toast message
    @Step("Waiting for toast message and getting text")
    public String waitForToastAndGetMessage() {
        By toastLocator = By.cssSelector(".oxd-toast-content");
        LogsUtil.info("Waiting for toast message to be present: " + toastLocator.toString());
        try {
            WebElement toastElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(driver1 -> {
                        try {
                            WebElement element = driver1.findElement(toastLocator);
                            return element.isDisplayed() ? element : null;
                        } catch (Exception e) {
                            return null;
                        }
                    });
            return toastElement.getText().trim();
        } catch (Exception e) {
            return null;
        }
    }

    // Navigate to page with JavaScript click and wait for header
    @Step("Navigate to page: {pageName}")
    public void navigateToPage(By menuLocator, String pageName) {
        WebElement element = waitForElementVisible(menuLocator);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        waitForElementVisible(By.xpath("//h6[contains(@class,'oxd-text--h6') and text()='" + pageName + "']"));
    }

    // Wait for element to be invisible
    @Step("Waiting for element to be invisible: {locator}")
    public void waitForElementInvisible(By locator) {
        LogsUtil.info("Waiting for element to be invisible: " + locator.toString());
        new WebDriverWait(driver, Duration.ofSeconds(6))
                .until(driver1 -> {
                    try {
                        WebElement element = driver1.findElement(locator);
                        return !element.isDisplayed();
                    } catch (Exception e) {
                        return true; // Element not found means it's invisible
                    }
                });
    }
}
