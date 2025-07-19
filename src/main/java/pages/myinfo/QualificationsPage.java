package pages.myinfo;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class QualificationsPage extends AbstractComponent {

    WebDriver driver;

    public QualificationsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    // ==================== COMMON METHODS ====================
    
    public String waitForToastAndGetMessage() {
        return super.waitForToastAndGetMessage();
    }
    
    public boolean isErrorMessageDisplayed(String errorText) {
        return super.isErrorMessageDisplayed(errorText);
    }

    // ==================== WORK EXPERIENCE SECTION ====================
    
    public void clickAddWorkExperience() {
        WebElement addWorkExperienceButton = driver.findElement(By.xpath("//h6[text()='Work Experience']/following::button[normalize-space()='Add'][1]"));
        waitForElementToAppear(addWorkExperienceButton);
        addWorkExperienceButton.click();
    }

    public void fillWorkExperienceDetails(String company, String jobTitle, String fromDate, String toDate) {
        WebElement companyInput = driver.findElement(By.xpath("//label[text()='Company']/following::input[1]"));
        waitForElementToAppear(companyInput);
        clearFieldReliably(companyInput);
        companyInput.sendKeys(company);
        
        WebElement jobTitleInput = driver.findElement(By.xpath("//label[text()='Job Title']/following::input[1]"));
        waitForElementToAppear(jobTitleInput);
        clearFieldReliably(jobTitleInput);
        jobTitleInput.sendKeys(jobTitle);
        
        WebElement fromDateWorkInput = driver.findElement(By.xpath("//label[text()='From']/following::input[1]"));
        waitForElementToAppear(fromDateWorkInput);
        selectDateBySendKeys(fromDateWorkInput, fromDate);
        
        WebElement toDateWorkInput = driver.findElement(By.xpath("//label[text()='To']/following::input[1]"));
        waitForElementToAppear(toDateWorkInput);
        selectDateBySendKeys(toDateWorkInput, toDate);
    }

    public void clickWorkExperienceSave() {
        WebElement workSaveButton = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']"));
        waitForElementToAppear(workSaveButton);
        workSaveButton.click();
    }

    public void addWorkExperience(String company, String jobTitle, String fromDate, String toDate) {
        clickAddWorkExperience();
        fillWorkExperienceDetails(company, jobTitle, fromDate, toDate);
        clickWorkExperienceSave();
    }

    // ==================== EDUCATION SECTION ====================
    
    public void clickAddEducation() {
        WebElement addEducationButton = driver.findElement(By.xpath("//h6[text()='Education']/following::button[normalize-space()='Add'][1]"));
        waitForElementToAppear(addEducationButton);
        addEducationButton.click();
    }

    public void fillEducationDetails(String level, String institute, String major, String year, String gpa, String startDate, String endDate) {
        // Only select from dropdown if level is not empty
        if (!level.isEmpty()) {
            WebElement levelDropdown = driver.findElement(By.xpath("//label[text()='Level']/following::div[contains(@class,'oxd-select-wrapper')][1]"));
            waitForElementToAppear(levelDropdown);
            // Scroll to element to ensure it's visible
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", levelDropdown);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            selectFromDropdown(levelDropdown, level);
        }
        
        WebElement instituteInput = driver.findElement(By.xpath("//label[text()='Institute']/following::input[1]"));
        waitForElementToAppear(instituteInput);
        clearFieldReliably(instituteInput);
        instituteInput.sendKeys(institute);
        
        WebElement majorInput = driver.findElement(By.xpath("//label[text()='Major/Specialization']/following::input[1]"));
        waitForElementToAppear(majorInput);
        clearFieldReliably(majorInput);
        majorInput.sendKeys(major);
        
        WebElement yearInput = driver.findElement(By.xpath("//label[text()='Year']/following::input[1]"));
        waitForElementToAppear(yearInput);
        clearFieldReliably(yearInput);
        yearInput.sendKeys(year);
        
        WebElement gpaInput = driver.findElement(By.xpath("//label[text()='GPA/Score']/following::input[1]"));
        waitForElementToAppear(gpaInput);
        clearFieldReliably(gpaInput);
        gpaInput.sendKeys(gpa);
        
        WebElement startDateEduInput = driver.findElement(By.xpath("//label[text()='Start Date']/following::input[1]"));
        waitForElementToAppear(startDateEduInput);
        selectDateBySendKeys(startDateEduInput, startDate);
        
        WebElement endDateEduInput = driver.findElement(By.xpath("//label[text()='End Date']/following::input[1]"));
        waitForElementToAppear(endDateEduInput);
        selectDateBySendKeys(endDateEduInput, endDate);
    }

    public void clickEducationSave() {
        WebElement eduSaveButton = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']"));
        waitForElementToAppear(eduSaveButton);
        eduSaveButton.click();
    }

    public void addEducation(String level, String institute, String major, String year, String gpa, String startDate, String endDate) {
        clickAddEducation();
        fillEducationDetails(level, institute, major, year, gpa, startDate, endDate);
        clickEducationSave();
    }

    // ==================== SKILLS SECTION ====================
    
    public void clickAddSkill() {
        WebElement addSkillButton = driver.findElement(By.xpath("//h6[text()='Skills']/following::button[normalize-space()='Add'][1]"));
        waitForElementToAppear(addSkillButton);
        addSkillButton.click();
    }

    public void fillSkillDetails(String skill, String experience, String comment) {
        // Only select from dropdown if skill is not empty
        if (!skill.isEmpty()) {
            WebElement skillDropdown = driver.findElement(By.xpath("//label[text()='Skill']/following::div[contains(@class,'oxd-select-wrapper')][1]"));
            waitForElementToAppear(skillDropdown);
            // Scroll to element to ensure it's visible
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", skillDropdown);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            selectFromDropdown(skillDropdown, skill);
        }
        
        WebElement experienceInput = driver.findElement(By.xpath("//label[text()='Years of Experience']/following::input[1]"));
        waitForElementToAppear(experienceInput);
        clearFieldReliably(experienceInput);
        experienceInput.sendKeys(experience);
        
        WebElement skillCommentInput = driver.findElement(By.xpath("//label[text()='Comments']/following::textarea[1]"));
        waitForElementToAppear(skillCommentInput);
        clearFieldReliably(skillCommentInput);
        skillCommentInput.sendKeys(comment);
    }

    public void clickSkillSave() {
        WebElement skillSaveButton = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']"));
        waitForElementToAppear(skillSaveButton);
        skillSaveButton.click();
    }

    public void addSkill(String skill, String experience, String comments) {
        clickAddSkill();
        fillSkillDetails(skill, experience, comments);
        clickSkillSave();
    }

    // ==================== LANGUAGES SECTION ====================
    
    public void clickAddLanguage() {
        WebElement addLanguageButton = driver.findElement(By.xpath("//h6[text()='Languages']/following::button[normalize-space()='Add'][1]"));
        waitForElementToAppear(addLanguageButton);
        addLanguageButton.click();
    }

    public void fillLanguageDetails(String language, String fluency, String competency, String comment) {
        // Add small wait to ensure page is stable
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Only select from dropdown if language is not empty
        if (!language.isEmpty()) {
            WebElement languageDropdown = driver.findElement(By.xpath("//label[text()='Language']/following::div[contains(@class,'oxd-select-wrapper')][1]"));
            waitForElementToAppear(languageDropdown);
            // Scroll to element to ensure it's visible
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", languageDropdown);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            selectFromDropdown(languageDropdown, language);
        }
        
        // Only select from dropdown if fluency is not empty
        if (!fluency.isEmpty()) {
            WebElement fluencyDropdown = driver.findElement(By.xpath("//label[text()='Fluency']/following::div[contains(@class,'oxd-select-wrapper')][1]"));
            waitForElementToAppear(fluencyDropdown);
            // Scroll to element to ensure it's visible
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fluencyDropdown);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            selectFromDropdown(fluencyDropdown, fluency);
        }
        
        // Only select from dropdown if competency is not empty
        if (!competency.isEmpty()) {
            WebElement competencyDropdown = driver.findElement(By.xpath("//label[text()='Competency']/following::div[contains(@class,'oxd-select-wrapper')][1]"));
            waitForElementToAppear(competencyDropdown);
            // Scroll to element to ensure it's visible
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", competencyDropdown);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            selectFromDropdown(competencyDropdown, competency);
        }
        
        WebElement languageCommentInput = driver.findElement(By.xpath("//label[text()='Comment']/following::textarea[1]"));
        waitForElementToAppear(languageCommentInput);
        clearFieldReliably(languageCommentInput);
        languageCommentInput.sendKeys(comment);
    }

    public void clickLanguageSave() {
        WebElement languageSaveButton = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']"));
        waitForElementToAppear(languageSaveButton);
        languageSaveButton.click();
    }

    public void addLanguage(String language, String fluency, String competency, String comment) {
        clickAddLanguage();
        fillLanguageDetails(language, fluency, competency, comment);
        clickLanguageSave();
    }

    // ==================== LICENSES SECTION ====================
    
    public void clickAddLicense() {
        WebElement addLicenseButton = driver.findElement(By.xpath("//h6[text()='License']/following::button[normalize-space()='Add'][1]"));
        waitForElementToAppear(addLicenseButton);
        addLicenseButton.click();
    }

    public void fillLicenseDetails(String type, String number, String issuedDate, String expiryDate) {
        // Only select from dropdown if type is not empty
        if (!type.isEmpty()) {
            WebElement licenseDropdown = driver.findElement(By.xpath("//label[text()='License Type']/following::div[contains(@class,'oxd-select-wrapper')][1]"));
            waitForElementToAppear(licenseDropdown);
            // Scroll to element to ensure it's visible
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", licenseDropdown);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            selectFromDropdown(licenseDropdown, type);
        }
        
        WebElement licenseNumberInput = driver.findElement(By.xpath("//label[text()='License Number']/following::input[1]"));
        waitForElementToAppear(licenseNumberInput);
        clearFieldReliably(licenseNumberInput);
        licenseNumberInput.sendKeys(number);
        
        WebElement issuedDateInput = driver.findElement(By.xpath("//label[text()='Issued Date']/following::input[1]"));
        waitForElementToAppear(issuedDateInput);
        selectDateBySendKeys(issuedDateInput, issuedDate);
        
        WebElement expiryDateInput = driver.findElement(By.xpath("//label[text()='Expiry Date']/following::input[1]"));
        waitForElementToAppear(expiryDateInput);
        selectDateBySendKeys(expiryDateInput, expiryDate);
    }

    public void clickLicenseSave() {
        WebElement licenseSaveButton = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']"));
        waitForElementToAppear(licenseSaveButton);
        licenseSaveButton.click();
    }

    public void addLicense(String type, String number, String issuedDate, String expiryDate) {
        clickAddLicense();
        fillLicenseDetails(type, number, issuedDate, expiryDate);
        clickLicenseSave();
    }

    // ==================== ATTACHMENTS SECTION ====================
    
    public void clickAddAttachment() {
        WebElement addAttachmentButton = driver.findElement(By.xpath("//h6[text()='Attachments']/following::button[normalize-space()='Add'][1]"));
        waitForElementToAppear(addAttachmentButton);
        addAttachmentButton.click();
    }

    public void fillAttachmentDetails(String filePath, String comment) {
        WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
        waitForElementToAppear(fileInput);
        fileInput.sendKeys(filePath);
        
        WebElement commentInput = driver.findElement(By.xpath("//label[text()='Comment']/following::textarea[1]"));
        waitForElementToAppear(commentInput);
        clearFieldReliably(commentInput);
        commentInput.sendKeys(comment);
    }

    public void clickAttachmentSave() {
        WebElement attachmentSaveButton = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']"));
        waitForElementToAppear(attachmentSaveButton);
        attachmentSaveButton.click();
    }

    public void uploadAttachment(String filePath, String comment) {
        clickAddAttachment();
        fillAttachmentDetails(filePath, comment);
        clickAttachmentSave();
    }
}
