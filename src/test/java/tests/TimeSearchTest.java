package tests;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TimePage;
import java.util.HashMap;
import java.util.List;

public class TimeSearchTest extends BaseTest {

    private TimePage timePage;

    @BeforeMethod
    public void navigateToTimePage() {
        DashboardPage dashboard = loginPage.login("Admin", "admin123");
        dashboard.goToTime();
        timePage = new TimePage(driver);
    }

    @Test
    public void testInvalidEmployeeName() {
        timePage.enterEmployeeName("invaliduser", "invaliduser");
        timePage.clickView();
        assert timePage.isInvalidErrorDisplayedForEmployeeName() : "Expected 'Invalid' error under Employee Name";
    }

    @Test
    public void testValidEmployeeNoData() {
        timePage.viewEmployeeTimesheet("f", "First Name akhil Last Name");
        String toastMsg = timePage.waitForToastAndGetMessage();
        assert toastMsg != null && toastMsg.toLowerCase().contains("no records found")
                : "Expected toast message: 'No Records Found', but got: '" + toastMsg + "'";
    }

    @Test
    public void testValidEmployeeNoTimesheets() {
        timePage.viewEmployeeTimesheet("peter", "Peter Mac Anderson");
        String pageMsg = timePage.getTimesheetPageMessage();
        assert pageMsg != null && pageMsg.contains("No Timesheets Found")
                : "Expected page message: 'No Timesheets Found', but got: '" + pageMsg + "'";
    }
} 