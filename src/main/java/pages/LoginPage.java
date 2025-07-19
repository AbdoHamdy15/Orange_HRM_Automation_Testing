package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class LoginPage extends AbstractComponent {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(css = "button.orangehrm-login-button")
    private WebElement loginButton;

    @FindBy(className = "oxd-alert-content-text")
    private WebElement errorMessage;

    @FindAll(@FindBy(xpath = "//span[contains(@class, 'oxd-input-field-error-message')]"))
    private List<WebElement> requiredMessages;

    public void enterUsername(String username) {
        clearFieldReliably(usernameField);
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        clearFieldReliably(passwordField);
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        loginButton.click();
    }

    public String getErrorMessage() {
        waitForElementToAppear(errorMessage);
        return errorMessage.getText();
    }

    public String getUsernameRequiredMessage() {
        waitForElementToAppear(requiredMessages.get(0));
        return requiredMessages.get(0).getText();
    }

    public String getPasswordRequiredMessage() {
        if (requiredMessages.size() > 1) {
            return requiredMessages.get(1).getText();
        } else {
            return requiredMessages.get(0).getText();
        }
    }

    public DashboardPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new DashboardPage(driver);
    }
}
