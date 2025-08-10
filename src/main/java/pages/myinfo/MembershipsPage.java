package pages.myinfo;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class MembershipsPage {

    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // Locators
    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    private final By membershipDropdown = By.xpath("//label[text()='Membership']/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By subscriptionPaidByDropdown = By.xpath("//label[text()='Subscription Paid By']/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By subscriptionAmountInput = By.xpath("//label[text()='Subscription Amount']/following::input[1]");
    private final By currencyDropdown = By.xpath("//label[text()='Currency']/following::div[contains(@class,'oxd-select-wrapper')][1]");
    private final By commenceDateInput = By.xpath("//label[contains(text(),'Commence')]/following::input[1]");
    private final By renewalDateInput = By.xpath("//label[contains(text(),'Renewal')]/following::input[1]");
    private final By saveButton = By.xpath("//button[normalize-space()='Save']");
    private final By membershipsHeader = By.xpath("//h6[text()='Assigned Memberships']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]");

    public MembershipsPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
    }

    // Navigation methods
    @Step("Assert memberships page is displayed")
    public MembershipsPage assertMembershipsPageDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(membershipsHeader), "Memberships page should be displayed");
        return this;
    }

    // Action methods
    @Step("Click add button")
    public MembershipsPage clickAdd() {
        waits.waitForElementClickable(addButton);
        elementActions.click(addButton);
        return this;
    }

    @Step("Select membership: {membership}")
    public MembershipsPage selectMembership(String membership) {
        if (membership != null && !membership.isEmpty()) {
            elementActions.selectFromDropdown(membershipDropdown, membership);
        }
        return this;
    }

    @Step("Select subscription paid by: {paidBy}")
    public MembershipsPage selectSubscriptionPaidBy(String paidBy) {
        if (paidBy != null && !paidBy.isEmpty()) {
            elementActions.selectFromDropdown(subscriptionPaidByDropdown, paidBy);
        }
        return this;
    }

    @Step("Enter subscription amount: {amount}")
    public MembershipsPage enterSubscriptionAmount(String amount) {
        if (amount != null && !amount.isEmpty()) {
            elementActions.clearField(subscriptionAmountInput);
            elementActions.type(subscriptionAmountInput, amount);
        }
        return this;
    }

    @Step("Select currency: {currency}")
    public MembershipsPage selectCurrency(String currency) {
        if (currency != null && !currency.isEmpty()) {
            elementActions.selectFromDropdown(currencyDropdown, currency);
        }
        return this;
    }

    @Step("Set commence date: {commenceDate}")
    public MembershipsPage setCommenceDate(String commenceDate) {
        if (commenceDate != null && !commenceDate.isEmpty()) {
            elementActions.clearField(commenceDateInput);
            elementActions.type(commenceDateInput, commenceDate);
        }
        return this;
    }

    @Step("Set renewal date: {renewalDate}")
    public MembershipsPage setRenewalDate(String renewalDate) {
        if (renewalDate != null && !renewalDate.isEmpty()) {
            elementActions.clearField(renewalDateInput);
            elementActions.type(renewalDateInput, renewalDate);
        }
        return this;
    }

    @Step("Click save button")
    public MembershipsPage clickSave() {
        elementActions.click(saveButton);
        return this;
    }

    // Complete workflow
    @Step("Add membership: {membership}, {paidBy}, {amount}, {currency}, {commenceDate}, {renewalDate}")
    public MembershipsPage addMembership(String membership, String paidBy, String amount, String currency, 
                                         String commenceDate, String renewalDate) {
        return clickAdd()
                .selectMembership(membership)
                .selectSubscriptionPaidBy(paidBy)
                .enterSubscriptionAmount(amount)
                .selectCurrency(currency)
                .setCommenceDate(commenceDate)
                .setRenewalDate(renewalDate)
                .clickSave();
    }

    // Validation methods
    @Step("Assert specific error message is displayed: {expectedError}")
    public MembershipsPage assertSpecificErrorDisplayed(String expectedError) {
        String actualError = validations.getActualErrorText(expectedError);
        validations.isErrorMessageDisplayed(actualError);
        return this;
    }

    @Step("Assert success toast is displayed")
    public MembershipsPage assertSuccessToastDisplayed() {
        // Wait for toast message to appear
        String toastMessage = waits.waitForToastAndGetMessage();
        validations.validateTrue(toastMessage != null && toastMessage.contains("Success"),
            "Success toast message should be displayed");
        return this;
    }
}
