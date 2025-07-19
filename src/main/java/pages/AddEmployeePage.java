package pages;

import abstractComponents.AbstractComponent;
import enums.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.myinfo.PersonalDetailsPage;

import java.time.Duration;

public class AddEmployeePage extends AbstractComponent {

    private WebDriver driver;

    public AddEmployeePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(name = "firstName")
    private WebElement firstNameInput;

    @FindBy(name = "middleName")
    private WebElement middleNameInput;

    @FindBy(name = "lastName")
    private WebElement lastNameInput;

    @FindBy(xpath = "//label[text()='Employee Id']/following::input[1]")
    private WebElement employeeIdInput;

    @FindBy(css = "input[type='file']")
    private WebElement uploadPictureInput;

    @FindBy(css = ".oxd-switch-input")
    private WebElement createLoginToggle;

    @FindBy(xpath = "//label[text()='Username']/following::input[1]")
    private WebElement usernameInput;

    @FindBy(xpath = "//label[text()='Password']/following::input[1]")
    private WebElement passwordInput;

    @FindBy(xpath = "//label[text()='Confirm Password']/following::input[1]")
    private WebElement confirmPasswordInput;

    @FindBy(xpath = "//input[@type='radio' and @value='1']/parent::label")
    private WebElement enabledStatusOption;

    @FindBy(xpath = "//input[@type='radio' and @value='2']")
    private WebElement disabledStatusOption;

    @FindBy(css = "button[type='submit']")
    private WebElement saveButton;

    @FindBy(css = ".oxd-toast-content")
    private WebElement notificationMessage;

    // Error message locators
    @FindBy(xpath = "//span[text()='Required']")
    private WebElement requiredErrorMessage;

    @FindBy(xpath = "//span[contains(text(),'Should be at least 2 characters')]")
    private WebElement minimumLengthErrorMessage;

    @FindBy(xpath = "//span[contains(text(),'Should not exceed 30 characters')]")
    private WebElement maximumLengthErrorMessage;

    @FindBy(xpath = "//span[contains(text(),'Already exists')]")
    private WebElement alreadyExistsErrorMessage;

    @FindBy(xpath = "//span[contains(text(),'Passwords do not match')]")
    private WebElement passwordMismatchErrorMessage;

    // Page title locator
    @FindBy(xpath = "//h6[text()='Add Employee']")
    private WebElement pageTitle;

    public void enterName(String firstName, String middleName, String lastName) {
        clearFieldReliably(firstNameInput);
        firstNameInput.sendKeys(firstName);

        clearFieldReliably(middleNameInput);
        middleNameInput.sendKeys(middleName);

        clearFieldReliably(lastNameInput);
        lastNameInput.sendKeys(lastName);
    }

    public void enterEmployeeId(String empId) {
        clearFieldReliably(employeeIdInput);
        employeeIdInput.sendKeys(empId);
    }

    public void uploadPicture(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            uploadPictureInput.sendKeys(imagePath);
        }
    }

    public void enableLoginDetails() {
        createLoginToggle.click();
    }

    public void disableLoginDetails() {
        if (createLoginToggle.isSelected()) {
            createLoginToggle.click();
        }
    }

    public void enterLoginDetails(String username, String password, String confirmPassword) {
        clearFieldReliably(usernameInput);
        usernameInput.sendKeys(username);

        clearFieldReliably(passwordInput);
        passwordInput.sendKeys(password);

        clearFieldReliably(confirmPasswordInput);
        confirmPasswordInput.sendKeys(confirmPassword);
    }

    public void setStatus(Status status) {
        if (status == Status.ENABLED) {
            enabledStatusOption.click();
        } else {
            disabledStatusOption.click();
        }
    }

    public PersonalDetailsPage clickSave() {
        saveButton.click();
        waitForElementToAppear(notificationMessage);
        return new PersonalDetailsPage(driver);
    }

    public void clickSaveWithoutWaiting() {
        saveButton.click();
    }

    public String waitForToastAndGetMessage() {
        By toastLocator = By.cssSelector(".oxd-toast-content");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(toastLocator));

        return toastElement.getText().trim();
    }

    public void waitForSuccessMessage() {
        waitForElementToAppear(notificationMessage);
    }

    public boolean isErrorMessageDisplayed(String expectedText) {
        try {
            java.util.List<org.openqa.selenium.WebElement> errorSpans = driver.findElements(org.openqa.selenium.By.cssSelector(".oxd-input-field-error-message"));
            for (org.openqa.selenium.WebElement span : errorSpans) {
                if (span.getText().trim().contains(expectedText)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}