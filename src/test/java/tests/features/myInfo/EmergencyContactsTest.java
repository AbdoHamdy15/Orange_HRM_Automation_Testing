package tests.myInfo;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.EmergencyContactsPage;
import pages.LoginPage;

@Epic("OrangeHRM Application")
@Feature("My Info")
@Story("Emergency Contacts")
public class EmergencyContactsTest {
    
    private GUIDriver driver;
    private EmergencyContactsPage emergencyContactsPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Emergency Contacts page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        emergencyContactsPage = dashboard.navigateToEmergencyContacts();
    }

    @Test(groups = {"myinfo", "emergency", "regression"})
    @Story("Valid Emergency Contact")
    @Description("Test adding emergency contact with valid information")
    @Severity(SeverityLevel.NORMAL)
    public void testValidEmergencyContact() {
                emergencyContactsPage.clickAdd()
                              .enterName("Fatima Hassan")
                              .enterRelationship("Spouse")
                              .enterHomePhone("123-456-7890")
                              .enterMobile("098-765-4321")
                              .enterWorkPhone("555-123-4567")
                              .clickSave()
                              .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "emergency", "regression"})
    @Story("Invalid Emergency Contact")
    @Description("Test emergency contact validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidEmergencyContact() {
        emergencyContactsPage.clickAdd()
                             .enterName("")
                             .enterRelationship("")
                             .enterHomePhone("")
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