package pages.myinfo;

import drivers.GUIDriver;
import enums.Gender;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class PersonalDetailsPage {

    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // Locators
    private final By firstNameInput = By.name("firstName");
    private final By middleNameInput = By.name("middleName");
    private final By lastNameInput = By.name("lastName");
    private final By employeeIdInput = By.xpath("//label[text()='Employee Id']/following::input[1]");
    private final By otherIdInput = By.xpath("//label[text()='Other Id']/following::input[1]");
    private final By licenseNumberInput = By.xpath("//label[text()=\"Driver's License Number\"]/following::input[1]");
    private final By licenseExpiryDateInput = By.xpath("//label[text()='License Expiry Date']/following::input[1]");
    private final By licenseExpiryCalendarIcon = By.xpath("//label[text()='License Expiry Date']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon']");
    private final By nationalityDropdown = By.xpath("//label[text()='Nationality']/following::div[contains(@class,'oxd-select-wrapper')]");
    private final By maritalStatusDropdown = By.xpath("//label[text()='Marital Status']/following::div[contains(@class,'oxd-select-wrapper')]");
    private final By dobInput = By.xpath("//label[text()='Date of Birth']/following::input[1]");
    private final By dobCalendarIcon = By.xpath("//label[text()='Date of Birth']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon']");
    private final By bloodTypeDropdown = By.xpath("//label[text()='Blood Type']/following::div[contains(@class,'oxd-select-wrapper')]");
    private final By testFieldInput = By.xpath("//label[text()='Test_Field']/following::input[1]");
    private final By addAttachmentButton = By.xpath("//h6[text()='Attachments']/following::button[normalize-space()='Add'][1]");
    private final By fileInput = By.cssSelector("input[type='file']");
    private final By commentInput = By.xpath("//label[text()='Comment']/following::textarea[1]");
    private final By attachmentSaveButton = By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']");
    private final By saveButtonAfterGender = By.xpath("(//button[normalize-space()='Save'])[1]");
    private final By saveButtonAfterTestField = By.xpath("(//button[normalize-space()='Save'])[2]");
    private final By personalDetailsHeader = By.xpath("//h6[text()='Personal Details']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    public PersonalDetailsPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        // Wait for Personal Details page to load
        waits.waitForElementVisible(personalDetailsHeader);
    }

    // Navigation methods
    @Step("Assert personal details page is displayed")
    public PersonalDetailsPage assertPersonalDetailsPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(personalDetailsHeader), "Personal Details page should be displayed");
        return this;
    }

    // Action methods
    @Step("Enter first name: {firstName}")
    public PersonalDetailsPage enterFirstName(String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            // Wait for loader to disappear
            waits.waitForElementInvisible(By.xpath("//div[contains(@class,'oxd-form-loader')]"));
            elementActions.clearField(firstNameInput);
            elementActions.type(firstNameInput, firstName);
        }
        return this;
    }

    @Step("Enter middle name: {middleName}")
    public PersonalDetailsPage enterMiddleName(String middleName) {
        if (middleName != null && !middleName.isEmpty()) {
            // Wait for loader to disappear
            waits.waitForElementInvisible(By.xpath("//div[contains(@class,'oxd-form-loader')]"));
            elementActions.clearField(middleNameInput);
            elementActions.type(middleNameInput, middleName);
        }
        return this;
    }

    @Step("Enter last name: {lastName}")
    public PersonalDetailsPage enterLastName(String lastName) {
        if (lastName != null && !lastName.isEmpty()) {
            elementActions.clearField(lastNameInput);
            elementActions.type(lastNameInput, lastName);
        }
        return this;
    }

    @Step("Enter employee ID: {employeeId}")
    public PersonalDetailsPage enterEmployeeId(String employeeId) {
        if (employeeId != null && !employeeId.isEmpty()) {
            waits.waitForElementVisible(employeeIdInput);
            elementActions.clearField(employeeIdInput);
            elementActions.type(employeeIdInput, employeeId);
        }
        return this;
    }

    @Step("Enter other ID: {otherId}")
    public PersonalDetailsPage enterOtherId(String otherId) {
        if (otherId != null && !otherId.isEmpty()) {
            elementActions.clearField(otherIdInput);
            elementActions.type(otherIdInput, otherId);
        }
        return this;
    }

    @Step("Enter license number: {licenseNumber}")
    public PersonalDetailsPage enterLicenseNumber(String licenseNumber) {
        if (licenseNumber != null && !licenseNumber.isEmpty()) {
            elementActions.clearField(licenseNumberInput);
            elementActions.type(licenseNumberInput, licenseNumber);
        }
        return this;
    }

    @Step("Set license expiry date: {date}")
    public PersonalDetailsPage setLicenseExpiryDate(String date) {
        if (date != null && !date.isEmpty()) {
            elementActions.clearField(licenseExpiryDateInput);
            elementActions.type(licenseExpiryDateInput, date);
        }
        return this;
    }

    @Step("Select nationality: {nationality}")
    public PersonalDetailsPage selectNationality(String nationality) {
        if (nationality != null && !nationality.isEmpty()) {
            elementActions.selectFromDropdown(nationalityDropdown, nationality);
        }
        return this;
    }

    @Step("Select marital status: {status}")
    public PersonalDetailsPage selectMaritalStatus(String status) {
        if (status != null && !status.isEmpty()) {
            elementActions.selectFromDropdown(maritalStatusDropdown, status);
        }
        return this;
    }

    @Step("Set date of birth: {date}")
    public PersonalDetailsPage setDateOfBirth(String date) {
        if (date != null && !date.isEmpty()) {
            elementActions.clearField(dobInput);
            elementActions.type(dobInput, date);
        }
        return this;
    }

    @Step("Set gender: {gender}")
    public PersonalDetailsPage setGender(Gender gender) {
        if (gender != null) {
            String genderValue = (gender == Gender.MALE) ? "1" : "2";
            By genderRadio = By.xpath("//input[@type='radio' and @value='" + genderValue + "']");
            elementActions.click(genderRadio);
        }
        return this;
    }

    @Step("Select blood type: {bloodType}")
    public PersonalDetailsPage selectBloodType(String bloodType) {
        if (bloodType != null && !bloodType.isEmpty()) {
            elementActions.selectFromDropdown(bloodTypeDropdown, bloodType);
        }
        return this;
    }

    @Step("Enter test field: {value}")
    public PersonalDetailsPage enterTestField(String value) {
        if (value != null && !value.isEmpty()) {
            elementActions.clearField(testFieldInput);
            elementActions.type(testFieldInput, value);
        }
        return this;
    }

    @Step("Upload attachment: {filePath}, comment: {comment}")
    public PersonalDetailsPage uploadAttachment(String filePath, String comment) {
        if (filePath != null && !filePath.isEmpty()) {
            elementActions.addAttachment(addAttachmentButton, fileInput, commentInput, attachmentSaveButton, filePath, comment);
        }
        return this;
    }

    @Step("Click save after gender")
    public PersonalDetailsPage clickSaveAfterGender() {
        elementActions.click(saveButtonAfterGender);
        return this;
    }

    @Step("Click save after test field")
    public PersonalDetailsPage clickSaveAfterTestField() {
        elementActions.click(saveButtonAfterTestField);
        return this;
    }

    // Complete workflow
    @Step("Fill personal details basic: {firstName} {lastName}, employee ID: {employeeId}")
    public PersonalDetailsPage fillPersonalDetailsBasic(String firstName, String middleName, String lastName, String employeeId, String otherId, String licenseNumber) {
        return enterFirstName(firstName)
                .enterMiddleName(middleName)
                .enterLastName(lastName)
                .enterEmployeeId(employeeId)
                .enterOtherId(otherId)
                .enterLicenseNumber(licenseNumber);
    }

    @Step("Fill personal details complete: {firstName} {lastName}, nationality: {nationality}")
    public PersonalDetailsPage fillPersonalDetailsComplete(String firstName, String middleName, String lastName, String employeeId, String otherId,
                                                         String licenseNum, String licenseExpiryDate, String nationality, String maritalStatus,
                                                         String dateOfBirth, Gender gender, String bloodType, String testFieldValue) {
        return enterFirstName(firstName)
                .enterMiddleName(middleName)
                .enterLastName(lastName)
                .enterEmployeeId(employeeId)
                .enterOtherId(otherId)
                .enterLicenseNumber(licenseNum)
                .setLicenseExpiryDate(licenseExpiryDate)
                .selectNationality(nationality)
                .selectMaritalStatus(maritalStatus)
                .setDateOfBirth(dateOfBirth)
                .setGender(gender)
                .selectBloodType(bloodType)
                .enterTestField(testFieldValue);
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public PersonalDetailsPage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public PersonalDetailsPage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }

    @Step("Check if at personal details page")
    public boolean isAt() {
        try {
            return elementActions.isDisplayed(personalDetailsHeader);
        } catch (Exception e) {
            return false;
        }
    }
}
