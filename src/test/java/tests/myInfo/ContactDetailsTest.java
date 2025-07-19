package tests.myInfo;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.ContactDetailsPage;

public class ContactDetailsTest extends BaseTest {

    private ContactDetailsPage contactDetailsPage;

    @BeforeMethod
    public void setUp() {
        DashboardPage dashboardPage = loginPage.login("Admin", "admin123");
        contactDetailsPage = dashboardPage.navigateToContactDetails();
    }

    @Test
    public void testValidContactDetails() {
        contactDetailsPage.fillAddress("123 Main St", "Apt 4B", "New York", "NY", "10001", "United States");
        contactDetailsPage.fillPhoneNumbers("555-1234", "555-5678", "555-9012");
        contactDetailsPage.fillEmails("work@example.com", "personal@example.com");
        contactDetailsPage.clickSave();
        
        // Verify success message or toast
        String toastMessage = contactDetailsPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidContactDetails() {
        contactDetailsPage.fillAddress("123 Main St", "Apt 4B", "New York", "NY", "10001", "United States");
        contactDetailsPage.fillPhoneNumbers("555-1234", "555-5678", "555-9012");
        contactDetailsPage.fillEmails("invalid-email", "not-an-email");
        contactDetailsPage.clickSave();
        
        // Verify email format error messages
        assert contactDetailsPage.isErrorMessageDisplayed("Expected format: admin@example.com") : "Expected email format error message";
    }
} 