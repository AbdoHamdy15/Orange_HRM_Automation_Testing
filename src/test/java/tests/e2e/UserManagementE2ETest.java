package tests.e2e;

import drivers.GUIDriver;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AddUserPage;
import pages.AdminPage;
import pages.DashboardPage;
import pages.LoginPage;

@Epic("User Management")
@Feature("Admin User Management")
public class UserManagementE2ETest {

    private GUIDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        loginPage = new LoginPage(driver);
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Admin → Search Users → Add New User → Logout")
    @Story("User Management - Search and Add User Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserSearchAndAddE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Admin page
        AdminPage adminPage = dashboard.goToAdmin()
                                      .assertAdminPageDisplayed();

        // Step 3: Search for existing users
        adminPage.searchUsers("Abdelrahman", "Admin", "r", "Ranga  Akunuri", "Enabled")
                 .assertSearchResultsDisplayed();

        // Step 4: Reset search and add new user
        adminPage.clickReset()
                 .assertAdminPageDisplayed();

        // Step 5: Add a new user
        AddUserPage addUserPage = adminPage.clickAdd();
        addUserPage.addNewUser("ESS", "r", "Ranga  Akunuri", "Enabled", "Abdelrahman5", "Password123")
                   .clickSave()
                   .assertSuccessToastDisplayed();

        // Step 6: Logout
        dashboard.logout()
                .assertLoginPageDisplayed();
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Admin → Search with Invalid Data → Validate Errors → Logout")
    @Story("User Management - Error Handling Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserSearchErrorHandlingE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Admin page
        AdminPage adminPage = dashboard.goToAdmin()
                                      .assertAdminPageDisplayed();

        // Step 3: Search with invalid employee name
        adminPage.enterEmployeeName("InvalidEmployee", null)
                 .clickSearch()
                 .assertInvalidErrorMessageDisplayed();

        // Step 4: Perform valid search after error
        adminPage.searchUsers("admin", "Admin", "J", "James  Butler", "Enabled")
                 .assertSearchResultsDisplayed();

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
