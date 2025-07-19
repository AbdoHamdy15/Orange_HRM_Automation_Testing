package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AddCandidatePage extends AbstractComponent {

    WebDriver driver;

    public AddCandidatePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(name = "firstName")
    WebElement firstNameInput;

    @FindBy(name = "middleName")
    WebElement middleNameInput;

    @FindBy(name = "lastName")
    WebElement lastNameInput;

    @FindBy(xpath = "//label[text()='Email']/following::input[1]")
    WebElement emailInput;

    @FindBy(xpath = "//label[text()='Contact Number']/following::input[1]")
    WebElement contactNumberInput;

    @FindBy(xpath = "//label[text()='Vacancy']/following::div[contains(@class,'oxd-select-wrapper')]")
    WebElement vacancyDropdown;

    @FindBy(css = "input[type='file']")
    WebElement resumeUploadInput;

    @FindBy(xpath = "//label[text()='Keywords']/following::input[1]")
    WebElement keywordsInput;

    @FindBy(xpath = "//label[text()='Notes']/following::textarea[1]")
    WebElement notesInput;

    @FindBy(css = "span[class='oxd-checkbox-input oxd-checkbox-input--active --label-right oxd-checkbox-input']")
    WebElement consentCheckbox;

    @FindBy(css = "button[type='submit']")
    WebElement saveButton;

    public void enterName(String first, String middle, String last) {
        clearFieldReliably(firstNameInput);
        firstNameInput.sendKeys(first);

        clearFieldReliably(middleNameInput);
        middleNameInput.sendKeys(middle);

        clearFieldReliably(lastNameInput);
        lastNameInput.sendKeys(last);
    }

    public void enterEmail(String email) {
        clearFieldReliably(emailInput);
        emailInput.sendKeys(email);
    }

    public void enterContactNumber(String number) {
        clearFieldReliably(contactNumberInput);
        contactNumberInput.sendKeys(number);
    }

    public void selectVacancy(String vacancyText) {
        selectFromDropdown(vacancyDropdown, vacancyText);
    }

    public void uploadResume(String filePath) {
        resumeUploadInput.sendKeys(filePath);
    }

    public void enterKeywords(String keywords) {
        clearFieldReliably(keywordsInput);
        keywordsInput.sendKeys(keywords);
    }

    public void enterNotes(String note) {
        clearFieldReliably(notesInput);
        notesInput.sendKeys(note);
    }

    public void setConsent(boolean check) {
        if (consentCheckbox.isSelected() != check) {
            consentCheckbox.click();
        }
    }

    public void clickSave() {
        saveButton.click();
    }

    public void addCandidate(String first, String middle, String last, String email, String contact,
                             String vacancy, String resumePath, String keywords,
                             String notes, boolean consent) {

        enterName(first, middle, last);
        enterEmail(email);
        enterContactNumber(contact);
        selectVacancy(vacancy);
        uploadResume(resumePath);
        enterKeywords(keywords);
        enterNotes(notes);
        setConsent(consent);
        clickSave();
    }

    public boolean isErrorMessageDisplayed(String errorText) {
        return super.isErrorMessageDisplayed(errorText);
    }

    public String waitForToastAndGetMessage() {
        return super.waitForToastAndGetMessage();
    }
}
