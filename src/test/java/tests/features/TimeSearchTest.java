package tests.features;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TimePage;
import pages.LoginPage;

@Epic("OrangeHRM Application")
@Feature("Search Functionality")
@Story("Time Search")
public class TimeSearchTest {

    private GUIDriver driver;
    private TimePage timePage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Time page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        dashboard.goToTime();
        timePage = new TimePage(driver);
    }

    @Test(groups = {"search", "time"})
    @Story("Invalid Employee Name")
    @Description("Test search with invalid employee name and verify error message")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidEmployeeName() {
        timePage.enterEmployeeName("invaliduser", "")
                .clickView()
                .assertInvalidErrorForEmployeeName();
    }

    @Test(groups = {"search", "time"})
    @Story("Valid Employee No Timesheets")
    @Description("Test search with valid employee name but no timesheets found")
    @Severity(SeverityLevel.MINOR)
    public void testValidEmployeeNoTimesheets() {
        timePage.enterEmployeeName("peter", "Peter Mac Anderson")
                .clickView()
                .assertNoTimesheetsFound();
    }

    @Test(groups = {"search", "time"})
    @Story("Empty Employee Name")
    @Description("Test search with empty employee name and verify required field error")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptyEmployeeName() {
        timePage.enterEmployeeName("", "")
                .clickView()
                .assertSpecificErrorDisplayed("Required");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }
} 
