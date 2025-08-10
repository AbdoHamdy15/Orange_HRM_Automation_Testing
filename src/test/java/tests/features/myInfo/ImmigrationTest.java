package tests.myInfo;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.ImmigrationPage;
import pages.LoginPage;

@Epic("OrangeHRM Application")
@Feature("My Info")
@Story("Immigration")
public class ImmigrationTest {
    
    private GUIDriver driver;
    private ImmigrationPage immigrationPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Immigration page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        immigrationPage = dashboard.navigateToImmigration();
    }

    @Test(groups = {"myinfo", "immigration", "regression"})
    @Story("Valid Immigration Record")
    @Description("Test adding immigration record with valid information")
    @Severity(SeverityLevel.NORMAL)
    public void testValidImmigrationRecord() {
        immigrationPage.clickAdd()
                      .enterNumber("P123456789")
                      .setIssueDate("2020-01-01")
                      .setExpiryDate("2030-01-01")
                      .selectIssuedBy("Egypt")
                      .clickSave()
                      .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "immigration", "regression"})
    @Story("Invalid Immigration Record")
    @Description("Test immigration record validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidImmigrationRecord() {
        immigrationPage.clickAdd()
                      .enterNumber("")
                      .setIssueDate("")
                      .clickSave()
                      .assertSpecificErrorDisplayed("Required");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }
} 