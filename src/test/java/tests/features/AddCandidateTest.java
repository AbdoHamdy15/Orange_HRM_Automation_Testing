package tests;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.RecruitmentPage;
import pages.AddCandidatePage;
import pages.LoginPage;
import utilities.JsonUtils;
import java.util.HashMap;
import java.util.List;

@Epic("OrangeHRM Application")
@Feature("Recruitment")
@Story("Add Candidate")
public class AddCandidateTest {
    
    private GUIDriver driver;
    private AddCandidatePage addCandidatePage;

    public AddCandidateTest(HashMap<String, String> candidateData) {
    }

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Add Candidate page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        RecruitmentPage recruitmentPage = dashboard.goToRecruitment();
        addCandidatePage = recruitmentPage.clickAdd();
    }

    @Test(dataProvider = "addCandidateData", groups = {"management", "recruitment", "regression"})
    @Story("Add New Candidate")
    @Description("Test adding candidate with valid information from data provider")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddCandidate(HashMap<String, String> candidate) {
        // Fill the form using Method Chaining
        addCandidatePage.enterFirstName(candidate.get("firstName"))
                       .enterMiddleName(candidate.get("middleName"))
                       .enterLastName(candidate.get("lastName"))
                       .enterEmail(candidate.get("email"))
                       .enterContactNumber(candidate.get("contactNumber"))
                       .selectVacancy(candidate.get("vacancy"))
                       .uploadResume(candidate.get("resumePath"))
                       .enterKeywords(candidate.get("keywords"))
                       .enterNotes(candidate.get("notes"))
                       .setConsent(Boolean.parseBoolean(String.valueOf(candidate.get("consent"))))
                       .clickSave();

        // Assertions
        String expectedError = candidate.getOrDefault("expectedError", "");
        if (expectedError != null && !expectedError.isEmpty()) {
            addCandidatePage.assertSpecificErrorDisplayed(expectedError);
        } else {
            addCandidatePage.assertSuccessToastDisplayed();
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }

    @DataProvider(name = "addCandidateData")
    public Object[][] addCandidateData() {
        List<HashMap<String, String>> data = JsonUtils.readJsonDataAsList("src/test/resources/addCandidateData.json");
        return data.stream().map(candidate -> new Object[]{candidate}).toArray(Object[][]::new);
    }
} 