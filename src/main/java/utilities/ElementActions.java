package utilities;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Keys;

public class ElementActions {
    private WebDriver driver;
    private Waits waits;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    // ==================== CLICK METHODS ====================

    // Click element with wait
    @Step("Clicking on the element: {locator}")
    public void click(By locator) {
        scrollToElement(locator);
        findElement(locator).click();
    }

    // ==================== TEXT INPUT METHODS ====================

    // Type text into element
    @Step("Sending data: {data} to the element: {locator}")
    public void type(By locator, String data) {
        scrollToElement(locator);
        WebElement element = findElement(locator);
        element.click();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(data);
    }

    // ==================== TEXT RETRIEVAL METHODS ====================

    // Get text from element
    @Step("Getting text from the element: {locator}")
    public String getText(By locator) {
        waits.waitForElementVisible(locator);
        scrollToElement(locator);
        return findElement(locator).getText().trim();
    }

    // ==================== DROPDOWN METHODS ====================

    // Select by visible text
    @Step("Selecting by visible text: {text} from element: {locator}")
    public void selectByVisibleText(By locator, String text) {
        scrollToElement(locator);
        new Select(findElement(locator)).selectByVisibleText(text);
    }

    // Select by value
    @Step("Selecting by value: {value} from element: {locator}")
    public void selectByValue(By locator, String value) {
        scrollToElement(locator);
        new Select(findElement(locator)).selectByValue(value);
    }

    // Select by index
    @Step("Selecting by index: {index} from element: {locator}")
    public void selectByIndex(By locator, int index) {
        scrollToElement(locator);
        new Select(findElement(locator)).selectByIndex(index);
    }

    // Get selected option text
    @Step("Getting selected option text from element: {locator}")
    public String getSelectedOptionText(By locator) {
        scrollToElement(locator);
        return new Select(findElement(locator)).getFirstSelectedOption().getText().trim();
    }

    // ==================== CHECKBOX AND RADIO METHODS ====================

    // Check checkbox
    @Step("Checking checkbox: {locator}")
    public void check(By locator) {
        scrollToElement(locator);
        WebElement element = findElement(locator);
        if (!element.isSelected()) {
            element.click();
        }
    }

    // Uncheck checkbox
    @Step("Unchecking checkbox: {locator}")
    public void uncheck(By locator) {
        scrollToElement(locator);
        WebElement element = findElement(locator);
        if (element.isSelected()) {
            element.click();
        }
    }

    // Toggle checkbox
    @Step("Toggling checkbox: {locator}")
    public void toggle(By locator) {
        scrollToElement(locator);
        findElement(locator).click();
    }

    // Check if element is selected
    public boolean isSelected(By locator) {
        scrollToElement(locator);
        return findElement(locator).isSelected();
    }

    // ==================== ELEMENT STATE METHODS ====================

    // Check if element is displayed
    public boolean isDisplayed(By locator) {
        try {
            scrollToElement(locator);
            return findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if element is enabled
    public boolean isEnabled(By locator) {
        try {
            scrollToElement(locator);
            return findElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== SCROLL METHODS ====================

    // Scroll to element
    @Step("Scrolling to the element: {locator}")
    public void scrollToElement(By locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", findElement(locator));
    }

    // ==================== FILE UPLOAD METHODS ====================

    // Upload file
    @Step("Uploading file: {filePath} to element: {locator}")
    public void uploadFile(By locator, String filePath) {
        scrollToElement(locator);
        findElement(locator).sendKeys(filePath);
    }

    // Find element (no wait, no logging)
    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

} 