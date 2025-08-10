package pages.myinfo;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class QualificationsPage {

    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // ==================== LOCATORS ====================
    
    // Work Experience Locators
    private final By addWorkExperienceButton = By.xpath("//h6[text()='Work Experience']/following::button[contains(@class,'oxd-button--text') and normalize-space()='Add'][1]");
    private final By companyInput = By.xpath("//label[contains(text(),'Company')]/following::input[1]");
    private final By jobTitleInput = By.xpath("//label[contains(text(),'Job Title')]/following::input[1]");
    private final By fromDateWorkInput = By.xpath("//label[contains(text(),'From')]/following::input[1]");
    private final By toDateWorkInput = By.xpath("//label[contains(text(),'To')]/following::input[1]");
    private final By workSaveButton = By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']");
    
    // Education Locators
    private final By addEducationButton = By.xpath("//h6[text()='Education']/following::button[contains(@class,'oxd-button--text') and normalize-space()='Add'][1]");
    private final By levelDropdown = By.xpath("//label[contains(text(),'Level')]/following::div[contains(@class,'oxd-select-wrapper')][1] | //div[contains(@class,'oxd-select-text-input') and contains(text(),'Select')]");
    private final By instituteInput = By.xpath("//label[contains(text(),'Institute')]/following::input[1]");
    private final By majorInput = By.xpath("//label[contains(text(),'Major')]/following::input[1]");
    private final By yearInput = By.xpath("//label[contains(text(),'Year')]/following::input[1]");
    private final By gpaInput = By.xpath("//label[contains(text(),'GPA')]/following::input[1]");
    private final By startDateEduInput = By.xpath("//label[contains(text(),'Start Date')]/following::input[1]");
    private final By endDateEduInput = By.xpath("//label[contains(text(),'End Date')]/following::input[1]");
    private final By eduSaveButton = By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']");
    
    // Skills Locators
    private final By addSkillsButton = By.xpath("//h6[text()='Skills']/following::button[contains(@class,'oxd-button--text') and normalize-space()='Add'][1]");
    private final By skillDropdown = By.xpath("//label[contains(text(),'Skill')]/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By yearsOfExperienceInput = By.xpath("//label[contains(text(),'Years of Experience')]/following::input[1]");
    private final By skillCommentInput = By.xpath("//label[contains(text(),'Comments')]/following::textarea[1]");
    private final By skillSaveButton = By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']");
    
    // Languages Locators
    private final By addLanguagesButton = By.xpath("//h6[text()='Languages']/following::button[contains(@class,'oxd-button--text') and normalize-space()='Add'][1]");
    private final By languageDropdown = By.xpath("//label[text()='Language']/following::div[contains(@class,'oxd-select-text')][1] | //div[contains(@class,'oxd-select-text-input') and contains(text(),'Select')]");
    private final By fluencyDropdown = By.xpath("//label[contains(text(),'Fluency')]/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By competencyDropdown = By.xpath("//label[contains(text(),'Competency')]/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By languageCommentInput = By.xpath("//label[contains(text(),'Comments')]/following::textarea[1]");
    private final By languageSaveButton = By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']");
    
    // Licenses Locators
    private final By addLicensesButton = By.xpath("//h6[text()='License']/following::button[contains(@class,'oxd-button--text') and normalize-space()='Add'][1]");
    private final By licenseTypeDropdown = By.xpath("//label[contains(text(),'License Type')]/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By licenseNumberInput = By.xpath("//label[contains(text(),'License Number')]/following::input[1]");
    private final By licenseIssuedDateInput = By.xpath("//label[contains(text(),'Issued Date')]/following::input[1]");
    private final By licenseExpiryDateInput = By.xpath("//label[contains(text(),'Expiry Date')]/following::input[1]");
    private final By licenseSaveButton = By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']");
    
    // Attachment Locators
    private final By addAttachmentButton = By.xpath("//h6[text()='Attachments']/following::button[contains(@class,'oxd-button--text') and normalize-space()='Add'][1]");
    private final By fileInput = By.cssSelector("input[type='file']");
    private final By attachmentCommentInput = By.xpath("//label[contains(text(),'Comment')]/following::textarea[1]");
    private final By attachmentSaveButton = By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']");

    // Header
    private final By qualificationsHeader = By.xpath("//h6[text()='Qualifications']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    public QualificationsPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
    }

    // Navigation methods
    @Step("Assert qualifications page is displayed")
    public QualificationsPage assertQualificationsPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(qualificationsHeader), "Qualifications page should be displayed");
        return this;
    }

    @Step("Wait for qualifications page to be fully loaded")
    public QualificationsPage waitForPageToBeReady() {
        // Wait for the main header to be visible
        waits.waitForElementVisible(qualificationsHeader);
        
        // Wait for any auto-scroll to complete
        waits.waitForElementClickable(qualificationsHeader);
        
        // Wait a bit more for all sections to be loaded
        waits.waitForElementVisible(By.xpath("//h6[text()='Work Experience']"));
        
        return this;
    }

    // ==================== WORK EXPERIENCE SECTION ====================
    
    @Step("Click add work experience")
    public QualificationsPage clickAddWorkExperience() {
        elementActions.scrollToElement(addWorkExperienceButton);
        waits.waitForElementClickable(addWorkExperienceButton);
        elementActions.click(addWorkExperienceButton);
        return this;
    }

    @Step("Enter company: {company}")
    public QualificationsPage enterCompany(String company) {
        if (company != null && !company.isEmpty()) {
            elementActions.clearField(companyInput);
            elementActions.type(companyInput, company);
        }
        return this;
    }

    @Step("Enter job title: {jobTitle}")
    public QualificationsPage enterJobTitle(String jobTitle) {
        if (jobTitle != null && !jobTitle.isEmpty()) {
            elementActions.clearField(jobTitleInput);
            elementActions.type(jobTitleInput, jobTitle);
        }
        return this;
    }

    @Step("Set work from date: {fromDate}")
    public QualificationsPage setWorkFromDate(String fromDate) {
        if (fromDate != null && !fromDate.isEmpty()) {
            elementActions.clearField(fromDateWorkInput);
            elementActions.type(fromDateWorkInput, fromDate);
        }
        return this;
    }

    @Step("Set work to date: {toDate}")
    public QualificationsPage setWorkToDate(String toDate) {
        if (toDate != null && !toDate.isEmpty()) {
            elementActions.clearField(toDateWorkInput);
            elementActions.type(toDateWorkInput, toDate);
        }
        return this;
    }

    @Step("Click work experience save")
    public QualificationsPage clickWorkExperienceSave() {
        elementActions.click(workSaveButton);
        return this;
    }



    // ==================== EDUCATION SECTION ====================
    
    @Step("Click add education")
    public QualificationsPage clickAddEducation() {
        elementActions.scrollToElement(addEducationButton);
        waits.waitForElementClickable(addEducationButton);
        elementActions.click(addEducationButton);
        return this;
    }

    @Step("Select education level: {level}")
    public QualificationsPage selectEducationLevel(String level) {
        if (level != null && !level.isEmpty()) {
            // Wait for dropdown to be ready and scroll to it
            waits.waitForElementClickable(levelDropdown);
            elementActions.selectFromDropdown(levelDropdown, level);
        }
        return this;
    }

    @Step("Enter institute: {institute}")
    public QualificationsPage enterInstitute(String institute) {
        if (institute != null && !institute.isEmpty()) {
            elementActions.scrollToElement(instituteInput);
            waits.waitForElementClickable(instituteInput);
            elementActions.clearField(instituteInput);
            elementActions.type(instituteInput, institute);
        }
        return this;
    }

    @Step("Enter major: {major}")
    public QualificationsPage enterMajor(String major) {
        if (major != null && !major.isEmpty()) {
            elementActions.clearField(majorInput);
            elementActions.type(majorInput, major);
        }
        return this;
    }

    @Step("Enter year: {year}")
    public QualificationsPage enterYear(String year) {
        if (year != null && !year.isEmpty()) {
            elementActions.clearField(yearInput);
            elementActions.type(yearInput, year);
        }
        return this;
    }

    @Step("Enter GPA: {gpa}")
    public QualificationsPage enterGPA(String gpa) {
        if (gpa != null && !gpa.isEmpty()) {
            elementActions.clearField(gpaInput);
            elementActions.type(gpaInput, gpa);
        }
        return this;
    }

    @Step("Set education start date: {startDate}")
    public QualificationsPage setEducationStartDate(String startDate) {
        if (startDate != null && !startDate.isEmpty()) {
            elementActions.clearField(startDateEduInput);
            elementActions.type(startDateEduInput, startDate);
        }
        return this;
    }

    @Step("Set education end date: {endDate}")
    public QualificationsPage setEducationEndDate(String endDate) {
        if (endDate != null && !endDate.isEmpty()) {
            elementActions.clearField(endDateEduInput);
            elementActions.type(endDateEduInput, endDate);
        }
        return this;
    }

    @Step("Click education save")
    public QualificationsPage clickEducationSave() {
        elementActions.click(eduSaveButton);
        return this;
    }

    @Step("Add education: {level}, {institute}, {major}, {year}, {gpa}, {startDate}, {endDate}")
    public QualificationsPage addEducation(String level, String institute, String major, String year, String gpa, String startDate, String endDate) {
        return clickAddEducation()
                .selectEducationLevel(level)
                .enterInstitute(institute)
                .enterMajor(major)
                .enterYear(year)
                .enterGPA(gpa)
                .setEducationStartDate(startDate)
                .setEducationEndDate(endDate)
                .clickEducationSave();
    }

    // ==================== SKILLS SECTION ====================
    
    @Step("Click add skills")
    public QualificationsPage clickAddSkills() {
        elementActions.scrollToElement(addSkillsButton);
        waits.waitForElementClickable(addSkillsButton);
        elementActions.click(addSkillsButton);
        return this;
    }

    @Step("Select skill: {skill}")
    public QualificationsPage selectSkill(String skill) {
        if (skill != null && !skill.isEmpty()) {
            elementActions.selectFromDropdown(skillDropdown, skill);
        }
        return this;
    }

    @Step("Enter years of experience: {years}")
    public QualificationsPage enterYearsOfExperience(String years) {
        if (years != null && !years.isEmpty()) {
            elementActions.clearField(yearsOfExperienceInput);
            elementActions.type(yearsOfExperienceInput, years);
        }
        return this;
    }

    @Step("Enter skill comment: {comment}")
    public QualificationsPage enterSkillComment(String comment) {
        if (comment != null && !comment.isEmpty()) {
            elementActions.clearField(skillCommentInput);
            elementActions.type(skillCommentInput, comment);
        }
        return this;
    }

    @Step("Click skill save")
    public QualificationsPage clickSkillSave() {
        elementActions.click(skillSaveButton);
        return this;
    }

    @Step("Add skill: {skill}, {years}, {comment}")
    public QualificationsPage addSkill(String skill, String years, String comment) {
        return clickAddSkills()
                .selectSkill(skill)
                .enterYearsOfExperience(years)
                .enterSkillComment(comment)
                .clickSkillSave();
    }

    // ==================== LANGUAGES SECTION ====================
    
    @Step("Click add languages")
    public QualificationsPage clickAddLanguages() {
        elementActions.scrollToElement(addLanguagesButton);
        waits.waitForElementClickable(addLanguagesButton);
        elementActions.click(addLanguagesButton);
        return this;
    }

    @Step("Select language: {language}")
    public QualificationsPage selectLanguage(String language) {
        if (language != null && !language.isEmpty()) {
            // Wait for dropdown to be ready and scroll to it
            waits.waitForElementClickable(languageDropdown);
            elementActions.scrollToElement(languageDropdown);
            elementActions.selectFromDropdown(languageDropdown, language);
        }
        return this;
    }

    @Step("Select fluency: {fluency}")
    public QualificationsPage selectFluency(String fluency) {
        if (fluency != null && !fluency.isEmpty()) {
            // Wait for dropdown to be ready and scroll to it
            waits.waitForElementClickable(fluencyDropdown);
            elementActions.scrollToElement(fluencyDropdown);
            elementActions.selectFromDropdown(fluencyDropdown, fluency);
        }
        return this;
    }

    @Step("Select competency: {competency}")
    public QualificationsPage selectCompetency(String competency) {
        if (competency != null && !competency.isEmpty()) {
            // Wait for dropdown to be ready and scroll to it
            waits.waitForElementClickable(competencyDropdown);
            elementActions.scrollToElement(competencyDropdown);
            elementActions.selectFromDropdown(competencyDropdown, competency);
        }
        return this;
    }

    @Step("Enter language comment: {comment}")
    public QualificationsPage enterLanguageComment(String comment) {
        if (comment != null && !comment.isEmpty()) {
            elementActions.clearField(languageCommentInput);
            elementActions.type(languageCommentInput, comment);
        }
        return this;
    }

    @Step("Click language save")
    public QualificationsPage clickLanguageSave() {
        elementActions.click(languageSaveButton);
        return this;
    }

    @Step("Add language: {language}, {fluency}, {competency}, {comment}")
    public QualificationsPage addLanguage(String language, String fluency, String competency, String comment) {
        return clickAddLanguages()
                .selectLanguage(language)
                .selectFluency(fluency)
                .selectCompetency(competency)
                .enterLanguageComment(comment)
                .clickLanguageSave();
    }

    // ==================== LICENSES SECTION ====================
    
    @Step("Click add licenses")
    public QualificationsPage clickAddLicenses() {
        elementActions.scrollToElement(addLicensesButton);
        waits.waitForElementClickable(addLicensesButton);
        elementActions.click(addLicensesButton);
        return this;
    }

    @Step("Select license type: {licenseType}")
    public QualificationsPage selectLicenseType(String licenseType) {
        if (licenseType != null && !licenseType.isEmpty()) {
            elementActions.selectFromDropdown(licenseTypeDropdown, licenseType);
        }
        return this;
    }

    @Step("Enter license number: {licenseNumber}")
    public QualificationsPage enterLicenseNumber(String licenseNumber) {
        if (licenseNumber != null && !licenseNumber.isEmpty()) {
            elementActions.clearField(licenseNumberInput);
            elementActions.type(licenseNumberInput, licenseNumber);
        }
        return this;
    }

    @Step("Set license issued date: {issuedDate}")
    public QualificationsPage setLicenseIssuedDate(String issuedDate) {
        if (issuedDate != null && !issuedDate.isEmpty()) {
            elementActions.clearField(licenseIssuedDateInput);
            elementActions.type(licenseIssuedDateInput, issuedDate);
        }
        return this;
    }

    @Step("Set license expiry date: {expiryDate}")
    public QualificationsPage setLicenseExpiryDate(String expiryDate) {
        if (expiryDate != null && !expiryDate.isEmpty()) {
            elementActions.clearField(licenseExpiryDateInput);
            elementActions.type(licenseExpiryDateInput, expiryDate);
        }
        return this;
    }

    @Step("Click license save")
    public QualificationsPage clickLicenseSave() {
        elementActions.click(licenseSaveButton);
        return this;
    }

    @Step("Add license: {licenseType}, {licenseNumber}, {issuedDate}, {expiryDate}")
    public QualificationsPage addLicense(String licenseType, String licenseNumber, String issuedDate, String expiryDate) {
        return clickAddLicenses()
                .selectLicenseType(licenseType)
                .enterLicenseNumber(licenseNumber)
                .setLicenseIssuedDate(issuedDate)
                .setLicenseExpiryDate(expiryDate)
                .clickLicenseSave();
    }

    // ==================== ATTACHMENTS SECTION ====================
    
    @Step("Click add attachment")
    public QualificationsPage clickAddAttachment() {
        elementActions.scrollToElement(addAttachmentButton);
        waits.waitForElementClickable(addAttachmentButton);
        elementActions.click(addAttachmentButton);
        return this;
    }

    @Step("Upload file: {filePath}")
    public QualificationsPage uploadFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            elementActions.uploadFile(fileInput, filePath);
        }
        return this;
    }

    @Step("Enter attachment comment: {comment}")
    public QualificationsPage enterAttachmentComment(String comment) {
        if (comment != null && !comment.isEmpty()) {
            elementActions.clearField(attachmentCommentInput);
            elementActions.type(attachmentCommentInput, comment);
        }
        return this;
    }

    @Step("Click attachment save")
    public QualificationsPage clickAttachmentSave() {
        elementActions.click(attachmentSaveButton);
        return this;
    }

    @Step("Add attachment: {filePath}, {comment}")
    public QualificationsPage addAttachment(String filePath, String comment) {
        return clickAddAttachment()
                .uploadFile(filePath)
                .enterAttachmentComment(comment)
                .clickAttachmentSave();
    }

    // ==================== VALIDATION METHODS ====================
    
    @Step("Assert success toast is displayed")
    public QualificationsPage assertSuccessToastDisplayed() {
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }

    @Step("Assert specific error message is displayed: {expectedError}")
    public QualificationsPage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }
} 