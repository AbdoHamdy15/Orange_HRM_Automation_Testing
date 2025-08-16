package tests.features.myInfo;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.ContactDetailsPage;
import pages.LoginPage;

@Epic("OrangeHRM Application")
@Feature("My Info")
@Story("Contact Details")
public class ContactDetailsTest {
    
    private GUIDriver driver;
    private ContactDetailsPage contactDetailsPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Contact Details page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        contactDetailsPage = dashboard.navigateToContactDetails();
    }

    @Test(groups = {"myinfo", "contact", "smoke"})
    @Story("Valid Contact Details")
    @Description("Test updating contact details with valid information")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidContactDetails() {
        contactDetailsPage.fillContactDetailsBasic("123 Main St", "Cairo", "Egypt")
                          .enterHomePhone("123-456-7890")
                          .enterMobile("098-765-4321")
                          .enterWorkPhone("555-123-4567")
                          .enterWorkEmail("john.doe@company.com")
                          .enterOtherEmail("john.doe.personal@gmail.com")
                          .clickSave()
                          .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "contact"})
    @Story("Invalid Contact Details")
    @Description("Test contact details validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidContactDetails() {
        contactDetailsPage.fillContactDetailsBasic("", "", "")
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
