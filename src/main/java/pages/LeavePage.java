package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

import java.util.List;

public class LeavePage {
    
    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;
    
    // Locators
    private final By fromDateInput = By.xpath("//label[text()='From Date']/following::input[1]");
    private final By toDateInput = By.xpath("//label[text()='To Date']/following::input[1]");
    private final By employeeNameInput = By.xpath("//label[text()='Employee Name']/following::input[1]");
    private final By dropdownList = By.cssSelector(".oxd-input-group");
    private final By includePastEmployeesToggle = By.cssSelector("span.oxd-switch-input");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By resetButton = By.xpath("//button[normalize-space()='Reset']");
    private final By leaveHeader = By.xpath("//h6[text()='Leave']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");
    private final By employeeNameError = By.xpath("//label[text()='Employee Name']/following::span[contains(@class,'oxd-input-field-error-message')][1]");
    private final By statusError = By.xpath("//label[contains(text(),'Show Leave with Status')]/following::span[contains(@class,'oxd-input-field-error-message')][1]");
    private final By clearButton = By.xpath("//i[contains(@class,'--clear')]");

    // Constructor
    public LeavePage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(leaveHeader);
    }

    // Validations
    @Step("Assert leave page is displayed")
    public LeavePage assertLeavePageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(leaveHeader), "Leave page should be displayed");
        return this;
    }

    // Actions
    @Step("Enter from date: {formattedDate}")
    public LeavePage enterFromDate(String formattedDate) {
        if (formattedDate != null && !formattedDate.isEmpty()) {
            elementActions.clearField(fromDateInput);
            elementActions.type(fromDateInput, formattedDate);
        }
        return this;
    }

    @Step("Enter to date: {formattedDate}")
    public LeavePage enterToDate(String formattedDate) {
        if (formattedDate != null && !formattedDate.isEmpty()) {
            elementActions.clearField(toDateInput);
            elementActions.type(toDateInput, formattedDate);
        }
        return this;
    }

    @Step("Enter employee name: {inputText} and select: {nameToSelect}")
    public LeavePage enterEmployeeName(String inputText, String nameToSelect) {
        if (inputText != null && !inputText.isEmpty()) {
            elementActions.clearField(employeeNameInput);
            elementActions.type(employeeNameInput, inputText);
            if (nameToSelect != null && !nameToSelect.isEmpty()) {
                waits.waitForElementVisible(By.xpath("//div[@role='option']/span[text()='" + nameToSelect + "']"));
                elementActions.click(By.xpath("//div[@role='option']/span[text()='" + nameToSelect + "']"));
            }
        }
        return this;
    }

    @Step("Set include past employees: {include}")
    public LeavePage setIncludePastEmployees(boolean include) {
        elementActions.click(includePastEmployeesToggle);
        return this;
    }

    @Step("Click search button")
    public LeavePage clickSearch() {
        elementActions.click(searchButton);
        return this;
    }

    @Step("Click reset button")
    public LeavePage clickReset() {
        elementActions.click(resetButton);
        return this;
    }

    @Step("Select status dropdown: {typeText}")
    public LeavePage selectStatusDropdown(String typeText) {
        elementActions.selectFromDropdownByLabel(dropdownList, "Status", typeText);
        return this;
    }

    @Step("Select leave type dropdown: {typeText}")
    public LeavePage selectLeaveTypeDropdown(String typeText) {
        elementActions.selectFromDropdownByLabel(dropdownList, "Leave Type", typeText);
        return this;
    }

    @Step("Select sub unit dropdown: {typeText}")
    public LeavePage selectSubUnitDropdown(String typeText) {
        elementActions.selectFromDropdownByLabel(dropdownList, "Sub Unit", typeText);
        return this;
    }

    @Step("Clear status selection")
    public LeavePage clearStatusSelection() {
        try {
            elementActions.click(clearButton);
        } catch (Exception e) {
            // If clear button is not present, do nothing
        }
        return this;
    }

    // Complete workflows
    @Step("Perform leave search with from date: {fromDate}, to date: {toDate}, status: {status}")
    public LeavePage searchLeave(String fromDate, String toDate, String status, String leaveType, String nameToType,
                                String nameToSelect, String subUnit, boolean includePastEmployees) {
        return enterFromDate(fromDate)
                .enterToDate(toDate)
                .selectStatusDropdown(status)
                .selectLeaveTypeDropdown(leaveType)
                .enterEmployeeName(nameToType, nameToSelect)
                .selectSubUnitDropdown(subUnit)
                .setIncludePastEmployees(includePastEmployees)
                .clickSearch();
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public LeavePage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public LeavePage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }

    @Step("Assert no records found message")
    public LeavePage assertNoRecordsFound() {
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("No Records Found"),
            "No Records Found message should be displayed");
        return this;
    }

    @Step("Assert invalid error for employee name is displayed")
    public LeavePage assertInvalidErrorForEmployeeName() {
        validations.validateTrue(isInvalidErrorDisplayedForEmployeeName(), "Invalid error for employee name should be displayed");
        return this;
    }

    @Step("Assert required error for status is displayed")
    public LeavePage assertRequiredErrorForStatus() {
        validations.validateTrue(isRequiredErrorDisplayedForStatus(), "Required error for status should be displayed");
        return this;
    }

    @Step("Check if invalid error is displayed for employee name")
    public boolean isInvalidErrorDisplayedForEmployeeName() {
        try {
            return elementActions.isDisplayed(employeeNameError) && 
                   elementActions.getText(employeeNameError).trim().equalsIgnoreCase("Invalid");
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Check if required error is displayed for status")
    public boolean isRequiredErrorDisplayedForStatus() {
        try {
            return elementActions.isDisplayed(statusError) && 
                   elementActions.getText(statusError).trim().equalsIgnoreCase("Required");
        } catch (Exception e) {
            return false;
        }
    }
}
