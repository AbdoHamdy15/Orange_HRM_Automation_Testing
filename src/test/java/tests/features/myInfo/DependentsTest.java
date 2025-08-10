package tests.features.myInfo;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.DependentsPage;
import pages.LoginPage;

@Epic("OrangeHRM Application")
@Feature("My Info")
@Story("Dependents")
public class DependentsTest {
    
    private GUIDriver driver;
    private DependentsPage dependentsPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Dependents page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        dependentsPage = dashboard.navigateToDependents();
    }

    @Test(groups = {"myinfo", "dependents"})
    @Story("Valid Dependent")
    @Description("Test adding dependent with valid information")
    @Severity(SeverityLevel.NORMAL)
    public void testValidDependent() {
        dependentsPage.clickAdd()
                      .enterName("Ahmed Hassan")
                      .selectRelationship("Child")
                      .setDateOfBirth("2015-06-15")
                      .clickSave()
                      .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "dependents"})
    @Story("Invalid Dependent")
    @Description("Test dependent validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidDependent() {
        dependentsPage.clickAdd()
                      .enterName("")
                      .selectRelationship("")
                      .setDateOfBirth("")
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
