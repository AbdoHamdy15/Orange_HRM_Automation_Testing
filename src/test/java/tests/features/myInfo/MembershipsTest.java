package tests.myInfo;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.MembershipsPage;
import pages.LoginPage;

@Epic("OrangeHRM Application")
@Feature("My Info")
@Story("Memberships")
public class MembershipsTest {
    
    private GUIDriver driver;
    private MembershipsPage membershipsPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Memberships page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        membershipsPage = dashboard.navigateToMemberships();
    }

    @Test(groups = {"myinfo", "memberships", "regression"})
    @Story("Valid Membership")
    @Description("Test adding membership with valid information")
    @Severity(SeverityLevel.NORMAL)
    public void testValidMembership() {
        membershipsPage.clickAdd()
                      .selectMembership("ACCA")
                      .selectSubscriptionPaidBy("Individual")
                      .enterSubscriptionAmount("500")
                      .selectCurrency("Egyptian Pound")
                      .setCommenceDate("2024-01-01")
                      .setRenewalDate("2025-01-01")
                      .clickSave()
                      .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "memberships", "regression"})
    @Story("Invalid Membership")
    @Description("Test membership validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidMembership() {
        membershipsPage.clickAdd()
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