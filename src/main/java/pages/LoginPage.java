package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class LoginPage {
    
    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;
    
    // Locators
    private final By usernameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.cssSelector("button.orangehrm-login-button");
    private final By errorMessage = By.cssSelector(".oxd-alert-content-text");
    private final By requiredMessages = By.xpath("//span[contains(@class, 'oxd-input-field-error-message')]");
    private final By loginForm = By.cssSelector("div.orangehrm-login-form");

    // Constructor
    public LoginPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
    }

    // Navigation
    @Step("Navigate to OrangeHRM login page")
    public LoginPage navigateToLoginPage() {
        driver.browser().navigateToURL("https://opensource-demo.orangehrmlive.com/");
        // Wait for page to load completely
        waits.waitForElementVisible(loginForm);
        return this;
    }

    // Actions
    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        elementActions.clearField(usernameField);
        elementActions.type(usernameField, username);
        return this;
    }

    @Step("Enter password: {password}")
    public LoginPage enterPassword(String password) {
        elementActions.clearField(passwordField);
        elementActions.type(passwordField, password);
        return this;
    }

    @Step("Click login button")
    public LoginPage clickLoginButton() {
        elementActions.click(loginButton);
        return this;
    }

    // Getters
    @Step("Get error message")
    public String getErrorMessage() {
        waits.waitForElementVisible(errorMessage);
        return elementActions.getText(errorMessage);
    }

    @Step("Get required field messages")
    public String getRequiredMessages() {
        waits.waitForElementVisible(requiredMessages);
        return elementActions.getText(requiredMessages);
    }

    // Validations
    @Step("Assert login page is displayed")
    public LoginPage assertLoginPageDisplayed() {
        String currentUrl = driver.browser().getCurrentURL();
        validations.validateTrue(currentUrl.contains("/auth/login"), "Login page URL should contain '/auth/login' but was '" + currentUrl + "'");
        return this;
    }

    // Debug method
    public boolean isFormVisible() {
        return elementActions.isDisplayed(loginForm);
    }

    @Step("Assert invalid credentials error: {expectedError}")
    public LoginPage assertInvalidCredentialsError(String expectedError) {
        waits.waitForElementVisible(errorMessage);
        String actualError = elementActions.getText(errorMessage);
        validations.validateTrue(actualError.equals(expectedError), 
            "Invalid credentials error should be '" + expectedError + "' but was '" + actualError + "'");
        return this;
    }

    @Step("Assert required field error: {expectedError}")
    public LoginPage assertRequiredFieldError(String expectedError) {
        waits.waitForElementVisible(requiredMessages);
        String actualError = elementActions.getText(requiredMessages);
        validations.validateTrue(actualError.equals(expectedError), 
            "Required field error should be '" + expectedError + "' but was '" + actualError + "'");
        return this;
    }

    @Step("Assert successful login")
    public DashboardPage assertSuccessfulLogin() {
        // Light wait before validation
        waits.waitForSeconds(2);
        // Create DashboardPage instance to access its header locator
        DashboardPage dashboardPage = new DashboardPage(driver);
        // Validate that Dashboard header is displayed
        validations.validateTrue(elementActions.isDisplayed(dashboardPage.getDashboardHeader()), "Dashboard header should be displayed after successful login");
        return dashboardPage;
    }

    // Complete workflows
    @Step("Perform login with username: {username} and password: {password}")
    public DashboardPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton()
                .assertSuccessfulLogin();
    }

    @Step("Perform invalid login with username: {username} and password: {password}")
    public LoginPage invalidLogin(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }
}
