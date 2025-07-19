package tests;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LeavePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.HashMap;
import java.util.List;

public class LeaveSearchTest extends BaseTest {

    private LeavePage leavePage;

    @BeforeMethod
    public void navigateToLeavePage() {
        DashboardPage dashboard = loginPage.login("Admin", "admin123");
        dashboard.goToLeave();
        leavePage = new LeavePage(driver);
    }

    @Test(dataProvider = "leaveSearchData")
    public void testLeaveSearch(HashMap<String, String> data) {
        // Fill the form
        if (data.get("fromDate") != null && !data.get("fromDate").isEmpty()) leavePage.enterFromDate(data.get("fromDate"));
        if (data.get("toDate") != null && !data.get("toDate").isEmpty()) leavePage.enterToDate(data.get("toDate"));
        if (data.get("status") != null && !data.get("status").isEmpty()) {
            if ("CLEAR".equals(data.get("status"))) {
                leavePage.clearStatusSelection();
            } else {
                leavePage.selectStatusDropdown(data.get("status"));
            }
        }
        if (data.get("leaveType") != null && !data.get("leaveType").isEmpty()) leavePage.selectLeaveTypeDropdown(data.get("leaveType"));
        if (data.get("employeeNameInput") != null && !data.get("employeeNameInput").isEmpty())
            leavePage.enterEmployeeName(data.get("employeeNameInput"), data.get("employeeNameToSelect"));
        if (data.get("subUnit") != null && !data.get("subUnit").isEmpty()) leavePage.selectSubUnitDropdown(data.get("subUnit"));
        leavePage.setIncludePastEmployees(Boolean.parseBoolean(data.getOrDefault("includePastEmployees", "false")));
        leavePage.clickSearch();

        // Assertions
        String expectedError = data.getOrDefault("expectedError", "");
        String expectedErrorField = data.getOrDefault("expectedErrorField", "");
        String expectedResult = data.getOrDefault("expectedResult", "");
        if (!expectedError.isEmpty()) {
            if ("Employee Name".equals(expectedErrorField)) {
                assert leavePage.isInvalidErrorDisplayedForEmployeeName() : "Expected 'Invalid' error under Employee Name";
            } else if ("Show Leave with Status".equals(expectedErrorField)) {
                assert leavePage.isRequiredErrorDisplayedForStatus() : "Expected 'Required' error under Status";
            } else {
                assert false : "Unknown error field: " + expectedErrorField;
            }
        } else if (!expectedResult.isEmpty()) {
            String toastMsg = leavePage.waitForToastAndGetMessage();
            assert toastMsg != null && toastMsg.toLowerCase().contains(expectedResult.toLowerCase())
                    : "Expected toast/message: '" + expectedResult + "', but got: '" + toastMsg + "'";
        }
    }

    @DataProvider(name = "leaveSearchData")
    public Object[][] leaveSearchData() {
        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "/src/test/resources/leaveSearchData.json");
        return data.stream().map(row -> new Object[]{row}).toArray(Object[][]::new);
    }
} 