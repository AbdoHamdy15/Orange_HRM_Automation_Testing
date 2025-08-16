package pages;

import drivers.GUIDriver;
import enums.Status;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;
import pages.myinfo.PersonalDetailsPage;

public class AddEmployeePage {
    
    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;
    
    // Locators
    private final By firstNameInput = By.name("firstName");
    private final By middleNameInput = By.name("middleName");
    private final By lastNameInput = By.name("lastName");
    private final By employeeIdInput = By.xpath("//label[text()='Employee Id']/following::input[1]");
    private final By uploadPictureInput = By.xpath("//button[contains(@class,'employee-image-action')]]");
    private final By createLoginToggle = By.cssSelector(".oxd-switch-input");
    private final By usernameInput = By.xpath("//label[text()='Username']/following::input[1]");
    private final By passwordInput = By.xpath("//label[text()='Password']/following::input[1]");
    private final By confirmPasswordInput = By.xpath("//label[text()='Confirm Password']/following::input[1]");
    private final By enabledStatusOption = By.xpath("//input[@type='radio' and @value='1']/parent::label");
    private final By disabledStatusOption = By.xpath("//input[@type='radio' and @value='2']");
    private final By saveButton = By.cssSelector("button[type='submit']");
    private final By addEmployeeHeader = By.xpath("//h6[text()='Add Employee']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    // Constructor
    public AddEmployeePage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(addEmployeeHeader);
    }

    // Validations
    @Step("Assert add employee page is displayed")
    public AddEmployeePage assertAddEmployeePageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(addEmployeeHeader), "Add Employee page should be displayed");
        return this;
    }

    // Actions
    @Step("Enter first name: {firstName}")
    public AddEmployeePage enterFirstName(String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            elementActions.clearField(firstNameInput);
            elementActions.type(firstNameInput, firstName);
        }
        return this;
    }

    @Step("Enter middle name: {middleName}")
    public AddEmployeePage enterMiddleName(String middleName) {
        if (middleName != null && !middleName.isEmpty()) {
            elementActions.clearField(middleNameInput);
            elementActions.type(middleNameInput, middleName);
        }
        return this;
    }

    @Step("Enter last name: {lastName}")
    public AddEmployeePage enterLastName(String lastName) {
        if (lastName != null && !lastName.isEmpty()) {
            elementActions.clearField(lastNameInput);
            elementActions.type(lastNameInput, lastName);
        }
        return this;
    }

    @Step("Enter employee ID: {empId}")
    public AddEmployeePage enterEmployeeId(String empId) {
        if (empId != null && !empId.isEmpty()) {
            elementActions.clearField(employeeIdInput);
            elementActions.type(employeeIdInput, empId);
        }
        return this;
    }

    @Step("Upload picture: {imagePath}")
    public AddEmployeePage uploadPicture(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {

            elementActions.uploadFileByButton(uploadPictureInput, imagePath);
        }
        return this;
    }

    @Step("Enable login details")
    public AddEmployeePage enableLoginDetails() {
        elementActions.click(createLoginToggle);
        return this;
    }

    @Step("Disable login details")
    public AddEmployeePage disableLoginDetails() {
        // Check if toggle is enabled and click to disable
        if (elementActions.isSelected(createLoginToggle)) {
            elementActions.click(createLoginToggle);
        }
        return this;
    }

    @Step("Enter username: {username}")
    public AddEmployeePage enterUsername(String username) {
        if (username != null && !username.isEmpty()) {
            elementActions.clearField(usernameInput);
            elementActions.type(usernameInput, username);
        }
        return this;
    }

    @Step("Enter password: {password}")
    public AddEmployeePage enterPassword(String password) {
        if (password != null && !password.isEmpty()) {
            elementActions.clearField(passwordInput);
            elementActions.type(passwordInput, password);
        }
        return this;
    }

    @Step("Enter confirm password: {password}")
    public AddEmployeePage enterConfirmPassword(String password) {
        if (password != null && !password.isEmpty()) {
            elementActions.clearField(confirmPasswordInput);
            elementActions.type(confirmPasswordInput, password);
        }
        return this;
    }

    @Step("Set status: {status}")
    public AddEmployeePage setStatus(Status status) {
        if (status == Status.ENABLED) {
            elementActions.click(enabledStatusOption);
        } else {
            elementActions.click(disabledStatusOption);
        }
        return this;
    }

    @Step("Click save button")
    public PersonalDetailsPage clickSave() {
        elementActions.click(saveButton);
        return new PersonalDetailsPage(driver);
    }

    @Step("Click save button and stay on current page")
    public AddEmployeePage clickSaveAndStay() {
        elementActions.click(saveButton);
        return this;
    }

    // Complete workflows
    @Step("Add employee with basic details: {firstName} {lastName}, employee ID: {empId}")
    public AddEmployeePage addEmployeeBasic(String firstName, String middleName, String lastName, String empId, String imagePath) {
        return enterFirstName(firstName)
                .enterMiddleName(middleName)
                .enterLastName(lastName)
                .enterEmployeeId(empId)
                .uploadPicture(imagePath);
    }

    @Step("Add employee with login details: {firstName} {lastName}, employee ID: {empId}, username: {username}")
    public AddEmployeePage addEmployeeWithLogin(String firstName, String middleName, String lastName, String empId, 
                                              String imagePath, String username, String password, Status status) {
        return enterFirstName(firstName)
                .enterMiddleName(middleName)
                .enterLastName(lastName)
                .enterEmployeeId(empId)
                .uploadPicture(imagePath)
                .enableLoginDetails()
                .enterUsername(username)
                .enterPassword(password)
                .enterConfirmPassword(password)
                .setStatus(status);
    }

    @Step("Wait for Personal Details page to load")
    public void waitForPersonalDetailsPage() {
        new Waits(driver.get()).waitForElementVisible(By.xpath("//h6[text()='Personal Details']"));
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public AddEmployeePage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public AddEmployeePage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }
}