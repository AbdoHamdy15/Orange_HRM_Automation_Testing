package tests.myInfo;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.EmergencyContactsPage;

public class EmergencyContactsTest extends BaseTest {

    private EmergencyContactsPage emergencyContactsPage;

    @BeforeMethod
    public void setUp() {
        DashboardPage dashboardPage = loginPage.login("Admin", "admin123");
        emergencyContactsPage = dashboardPage.navigateToEmergencyContacts();
    }

    @Test
    public void testValidEmergencyContact() {
        emergencyContactsPage.addEmergencyContact("John Smith", "Father", "555-1234", "555-5678", "555-9012");
        
        // Verify success message
        String toastMessage = emergencyContactsPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidEmergencyContact() {
        emergencyContactsPage.addEmergencyContact("", "", "555-1234", "555-5678", "555-9012");
        
        // Verify error messages for required fields
        assert emergencyContactsPage.isErrorMessageDisplayed("Required") : "Expected 'Required' error message for name and relationship";
    }
} 