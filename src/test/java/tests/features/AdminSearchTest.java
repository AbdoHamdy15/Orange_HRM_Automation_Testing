package tests;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.AdminPage;
import pages.LoginPage;
import utilities.JsonUtils;

import java.util.HashMap;
import java.util.List;

@Epic("OrangeHRM Application")
@Feature("Search Functionality")
@Story("Admin Search")
public class AdminSearchTest {

    private GUIDriver driver;
    private AdminPage adminPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Admin page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        adminPage = dashboard.goToAdmin();
    }

    @Test(dataProvider = "adminSearchData", groups = {"search", "admin", "regression"})
    @Story("Search Admin Users")
    @Description("Test admin search functionality with various filters")
    @Severity(SeverityLevel.NORMAL)
    public void testAdminSearch(HashMap<String, String> data) {
        // Perform search using Method Chaining
            adminPage.enterUsername(data.get("username"))
                 .selectUserRole(data.get("userRole"))
                 .enterEmployeeName(data.get("employeeNameInput"), data.get("employeeNameToSelect"))
                 .selectStatus(data.get("status"))
                 .clickSearch();

        // Assertions based on expectedResults
        String expectedResults = data.getOrDefault("expectedResults", "");
        if (expectedResults.equals("Should show no results found")) {
            adminPage.assertNoRecordsFoundMessageDisplayed();
        } else if (expectedResults.equals("Should show invalid error")) {
            adminPage.assertInvalidErrorMessageDisplayed();
        } else {
            adminPage.assertSearchResultsDisplayed();
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }

    @DataProvider(name = "adminSearchData")
    public Object[][] adminSearchData() {
        List<HashMap<String, String>> data = JsonUtils.readJsonDataAsList("src/test/resources/adminSearchData.json");
        return data.stream().map(emp -> new Object[]{emp}).toArray(Object[][]::new);
    }
} 