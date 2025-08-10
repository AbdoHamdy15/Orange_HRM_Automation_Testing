package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class PIMPage {
    
    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;
    
    // Locators
    private final By employeeNameInput = By.xpath("//label[text()='Employee Name']/following::input[1]");
    private final By employeeIdInput = By.xpath("//label[text()='Employee Id']/following::input[1]");
    private final By jobTitleDropdown = By.xpath("//label[text()='Job Title']/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By employmentStatusDropdown = By.xpath("//label[text()='Employment Status']/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By includeDropdown = By.xpath("//label[text()='Include']/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By supervisorNameInput = By.xpath("//label[text()='Supervisor Name']/following::input[1]");
    private final By subUnitDropdown = By.xpath("//label[text()='Sub Unit']/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By resetButton = By.xpath("//button[normalize-space()='Reset']");
    private final By addEmployeeButton = By.xpath("//button[normalize-space()='Add']");
    private final By searchResultsTable = By.xpath("//div[@class='oxd-table orangehrm-employee-list']");
    private final By invalidMessage = By.xpath("//span[@class='oxd-text oxd-text--span oxd-input-field-error-message oxd-input-group__message' and text()='Invalid']");
    private final By pimHeader = By.xpath("//h6[text()='PIM']");

    // Constructor
    public PIMPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(pimHeader);
    }

    // Validations
    @Step("Assert PIM page is displayed")
    public PIMPage assertPIMPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(pimHeader), "PIM page should be displayed");
        return this;
    }

    // Actions
    @Step("Enter employee name: {nameInput} and select: {nameToSelect}")
    public PIMPage enterEmployeeName(String nameInput, String nameToSelect) {
        if (nameInput != null && !nameInput.isEmpty()) {
            elementActions.enterAutoSuggestField(employeeNameInput, nameInput, nameToSelect);
        }
        return this;
    }

    @Step("Enter supervisor name: {nameInput} and select: {nameToSelect}")
    public PIMPage enterSupervisorName(String nameInput, String nameToSelect) {
        if (nameInput != null && !nameInput.isEmpty()) {
            elementActions.enterAutoSuggestField(supervisorNameInput, nameInput, nameToSelect);
        }
        return this;
    }

    @Step("Enter employee ID: {id}")
    public PIMPage enterEmployeeId(String id) {
        if (id != null && !id.isEmpty()) {
            elementActions.clearField(employeeIdInput);
            elementActions.type(employeeIdInput, id);
        }
        return this;
    }

    @Step("Select job title: {title}")
    public PIMPage selectJobTitle(String title) {
        if (title != null && !title.isEmpty()) {
            elementActions.selectFromDropdown(jobTitleDropdown, title);
        }
        return this;
    }

    @Step("Select employment status: {status}")
    public PIMPage selectEmploymentStatus(String status) {
        if (status != null && !status.isEmpty()) {
            elementActions.selectFromDropdown(employmentStatusDropdown, status);
        }
        return this;
    }

    @Step("Select include: {includeText}")
    public PIMPage selectInclude(String includeText) {
        if (includeText != null && !includeText.isEmpty()) {
            elementActions.selectFromDropdown(includeDropdown, includeText);
        }
        return this;
    }

    @Step("Select sub unit: {subUnit}")
    public PIMPage selectSubUnit(String subUnit) {
        if (subUnit != null && !subUnit.isEmpty()) {
            elementActions.selectFromDropdown(subUnitDropdown, subUnit);
        }
        return this;
    }

    @Step("Click search button")
    public PIMPage clickSearch() {
        elementActions.click(searchButton);
        return this;
    }

    @Step("Click reset button")
    public PIMPage clickReset() {
        elementActions.click(resetButton);
        return this;
    }

    @Step("Click add employee button")
    public AddEmployeePage clickAddEmployee() {
        elementActions.click(addEmployeeButton);
        return new AddEmployeePage(driver);
    }

    // Complete workflows
    @Step("Perform PIM search with employee name: {nameInput}, employee ID: {id}, job title: {title}")
    public PIMPage searchEmployees(String nameInput, String nameToSelect, String id, String title, String status, String include, String subUnit, String supervisorInput, String supervisorToSelect) {
        return enterEmployeeName(nameInput, nameToSelect)
                .enterEmployeeId(id)
                .selectJobTitle(title)
                .selectEmploymentStatus(status)
                .selectInclude(include)
                .selectSubUnit(subUnit)
                .enterSupervisorName(supervisorInput, supervisorToSelect)
                .clickSearch();
    }

    // Validation methods
    @Step("Assert search results are displayed")
    public PIMPage assertSearchResultsDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(searchResultsTable), "Search results table should be displayed");
        return this;
    }

    @Step("Assert invalid message is displayed")
    public PIMPage assertInvalidMessageDisplayed() {
        validations.isErrorMessageDisplayed("Invalid");
        return this;
    }
}
