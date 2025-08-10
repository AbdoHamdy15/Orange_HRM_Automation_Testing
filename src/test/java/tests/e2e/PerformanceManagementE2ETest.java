package tests.e2e;

import drivers.GUIDriver;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.PerformancePage;
import pages.LoginPage;

@Epic("Performance Management")
@Feature("Performance Review and Search")
public class PerformanceManagementE2ETest {

    private GUIDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        loginPage = new LoginPage(driver);
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Performance → Search Employee Reviews → Reset → Logout")
    @Story("Performance Management - Search and Reset Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testPerformanceSearchAndResetE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Performance page
        PerformancePage performancePage = dashboard.goToPerformance()
                                                  .assertPerformancePageDisplayed();

        // Step 3: Search for performance reviews with Employee Name
        performancePage.enterEmployeeName("peter", "Peter Mac Anderson")
                .clickSearch()
                .assertNoRecordsFound();

        // Step 4: Reset the search form
        performancePage.clickReset()
                      .assertPerformancePageDisplayed();

        // Step 5: Perform another search with job title
        performancePage.selectJobTitle("Software Engineer")
                .clickSearch()
                .assertNoRecordsFound();

        // Step 6: Logout
        dashboard.logout()
                .assertLoginPageDisplayed();
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Performance → Search with Invalid Data → Validate Errors → Logout")
    @Story("Performance Management - Error Handling Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testPerformanceSearchErrorHandlingE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Performance page
        PerformancePage performancePage = dashboard.goToPerformance()
                                                  .assertPerformancePageDisplayed();

        // Step 3: Search with invalid employee name
        performancePage.enterEmployeeName("Abdelrahman", "")
                .clickSearch()
                .assertInvalidErrorForEmployeeName();

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
