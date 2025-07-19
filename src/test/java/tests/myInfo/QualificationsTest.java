package tests.myInfo;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.QualificationsPage;

public class QualificationsTest extends BaseTest {

    private QualificationsPage qualificationsPage;

    @BeforeMethod
    public void setUp() {
        DashboardPage dashboardPage = loginPage.login("Admin", "admin123");
        qualificationsPage = dashboardPage.navigateToQualifications();
    }

    // Work Experience Tests
    @Test
    public void testValidWorkExperience() {
        qualificationsPage.addWorkExperience("Tech Solutions Inc", "Software Engineer", "2020-01-15", "2023-12-31");
        
        // Verify success message
        String toastMessage = qualificationsPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidWorkExperience() {
        qualificationsPage.addWorkExperience("", "", "2020-01-15", "2023-12-31");
        
        // Verify error messages for required fields
        assert qualificationsPage.isErrorMessageDisplayed("Required") : "Expected 'Required' error message for company and job title";
    }

    // Education Tests
    @Test
    public void testValidEducation() {
        qualificationsPage.clickAddEducation();
        qualificationsPage.fillEducationDetails("College Undergraduate", "Cairo University", "Computer Science", "2020", "3.8", "2016-09-01", "2020-06-30");
        qualificationsPage.clickEducationSave();
        
        // Verify success message
        String toastMessage = qualificationsPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidEducation() {
        qualificationsPage.clickAddEducation();
        qualificationsPage.fillEducationDetails("", "Fayoum University", "Communications", "2022", "3.1", "2016-09-01", "2020-06-30");
        qualificationsPage.clickEducationSave();
        
        // Verify error messages for required fields
        assert qualificationsPage.isErrorMessageDisplayed("Required") : "Expected 'Required' error message for level";
    }

    @Test
    public void testInvalidEducationYear() {
        qualificationsPage.clickAddEducation();
        qualificationsPage.fillEducationDetails("College Undergraduate", "Cairo University", "Computer Science", "abc", "3.8", "2016-09-01", "2020-06-30");
        qualificationsPage.clickEducationSave();
        
        // Verify error message for invalid year
        assert qualificationsPage.isErrorMessageDisplayed("Should be a number") : "Expected 'Should be a number' error message";
    }

    // Skills Tests
    @Test
    public void testValidSkill() {
        qualificationsPage.clickAddSkill();
        qualificationsPage.fillSkillDetails("SQL", "5", "Expert in Java development");
        qualificationsPage.clickSkillSave();
        
        // Verify success message
        String toastMessage = qualificationsPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidSkill() {
        qualificationsPage.clickAddSkill();
        qualificationsPage.fillSkillDetails("", "5", "Expert in Java development");
        qualificationsPage.clickSkillSave();
        
        // Verify error messages for required fields
        assert qualificationsPage.isErrorMessageDisplayed("Required") : "Expected 'Required' error message for skill field";
    }

    @Test
    public void testInvalidSkillYear() {
        qualificationsPage.clickAddSkill();
        qualificationsPage.fillSkillDetails("JIRA", "abc", "Expert in Java development");
        qualificationsPage.clickSkillSave();
        
        // Verify error message for invalid year
        assert qualificationsPage.isErrorMessageDisplayed("Should be a number") : "Expected 'Should be a number' error message";
    }

    // Languages Tests
    @Test
    public void testValidLanguage() {
        qualificationsPage.clickAddLanguage();
        qualificationsPage.fillLanguageDetails("English", "", "Good", "Fluent in English");
        qualificationsPage.clickLanguageSave();
        
        // Verify success message
        String toastMessage = qualificationsPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidLanguage() {
        qualificationsPage.clickAddLanguage();
        qualificationsPage.fillLanguageDetails("", "", "", "Fluent in English");
        qualificationsPage.clickLanguageSave();
        
        // Verify error messages for required fields
        assert qualificationsPage.isErrorMessageDisplayed("Required") : "Expected 'Required' error message for language fields";
    }

    // Licenses Tests
    @Test
    public void testValidLicense() {
        qualificationsPage.clickAddLicense();
        qualificationsPage.fillLicenseDetails("Cisco Certified Network Associate (CCNA)", "PL123456", "2018-01-15", "2028-01-15");
        qualificationsPage.clickLicenseSave();
        
        // Verify success message
        String toastMessage = qualificationsPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidLicense() {
        qualificationsPage.clickAddLicense();
        qualificationsPage.fillLicenseDetails("", "PL123456", "2018-01-15", "2028-01-15");
        qualificationsPage.clickLicenseSave();
        
        // Verify error messages for required fields
        assert qualificationsPage.isErrorMessageDisplayed("Required") : "Expected 'Required' error message for license type field";
    }

    // Attachments Tests
    @Test
    public void testValidAttachment() {
        qualificationsPage.uploadAttachment("C:\\test\\sample.pdf", "Test attachment");
        
        // Verify success message
        String toastMessage = qualificationsPage.waitForToastAndGetMessage();
        assert toastMessage != null && toastMessage.contains("Success") : "Expected success message";
    }

    @Test
    public void testInvalidAttachment() {
        qualificationsPage.uploadAttachment("", "");
        
        // Verify error messages for required fields
        assert qualificationsPage.isErrorMessageDisplayed("Required") : "Expected 'Required' error message for attachment fields";
    }
} 