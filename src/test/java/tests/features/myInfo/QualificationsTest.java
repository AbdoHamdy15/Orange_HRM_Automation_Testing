package tests.myInfo;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.myinfo.QualificationsPage;
import pages.LoginPage;

@Epic("OrangeHRM Application")
@Feature("My Info")
@Story("Qualifications")
public class QualificationsTest {
    
    private GUIDriver driver;
    private QualificationsPage qualificationsPage;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Qualifications page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        qualificationsPage = dashboard.navigateToQualifications()
                .waitForPageToBeReady(); // Wait for page to be fully loaded
    }

    // Work Experience Tests
    @Test(groups = {"myinfo", "qualifications"})
    @Story("Valid Work Experience")
    @Description("Test adding work experience with valid information")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidWorkExperience() {
        qualificationsPage.clickAddWorkExperience()
                          .enterCompany("Tech Solutions Inc")
                          .enterJobTitle("Software Engineer")
                          .setWorkFromDate("2020-01-15")
                          .setWorkToDate("2023-12-31")
                          .clickWorkExperienceSave()
                          .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "qualifications"})
    @Story("Invalid Work Experience")
    @Description("Test work experience validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidWorkExperience() {
        qualificationsPage.clickAddWorkExperience()
                          .enterCompany("")
                          .enterJobTitle("")
                          .setWorkFromDate("2020-01-15")
                          .setWorkToDate("2023-12-31")
                          .clickWorkExperienceSave()
                          .assertSpecificErrorDisplayed("Required");
    }

    // Education Tests
    @Test(groups = {"myinfo", "qualifications"})
    @Story("Valid Education")
    @Description("Test adding education with valid information")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidEducation() {
        qualificationsPage.clickAddEducation()
                          .selectEducationLevel("Bachelor's Degree")
                          .enterInstitute("Fayoum University")
                          .enterMajor("Communications and electronics Engineering")
                          .enterYear("2022")
                          .enterGPA("3.1")
                          .setEducationStartDate("2017-09-01")
                          .setEducationEndDate("2022-06-30")
                          .clickEducationSave()
                          .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "qualifications"})
    @Story("Invalid Education")
    @Description("Test education validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidEducation() {
        qualificationsPage.clickAddEducation()
                          .selectEducationLevel("")
                          .enterInstitute("")
                          .enterMajor("")
                          .enterYear("")
                          .enterGPA("")
                          .setEducationStartDate("")
                          .setEducationEndDate("")
                          .clickEducationSave()
                          .assertSpecificErrorDisplayed("Required");
    }

    // Skills Tests
    @Test(groups = {"myinfo", "qualifications"})
    @Story("Valid Skills")
    @Description("Test adding skills with valid information")
    @Severity(SeverityLevel.NORMAL)
    public void testValidSkills() {
        qualificationsPage.clickAddSkills()
                          .selectSkill("Java")
                          .enterYearsOfExperience("5")
                          .enterSkillComment("Expert in Java development")
                          .clickSkillSave()
                          .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "qualifications"})
    @Story("Invalid Skills")
    @Description("Test skills validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidSkills() {
        qualificationsPage.clickAddSkills()
                          .selectSkill("")
                          .enterYearsOfExperience("")
                          .enterSkillComment("")
                          .clickSkillSave()
                          .assertSpecificErrorDisplayed("Required");
    }

    // Languages Tests
    @Test(groups = {"myinfo", "qualifications"})
    @Story("Valid Languages")
    @Description("Test adding languages with valid information")
    @Severity(SeverityLevel.NORMAL)
    public void testValidLanguages() {
        qualificationsPage.clickAddLanguages()
                          .selectLanguage("English")
                          .selectFluency("Speaking")
                          .selectCompetency("Excellent")
                          .enterLanguageComment("Native speaker")
                          .clickLanguageSave()
                          .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "qualifications"})
    @Story("Invalid Languages")
    @Description("Test languages validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidLanguages() {
        qualificationsPage.clickAddLanguages()
                          .selectLanguage("")
                          .selectFluency("")
                          .selectCompetency("")
                          .enterLanguageComment("")
                          .clickLanguageSave()
                          .assertSpecificErrorDisplayed("Required");
    }

    // Licenses Tests
    @Test(groups = {"myinfo", "qualifications"})
    @Story("Valid Licenses")
    @Description("Test adding licenses with valid information")
    @Severity(SeverityLevel.NORMAL)
    public void testValidLicenses() {
        qualificationsPage.clickAddLicenses()
                          .selectLicenseType("Cisco Certified Network Associate (CCNA)")
                          .enterLicenseNumber("DL123456789")
                          .setLicenseIssuedDate("2020-01-15")
                          .setLicenseExpiryDate("2025-01-15")
                          .clickLicenseSave()
                          .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "qualifications"})
    @Story("Invalid Licenses")
    @Description("Test licenses validation with empty required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidLicenses() {
        qualificationsPage.clickAddLicenses()
                          .selectLicenseType("")
                          .enterLicenseNumber("")
                          .setLicenseIssuedDate("")
                          .setLicenseExpiryDate("")
                          .clickLicenseSave()
                          .assertSpecificErrorDisplayed("Required");
    }

    // Attachments Tests
    @Test(groups = {"myinfo", "qualifications"})
    @Story("Valid Attachments")
    @Description("Test adding attachments with valid file")
    @Severity(SeverityLevel.MINOR)
    public void testValidAttachments() {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/test-document.txt";
        qualificationsPage.clickAddAttachment()
                          .uploadFile(filePath)
                          .enterAttachmentComment("Test attachment")
                          .clickAttachmentSave()
                          .assertSuccessToastDisplayed();
    }

    @Test(groups = {"myinfo", "qualifications"})
    @Story("Invalid Attachments")
    @Description("Test attachments validation with empty file")
    @Severity(SeverityLevel.MINOR)
    public void testInvalidAttachments() {
        qualificationsPage.clickAddAttachment()
                          .uploadFile("")
                          .enterAttachmentComment("")
                          .clickAttachmentSave()
                          .assertSpecificErrorDisplayed("Required");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.teardown();
        }
    }
} 