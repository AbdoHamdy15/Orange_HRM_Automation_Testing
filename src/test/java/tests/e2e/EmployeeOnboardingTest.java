package tests.e2e;

import drivers.GUIDriver;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AddEmployeePage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.myinfo.PersonalDetailsPage;
import pages.myinfo.DependentsPage;

@Epic("Employee Management")
@Feature("Employee Onboarding")
public class EmployeeOnboardingTest {

    private GUIDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        loginPage = new LoginPage(driver);
    }

    @Test(groups = {"e2e", "onboarding", "regression"})
    @Description("E2E Scenario 1: Login → Add Employee → Personal Details → Contact Details → Dependents → Logout")
    @Story("Employee Onboarding - Basic Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmployeeOnboardingBasicE2E() {
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Add Employee (takes us directly to Personal Details)
        AddEmployeePage addEmployeePage = dashboard.goToPIM()
                                                  .clickAddEmployee();

        // Step 3: Fill Employee Basic Details, Save and Wait
        PersonalDetailsPage personalDetailsPage = addEmployeePage.enterFirstName("John")
                      .enterLastName("Doe")
                      .enterEmployeeId("EPM0003")
                      .clickSave();

        // Step 4: Complete Personal Details
        personalDetailsPage.enterFirstName("John")
                          .enterMiddleName("Michael")
                          .enterLastName("Doe")
                          .enterOtherId("12345")
                          .selectNationality("American")
                          .selectMaritalStatus("Single")
                          .clickSaveAfterGender()
                          .assertSuccessToastDisplayed();



        // Step 5: Navigate to Dependents and add a dependent
        DependentsPage dependentsPage = dashboard.navigateToDependents();
        dependentsPage.clickAdd()
                     .enterName("Sarah Doe")
                     .selectRelationship("Child")
                     .setDateOfBirth("2015-06-15")
                     .clickSave()
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