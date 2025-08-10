package tests;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.PIMPage;
import utilities.JsonUtils;

import java.util.HashMap;
import java.util.List;

@Epic("OrangeHRM Application")
@Feature("Search Functionality")
@Story("PIM Search")
public class PIMSearchTest {

    private GUIDriver driver;
    private PIMPage pimPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();

        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        // Navigate to PIM page
        pimPage = dashboard.goToPIM();
    }

    @Test(dataProvider = "pimSearchData", groups = {"search", "pim", "regression"})
    @Story("Search PIM Employees")
    @Description("Test PIM employee search functionality with various filters")
    @Severity(SeverityLevel.NORMAL)
    public void testPIMSearch(HashMap<String, String> data) {
        // Perform search using Method Chaining
        pimPage.enterEmployeeName(data.get("employeeNameInput"), data.get("employeeNameToSelect"))
                .enterSupervisorName(data.get("supervisorNameInput"), data.get("supervisorNameToSelect"))
                .enterEmployeeId(data.get("employeeId"))
                .selectJobTitle(data.get("jobTitle"))
                .selectEmploymentStatus(data.get("employmentStatus"))
                .selectInclude(data.get("include"))
                .selectSubUnit(data.get("subUnit"))
                .clickSearch();

        // Assertions based on expectedResults
        String expectedResults = data.getOrDefault("expectedResults", "");
        if (expectedResults.equals("Invalid")) {
            pimPage.assertInvalidMessageDisplayed();
        } else {
            pimPage.assertSearchResultsDisplayed();
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }

    @DataProvider(name = "pimSearchData")
    public Object[][] pimSearchData() {
        List<HashMap<String, String>> data = JsonUtils.readJsonDataAsList("src/test/resources/pimSearchData.json");
        return data.stream().map(emp -> new Object[]{emp}).toArray(Object[][]::new);
    }
} 