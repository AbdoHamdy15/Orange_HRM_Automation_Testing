package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

import java.util.List;

public class AddUserPage {
    
    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;
    
    // Locators
    private final By userRoleDropdown = By.xpath("//label[text()='User Role']/following::div[@class='oxd-select-wrapper'][1]");
    private final By employeeNameInput = By.xpath("//label[text()='Employee Name']/following::input[1]");
    private final By statusDropdown = By.xpath("//label[text()='Status']/following::div[@class='oxd-select-wrapper'][1]");
    private final By usernameInput = By.xpath("//label[text()='Username']/following::input[1]");
    private final By passwordInput = By.xpath("//label[text()='Password']/following::input[1]");
    private final By confirmPasswordInput = By.xpath("//label[text()='Confirm Password']/following::input[1]");
    private final By saveButton = By.xpath("//button[contains(@class, 'oxd-button') and text()=' Save ']");
    private final By addUserHeader = By.xpath("//h6[text()='Add User']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    // Constructor
    public AddUserPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(addUserHeader);
    }

    // Validations
    @Step("Assert add user page is displayed")
    public AddUserPage assertAddUserPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(addUserHeader), "Add User page should be displayed");
        return this;
    }

    // Actions
    @Step("Select user role: {roleText}")
    public AddUserPage selectUserRole(String roleText) {
        if (roleText != null && !roleText.isEmpty()) {
            elementActions.selectFromDropdown(userRoleDropdown, roleText);
        }
        return this;
    }

    @Step("Enter employee name: {inputText} and select: {nameToSelect}")
    public AddUserPage enterEmployeeName(String inputText, String nameToSelect) {
        if (inputText != null && !inputText.isEmpty()) {
            elementActions.enterAutoSuggestField(employeeNameInput, inputText, nameToSelect);
        }
        return this;
    }

    @Step("Select status: {statusText}")
    public AddUserPage selectStatus(String statusText) {
        if (statusText != null && !statusText.isEmpty()) {
            elementActions.selectFromDropdown(statusDropdown, statusText);
        }
        return this;
    }

    @Step("Enter username: {username}")
    public AddUserPage enterUsername(String username) {
        if (username != null && !username.isEmpty()) {
            elementActions.clearField(usernameInput);
            elementActions.type(usernameInput, username);
        }
        return this;
    }

    @Step("Enter password: {password}")
    public AddUserPage enterPassword(String password) {
        if (password != null && !password.isEmpty()) {
            elementActions.clearField(passwordInput);
            elementActions.type(passwordInput, password);
        }
        return this;
    }

    @Step("Enter confirm password: {password}")
    public AddUserPage enterConfirmPassword(String password) {
        if (password != null && !password.isEmpty()) {
            elementActions.clearField(confirmPasswordInput);
            elementActions.type(confirmPasswordInput, password);
        }
        return this;
    }

    @Step("Click save button")
    public AddUserPage clickSave() {
        elementActions.click(saveButton);
        return this;
    }

    // Complete workflows
    @Step("Add new user with role: {role}, employee: {employeeInputText}, status: {status}")
    public AddUserPage addNewUser(String role, String employeeInputText, String employeeNameToSelect, String status, String username, String password) {
        return selectUserRole(role)
                .enterEmployeeName(employeeInputText, employeeNameToSelect)
                .selectStatus(status)
                .enterUsername(username)
                .enterPassword(password)
                .enterConfirmPassword(password)
                .clickSave();
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public AddUserPage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public AddUserPage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }
}
