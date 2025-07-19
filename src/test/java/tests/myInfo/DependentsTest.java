package tests.myInfo;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.DependentsPage;

public class DependentsTest extends BaseTest {

    private DependentsPage dependentsPage;

    @BeforeMethod
    public void setUp() {
        DashboardPage dashboardPage = loginPage.login("Admin", "admin123");
        dependentsPage = dashboardPage.navigateToDependents();
    }

    @Test
    public void testValidDependent() {
        dependentsPage.addDependent("Sarah Smith", "Child", "15", "03", "2010");
        
        // Verify success message
        String toastMessage = dependentsPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidDependent() {
        dependentsPage.addDependent("", "Child", "15", "03", "2010");
        
        // Verify error messages for required fields
        assert dependentsPage.isErrorMessageDisplayed("Required") : "Expected 'Required' error message for name";
    }
} 