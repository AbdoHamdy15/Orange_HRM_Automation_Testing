package pages.myinfo;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MembershipsPage extends AbstractComponent {

    WebDriver driver;

    public MembershipsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Membership Info
    @FindBy(xpath = "//label[text()='Membership']/following::div[contains(@class,'oxd-select-wrapper')][1]")
    WebElement membershipDropdown;

    @FindBy(xpath = "//label[text()='Subscription Paid By']/following::div[contains(@class,'oxd-select-wrapper')][1]")
    WebElement subscriptionPaidByDropdown;

    @FindBy(xpath = "//label[text()='Subscription Amount']/following::input[1]")
    WebElement subscriptionAmountInput;

    @FindBy(xpath = "//label[text()='Currency']/following::div[contains(@class,'oxd-select-wrapper')][1]")
    WebElement currencyDropdown;

    @FindBy(xpath = "//label[text()='Commence Date']/following::input[1]")
    WebElement commenceDateInput;

    @FindBy(xpath = "//label[text()='Renewal Date']/following::input[1]")
    WebElement renewalDateInput;

    @FindBy(xpath = "//label[text()='Commence Date']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon'][1]")
    WebElement commenceDateCalendarIcon;

    @FindBy(xpath = "//label[text()='Renewal Date']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon'][1]")
    WebElement renewalDateCalendarIcon;

    @FindBy(xpath = "//button[normalize-space()='Save']")
    WebElement saveButton;

    @FindBy(xpath = "//button[normalize-space()='Add']")
    WebElement addButton;

    // Attachment
    @FindBy(xpath = "//h6[text()='Attachments']/following::button[normalize-space()='Add'][1]")
    WebElement addAttachmentButton;

    @FindBy(css = "input[type='file']")
    WebElement fileInput;

    @FindBy(xpath = "//label[text()='Comment']/following::textarea[1]")
    WebElement commentInput;

    @FindBy(xpath = "//div[@class='oxd-form-actions']//button[normalize-space()='Save']")
    WebElement attachmentSaveButton;

    // Methods

    public void selectMembership(String membership) {
        selectFromDropdown(membershipDropdown, membership);
    }

    public void selectPaidBy(String paidBy) {
        selectFromDropdown(subscriptionPaidByDropdown, paidBy);
    }

    public void enterSubscriptionAmount(String amount) {
        clearFieldReliably(subscriptionAmountInput);
        subscriptionAmountInput.sendKeys(amount);
    }

    public void selectCurrency(String currency) {
        selectFromDropdown(currencyDropdown, currency);
    }

    public void setCommenceDate(String day, String month, String year) {
        selectDateFromCalendar(commenceDateCalendarIcon, day, month, year);
    }

    public void setRenewalDate(String day, String month, String year) {
        selectDateFromCalendar(renewalDateCalendarIcon, day, month, year);
    }

    public void clickSave() {
        saveButton.click();
    }

    public void clickAdd() {
        addButton.click();
    }

    public void uploadAttachment(String filePath, String comment) {
        addAttachment(addAttachmentButton, fileInput, commentInput, attachmentSaveButton, filePath, comment);
    }

    public void fillMembershipDetails(String membership, String paidBy, String amount, String currency,
                                      String commenceDay, String commenceMonth, String commenceYear,
                                      String renewalDay, String renewalMonth, String renewalYear) {
        selectMembership(membership);
        selectPaidBy(paidBy);
        enterSubscriptionAmount(amount);
        selectCurrency(currency);
        setCommenceDate(commenceDay, commenceMonth, commenceYear);
        setRenewalDate(renewalDay, renewalMonth, renewalYear);
        clickSave();
    }
}
