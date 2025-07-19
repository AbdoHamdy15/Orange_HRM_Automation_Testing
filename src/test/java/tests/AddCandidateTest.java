package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.RecruitmentPage;
import pages.AddCandidatePage;
import java.util.HashMap;
import java.util.List;
import org.testng.ITest;

public class AddCandidateTest extends BaseTest implements ITest {
    private AddCandidatePage addCandidatePage;
    private RecruitmentPage recruitmentPage;
    private ThreadLocal<String> testName = new ThreadLocal<>();

    @BeforeMethod
    public void navigateToAddCandidate() {
        DashboardPage dashboard = loginPage.login("Admin", "admin123");
        recruitmentPage = dashboard.goToRecruitment();
        addCandidatePage = recruitmentPage.clickAdd();
    }

    @DataProvider(name = "addCandidateData")
    public Object[][] addCandidateData() {
        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "/src/test/resources/addCandidateData.json");
        return data.stream().map(candidate -> new Object[]{candidate}).toArray(Object[][]::new);
    }

    @Test(dataProvider = "addCandidateData")
    public void testAddCandidate(HashMap<String, String> candidate) {
        testName.set(candidate.get("description"));
        String description = candidate.getOrDefault("description", "");
        if (candidate.get("firstName") != null && !candidate.get("firstName").isEmpty()) {
            addCandidatePage.enterName(candidate.get("firstName"), candidate.get("middleName"), candidate.get("lastName"));
        } else {
            addCandidatePage.enterName("", candidate.get("middleName"), candidate.get("lastName"));
        }
        if (candidate.get("email") != null && !candidate.get("email").isEmpty()) {
            addCandidatePage.enterEmail(candidate.get("email"));
        }
        if (candidate.get("contactNumber") != null && !candidate.get("contactNumber").isEmpty()) {
            addCandidatePage.enterContactNumber(candidate.get("contactNumber"));
        }
        if (candidate.get("vacancy") != null && !candidate.get("vacancy").isEmpty()) {
            addCandidatePage.selectVacancy(candidate.get("vacancy"));
        }
        if (candidate.get("resumePath") != null && !candidate.get("resumePath").isEmpty()) {
            addCandidatePage.uploadResume(candidate.get("resumePath"));
        }
        if (candidate.get("keywords") != null && !candidate.get("keywords").isEmpty()) {
            addCandidatePage.enterKeywords(candidate.get("keywords"));
        }
        if (candidate.get("notes") != null && !candidate.get("notes").isEmpty()) {
            addCandidatePage.enterNotes(candidate.get("notes"));
        }
        addCandidatePage.setConsent(Boolean.parseBoolean(String.valueOf(candidate.get("consent"))));
        addCandidatePage.clickSave();

        String expectedError = candidate.get("expectedError");
        if (expectedError != null && !expectedError.isEmpty()) {
            String errorText = getErrorTextForType(expectedError, candidate);
            Assert.assertTrue(addCandidatePage.isErrorMessageDisplayed(errorText), description + " - Error message should be displayed: " + errorText);
        } else {
            String successMessage = addCandidatePage.waitForToastAndGetMessage();
            Assert.assertTrue(successMessage != null && successMessage.contains("Successfully Saved"), description + " - Candidate should be saved successfully");
        }
    }

    private String getErrorTextForType(String expectedError, HashMap<String, String> candidate) {
        switch (expectedError) {
            case "Required":
                return "Required";
            case "Should not exceed 30 characters":
                return "Should not exceed 30 characters";
            case "Should not exceed 25 characters":
                return "Should not exceed 25 characters";
            case "Should not exceed 250 characters":
                return "Should not exceed 250 characters";
            case "Expected format: admin@example.com":
                return "Expected format: admin@example.com";
            case "Allows numbers and only + - / ( )":
                return "Allows numbers and only + - / ( )";
            default:
                return expectedError;
        }
    }

    @Override
    public String getTestName() {
        return testName.get();
    }
} 