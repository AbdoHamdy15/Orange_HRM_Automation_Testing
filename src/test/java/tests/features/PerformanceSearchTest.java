package tests;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.PerformancePage;
import pages.LoginPage;

@Epic("OrangeHRM Application")
@Feature("Search Functionality")
@Story("Performance Search")
public class PerformanceSearchTest {

    private GUIDriver driver;
    private PerformancePage performancePage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Performance page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        dashboard.goToPerformance();
        performancePage = new PerformancePage(driver);
    }

    @Test(groups = {"search", "performance", "regression"})
    @Story("Empty Search")
    @Description("Test performance search with no filters applied")
    @Severity(SeverityLevel.MINOR)
    public void testEmptySearch() {
        performancePage.clickSearch()
                .assertNoRecordsFound();
    }

    @Test(groups = {"search", "performance", "regression"})
    @Story("Invalid Employee Name")
    @Description("Test performance search with invalid employee name")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidEmployeeName() {
        performancePage.enterEmployeeName("invaliduser", "")
                       .clickSearch()
                       .assertInvalidErrorForEmployeeName();
    }

    @Test(groups = {"search", "performance", "regression"})
    @Story("Valid Employee Name")
    @Description("Test performance search with valid employee name")
    @Severity(SeverityLevel.NORMAL)
    public void testValidEmployeeName() {
        performancePage.enterEmployeeName("peter", "Peter Mac Anderson")
                       .clickSearch()
                       .assertNoRecordsFound();
    }

    @Test(groups = {"search", "performance", "regression"})
    @Story("Job Title Search")
    @Description("Test performance search by job title filter")
    @Severity(SeverityLevel.NORMAL)
    public void testJobTitleSearch() {
        performancePage.selectJobTitle("Software Engineer")
                       .clickSearch()
                       .assertNoRecordsFound();
    }

    @Test(groups = {"search", "performance", "regression"})
    @Story("Sub Unit Search")
    @Description("Test performance search by sub unit filter")
    @Severity(SeverityLevel.NORMAL)
    public void testSubUnitSearch() {
        performancePage.selectSubUnit("Engineering")
                       .clickSearch()
                       .assertNoRecordsFound();
    }

    @Test(groups = {"search", "performance", "regression"})
    @Story("Review Status Search")
    @Description("Test performance search by review status filter")
    @Severity(SeverityLevel.NORMAL)
    public void testReviewStatusSearch() {
        performancePage.selectReviewStatus("Completed")
                        .clickSearch()
                        .assertNoRecordsFound();
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }
} 