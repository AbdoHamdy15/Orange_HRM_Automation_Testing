package pages.myinfo;

import drivers.GUIDriver;
import enums.DocumentType;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class ImmigrationPage {

    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // Locators
    private final By addButton = By.xpath("//button[normalize-space()='Add'] | //button[contains(text(),'Add')] | //button[@type='button' and contains(text(),'Add')] | //button[contains(@class,'oxd-button') and contains(text(),'Add')] | //button[@type='submit' and contains(text(),'Add')] | //button[contains(@class,'btn') and contains(text(),'Add')] | //button[contains(@class,'button') and contains(text(),'Add')] | //button[contains(@class,'oxd-button--secondary') and contains(text(),'Add')] | //button[contains(@class,'oxd-button--label-secondary') and contains(text(),'Add')]");
    private final By passportRadio = By.xpath("//input[@type='radio' and @value='passport']");
    private final By visaRadio = By.xpath("//input[@type='radio' and @value='visa']");
    private final By numberInput = By.xpath("//label[text()='Number']/following::input[1]");
    private final By issueDateInput = By.xpath("//label[text()='Issued Date']/following::input[1]");
    private final By expiryDateInput = By.xpath("//label[text()='Expiry Date']/following::input[1]");
    private final By issuedByDropdown = By.xpath("//label[text()='Issued By']/following::div[contains(@class,'oxd-select-wrapper')]");
    private final By saveButton = By.xpath("//button[normalize-space()='Save']");
    private final By immigrationHeader = By.xpath("//h6[text()='Assigned Immigration Records']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    public ImmigrationPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
    }

    // Navigation methods
    @Step("Assert immigration page is displayed")
    public ImmigrationPage assertImmigrationPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(immigrationHeader), "Immigration page should be displayed");
        return this;
    }

    // Action methods
    @Step("Click add button")
    public ImmigrationPage clickAdd() {
        waits.waitForElementClickable(addButton);
        elementActions.click(addButton);
        return this;
    }

    @Step("Select document type: {documentType}")
    public ImmigrationPage selectDocumentType(DocumentType documentType) {
        if (documentType != null) {
            if (documentType == DocumentType.PASSPORT) {
                elementActions.click(passportRadio);
            } else if (documentType == DocumentType.VISA) {
                elementActions.click(visaRadio);
            }
        }
        return this;
    }

    @Step("Enter number: {number}")
    public ImmigrationPage enterNumber(String number) {
        if (number != null && !number.isEmpty()) {
            elementActions.clearField(numberInput);
            elementActions.type(numberInput, number);
        }
        return this;
    }

    @Step("Set issue date: {issueDate}")
    public ImmigrationPage setIssueDate(String issueDate) {
        if (issueDate != null && !issueDate.isEmpty()) {
            elementActions.clearField(issueDateInput);
            elementActions.type(issueDateInput, issueDate);
        }
        return this;
    }

    @Step("Set expiry date: {expiryDate}")
    public ImmigrationPage setExpiryDate(String expiryDate) {
        if (expiryDate != null && !expiryDate.isEmpty()) {
            elementActions.clearField(expiryDateInput);
            elementActions.type(expiryDateInput, expiryDate);
        }
        return this;
    }



    @Step("Select issued by: {issuedBy}")
    public ImmigrationPage selectIssuedBy(String issuedBy) {
        if (issuedBy != null && !issuedBy.isEmpty()) {
            elementActions.selectFromDropdown(issuedByDropdown, issuedBy);
        }
        return this;
    }

    @Step("Click save button")
    public ImmigrationPage clickSave() {
        elementActions.click(saveButton);
        return this;
    }

    // Complete workflow
    @Step("Add immigration record: {documentType}, {number}, {issueDate}, {expiryDate}, {issuedBy}")
    public ImmigrationPage addImmigrationRecord(DocumentType documentType, String number, String issueDate, String expiryDate, String issuedBy) {
        return clickAdd()
                .selectDocumentType(documentType)
                .enterNumber(number)
                .setIssueDate(issueDate)
                .setExpiryDate(expiryDate)
                .selectIssuedBy(issuedBy)
                .clickSave();
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public ImmigrationPage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public ImmigrationPage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }
}
