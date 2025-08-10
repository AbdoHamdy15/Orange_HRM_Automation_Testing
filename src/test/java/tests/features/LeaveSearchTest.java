package tests;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LeavePage;
import pages.LoginPage;
import utilities.JsonUtils;

import java.util.HashMap;
import java.util.List;

@Epic("OrangeHRM Application")
@Feature("Search Functionality")
@Story("Leave Search")
public class LeaveSearchTest {

    private GUIDriver driver;
    private LeavePage leavePage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login first
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        // Navigate to Leave page
        dashboard.goToLeave();
        leavePage = new LeavePage(driver);
    }

    @Test(dataProvider = "leaveSearchData", groups = {"search", "leave", "regression"})
    @Story("Search Leave Records")
    @Description("Test leave search functionality with various filters")
    @Severity(SeverityLevel.NORMAL)
    public void testLeaveSearch(HashMap<String, String> data) {
        // Fill the form using Method Chaining
        leavePage.enterFromDate(data.get("fromDate"))
                 .enterToDate(data.get("toDate"))
                 .selectLeaveTypeDropdown(data.get("leaveType"))
                 .enterEmployeeName(data.get("employeeNameInput"), data.get("employeeNameToSelect"))
                 .selectSubUnitDropdown(data.get("subUnit"))
                 .setIncludePastEmployees(Boolean.parseBoolean(data.getOrDefault("includePastEmployees", "false")));

        // Handle status selection if provided (special case for CLEAR)
        String status = data.get("status");
        if (status != null && !status.isEmpty()) {
            if ("CLEAR".equals(status)) {
                leavePage.clearStatusSelection();
            } else {
                leavePage.selectStatusDropdown(status);
            }
        }

        // Click search after all selections
        leavePage.clickSearch();

        // Assertions
        String expectedError = data.getOrDefault("expectedError", "");
        String expectedErrorField = data.getOrDefault("expectedErrorField", "");
        String expectedResult = data.getOrDefault("expectedResult", "");
        
        if (!expectedError.isEmpty()) {
            if ("Employee Name".equals(expectedErrorField)) {
                leavePage.assertInvalidErrorForEmployeeName();
            } else if ("Show Leave with Status".equals(expectedErrorField)) {
                leavePage.assertRequiredErrorForStatus();
            } else {
                leavePage.assertSpecificErrorDisplayed(expectedError);
            }
        } else if (!expectedResult.isEmpty()) {
            if ("No Records Found".equals(expectedResult)) {
                leavePage.assertNoRecordsFound();
            } else {
                leavePage.assertSuccessToastDisplayed();
            }
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }

    @DataProvider(name = "leaveSearchData")
    public Object[][] leaveSearchData() {
        List<HashMap<String, String>> data = JsonUtils.readJsonDataAsList("src/test/resources/leaveSearchData.json");
        return data.stream().map(emp -> new Object[]{emp}).toArray(Object[][]::new);
    }
} 