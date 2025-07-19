package tests.myInfo;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.PersonalDetailsPage;
import enums.Gender;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class PersonalDetailsTest extends BaseTest {
    private PersonalDetailsPage personalDetailsPage;

    @BeforeMethod
    public void setup() {
        DashboardPage dashboard = loginPage.login("Admin", "admin123");
        dashboard.goToMyInfo();
        personalDetailsPage = new PersonalDetailsPage(driver);
    }

    @Test
    public void testValidPersonalDetailsSections() {
        // Section 1: Personal Details
        personalDetailsPage.updateName("Ahmed", "Ali", "Hassan");
        personalDetailsPage.updateIDs("12345", "67890", "A1234567");
        personalDetailsPage.setLicenseExpiryDate("2025-12-31");
        personalDetailsPage.selectNationality("Egyptian");
        personalDetailsPage.selectMaritalStatus("Single");
        personalDetailsPage.setDateOfBirth("1990-01-01");
        personalDetailsPage.setGender(Gender.MALE);
        personalDetailsPage.clickSaveAfterGender();
        Assert.assertTrue(getToastMessage().contains("Success"), "Success toast should appear after IDs/Gender section save");

        // Section 2: Custom Fields
        personalDetailsPage.enterTestField("Test Value 2");
        personalDetailsPage.selectBloodType("A+");
        personalDetailsPage.clickSaveAfterTestField();
        Assert.assertTrue(getToastMessage().contains("Success"), "Success toast should appear after test field section save");
    }

    @Test
    public void testInvalidPersonalDetailsRequiredFields() {
        personalDetailsPage.updateName("", "Ali", "");
        personalDetailsPage.clickSaveAfterGender();
        Assert.assertEquals(getFieldErrorMessage("firstName"), "Required", "Required error should appear under First Name");
        Assert.assertEquals(getFieldErrorMessage("lastName"), "Required", "Required error should appear under Last Name");
    }

    private String getToastMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement toastElem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".oxd-toast-content"))
        );
        return toastElem.getText();
    }

    private String getFieldErrorMessage(String fieldName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorElem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@name='" + fieldName + "']/following::span[contains(@class,'oxd-input-field-error-message')][1]")
                )
        );
        return errorElem.getText().trim();
    }
}