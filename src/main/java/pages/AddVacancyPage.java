package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class AddVacancyPage {

    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // Locators
    private final By vacancyNameInput = By.xpath("//label[text()='Vacancy Name']/following::input[1]");
    private final By jobTitleDropdown = By.xpath("//label[text()='Job Title']/following::div[contains(@class,'oxd-select-wrapper')]");
    private final By descriptionTextarea = By.xpath("//label[text()='Description']/following::textarea[1]");
    private final By hiringManagerDropdown = By.xpath("//input[@placeholder='Type for hints...']");
    private final By numberOfPositionsInput = By.xpath("//label[text()='Number of Positions']/following::input[1]");
    private final By activeToggle = By.xpath("//label[text()='Active']/following::span[contains(@class,'oxd-switch-input')]");
    private final By publishToggle = By.xpath("//label[text()='Publish in RSS Feed and Web Page']/following::span[contains(@class,'oxd-switch-input')]");
    private final By saveButton = By.xpath("//button[normalize-space()='Save']");
    private final By cancelButton = By.xpath("//button[normalize-space()='Cancel']");
    private final By candidatesButton = By.xpath("//a[text()='Candidates']");
    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    private final By addVacancyHeader = By.xpath("//h6[text()='Add Vacancy']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    public AddVacancyPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
        waits.waitForElementVisible(addVacancyHeader);
    }

    // Navigation methods
    @Step("Assert add vacancy page is displayed")
    public AddVacancyPage assertAddVacancyPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(addVacancyHeader), "Add Vacancy page should be displayed");
        return this;
    }

    // Action methods
    @Step("Enter vacancy name: {vacancyName}")
    public AddVacancyPage enterVacancyName(String vacancyName) {
        if (vacancyName != null && !vacancyName.isEmpty()) {
            elementActions.clearField(vacancyNameInput);
            elementActions.type(vacancyNameInput, vacancyName);
        }
        return this;
    }

    @Step("Select job title: {jobTitle}")
    public AddVacancyPage selectJobTitle(String jobTitle) {
        if (jobTitle != null && !jobTitle.isEmpty()) {
            elementActions.selectFromDropdown(jobTitleDropdown, jobTitle);
        }
        return this;
    }

    @Step("Enter description: {description}")
    public AddVacancyPage enterDescription(String description) {
        if (description != null && !description.isEmpty()) {
            elementActions.clearField(descriptionTextarea);
            elementActions.type(descriptionTextarea, description);
        }
        return this;
    }

    @Step("Select hiring manager with input: {inputText} and select: {nameToSelect}")
    public AddVacancyPage selectHiringManager(String inputText, String nameToSelect) {
        if (inputText != null && !inputText.isEmpty()) {
            elementActions.enterAutoSuggestField(hiringManagerDropdown, inputText, nameToSelect);
        }
        return this;
    }

    @Step("Enter number of positions: {number}")
    public AddVacancyPage enterNumberOfPositions(String number) {
        if (number != null && !number.isEmpty()) {
            elementActions.clearField(numberOfPositionsInput);
            elementActions.type(numberOfPositionsInput, number);
        }
        return this;
    }

    @Step("Set active status: {isActive}")
    public AddVacancyPage setActiveStatus(boolean isActive) {
        if (isActive) {
            elementActions.toggle(activeToggle);
        }
        return this;
    }

    @Step("Set publish status: {isPublished}")
    public AddVacancyPage setPublishStatus(boolean isPublished) {
        if (isPublished) {
            elementActions.toggle(publishToggle);
        }
        return this;
    }

    @Step("Click save button")
    public RecruitmentPage clickSave() {
        elementActions.click(saveButton);
        return new RecruitmentPage(driver);
    }

    @Step("Click cancel button")
    public AddVacancyPage clickCancel() {
        elementActions.click(cancelButton);
        return this;
    }

    @Step("Click candidates button")
    public RecruitmentPage clickCandidates() {
        elementActions.click(candidatesButton);
        return new RecruitmentPage(driver);
    }

    @Step("Click add button")
    public AddVacancyPage clickAdd() {
        elementActions.click(addButton);
        return this;
    }

    // Complete workflow methods
    @Step("Add vacancy with details: {vacancyName}, job title: {jobTitle}")
    public AddVacancyPage addVacancy(String vacancyName, String jobTitle, String description, String hiringManagerInput, String hiringManagerSelect, String numberOfPositions) {
        return enterVacancyName(vacancyName)
                .selectJobTitle(jobTitle)
                .enterDescription(description)
                .selectHiringManager(hiringManagerInput, hiringManagerSelect)
                .enterNumberOfPositions(numberOfPositions);
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public AddVacancyPage assertSpecificErrorDisplayed(String expectedError) {
        waits.waitForElementVisible(errorMessages);
        String actualError = elementActions.getText(errorMessages);
        validations.validateTrue(actualError.contains(expectedError), 
            "Error message should contain '" + expectedError + "' but was '" + actualError + "'");
        return this;
    }

    @Step("Assert success toast is displayed")
    public AddVacancyPage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }


}
