package pages.myinfo;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ContactDetailsPage extends AbstractComponent {

    WebDriver driver;

    public ContactDetailsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Address Fields
    @FindBy(xpath = "//label[text()='Street 1']/following::input[1]")
    WebElement street1Input;

    @FindBy(xpath = "//label[text()='Street 2']/following::input[1]")
    WebElement street2Input;

    @FindBy(xpath = "//label[text()='City']/following::input[1]")
    WebElement cityInput;

    @FindBy(xpath = "//label[text()='State/Province']/following::input[1]")
    WebElement stateInput;

    @FindBy(xpath = "//label[text()='Zip/Postal Code']/following::input[1]")
    WebElement zipInput;

    @FindBy(xpath = "//label[text()='Country']/following::div[@class='oxd-select-wrapper'][1]")
    WebElement countryDropdown;

    // Telephone Fields
    @FindBy(xpath = "//label[text()='Home']/following::input[1]")
    WebElement homePhoneInput;

    @FindBy(xpath = "//label[text()='Mobile']/following::input[1]")
    WebElement mobileInput;

    @FindBy(xpath = "//label[text()='Work']/following::input[1]")
    WebElement workPhoneInput;

    // Email Fields
    @FindBy(xpath = "//label[text()='Work Email']/following::input[1]")
    WebElement workEmailInput;

    @FindBy(xpath = "//label[text()='Other Email']/following::input[1]")
    WebElement otherEmailInput;

    // Save Button
    @FindBy(css = "button[type='submit']")
    WebElement saveButton;

    // Attachment Elements
    @FindBy(xpath = "//h6[text()='Attachments']/following::button[normalize-space()='Add'][1]")
    WebElement addAttachmentButton;

    @FindBy(css = "input[type='file']")
    WebElement fileInput;

    @FindBy(xpath = "//label[text()='Comment']/following::textarea[1]")
    WebElement commentInput;

    @FindBy(xpath = "//button[normalize-space()='Save']")
    WebElement attachmentSaveButton;

    // Actions
    public void fillAddress(String street1, String street2, String city, String state, String zip, String country) {
        clearFieldReliably(street1Input);
        street1Input.sendKeys(street1);

        clearFieldReliably(street2Input);
        street2Input.sendKeys(street2);

        clearFieldReliably(cityInput);
        cityInput.sendKeys(city);

        clearFieldReliably(stateInput);
        stateInput.sendKeys(state);

        clearFieldReliably(zipInput);
        zipInput.sendKeys(zip);

        selectFromDropdown(countryDropdown, country);
    }

    public void fillPhoneNumbers(String home, String mobile, String work) {
        clearFieldReliably(homePhoneInput);
        homePhoneInput.sendKeys(home);

        clearFieldReliably(mobileInput);
        mobileInput.sendKeys(mobile);

        clearFieldReliably(workPhoneInput);
        workPhoneInput.sendKeys(work);
    }

    public void fillEmails(String workEmail, String otherEmail) {
        clearFieldReliably(workEmailInput);
        workEmailInput.sendKeys(workEmail);

        clearFieldReliably(otherEmailInput);
        otherEmailInput.sendKeys(otherEmail);
    }

    public void clickSave() {
        saveButton.click();
    }

    public void uploadAttachment(String filePath, String comment) {
        addAttachment(addAttachmentButton, fileInput, commentInput, attachmentSaveButton, filePath, comment);
    }
    
    public String waitForToastAndGetMessage() {
        return super.waitForToastAndGetMessage();
    }
    
    public boolean isErrorMessageDisplayed(String errorText) {
        return super.isErrorMessageDisplayed(errorText);
    }
}
