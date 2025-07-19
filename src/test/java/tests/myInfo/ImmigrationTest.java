package tests.myInfo;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.ImmigrationPage;

public class ImmigrationTest extends BaseTest {

    private ImmigrationPage immigrationPage;

    @BeforeMethod
    public void setUp() {
        DashboardPage dashboardPage = loginPage.login("Admin", "admin123");
        immigrationPage = dashboardPage.navigateToImmigration();
    }

    @Test
    public void testValidImmigration() {
        immigrationPage.addImmigration("Passport", "P123456789", "15", "03", "2020", "15", "03", "2025");
        
        // Verify success message
        String toastMessage = immigrationPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidImmigration() {
        immigrationPage.addImmigration("Passport", "", "15", "03", "2020", "15", "03", "2025");
        
        // Verify error messages for required fields
        assert immigrationPage.isErrorMessageDisplayed("Required") : "Expected 'Required' error message for document number";
    }
} 