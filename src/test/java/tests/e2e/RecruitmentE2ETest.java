package tests.e2e;

import drivers.GUIDriver;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AddCandidatePage;
import pages.AddVacancyPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.RecruitmentPage;

@Epic("Recruitment Management")
@Feature("Vacancy and Candidate Management")
public class RecruitmentE2ETest {

    private GUIDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        loginPage = new LoginPage(driver);
    }

    @Test(groups = {"e2e", "recruitment", "regression"})
    @Description("E2E Scenario: Login → Recruitment → Vacancies → Add Vacancy → Add Candidate → Logout")
    @Story("Recruitment Management - Complete Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void testRecruitmentCompleteE2E() {
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Recruitment and click Vacancies tab
        RecruitmentPage recruitmentPage = dashboard.goToRecruitment()
                                                  .clickVacanciesTab();

        // Step 3: Add a new vacancy
        AddVacancyPage addVacancyPage = recruitmentPage.clickAddVacancy();
        addVacancyPage.addVacancy("Software Tester", "QA Engineer", 
                                "Junior software tester (0 - 6 months experience)", "Ranga Akunuri", "1")
                     .clickSave();

        // Step 4: Navigate to Candidates from Vacancy page
        recruitmentPage = addVacancyPage.clickCandidates();
        AddCandidatePage addCandidatePage = recruitmentPage.clickAdd();

        // Step 5: Add candidate with complete details
        addCandidatePage.addCandidateComplete("John", "Michael", "Doe", "john.doe@example.com", 
                                           "1234567890", "Software Tester", 
                                           "path/to/resume.pdf", "Selenium, Java, TestNG", 
                                           "Experienced QA engineer with strong automation skills", true)
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
