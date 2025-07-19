package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class AddUserPage extends AbstractComponent {

    WebDriver driver;

    public AddUserPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//label[text()='User Role']/following::div[@class='oxd-select-wrapper'][1]")
    WebElement userRoleDropdown;

    @FindBy(xpath = "//label[text()='Employee Name']/following::input[1]")
    WebElement employeeNameInput;

    @FindBy(xpath = "//label[text()='Status']/following::div[@class='oxd-select-wrapper'][1]")
    WebElement statusDropdown;

    @FindBy(xpath = "//label[text()='Username']/following::input[1]")
    WebElement usernameInput;

    @FindBy(xpath = "//label[text()='Password']/following::input[1]")
    WebElement passwordInput;

    @FindBy(xpath = "//label[text()='Confirm Password']/following::input[1]")
    WebElement confirmPasswordInput;

    @FindBy(xpath = "//button[contains(@class, 'oxd-button') and text()=' Save ']")
    WebElement saveButton;

    public void selectUserRole(String roleText) {
        userRoleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//div[@role='listbox']//span[text()='" + roleText + "']"));
        option.click();
    }

    public void enterEmployeeName(String inputText, String nameToSelect) {
        enterAutoSuggestField(employeeNameInput, inputText, nameToSelect);
    }

    public void selectStatus(String statusText) {
        statusDropdown.click();
        WebElement option = driver.findElement(By.xpath("//div[@role='listbox']//span[text()='" + statusText + "']"));
        option.click();
    }

    public void enterUsername(String username) {
        clearFieldReliably(usernameInput);
        usernameInput.sendKeys(username);
    }

    public void enterPassword(String password) {
        clearFieldReliably(passwordInput);
        passwordInput.sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        clearFieldReliably(confirmPasswordInput);
        confirmPasswordInput.sendKeys(password);
    }

    public void clickSave() {
        saveButton.click();
    }

    public void addNewUser(String role, String employeeInputText, String employeeNameToSelect, String status, String username, String password) {
        selectUserRole(role);
        enterEmployeeName(employeeInputText, employeeNameToSelect);
        selectStatus(status);
        enterUsername(username);
        enterPassword(password);
        enterConfirmPassword(password);
        clickSave();
    }

    public boolean isErrorDisplayed(String expectedError) {
        List<WebElement> errors = driver.findElements(By.xpath("//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-input-group__message')]"));
        for (WebElement error : errors) {
            System.out.println("Actual error message: '" + error.getText().trim() + "'");
            if (error.getText().trim().toLowerCase().contains(expectedError.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isSuccessToastDisplayed() {
        try {
            WebElement toast = driver.findElement(By.xpath("//div[contains(@class,'oxd-toast') and contains(.,'Success')]"));
            return toast.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
