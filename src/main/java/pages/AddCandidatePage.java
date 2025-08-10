package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class AddCandidatePage {

    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // Locators
    private final By firstNameInput = By.name("firstName");
    private final By middleNameInput = By.name("middleName");
    private final By lastNameInput = By.name("lastName");
    private final By emailInput = By.xpath("//label[text()='Email']/following::input[1]");
    private final By contactNumberInput = By.xpath("//label[text()='Contact Number']/following::input[1]");
    private final By vacancyDropdown = By.xpath("//label[text()='Vacancy']/following::div[contains(@class,'oxd-select-wrapper')]");
    private final By resumeUploadInput = By.xpath("//input[@type='file']");
    private final By keywordsInput = By.xpath("//label[text()='Keywords']/following::input[1]");
    private final By notesInput = By.xpath("//label[text()='Notes']/following::textarea[1]");
    private final By consentCheckbox = By.cssSelector("span[class='oxd-checkbox-input oxd-checkbox-input--active --label-right oxd-checkbox-input']");
    private final By saveButton = By.cssSelector("button[type='submit']");
    private final By addCandidateHeader = By.xpath("//h6[text()='Add Candidate']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    public AddCandidatePage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(addCandidateHeader);
    }

    // Navigation methods
    @Step("Assert add candidate page is displayed")
    public AddCandidatePage assertAddCandidatePageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(addCandidateHeader), "Add Candidate page should be displayed");
        return this;
    }

    // Action methods
    @Step("Enter first name: {firstName}")
    public AddCandidatePage enterFirstName(String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            elementActions.clearField(firstNameInput);
            elementActions.type(firstNameInput, firstName);
        }
        return this;
    }

    @Step("Enter middle name: {middleName}")
    public AddCandidatePage enterMiddleName(String middleName) {
        if (middleName != null && !middleName.isEmpty()) {
            elementActions.clearField(middleNameInput);
            elementActions.type(middleNameInput, middleName);
        }
        return this;
    }

    @Step("Enter last name: {lastName}")
    public AddCandidatePage enterLastName(String lastName) {
        if (lastName != null && !lastName.isEmpty()) {
            elementActions.clearField(lastNameInput);
            elementActions.type(lastNameInput, lastName);
        }
        return this;
    }

    @Step("Enter email: {email}")
    public AddCandidatePage enterEmail(String email) {
        if (email != null && !email.isEmpty()) {
            elementActions.clearField(emailInput);
            elementActions.type(emailInput, email);
        }
        return this;
    }

    @Step("Enter contact number: {number}")
    public AddCandidatePage enterContactNumber(String number) {
        if (number != null && !number.isEmpty()) {
            elementActions.clearField(contactNumberInput);
            elementActions.type(contactNumberInput, number);
        }
        return this;
    }

    @Step("Select vacancy: {vacancyText}")
    public AddCandidatePage selectVacancy(String vacancyText) {
        if (vacancyText != null && !vacancyText.isEmpty()) {
            elementActions.selectFromDropdown(vacancyDropdown, vacancyText);
        }
        return this;
    }

    @Step("Upload resume: {filePath}")
    public AddCandidatePage uploadResume(String filePath) {
       elementActions.findElement(resumeUploadInput).sendKeys(filePath);
        return this;
    }

    @Step("Enter keywords: {keywords}")
    public AddCandidatePage enterKeywords(String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            elementActions.clearField(keywordsInput);
            elementActions.type(keywordsInput, keywords);
        }
        return this;
    }

    @Step("Enter notes: {note}")
    public AddCandidatePage enterNotes(String note) {
        if (note != null && !note.isEmpty()) {
            elementActions.clearField(notesInput);
            elementActions.type(notesInput, note);
        }
        return this;
    }

    @Step("Set consent: {check}")
    public AddCandidatePage setConsent(boolean check) {
        if (check) {
            elementActions.check(consentCheckbox);
        } else {
            elementActions.uncheck(consentCheckbox);
        }
        return this;
    }

    @Step("Click save button")
    public AddCandidatePage clickSave() {
        elementActions.click(saveButton);
        return this;
    }

    // Complete workflow
    @Step("Add candidate with basic details: {firstName} {lastName}, email: {email}")
    public AddCandidatePage addCandidateBasic(String firstName, String middleName, String lastName, String email, String contactNumber) {
        return enterFirstName(firstName)
                .enterMiddleName(middleName)
                .enterLastName(lastName)
                .enterEmail(email)
                .enterContactNumber(contactNumber);
    }

    @Step("Add candidate with all details: {firstName} {lastName}, email: {email}, vacancy: {vacancy}")
    public AddCandidatePage addCandidateComplete(String firstName, String middleName, String lastName, String email, String contactNumber,
                                               String vacancy, String resumePath, String keywords, String notes, boolean consent) {
        return enterFirstName(firstName)
                .enterMiddleName(middleName)
                .enterLastName(lastName)
                .enterEmail(email)
                .enterContactNumber(contactNumber)
                .selectVacancy(vacancy)
                .uploadResume(resumePath)
                .enterKeywords(keywords)
                .enterNotes(notes)
                .setConsent(consent);
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public AddCandidatePage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public AddCandidatePage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }
}
