package utilities;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class Validations {
    private WebDriver driver;

    public Validations(WebDriver driver) {
        this.driver = driver;
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
} 