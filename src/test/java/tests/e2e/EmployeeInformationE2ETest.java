package tests.e2e;

import drivers.GUIDriver;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.myinfo.ContactDetailsPage;
import pages.myinfo.EmergencyContactsPage;
import pages.myinfo.ImmigrationPage;
import pages.myinfo.MembershipsPage;
import pages.myinfo.QualificationsPage;

@Epic("Employee Information Management")
@Feature("My Info - Complete Employee Profile")
public class EmployeeInformationE2ETest {

    private GUIDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        loginPage = new LoginPage(driver);
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → My Info → Emergency Contacts → Immigration → Logout")
    @Story("Employee Information - Contact and Emergency Management")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmployeeContactAndEmergencyE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");



        // Step 2: Navigate to Emergency Contacts
        EmergencyContactsPage emergencyContactsPage = dashboard.navigateToEmergencyContacts();
        emergencyContactsPage.clickAdd()
                .enterName("Fatima Hassan")
                .enterRelationship("Spouse")
                .enterHomePhone("123-456-7890")
                .enterMobile("098-765-4321")
                .enterWorkPhone("555-123-4567")
                .clickSave()
                .assertSuccessToastDisplayed();

        // Step 3: Navigate to Immigration
        ImmigrationPage immigrationPage = dashboard.navigateToImmigration();
        immigrationPage.clickAdd()
                .enterNumber("P123456789")
                .setIssueDate("2020-01-01")
                .setExpiryDate("2030-01-01")
                .selectIssuedBy("Egypt")
                .clickSave()
                .assertSuccessToastDisplayed();

        // Step 5: Logout
        dashboard.logout()
                .assertLoginPageDisplayed();
    }

    @Test(groups = {"e2e"})
    @Description("E2E Scenario: Login → My Info → Qualifications → Memberships → Logout")
    @Story("Employee Information - Qualifications and Memberships")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmployeeQualificationsAndMembershipsE2E() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        
        // Step 1: Login
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                                         .login("Admin", "admin123");

        // Step 2: Navigate to Qualifications - Work Experience
        QualificationsPage qualificationsPage = dashboard.navigateToQualifications();
        qualificationsPage.waitForPageToBeReady()
                .clickAddWorkExperience()
                .enterCompany("Tech Solutions Inc")
                .enterJobTitle("Software Engineer")
                .setWorkFromDate("2020-01-15")
                .setWorkToDate("2023-12-31")
                .clickWorkExperienceSave()
                .assertSuccessToastDisplayed();

        // Step 3: Navigate to Memberships
        MembershipsPage membershipsPage = dashboard.navigateToMemberships();
        membershipsPage.clickAdd()
                .selectMembership("ACCA")
                .selectSubscriptionPaidBy("Individual")
                .enterSubscriptionAmount("500")
                .selectCurrency("Egyptian Pound")
                .setCommenceDate("2024-01-01")
                .setRenewalDate("2025-01-01")
                .clickSave()
                .assertSuccessToastDisplayed();

        // Step 4: Logout
        dashboard.logout()
                .assertLoginPageDisplayed();
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }
}
