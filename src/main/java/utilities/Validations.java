package utilities;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class Validations {
    private WebDriver driver;
    private BrowserActions browserActions;

    public Validations(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(driver);
    }

    @Step("Validate True")
    public void validateTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    @Step("Validate False")
    public void validateFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }

    @Step("Validate Equals")
    public void validateEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    @Step("Validate Not Equals")
    public void validateNotEquals(String actual, String expected, String message) {
        Assert.assertNotEquals(actual, expected, message);
    }

    @Step("Validate Contains")
    public void validateContains(String actual, String expected, String message) {
        Assert.assertTrue(actual.contains(expected), message);
    }

    @Step("Validate Not Contains")
    public void validateNotContains(String actual, String expected, String message) {
        Assert.assertFalse(actual.contains(expected), message);
    }

    @Step("Validate Not Null")
    public void validateNotNull(Object object, String message) {
        Assert.assertNotNull(object, message);
    }

    @Step("Validate Is Null")
    public void validateIsNull(Object object, String message) {
        Assert.assertNull(object, message);
    }

    @Step("Validate Page Url: {expected}")
    public void validatePageUrl(String expected) {
        Assert.assertEquals(browserActions.getCurrentURL(), expected);
    }

    @Step("Validate Page Title: {expected}")
    public void validatePageTitle(String expected) {
        Assert.assertEquals(browserActions.getPageTitle(), expected);
    }

    @Step("Validate Error Message Displayed: {errorText}")
    public boolean isErrorMessageDisplayed(String errorText) {
        try {
            // Check for error messages with specific classes
            org.openqa.selenium.By errorLocator = org.openqa.selenium.By.xpath("//*[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-text--toast-message') or contains(@class,'error') or contains(@class,'invalid')]");
            java.util.List<org.openqa.selenium.WebElement> errorElements = driver.findElements(errorLocator);
            
            for (org.openqa.selenium.WebElement elem : errorElements) {
                try {
                    if (elem.isDisplayed() && elem.getText().trim().equalsIgnoreCase(errorText)) {
                        LogsUtil.info("Error message found: " + errorText);
                        return true;
                    }
                } catch (org.openqa.selenium.StaleElementReferenceException e) {
                    // Element became stale, continue to next element
                    continue;
                }
            }
            
            // Check for any element with exact text
            org.openqa.selenium.By exactTextLocator = org.openqa.selenium.By.xpath("//*[text()='" + errorText + "']");
            java.util.List<org.openqa.selenium.WebElement> allElements = driver.findElements(exactTextLocator);
            for (org.openqa.selenium.WebElement elem : allElements) {
                try {
                    if (elem.isDisplayed()) {
                        LogsUtil.info("Error message found: " + errorText);
                        return true;
                    }
                } catch (org.openqa.selenium.StaleElementReferenceException e) {
                    // Element became stale, continue to next element
                    continue;
                }
            }
            
            LogsUtil.info("Error message not found: " + errorText);
            return false;
        } catch (Exception e) {
            LogsUtil.error("Error checking for error message: " + e.getMessage());
            return false;
        }
    }

    @Step("Get actual error text for expected error: {expectedError}")
    public String getActualErrorText(String expectedError) {
        switch (expectedError) {
            case "Required":
                return "Required";
            case "MaxLength":
                return "Should not exceed 30 characters";
            case "EmployeeIdExists":
                return "Employee Id already exists";
            case "EmployeeIdMaxLength":
                return "Should not exceed 10 characters";
            case "UsernameExists":
                return "Already exists";
            case "UsernameMinLength":
                return "Should be at least 5 characters";
            case "UsernameMaxLength":
                return "Should not exceed 40 characters";
            case "PasswordMinLength":
                return "Should have at least 7 characters";
            case "PasswordMaxLength":
                return "Should not exceed 64 characters";
            case "PasswordMismatch":
                return "Passwords do not match";
            case "PasswordMustContainNumber":
                return "Your password must contain minimum 1 number";
            case "PasswordMustContainLowercase":
                return "Your password must contain minimum 1 lower-case letter";
            case "Invalid":
                return "Invalid";
            case "Should not exceed 30 characters":
                return "Should not exceed 30 characters";
            case "Should not exceed 25 characters":
                return "Should not exceed 25 characters";
            case "Should not exceed 250 characters":
                return "Should not exceed 250 characters";
            case "Expected format: admin@example.com":
                return "Expected format: admin@example.com";
            case "Allows numbers and only + - / ( )":
                return "Allows numbers and only + - / ( )";
            default:
                return expectedError;
        }
    }
} 