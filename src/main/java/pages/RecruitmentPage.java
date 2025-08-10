package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class RecruitmentPage {
    
    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;
    
    // Locators
    private final By jobTitleDropdown = By.xpath("//label[text()='Job Title']/following::div[@class='oxd-select-wrapper'][1]");
    private final By vacancyDropdown = By.xpath("//label[text()='Vacancy']/following::div[@class='oxd-select-wrapper'][1]");
    private final By hiringManagerDropdown = By.xpath("//label[text()='Hiring Manager']/following::div[@class='oxd-select-wrapper'][1]");
    private final By statusDropdown = By.xpath("//label[text()='Status']/following::div[@class='oxd-select-wrapper'][1]");
    private final By candidateNameInput = By.xpath("//label[text()='Candidate Name']/following::input[1]");
    private final By fromDateInput = By.xpath("//input[@placeholder='From']");
    private final By toDateInput = By.xpath("//input[@placeholder='To']");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By resetButton = By.xpath("//button[normalize-space()='Reset']");
    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    private final By fromCalendarIcon = By.xpath("//input[@placeholder='From']/following::i[contains(@class, 'oxd-date-input-icon')][1]");
    private final By toCalendarIcon = By.xpath("//input[@placeholder='To']/following::i[contains(@class, 'oxd-date-input-icon')][1]");
    private final By searchResultsTable = By.cssSelector(".oxd-table");
    private final By candidateNameError = By.xpath("//label[text()='Candidate Name']/following::span[contains(@class,'oxd-input-field-error-message')][1]");
    private final By recruitmentHeader = By.xpath("//h6[text()='Recruitment']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");
    
    // Vacancies tab locators
    private final By vacanciesTab = By.xpath("//a[text()='Vacancies']");
    private final By candidatesTab = By.xpath("//a[text()='Candidates']");

    // Constructor
    public RecruitmentPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(recruitmentHeader);
    }

    // Validations
    @Step("Assert recruitment page is displayed")
    public RecruitmentPage assertRecruitmentPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(recruitmentHeader), "Recruitment page should be displayed");
        return this;
    }

    // Actions
    @Step("Select job title: {title}")
    public RecruitmentPage selectJobTitle(String title) {
        if (title != null && !title.isEmpty()) {
            elementActions.selectFromDropdown(jobTitleDropdown, title);
        }
        return this;
    }

    @Step("Select vacancy: {vacancy}")
    public RecruitmentPage selectVacancy(String vacancy) {
        if (vacancy != null && !vacancy.isEmpty()) {
            elementActions.selectFromDropdown(vacancyDropdown, vacancy);
        }
        return this;
    }

    @Step("Select hiring manager: {manager}")
    public RecruitmentPage selectHiringManager(String manager) {
        if (manager != null && !manager.isEmpty()) {
            elementActions.selectFromDropdown(hiringManagerDropdown, manager);
        }
        return this;
    }

    @Step("Select status: {status}")
    public RecruitmentPage selectStatus(String status) {
        if (status != null && !status.isEmpty()) {
            elementActions.selectFromDropdown(statusDropdown, status);
        }
        return this;
    }

    @Step("Enter candidate name: {inputText} and select: {nameToSelect}")
    public RecruitmentPage enterCandidateName(String inputText, String nameToSelect) {
        if (inputText != null && !inputText.isEmpty()) {
            elementActions.clearField(candidateNameInput);
            elementActions.type(candidateNameInput, inputText);
            if (nameToSelect != null && !nameToSelect.isEmpty()) {
                waits.waitForElementVisible(By.xpath("//div[@role='option']/span[text()='" + nameToSelect + "']"));
                elementActions.click(By.xpath("//div[@role='option']/span[text()='" + nameToSelect + "']"));
            }
        }
        return this;
    }

    @Step("Set from date: {date}")
    public RecruitmentPage setFromDate(String date) {
        if (date != null && !date.isEmpty()) {
            elementActions.clearField(fromDateInput);
            elementActions.type(fromDateInput, date);
        }
        return this;
    }

    @Step("Set to date: {date}")
    public RecruitmentPage setToDate(String date) {
        if (date != null && !date.isEmpty()) {
            elementActions.clearField(toDateInput);
            elementActions.type(toDateInput, date);
        }
        return this;
    }

    @Step("Click search button")
    public RecruitmentPage clickSearch() {
        elementActions.click(searchButton);
        return this;
    }

    @Step("Click reset button")
    public RecruitmentPage clickReset() {
        elementActions.click(resetButton);
        return this;
    }

    @Step("Click add button")
    public AddCandidatePage clickAdd() {
        elementActions.click(addButton);
        return new AddCandidatePage(driver);
    }

    @Step("Click Vacancies tab")
    public RecruitmentPage clickVacanciesTab() {
        elementActions.click(vacanciesTab);
        return this;
    }

    @Step("Click Candidates tab")
    public RecruitmentPage clickCandidatesTab() {
        elementActions.click(candidatesTab);
        return this;
    }

    @Step("Click add vacancy button")
    public AddVacancyPage clickAddVacancy() {
        waits.waitForElementVisible(addButton);
        elementActions.click(addButton);
        return new AddVacancyPage(driver);
    }

    // Complete workflow
    @Step("Search candidates with job title: {jobTitle}, vacancy: {vacancy}, manager: {manager}")
    public RecruitmentPage searchCandidates(String jobTitle, String vacancy, String manager, String status,
                                           String candidateInput, String candidateName,
                                           String fromDay, String fromMonth, String fromYear,
                                           String toDay, String toMonth, String toYear) {
        return selectJobTitle(jobTitle)
                .selectVacancy(vacancy)
                .selectHiringManager(manager)
                .selectStatus(status)
                .enterCandidateName(candidateInput, candidateName)
                .setFromDate(fromYear + "-" + fromMonth + "-" + fromDay)
                .setToDate(toYear + "-" + toMonth + "-" + toDay)
                .clickSearch();
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public RecruitmentPage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public RecruitmentPage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }

    @Step("Assert no records found message")
    public RecruitmentPage assertNoRecordsFound() {
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("No Records Found"),
            "No Records Found message should be displayed");
        return this;
    }

    @Step("Assert invalid error for candidate name is displayed")
    public RecruitmentPage assertInvalidErrorForCandidateName() {
        validations.validateTrue(isInvalidErrorDisplayedForCandidateName(), "Invalid error for candidate name should be displayed");
        return this;
    }

    @Step("Assert search results are displayed")
    public RecruitmentPage assertSearchResultsDisplayed() {
        validations.validateTrue(isSearchResultsTableDisplayed(), "Search results table should be displayed");
        return this;
    }

    @Step("Check if invalid error is displayed for candidate name")
    public boolean isInvalidErrorDisplayedForCandidateName() {
        try {
            return elementActions.isDisplayed(candidateNameError) && 
                   elementActions.getText(candidateNameError).trim().equalsIgnoreCase("Invalid");
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Check if search results table is displayed")
    public boolean isSearchResultsTableDisplayed() {
        try {
            return elementActions.isDisplayed(searchResultsTable);
        } catch (Exception e) {
            return false;
        }
    }
}
