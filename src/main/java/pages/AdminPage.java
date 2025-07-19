package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.*;

public class AdminPage extends AbstractComponent {

    private WebDriver driver;

    public AdminPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//label[text()='Username']/following::input[1]")
    private WebElement usernameInput;

    @FindBy(xpath = "//label[text()='User Role']/following::div[@class='oxd-select-wrapper'][1]")
    private WebElement userRoleDropdown;

    @FindBy(xpath = "//label[text()='Employee Name']/following::input[1]")
    private WebElement employeeNameInput;

    @FindBy(xpath = "//label[text()='Status']/following::div[@class='oxd-select-wrapper'][1]")
    private WebElement statusDropdown;

    @FindBy(xpath = "//button[normalize-space()='Search']")
    private WebElement searchButton;

    @FindBy(xpath = "//button[normalize-space()='Reset']")
    private WebElement resetButton;

    @FindBy(xpath = "//button[normalize-space()='Add']")
    private WebElement addButton;

    public void enterUsername(String username) {
        clearFieldReliably(usernameInput);
        usernameInput.sendKeys(username);
    }

    public void selectUserRole(String roleText) {
        selectFromDropdown(userRoleDropdown, roleText);
    }

    public void enterEmployeeName(String inputText, String nameToSelect) {
        enterAutoSuggestField(employeeNameInput, inputText, nameToSelect);
    }

    public void selectStatus(String statusText) {
        selectFromDropdown(statusDropdown, statusText);
    }

    public void clickSearch() {
        searchButton.click();
    }

    public void clickReset() {
        resetButton.click();
    }

    public AddUserPage clickAdd() {
        addButton.click();
        return new AddUserPage(driver);
    }

    public void searchUsers(String username, String role, String employeeInputText, String employeeNameToSelect, String status) {
        enterUsername(username);
        selectUserRole(role);
        enterEmployeeName(employeeInputText, employeeNameToSelect);
        selectStatus(status);
        clickSearch();
    }

    public boolean isSearchResultsTableDisplayed() {
        try {
            WebElement tableBody = driver.findElement(By.xpath("//div[@class='oxd-table-body']"));
            return tableBody.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoRecordsFoundMessageDisplayed() {
        try {
            WebElement noRecordsMessage = driver.findElement(By.xpath("//span[text()='No Records Found']"));
            return noRecordsMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}