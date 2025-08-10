package pages.myinfo;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class DependentsPage {

    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // Locators
    private final By addButton = By.xpath("//h6[text()='Assigned Dependents']/following::button[normalize-space()='Add'][1]");
    private final By nameInput = By.xpath("//label[contains(text(),'Name')]/following::input[1]");
    private final By relationshipDropdown = By.xpath("//label[contains(text(),'Relationship')]/following::div[contains(@class,'oxd-select-wrapper')]");
    private final By dateOfBirthInput = By.xpath("//label[contains(text(),'Date of Birth')]/following::input[1]");
    private final By saveButton = By.xpath("//button[normalize-space()='Save']");
    private final By dependentsHeader = By.xpath("//h6[text()='Assigned Dependents']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    public DependentsPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
    }

    // Navigation methods
    @Step("Assert dependents page is displayed")
    public DependentsPage assertDependentsPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(dependentsHeader), "Dependents page should be displayed");
        return this;
    }

    // Action methods
    @Step("Click add button")
    public DependentsPage clickAdd() {
        waits.waitForElementClickable(addButton);
        elementActions.click(addButton);
        return this;
    }

    @Step("Enter dependent name: {name}")
    public DependentsPage enterName(String name) {
        if (name != null && !name.isEmpty()) {
            elementActions.clearField(nameInput);
            elementActions.type(nameInput, name);
        }
        return this;
    }

    @Step("Select relationship: {relationship}")
    public DependentsPage selectRelationship(String relationship) {
        if (relationship != null && !relationship.isEmpty()) {
            elementActions.selectFromDropdown(relationshipDropdown, relationship);
        }
        return this;
    }

    @Step("Set date of birth: {dateOfBirth}")
    public DependentsPage setDateOfBirth(String dateOfBirth) {
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            elementActions.clearField(dateOfBirthInput);
            elementActions.type(dateOfBirthInput, dateOfBirth);
        }
        return this;
    }

    @Step("Click save button")
    public DependentsPage clickSave() {
        elementActions.click(saveButton);
        return this;
    }

    // Complete workflow
    @Step("Add dependent: {name}, {relationship}, {dateOfBirth}")
    public DependentsPage addDependent(String name, String relationship, String dateOfBirth) {
        return clickAdd()
                .enterName(name)
                .selectRelationship(relationship)
                .setDateOfBirth(dateOfBirth)
                .clickSave();
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public DependentsPage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public DependentsPage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }
}
