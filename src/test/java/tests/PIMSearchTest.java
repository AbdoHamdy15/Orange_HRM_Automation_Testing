package tests;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.PIMPage;
import utils.JsonDataReader;

import java.io.IOException;
import java.util.Iterator;

public class PIMSearchTest extends BaseTest {

    LoginPage loginPage;
    DashboardPage dashboardPage;
    PIMPage pimPage;

    @BeforeMethod
    public void setup() {
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        pimPage = new PIMPage(driver);
    }

    @Test(dataProvider = "pimSearchData")
    public void testPIMSearch(String description, String employeeNameInput, String employeeNameToSelect,
                             String supervisorNameInput, String supervisorNameToSelect,
                             String jobTitle, String subUnit, String include, String employeeId,
                             String expectedResults) throws InterruptedException {

        // Perform search based on test data
        if (!employeeNameInput.isEmpty() && !employeeNameToSelect.isEmpty()) {
            pimPage.enterEmployeeName(employeeNameInput, employeeNameToSelect);
        }

        if (!supervisorNameInput.isEmpty() && !supervisorNameToSelect.isEmpty()) {
            pimPage.enterSupervisorName(supervisorNameInput, supervisorNameToSelect);
        }

        if (!employeeId.isEmpty()) {
            pimPage.enterEmployeeId(employeeId);
        }

        if (!subUnit.isEmpty()) {
            pimPage.selectSubUnit(subUnit);
        }

        if (!include.isEmpty()) {
            pimPage.selectInclude(include);
        }

        pimPage.clickSearch();

        // Assertions based on expectedResults
        if (expectedResults.equals("Invalid")) {
            assert pimPage.isInvalidMessageDisplayed() : "Invalid message should be displayed";
        } else {
            assert pimPage.isSearchResultsTableDisplayed() : "Search results table should be displayed";
        }
    }

    @DataProvider(name = "pimSearchData")
    public Iterator<Object[]> getPIMSearchData() throws IOException {
        return JsonDataReader.getTestData("pimSearchData.json");
    }
} 