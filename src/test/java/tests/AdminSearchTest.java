package tests;

import base.BaseTest;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.AdminPage;
import java.util.HashMap;
import java.util.List;

public class AdminSearchTest extends BaseTest implements ITest {

    private AdminPage adminPage;
    private ThreadLocal<String> testName = new ThreadLocal<>();

    @BeforeMethod
    public void navigateToAdmin() {
        DashboardPage dashboard = loginPage.login("Admin", "admin123");
        adminPage = dashboard.goToAdmin();
    }

    @Test(dataProvider = "adminSearchData")
    public void testAdminSearch(HashMap<String, String> data) throws InterruptedException {
        // Perform search based on test data
        if (data.get("username") != null && !data.get("username").isEmpty()) {
            adminPage.enterUsername(data.get("username"));
        }
        if (data.get("userRole") != null && !data.get("userRole").isEmpty()) {
            adminPage.selectUserRole(data.get("userRole"));
        }
        if (data.get("employeeNameInput") != null && !data.get("employeeNameInput").isEmpty() && data.get("employeeNameToSelect") != null && !data.get("employeeNameToSelect").isEmpty()) {
            adminPage.enterEmployeeName(data.get("employeeNameInput"), data.get("employeeNameToSelect"));
        }
        if (data.get("status") != null && !data.get("status").isEmpty()) {
            adminPage.selectStatus(data.get("status"));
        }
        adminPage.clickSearch();

        // Assertions based on expectedResults
        String expectedResults = data.getOrDefault("expectedResults", "");
        if (expectedResults.equals("Should show no results found")) {
            assert adminPage.isNoRecordsFoundMessageDisplayed() : "No records found message should be displayed";
        } else {
            assert adminPage.isSearchResultsTableDisplayed() : "Search results table should be displayed";
        }
    }

    @DataProvider(name = "adminSearchData")
    public Object[][] adminSearchData() {
        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "/src/test/resources/adminSearchData.json");
        return data.stream().map(emp -> new Object[]{emp}).toArray(Object[][]::new);
    }

    @Override
    public String getTestName() {
        return testName.get();
    }
} 