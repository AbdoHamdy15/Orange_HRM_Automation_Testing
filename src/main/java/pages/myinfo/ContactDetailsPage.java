package pages.myinfo;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class ContactDetailsPage {

    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // Address Fields
    private final By street1Input = By.xpath("//label[normalize-space()='Street 1']/ancestor::div[contains(@class,'oxd-input-group')]//input\n");
    private final By street2Input = By.xpath("//label[contains(text(),'Street 2')]/following::input[1]");
    private final By cityInput = By.xpath("//label[contains(text(),'City')]/following::input[1]");
    private final By stateInput = By.xpath("//label[contains(text(),'State')]/following::input[1]");
    private final By zipInput = By.xpath("//label[contains(text(),'Zip')]/following::input[1]");
    private final By countryDropdown = By.xpath("//label[contains(text(),'Country')]/following::div[contains(@class,'oxd-select-wrapper')][1]");

    // Telephone Fields
    private final By homePhoneInput = By.xpath("//label[contains(text(),'Home')]/following::input[1] | //input[@name='homeTelephone']");
    private final By mobileInput = By.xpath("//label[contains(text(),'Mobile')]/following::input[1] | //input[@name='mobile']");
    private final By workPhoneInput = By.xpath("//label[contains(text(),'Work')]/following::input[1] | //input[@name='workTelephone']");

    // Email Fields
    private final By workEmailInput = By.xpath("//label[contains(text(),'Work Email')]/following::input[1] | //input[@name='workEmail']");
    private final By otherEmailInput = By.xpath("//label[contains(text(),'Other Email')]/following::input[1] | //input[@name='otherEmail']");

    // Save Button
    private final By saveButton = By.xpath("//button[@type='submit']");

    // Attachment Elements
    private final By addAttachmentButton = By.xpath("//h6[text()='Attachments']/following::button[normalize-space()='Add'][1]");
    private final By fileInput = By.cssSelector("input[type='file']");
    private final By commentInput = By.xpath("//label[text()='Comment']/following::textarea[1]");
    private final By attachmentSaveButton = By.xpath("//button[normalize-space()='Save']");

    // Header
    private final By contactDetailsHeader = By.xpath("//h6[contains(text(),'Contact') or contains(text(),'Details')]");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    public ContactDetailsPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
    }

    // Navigation methods
    @Step("Assert contact details page is displayed")
    public ContactDetailsPage assertContactDetailsPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(contactDetailsHeader), "Contact Details page should be displayed");
        return this;
    }

    // Action methods
    @Step("Enter street 1: {street1}")
    public ContactDetailsPage enterStreet1(String street1) {
        if (street1 != null && !street1.isEmpty()) {
            waits.waitForElementClickable(street1Input);
            elementActions.clearField(street1Input);
            elementActions.type(street1Input, street1);
        }
        return this;
    }

    @Step("Enter street 2: {street2}")
    public ContactDetailsPage enterStreet2(String street2) {
        if (street2 != null && !street2.isEmpty()) {
            elementActions.clearField(street2Input);
            elementActions.type(street2Input, street2);
        }
        return this;
    }

    @Step("Enter city: {city}")
    public ContactDetailsPage enterCity(String city) {
        if (city != null && !city.isEmpty()) {
            elementActions.clearField(cityInput);
            elementActions.type(cityInput, city);
        }
        return this;
    }

    @Step("Enter state: {state}")
    public ContactDetailsPage enterState(String state) {
        if (state != null && !state.isEmpty()) {
            elementActions.clearField(stateInput);
            elementActions.type(stateInput, state);
        }
        return this;
    }

    @Step("Enter zip code: {zip}")
    public ContactDetailsPage enterZipCode(String zip) {
        if (zip != null && !zip.isEmpty()) {
            elementActions.clearField(zipInput);
            elementActions.type(zipInput, zip);
        }
        return this;
    }

    @Step("Select country: {country}")
    public ContactDetailsPage selectCountry(String country) {
        if (country != null && !country.isEmpty()) {
            elementActions.selectFromDropdown(countryDropdown, country);
        }
        return this;
    }

    @Step("Enter home phone: {homePhone}")
    public ContactDetailsPage enterHomePhone(String homePhone) {
        if (homePhone != null && !homePhone.isEmpty()) {
            elementActions.clearField(homePhoneInput);
            elementActions.type(homePhoneInput, homePhone);
        }
        return this;
    }

    @Step("Enter mobile: {mobile}")
    public ContactDetailsPage enterMobile(String mobile) {
        if (mobile != null && !mobile.isEmpty()) {
            elementActions.clearField(mobileInput);
            elementActions.type(mobileInput, mobile);
        }
        return this;
    }

    @Step("Enter work phone: {workPhone}")
    public ContactDetailsPage enterWorkPhone(String workPhone) {
        if (workPhone != null && !workPhone.isEmpty()) {
            elementActions.clearField(workPhoneInput);
            elementActions.type(workPhoneInput, workPhone);
        }
        return this;
    }

    @Step("Enter work email: {workEmail}")
    public ContactDetailsPage enterWorkEmail(String workEmail) {
        if (workEmail != null && !workEmail.isEmpty()) {
            elementActions.clearField(workEmailInput);
            elementActions.type(workEmailInput, workEmail);
        }
        return this;
    }

    @Step("Enter other email: {otherEmail}")
    public ContactDetailsPage enterOtherEmail(String otherEmail) {
        if (otherEmail != null && !otherEmail.isEmpty()) {
            elementActions.clearField(otherEmailInput);
            elementActions.type(otherEmailInput, otherEmail);
        }
        return this;
    }

    @Step("Click save button")
    public ContactDetailsPage clickSave() {
        elementActions.scrollToElement(saveButton);
        elementActions.click(saveButton);
        return this;
    }

    @Step("Upload attachment: {filePath}, comment: {comment}")
    public ContactDetailsPage uploadAttachment(String filePath, String comment) {
        if (filePath != null && !filePath.isEmpty()) {
            elementActions.addAttachment(addAttachmentButton, fileInput, commentInput, attachmentSaveButton, filePath, comment);
        }
        return this;
    }

    // Complete workflow
    @Step("Fill contact details basic: {street1}, {city}, {country}")
    public ContactDetailsPage fillContactDetailsBasic(String street1, String city, String country) {
        return enterStreet1(street1)
                .enterCity(city)
                .selectCountry(country);
    }

    @Step("Fill contact details complete: {street1}, {city}, {country}, {mobile}, {workEmail}")
    public ContactDetailsPage fillContactDetailsComplete(String street1, String street2, String city, String state, String zip, String country,
                                                       String home, String mobile, String work, String workEmail, String otherEmail) {
        return enterStreet1(street1)
                .enterStreet2(street2)
                .enterCity(city)
                .enterState(state)
                .enterZipCode(zip)
                .selectCountry(country)
                .enterHomePhone(home)
                .enterMobile(mobile)
                .enterWorkPhone(work)
                .enterWorkEmail(workEmail)
                .enterOtherEmail(otherEmail);
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public ContactDetailsPage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public ContactDetailsPage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }
}
