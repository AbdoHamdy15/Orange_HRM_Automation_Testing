package pages.myinfo;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class EmergencyContactsPage {

    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // Locators
    private final By addButton = By.xpath("//h6[text()='Assigned Emergency Contacts']/following::button[normalize-space()='Add'][1]");
    private final By nameInput = By.xpath("//label[contains(text(),'Name')]/following::input[1]");
    private final By relationshipInput = By.xpath("//label[contains(text(),'Relationship')]/following::input[1]");
    private final By homePhoneInput = By.xpath("//label[contains(text(),'Home')]/following::input[1]");
    private final By mobileInput = By.xpath("//label[contains(text(),'Mobile')]/following::input[1]");
    private final By workPhoneInput = By.xpath("//label[contains(text(),'Work')]/following::input[1]");
    private final By saveButton = By.xpath("//button[normalize-space()='Save']");
    private final By emergencyContactsHeader = By.xpath("//h6[text()='Assigned Emergency Contacts']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    public EmergencyContactsPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
    }

    // Navigation methods
    @Step("Assert emergency contacts page is displayed")
    public EmergencyContactsPage assertEmergencyContactsPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(emergencyContactsHeader), "Emergency Contacts page should be displayed");
        return this;
    }

    // Action methods
    @Step("Click add button")
    public EmergencyContactsPage clickAdd() {
        waits.waitForElementClickable(addButton);
        elementActions.click(addButton);
        return this;
    }

    @Step("Enter contact name: {name}")
    public EmergencyContactsPage enterName(String name) {
        if (name != null && !name.isEmpty()) {
            elementActions.clearField(nameInput);
            elementActions.type(nameInput, name);
        }
        return this;
    }

    @Step("Enter relationship: {relationship}")
    public EmergencyContactsPage enterRelationship(String relationship) {
        if (relationship != null && !relationship.isEmpty()) {
            elementActions.clearField(relationshipInput);
            elementActions.type(relationshipInput, relationship);
        }
        return this;
    }

    @Step("Enter home phone: {homePhone}")
    public EmergencyContactsPage enterHomePhone(String homePhone) {
        if (homePhone != null && !homePhone.isEmpty()) {
            elementActions.clearField(homePhoneInput);
            elementActions.type(homePhoneInput, homePhone);
        }
        return this;
    }

    @Step("Enter mobile: {mobile}")
    public EmergencyContactsPage enterMobile(String mobile) {
        if (mobile != null && !mobile.isEmpty()) {
            elementActions.clearField(mobileInput);
            elementActions.type(mobileInput, mobile);
        }
        return this;
    }

    @Step("Enter work phone: {workPhone}")
    public EmergencyContactsPage enterWorkPhone(String workPhone) {
        if (workPhone != null && !workPhone.isEmpty()) {
            elementActions.clearField(workPhoneInput);
            elementActions.type(workPhoneInput, workPhone);
        }
        return this;
    }

    @Step("Click save button")
    public EmergencyContactsPage clickSave() {
        elementActions.click(saveButton);
        return this;
    }

    // Complete workflow
    @Step("Add emergency contact: {name}, {relationship}, {homePhone}, {mobile}, {workPhone}")
    public EmergencyContactsPage addEmergencyContact(String name, String relationship, String homePhone, String mobile, String workPhone) {
        return clickAdd()
                .enterName(name)
                .enterRelationship(relationship)
                .enterHomePhone(homePhone)
                .enterMobile(mobile)
                .enterWorkPhone(workPhone)
                .clickSave();
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public EmergencyContactsPage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public EmergencyContactsPage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }
}
