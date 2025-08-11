package tests.e2e;

import drivers.GUIDriver;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LeavePage;
import pages.LoginPage;

@Epic("Leave Management")
@Feature("Leave Search and Management")
public class LeaveManagementE2ETest {

    private GUIDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        loginPage = new LoginPage(driver);
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Leave → Search Leave Records → Reset → Logout")
    @Story("Leave Management - Search and Reset Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testLeaveSearchAndResetE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Leave page
        LeavePage leavePage = dashboard.goToLeave()
                                      .assertLeavePageDisplayed();

        // Step 3: Search for leave records with specific criteria
        leavePage.searchLeave("2024-01-01", "2024-12-31", "Scheduled", "US - Personal", 
                             "j", "James  Butler", "Engineering", true)
                 .assertNoRecordsFound();

        // Step 4: Reset the search form
        leavePage.clickReset()
                 .assertLeavePageDisplayed();

        // Step 5: Perform another search with different criteria
        leavePage.searchLeave("2024-06-01", "2024-06-30", "Taken", "US - FMLA",
                             "R", "Ranga  Akunuri", "Sales", false)
                .assertNoRecordsFound();

        // Step 6: Logout
        dashboard.logout()
                .assertLoginPageDisplayed();
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Leave → Search with Invalid Data → Validate Errors → Logout")
    @Story("Leave Management - Error Handling Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testLeaveSearchErrorHandlingE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Leave page
        LeavePage leavePage = dashboard.goToLeave()
                                      .assertLeavePageDisplayed();

        // Step 3: Search with invalid employee name
        leavePage.enterEmployeeName("InvalidEmployee", null)
                 .clickSearch()
                 .assertInvalidErrorForEmployeeName();

        // Step 4: Clear and search with invalid status
        leavePage.clearStatusSelection()
                 .clickSearch()
                 .assertRequiredErrorForStatus();

        // Step 5: Perform valid search after errors
        leavePage.searchLeave("2024-01-01", "2024-12-31", "Scheduled", "US - Personal", 
                             "A", "Amelia  Brown", "Engineering", true)
                 .assertSuccessToastDisplayed();

        // Step 6: Logout
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
