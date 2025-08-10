package tests.myInfo;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.PersonalDetailsPage;
import pages.LoginPage;
import enums.Gender;

@Epic("OrangeHRM Application")
@Feature("My Info")
@Story("Personal Details")
public class PersonalDetailsTest {
    
    private GUIDriver driver;
    private PersonalDetailsPage personalDetailsPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Personal Details page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        dashboard.goToMyInfo();
        personalDetailsPage = new PersonalDetailsPage(driver);
    }

    @Test(groups = {"myinfo", "personal", "smoke", "regression"})
    @Story("Valid Personal Details")
    @Description("Test updating personal details with valid information")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidPersonalDetailsSections() {
        // Section 1: Personal Details using Method Chaining
        personalDetailsPage.enterFirstName("Ahmed")
                          .enterMiddleName("Ali")
                          .enterLastName("Hassan")
                          .enterEmployeeId("12345")
                          .enterOtherId("67890")
                          .enterLicenseNumber("A1234567")
                          .setLicenseExpiryDate("2025-12-31")
                          .selectNationality("Egyptian")
                          .selectMaritalStatus("Single")
                          .setDateOfBirth("1990-01-01")
                          .setGender(Gender.MALE)
                          .clickSaveAfterGender()
                          .assertSuccessToastDisplayed();

        // Section 2: Custom Fields using Method Chaining
        personalDetailsPage.enterTestField("Test Value 2")
                          .selectBloodType("A+")
                          .clickSaveAfterTestField()
                          .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "personal", "regression"})
    @Story("Invalid Personal Details")
    @Description("Test personal details validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidPersonalDetailsRequiredFields() {
        personalDetailsPage.enterFirstName("")
                          .enterMiddleName("Ali")
                          .enterLastName("")
                          .clickSaveAfterGender()
                          .assertSpecificErrorDisplayed("Required");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }
}