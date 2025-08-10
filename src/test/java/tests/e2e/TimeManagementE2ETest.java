package tests.e2e;

import drivers.GUIDriver;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TimePage;
import pages.LoginPage;

@Epic("Time Management")
@Feature("Timesheet Viewing and Management")
public class TimeManagementE2ETest {

    private GUIDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        loginPage = new LoginPage(driver);
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Time → View Employee Timesheet → Logout")
    @Story("Time Management - Timesheet Viewing Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testTimesheetViewingE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Time page
        TimePage timePage = dashboard.goToTime()
                                    .assertTimePageDisplayed();

        // Step 3: View timesheet for a specific employee
        timePage.enterEmployeeName("peter", "Peter Mac Anderson")
                .clickView()
                .assertNoTimesheetsFound();

        // Step 4: Navigate back to time page and view another employee
        timePage = dashboard.goToTime()
                           .assertTimePageDisplayed();

        // Step 5: View timesheet for another employee
        timePage.viewEmployeeTimesheet("James", "James  Butler")
                .assertNoTimesheetsFound();

        // Step 6: Logout
        dashboard.logout()
                .assertLoginPageDisplayed();
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Time → Search with Invalid Employee → Validate Errors → Logout")
    @Story("Time Management - Error Handling Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testTimesheetErrorHandlingE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Time page
        TimePage timePage = dashboard.goToTime()
                                    .assertTimePageDisplayed();

        // Step 3: Try to view timesheet with invalid employee name
        timePage.enterEmployeeName("invaliduser", "")
                .clickView()
                .assertInvalidErrorForEmployeeName();

        // Step 4: View timesheet for valid employee
        timePage.viewEmployeeTimesheet("John", "John  Doe")
                .assertNoTimesheetsFound();

        // Step 5: Logout
        dashboard.logout()
                .assertLoginPageDisplayed();
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }
}
