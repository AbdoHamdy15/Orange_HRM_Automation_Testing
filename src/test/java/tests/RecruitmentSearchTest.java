package tests;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;
import pages.DashboardPage;
import pages.RecruitmentPage;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RecruitmentSearchTest extends BaseTest {
    private RecruitmentPage recruitmentPage;

    @BeforeMethod
    public void navigateToRecruitment() {
        DashboardPage dashboard = loginPage.login("Admin", "admin123");
        recruitmentPage = dashboard.goToRecruitment();
    }

    @DataProvider(name = "recruitmentSearchData")
    public Object[][] recruitmentSearchData() {
        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "/src/test/resources/recruitmentSearchData.json");
        return data.stream().map(row -> new Object[]{row}).toArray(Object[][]::new);
    }

    @Test(dataProvider = "recruitmentSearchData")
    public void testRecruitmentSearch(HashMap<String, String> data) {
        // Only select dropdowns if value is not empty 
        if (data.get("jobTitle") != null && !data.get("jobTitle").isEmpty()) {
            recruitmentPage.selectJobTitle(data.get("jobTitle"));
        }
        if (data.get("vacancy") != null && !data.get("vacancy").isEmpty()) {
            recruitmentPage.selectVacancy(data.get("vacancy"));
        }
        if (data.get("hiringManager") != null && !data.get("hiringManager").isEmpty()) {
            recruitmentPage.selectHiringManager(data.get("hiringManager"));
        }
        if (data.get("status") != null && !data.get("status").isEmpty()) {
            recruitmentPage.selectStatus(data.get("status"));
        }
        if (data.get("fromDate") != null && !data.get("fromDate").isEmpty()) {
            recruitmentPage.setFromDate(data.get("fromDate"));
        }
        if (data.get("toDate") != null && !data.get("toDate").isEmpty()) {
            recruitmentPage.setToDate(data.get("toDate"));
        }
        if (data.get("candidateNameInput") != null && !data.get("candidateNameInput").isEmpty()) {
            recruitmentPage.enterCandidateName(data.get("candidateNameInput"), data.get("candidateNameToSelect"));
        }
        recruitmentPage.clickSearch();

        String expectedError = data.getOrDefault("expectedError", "");
        String expectedResult = data.getOrDefault("expectedResult", "");
        if (!expectedError.isEmpty()) {
            Assert.assertTrue(recruitmentPage.isInvalidErrorDisplayedForCandidateName(), "Should show 'Invalid' error under candidate name");
        } else if (expectedResult.equals("table")) {
            Assert.assertTrue(recruitmentPage.isSearchResultsTableDisplayed(), "Search results table should be displayed");
        }
    }
} 