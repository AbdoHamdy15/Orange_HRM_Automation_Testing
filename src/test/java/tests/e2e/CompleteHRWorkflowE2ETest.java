package tests.e2e;

import drivers.GUIDriver;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AddCandidatePage;
import pages.AddEmployeePage;
import pages.AddUserPage;
import pages.AddVacancyPage;
import pages.AdminPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.RecruitmentPage;
import pages.myinfo.PersonalDetailsPage;

@Epic("Complete HR Workflow")
@Feature("End-to-End HR Management Process")
public class CompleteHRWorkflowE2ETest {

    private GUIDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        loginPage = new LoginPage(driver);
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Recruitment → Add Vacancy → Add Candidate → Add Employee → Add User → Logout")
    @Story("Complete HR Workflow - From Recruitment to Employee Onboarding")
    @Severity(SeverityLevel.CRITICAL)
    public void testCompleteHRWorkflowE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Recruitment - Add Vacancy
        RecruitmentPage recruitmentPage = dashboard.goToRecruitment()
                                                   .clickVacanciesTab();
        AddVacancyPage addVacancyPage = recruitmentPage.clickAddVacancy();
        addVacancyPage.addVacancy("Software Testing Engineer", "QA Engineer",
                        "Junior software tester (0 - 6 months experience)", "a", "Ranga Akunuri", "1")
                .clickSave();

        // Step 3: Add Candidate
        recruitmentPage = addVacancyPage.clickCandidates();
        AddCandidatePage addCandidatePage = recruitmentPage.clickAdd();
        addCandidatePage.addCandidateComplete("Abdelrahman", "Hamdy", "Ibrahim", "abdelrahman.hamdy12799@gmail.com",
                        "+201033223147", "Software Engineer",
                        "E:\\Newest desktop\\ITI Material\\Abdelrahman_Hamdy_Software_Tester.pdf", "Selenium, Java, TestNG",
                        "Experienced QA engineer with strong automation skills", true)
                .clickSave()
                .assertSuccessToastDisplayed();

        // Step 4: Add Employee
        AddEmployeePage addEmployeePage = dashboard.goToPIM()
                                                  .clickAddEmployee();
        PersonalDetailsPage personalDetailsPage = addEmployeePage.enterFirstName("Michael")
                      .enterLastName("Johnson")
                      .enterEmployeeId("EMP015")
                      .clickSave();

        // Step 5: Complete Personal Details
        personalDetailsPage.enterFirstName("Michael")
                          .enterMiddleName("Robert")
                          .enterLastName("Johnson")
                          .enterOtherId("EMP123")
                          .clickSaveAfterGender()
                          .assertSuccessToastDisplayed();

        // Step 6: Add User for the Employee
        AdminPage adminPage = dashboard.goToAdmin()
                                      .assertAdminPageDisplayed();
        AddUserPage addUserPage = adminPage.clickAdd();
        addUserPage.addNewUser("ESS", "Michael", "Michael Robert Johnson", "Enabled", "michael.johnson", "Password123")
                   .clickSave()
                   .assertSuccessToastDisplayed();

        // Step 7: Logout
        dashboard.logout()
                .assertLoginPageDisplayed();
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → Admin → Search Users → PIM → Add Employee → Admin → Add User → Logout")
    @Story("Employee and User Management Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmployeeAndUserManagementE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Search existing users
        AdminPage adminPage = dashboard.goToAdmin()
                                      .assertAdminPageDisplayed();
        adminPage.searchUsers("admin", "Admin", "John", "John Doe", "Enabled")
                 .assertSearchResultsDisplayed();

        // Step 3: Add new employee
        AddEmployeePage addEmployeePage = dashboard.goToPIM()
                                                  .clickAddEmployee();
        PersonalDetailsPage personalDetailsPage = addEmployeePage.enterFirstName("Emily")
                      .enterLastName("Davis")
                      .enterEmployeeId("EMP0015")
                      .clickSave();

        // Step 4: Complete employee details
        personalDetailsPage.enterFirstName("Emily")
                          .enterMiddleName("Grace")
                          .enterLastName("Davis")
                          .enterOtherId("EMP456")
                          .selectNationality("Canadian")
                          .selectMaritalStatus("Single")
                          .clickSaveAfterGender()
                          .assertSuccessToastDisplayed();

        // Step 5: Add user for the new employee
        adminPage = dashboard.goToAdmin()
                            .assertAdminPageDisplayed();
        AddUserPage addUserPage = adminPage.clickAdd();
        addUserPage.addNewUser("ESS", "Emily", "Emily Grace Davis", "Enabled", "emily.davis", "Password123")
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
