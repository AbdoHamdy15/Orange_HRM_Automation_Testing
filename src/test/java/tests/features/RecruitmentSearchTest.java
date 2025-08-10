package tests.features;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.RecruitmentPage;
import pages.LoginPage;
import utilities.JsonUtils;

import java.util.HashMap;
import java.util.List;

@Epic("OrangeHRM Application")
@Feature("Search Functionality")
@Story("Recruitment Search")
public class RecruitmentSearchTest {

    private GUIDriver driver;
    private RecruitmentPage recruitmentPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Recruitment page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        recruitmentPage = dashboard.goToRecruitment();
    }

    @Test(dataProvider = "recruitmentSearchData", groups = {"search", "recruitment"})
    @Story("Search Recruitment Records")
    @Description("Test recruitment search functionality with various filters")
    @Severity(SeverityLevel.NORMAL)
    public void testRecruitmentSearch(HashMap<String, String> data) {
        // Perform search using Method Chaining
        recruitmentPage.selectJobTitle(data.get("jobTitle"))
                .selectVacancy(data.get("vacancy"))
                .selectHiringManager(data.get("hiringManager"))
                .selectStatus(data.get("status"))
                .enterCandidateName(data.get("candidateNameInput"), data.get("candidateNameToSelect"))
                .setFromDate(data.get("fromDate"))
                .setToDate(data.get("toDate"))
                .clickSearch();

        // Assertions
        String expectedError = data.getOrDefault("expectedError", "");
        String expectedResult = data.getOrDefault("expectedResult", "");
        
        if (!expectedError.isEmpty()) {
            if ("Invalid".equals(expectedError)) {
                recruitmentPage.assertInvalidErrorForCandidateName();
            } else {
                recruitmentPage.assertSpecificErrorDisplayed(expectedError);
            }
        } else if (!expectedResult.isEmpty()) {
            if ("table".equals(expectedResult)) {
                recruitmentPage.assertSearchResultsDisplayed();
            } else if ("No Records Found".equals(expectedResult)) {
                recruitmentPage.assertNoRecordsFound();
            } else {
                recruitmentPage.assertSuccessToastDisplayed();
            }
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }

    @DataProvider(name = "recruitmentSearchData")
    public Object[][] recruitmentSearchData() {
        List<HashMap<String, String>> data = JsonUtils.readJsonDataAsList("src/test/resources/recruitmentSearchData.json");
        return data.stream().map(emp -> new Object[]{emp}).toArray(Object[][]::new);
    }
} 
