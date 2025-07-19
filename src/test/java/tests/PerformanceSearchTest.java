package tests;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.PerformancePage;

public class PerformanceSearchTest extends BaseTest {

    private PerformancePage performancePage;

    @BeforeMethod
    public void navigateToPerformancePage() {
        DashboardPage dashboard = loginPage.login("Admin", "admin123");
        dashboard.goToPerformance();
        performancePage = new PerformancePage(driver);
    }

    @Test
    public void testEmptySearch() {
        performancePage.clickSearch();
        String toastMsg = performancePage.waitForToastAndGetMessage();
        assert toastMsg != null && toastMsg.toLowerCase().contains("no records found")
                : "Expected toast message: 'No Records Found', but got: '" + toastMsg + "'";
    }

    @Test
    public void testInvalidEmployeeName() {
        performancePage.enterEmployeeName("invaliduser", "invaliduser");
        performancePage.clickSearch();
        assert performancePage.isInvalidErrorDisplayedForEmployeeName() : "Expected 'Invalid' error under Employee Name";
    }

    @Test
    public void testValidEmployeeName() {
        performancePage.enterEmployeeName("peter", "Peter Mac Anderson");
        performancePage.clickSearch();
        String toastMsg = performancePage.waitForToastAndGetMessage();
        assert toastMsg != null && toastMsg.toLowerCase().contains("no records found")
                : "Expected toast message: 'No Records Found', but got: '" + toastMsg + "'";
    }

    @Test
    public void testJobTitleSearch() {
        performancePage.selectJobTitle("Software Engineer");
        performancePage.clickSearch();
        String toastMsg = performancePage.waitForToastAndGetMessage();
        assert toastMsg != null && toastMsg.toLowerCase().contains("no records found")
                : "Expected toast message: 'No Records Found', but got: '" + toastMsg + "'";
    }

    @Test
    public void testSubUnitSearch() {
        performancePage.selectSubUnit("Engineering");
        performancePage.clickSearch();
        String toastMsg = performancePage.waitForToastAndGetMessage();
        assert toastMsg != null && toastMsg.toLowerCase().contains("no records found")
                : "Expected toast message: 'No Records Found', but got: '" + toastMsg + "'";
    }

    @Test
    public void testReviewStatusSearch() {
        performancePage.selectReviewStatus("Completed");
        performancePage.clickSearch();
        String toastMsg = performancePage.waitForToastAndGetMessage();
        assert toastMsg != null && toastMsg.toLowerCase().contains("no records found")
                : "Expected toast message: 'No Records Found', but got: '" + toastMsg + "'";
    }

} 