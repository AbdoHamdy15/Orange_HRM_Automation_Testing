package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class AdminPage {
    
    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;
    
    // Locators
    private final By usernameInput = By.xpath("//label[text()='Username']/following::input[1]");
    private final By userRoleDropdown = By.xpath("//label[text()='User Role']/following::div[@class='oxd-select-wrapper'][1]");
    private final By employeeNameInput = By.xpath("//label[text()='Employee Name']/following::input[1]");
    private final By statusDropdown = By.xpath("//label[text()='Status']/following::div[@class='oxd-select-wrapper'][1]");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By resetButton = By.xpath("//button[normalize-space()='Reset']");
    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    private final By searchResultsTable = By.xpath("//div[@class='oxd-table']");
    private final By noRecordsMessage = By.xpath("//span[text()='No Records Found']");
    private final By adminHeader = By.xpath("//h6[contains(@class,'oxd-text--h6') and text()='Admin']");

    // Constructor
    public AdminPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(adminHeader);
    }

    // Validations
    @Step("Assert admin page is displayed")
    public AdminPage assertAdminPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(adminHeader), "Admin page should be displayed");
        return this;
    }

    // Actions
    @Step("Enter username: {username}")
    public AdminPage enterUsername(String username) {
        if (username != null && !username.isEmpty()) {
            elementActions.clearField(usernameInput);
            elementActions.type(usernameInput, username);
        }
        return this;
    }

    @Step("Select user role: {role}")
    public AdminPage selectUserRole(String role) {
        if (role != null && !role.isEmpty()) {
            elementActions.selectFromDropdown(userRoleDropdown, role);
        }
        return this;
    }

    @Step("Enter employee name: {inputText} and select: {nameToSelect}")
    public AdminPage enterEmployeeName(String inputText, String nameToSelect) {
        if (inputText != null && !inputText.isEmpty()) {
            elementActions.enterAutoSuggestField(employeeNameInput, inputText, nameToSelect);
        }
        return this;
    }

    @Step("Select status: {status}")
    public AdminPage selectStatus(String status) {
        if (status != null && !status.isEmpty()) {
            elementActions.selectFromDropdown(statusDropdown, status);
        }
        return this;
    }

    @Step("Click search button")
    public AdminPage clickSearch() {
        elementActions.click(searchButton);
        return this;
    }

    @Step("Click reset button")
    public AdminPage clickReset() {
        elementActions.click(resetButton);
        return this;
    }

    @Step("Click add button")
    public AddUserPage clickAdd() {
        elementActions.click(addButton);
        return new AddUserPage(driver);
    }

    // Complete workflows
    @Step("Perform search with username: {username}, role: {role}, employee: {employeeInputText}, status: {status}")
    public AdminPage searchUsers(String username, String role, String employeeInputText, String employeeNameToSelect, String status) {
        return enterUsername(username)
                .selectUserRole(role)
                .enterEmployeeName(employeeInputText, employeeNameToSelect)
                .selectStatus(status)
                .clickSearch();
    }

    // Validation methods
    @Step("Check if search results table is displayed")
    public boolean isSearchResultsTableDisplayed() {
        try {
            return elementActions.isDisplayed(searchResultsTable);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Assert search results are displayed")
    public AdminPage assertSearchResultsDisplayed() {
        validations.validateTrue(isSearchResultsTableDisplayed(), "Search results table should be displayed");
        return this;
    }

    @Step("Assert no records found message is displayed")
    public AdminPage assertNoRecordsFoundMessageDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("No Records Found"), 
            "No records found toast message should be displayed");
        return this;
    }

    @Step("Assert invalid error message is displayed")
    public AdminPage assertInvalidErrorMessageDisplayed() {
        validations.isErrorMessageDisplayed("Invalid");
        return this;
    }
}