package pages.myinfo;

import abstractComponents.AbstractComponent;
import enums.Gender;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PersonalDetailsPage extends AbstractComponent {

    WebDriver driver;

    public PersonalDetailsPage(WebDriver driver) {
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

    @FindBy(xpath = "//label[text()='Employee Id']/following::input[1]")
    WebElement employeeIdInput;

    @FindBy(xpath = "//label[text()='Other Id']/following::input[1]")
    WebElement otherIdInput;

    @FindBy(xpath = "//label[text()=\"Driver's License Number\"]/following::input[1]")
    WebElement licenseNumberInput;

    @FindBy(xpath = "//label[text()='License Expiry Date']/following::input[1]")
    WebElement licenseExpiryDateInput;

    @FindBy(xpath = "//label[text()='License Expiry Date']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon']")
    WebElement licenseExpiryCalendarIcon;

    @FindBy(xpath = "//label[text()='Nationality']/following::div[contains(@class,'oxd-select-wrapper')]")
    WebElement nationalityDropdown;

    @FindBy(xpath = "//label[text()='Marital Status']/following::div[contains(@class,'oxd-select-wrapper')]")
    WebElement maritalStatusDropdown;

    @FindBy(xpath = "//label[text()='Date of Birth']/following::input[1]")
    WebElement dobInput;

    @FindBy(xpath = "//label[text()='Date of Birth']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon']")
    WebElement dobCalendarIcon;

    @FindBy(xpath = "//label[text()='Blood Type']/following::div[contains(@class,'oxd-select-wrapper')]")
    WebElement bloodTypeDropdown;

    @FindBy(xpath = "//label[text()='Test_Field']/following::input[1]")
    WebElement testFieldInput;

    @FindBy(xpath = "//h6[text()='Attachments']/following::button[normalize-space()='Add'][1]")
    WebElement addAttachmentButton;

    @FindBy(css = "input[type='file']")
    WebElement fileInput;

    @FindBy(xpath = "//label[text()='Comment']/following::textarea[1]")
    WebElement commentInput;

    @FindBy(xpath = "//div[@class='oxd-form-actions']//button[normalize-space()='Save']")
    WebElement attachmentSaveButton;

    @FindBy(xpath = "(//button[normalize-space()='Save'])[1]")
    WebElement saveButtonAfterGender;

    @FindBy(xpath = "(//button[normalize-space()='Save'])[2]")
    WebElement saveButtonAfterTestField;

    @FindBy(xpath = "//h6[text()='Personal Details']")
    private WebElement personalDetailsHeader;

    public void updateName(String first, String middle, String last) {
        clearFieldReliably(firstNameInput);
        firstNameInput.sendKeys(first);

        clearFieldReliably(middleNameInput);
        middleNameInput.sendKeys(middle);

        clearFieldReliably(lastNameInput);
        lastNameInput.sendKeys(last);
    }

    public void updateIDs(String employeeId, String otherId, String licenseNumber) {
        clearFieldReliably(employeeIdInput);
        employeeIdInput.sendKeys(employeeId);

        clearFieldReliably(otherIdInput);
        otherIdInput.sendKeys(otherId);

        clearFieldReliably(licenseNumberInput);
        licenseNumberInput.sendKeys(licenseNumber);
    }

    public void setLicenseExpiryDate(String date) {
        selectDateBySendKeys(licenseExpiryDateInput, date);
    }

    public void selectNationality(String nationality) {
        selectFromDropdown(nationalityDropdown, nationality);
    }

    public void selectMaritalStatus(String status) {
        selectFromDropdown(maritalStatusDropdown, status);
    }

    public void setDateOfBirth(String date) {
        selectDateBySendKeys(dobInput, date);
    }

    public void setGender(Gender gender) {
        // If gender is MALE, don't do anything since it's the default
        if (gender == Gender.MALE) {
            return;
        }
        selectRadioOptionByLabel(gender.getLabel());
    }

    public void selectBloodType(String bloodType) {
        selectFromDropdown(bloodTypeDropdown, bloodType);
    }

    public void enterTestField(String value) {
        clearFieldReliably(testFieldInput);
        testFieldInput.sendKeys(value);
    }

    public void uploadAttachment(String filePath, String comment) {
        addAttachment(addAttachmentButton, fileInput, commentInput, attachmentSaveButton, filePath, comment);
    }

    public boolean isAt() {
        waitForElementToAppear(personalDetailsHeader);
        return personalDetailsHeader.isDisplayed();
    }

    public void fillPersonalDetails(String first, String middle, String last, String empId, String otherId,
                                    String licenseNum, String licenseExpiryDate,
                                    String nationality, String maritalStatus,
                                    String dateOfBirth,
                                    Gender gender, String bloodType, String testFieldValue) {
        updateName(first, middle, last);
        updateIDs(empId, otherId, licenseNum);
        setLicenseExpiryDate(licenseExpiryDate);
        selectNationality(nationality);
        selectMaritalStatus(maritalStatus);
        setDateOfBirth(dateOfBirth);
        setGender(gender);
        clickSaveAfterGender();
        selectBloodType(bloodType);
        enterTestField(testFieldValue);
        clickSaveAfterTestField();
    }

    public void clickSaveAfterGender() {
        saveButtonAfterGender.click();
    }

    public void clickSaveAfterTestField() {
        saveButtonAfterTestField.click();
    }
}
