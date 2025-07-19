package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class PIMPage extends AbstractComponent {

    WebDriver driver;

    public PIMPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//label[text()='Employee Name']/following::input[1]")
    WebElement employeeNameInput;

    @FindBy(xpath = "//label[text()='Employee Id']/following::input[1]")
    WebElement employeeIdInput;

    @FindBy(xpath = "//label[text()='Job Title']/following::div[contains(@class,'oxd-select-wrapper')][1]")
    WebElement jobTitleDropdown;

    @FindBy(xpath = "//label[text()='Employment Status']/following::div[contains(@class,'oxd-select-wrapper')][1]")
    WebElement employmentStatusDropdown;

    @FindBy(xpath = "//label[text()='Include']/following::div[contains(@class,'oxd-select-wrapper')][1]")
    WebElement includeDropdown;

    @FindBy(xpath = "//label[text()='Supervisor Name']/following::input[1]")
    WebElement supervisorNameInput;

    @FindBy(xpath = "//label[text()='Sub Unit']/following::div[contains(@class,'oxd-select-wrapper')][1]")
    WebElement subUnitDropdown;

    @FindBy(xpath = "//button[normalize-space()='Search']")
    WebElement searchButton;

    @FindBy(xpath = "//button[normalize-space()='Reset']")
    WebElement resetButton;

    @FindBy(xpath = "//button[normalize-space()='Add']")
    WebElement addEmployeeButton;

    public void enterEmployeeName(String nameInput, String nameToSelect) {
        enterAutoSuggestField(employeeNameInput, nameInput, nameToSelect);
    }

    public void enterSupervisorName(String nameInput, String nameToSelect) {
        enterAutoSuggestField(supervisorNameInput, nameInput, nameToSelect);
    }

    public void enterEmployeeId(String id) {
        clearFieldReliably(employeeIdInput);
        employeeIdInput.sendKeys(id);
    }

    public void selectJobTitle(String title) {
        selectFromDropdown(jobTitleDropdown, title);
    }

    public void selectEmploymentStatus(String status) {
        selectFromDropdown(employmentStatusDropdown, status);
    }

    public void selectInclude(String includeText) {
        selectFromDropdown(includeDropdown, includeText);
    }

    public void selectSubUnit(String subUnit) {
        selectFromDropdown(subUnitDropdown, subUnit);
    }

    public void clickSearch() {
        searchButton.click();
    }

    public void clickReset() {
        resetButton.click();
    }

    public AddEmployeePage clickAddEmployee() {
        addEmployeeButton.click();
        return new AddEmployeePage(driver);
    }

    public boolean isInvalidMessageDisplayed() {
        try {
            // Use the exact locator for Invalid message
            WebElement invalidMessage = driver.findElement(By.xpath("//span[@class='oxd-text oxd-text--span oxd-input-field-error-message oxd-input-group__message' and text()='Invalid']"));
            boolean isDisplayed = invalidMessage.isDisplayed();
            System.out.println("Invalid message found and displayed: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            System.out.println("Invalid message not found: " + e.getMessage());
            return false;
        }
    }

    public boolean isSearchResultsTableDisplayed() {
        try {
            WebElement tableBody = driver.findElement(By.xpath("//div[@class='oxd-table-body']"));
            return tableBody.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
