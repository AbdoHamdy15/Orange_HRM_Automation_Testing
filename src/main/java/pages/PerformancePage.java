package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class PerformancePage {
    
    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;
    
    // Locators
    private final By employeeNameInput = By.xpath("//label[text()='Employee Name']/following::input[1]");
    private final By jobTitleDropdown = By.xpath("(//div[contains(@class,'oxd-select-wrapper')])[1]");
    private final By subUnitDropdown = By.xpath("(//div[contains(@class,'oxd-select-wrapper')])[2]");
    private final By includeDropdown = By.xpath("(//div[contains(@class,'oxd-select-wrapper')])[3]");
    private final By reviewStatusDropdown = By.xpath("(//div[contains(@class,'oxd-select-wrapper')])[4]");
    private final By fromDateInput = By.xpath("//label[text()='From']/following::input[1]");
    private final By toDateInput = By.xpath("//label[text()='To']/following::input[1]");
    private final By fromCalendarIcon = By.xpath("//label[text()='From']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon'][1]");
    private final By toCalendarIcon = By.xpath("//label[text()='To']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon'][1]");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By resetButton = By.xpath("//button[normalize-space()='Reset']");
    private final By performanceHeader = By.xpath("//h6[text()='Performance']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");
    private final By employeeNameError = By.xpath("//label[text()='Employee Name']/following::span[contains(@class,'oxd-input-field-error-message')][1]");

    // Constructor
    public PerformancePage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(performanceHeader);
    }

    // Validations
    @Step("Assert performance page is displayed")
    public PerformancePage assertPerformancePageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(performanceHeader), "Performance page should be displayed");
        return this;
    }

    // Actions
    @Step("Enter employee name: {inputText} and select: {nameToSelect}")
    public PerformancePage enterEmployeeName(String inputText, String nameToSelect) {
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

    @Step("Select job title: {jobTitle}")
    public PerformancePage selectJobTitle(String jobTitle) {
        if (jobTitle != null && !jobTitle.isEmpty()) {
            elementActions.selectFromDropdown(jobTitleDropdown, jobTitle);
        }
        return this;
    }

    @Step("Select sub unit: {subUnit}")
    public PerformancePage selectSubUnit(String subUnit) {
        if (subUnit != null && !subUnit.isEmpty()) {
            elementActions.selectFromDropdown(subUnitDropdown, subUnit);
        }
        return this;
    }

    @Step("Select include: {includeOption}")
    public PerformancePage selectInclude(String includeOption) {
        if (includeOption != null && !includeOption.isEmpty()) {
            elementActions.selectFromDropdown(includeDropdown, includeOption);
        }
        return this;
    }

    @Step("Select review status: {status}")
    public PerformancePage selectReviewStatus(String status) {
        if (status != null && !status.isEmpty()) {
            elementActions.selectFromDropdown(reviewStatusDropdown, status);
        }
        return this;
    }

    @Step("Set from date: {day}/{month}/{year}")
    public PerformancePage setFromDate(String day, String month, String year) {
        if (day != null && month != null && year != null) {
            elementActions.click(fromCalendarIcon);
            // Calendar selection logic would go here
            // For now, using direct input
            elementActions.clearField(fromDateInput);
            elementActions.type(fromDateInput, day + "/" + month + "/" + year);
        }
        return this;
    }

    @Step("Set to date: {day}/{month}/{year}")
    public PerformancePage setToDate(String day, String month, String year) {
        if (day != null && month != null && year != null) {
            elementActions.click(toCalendarIcon);
            // Calendar selection logic would go here
            // For now, using direct input
            elementActions.clearField(toDateInput);
            elementActions.type(toDateInput, day + "/" + month + "/" + year);
        }
        return this;
    }

    @Step("Click search button")
    public PerformancePage clickSearch() {
        elementActions.click(searchButton);
        return this;
    }

    @Step("Click reset button")
    public PerformancePage clickReset() {
        elementActions.click(resetButton);
        return this;
    }

    // Complete workflows
    @Step("Search employee reviews with employee: {empInput}, job title: {jobTitle}, sub unit: {subUnit}")
    public PerformancePage searchEmployeeReviews(String empInput, String empName, String jobTitle,
                                                String subUnit, String includeOption, String status,
                                                String fromDay, String fromMonth, String fromYear,
                                                String toDay, String toMonth, String toYear) {
        return enterEmployeeName(empInput, empName)
                .selectJobTitle(jobTitle)
                .selectSubUnit(subUnit)
                .selectInclude(includeOption)
                .selectReviewStatus(status)
                .setFromDate(fromDay, fromMonth, fromYear)
                .setToDate(toDay, toMonth, toYear)
                .clickSearch();
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public PerformancePage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public PerformancePage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }

    @Step("Assert no records found message")
    public PerformancePage assertNoRecordsFound() {
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("No Records Found"),
            "No Records Found message should be displayed");
        return this;
    }

    @Step("Assert invalid error for employee name is displayed")
    public PerformancePage assertInvalidErrorForEmployeeName() {
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
}
