package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class TimePage {
    
    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;
    
    // Locators
    private final By employeeNameInput = By.xpath("//label[text()='Employee Name']/following::input[1]");
    private final By viewButton = By.xpath("//button[normalize-space()='View']");
    private final By timeHeader = By.xpath("//h6[text()='Time']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");
    private final By employeeNameError = By.xpath("//label[text()='Employee Name']/following::span[contains(@class,'oxd-input-field-error-message')][1]");
    private final By timesheetPageMessage = By.xpath("//p[contains(@class,'oxd-text') and contains(@class,'oxd-alert-content-text')]");

    // Constructor
    public TimePage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(timeHeader);
    }

    // Validations
    @Step("Assert time page is displayed")
    public TimePage assertTimePageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(timeHeader), "Time page should be displayed");
        return this;
    }

    // Actions
    @Step("Enter employee name: {inputText} and select: {nameToSelect}")
    public TimePage enterEmployeeName(String inputText, String nameToSelect) {
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

    @Step("Click view button")
    public TimePage clickView() {
        elementActions.click(viewButton);
        return this;
    }

    // Complete workflows
    @Step("View employee timesheet: {inputText}, {nameToSelect}")
    public TimePage viewEmployeeTimesheet(String inputText, String nameToSelect) {
        return enterEmployeeName(inputText, nameToSelect)
                .clickView();
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public TimePage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public TimePage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }

    @Step("Assert no records found message")
    public TimePage assertNoRecordsFound() {
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("No Records Found"),
            "No Records Found message should be displayed");
        return this;
    }

    @Step("Assert no timesheets found message")
    public TimePage assertNoTimesheetsFound() {
        String pageMessage = getTimesheetPageMessage();
        validations.validateTrue(pageMessage != null && pageMessage.contains("No Timesheets Found"),
            "No Timesheets Found message should be displayed");
        return this;
    }

    @Step("Assert invalid error for employee name is displayed")
    public TimePage assertInvalidErrorForEmployeeName() {
        validations.validateTrue(isInvalidErrorDisplayedForEmployeeName(), "Invalid error for employee name should be displayed");
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

    @Step("Get timesheet page message")
    public String getTimesheetPageMessage() {
        try {
            return elementActions.getText(timesheetPageMessage);
        } catch (Exception e) {
            return null;
        }
    }
}
