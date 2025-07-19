package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PerformancePage extends AbstractComponent {

    WebDriver driver;

    public PerformancePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//label[text()='Employee Name']/following::input[1]")
    WebElement employeeNameInput;

    @FindBy(xpath = "(//div[contains(@class,'oxd-select-wrapper')])[1]")
    WebElement jobTitleDropdown;

    @FindBy(xpath = "(//div[contains(@class,'oxd-select-wrapper')])[2]")
    WebElement subUnitDropdown;

    @FindBy(xpath = "(//div[contains(@class,'oxd-select-wrapper')])[3]")
    WebElement includeDropdown;

    @FindBy(xpath = "(//div[contains(@class,'oxd-select-wrapper')])[4]")
    WebElement reviewStatusDropdown;


    @FindBy(xpath = "//label[text()='From']/following::input[1]")
    WebElement fromDateInput;

    @FindBy(xpath = "//label[text()='To']/following::input[1]")
    WebElement toDateInput;

    @FindBy(xpath = "//label[text()='From']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon'][1]")
    WebElement fromCalendarIcon;

    @FindBy(xpath = "//label[text()='To']/following::i[@class='oxd-icon bi-calendar oxd-date-input-icon'][1]")
    WebElement toCalendarIcon;

    @FindBy(xpath = "//button[normalize-space()='Search']")
    WebElement searchButton;

    @FindBy(xpath = "//button[normalize-space()='Reset']")
    WebElement resetButton;

    // Actions
    public void enterEmployeeName(String inputText, String nameToSelect) {
        enterAutoSuggestField(employeeNameInput, inputText, nameToSelect);
    }

    public void selectJobTitle(String jobTitle) {
        selectFromDropdown(jobTitleDropdown, jobTitle);
    }

    public void selectSubUnit(String subUnit) {
        selectFromDropdown(subUnitDropdown, subUnit);
    }

    public void selectInclude(String includeOption) {
        selectFromDropdown(includeDropdown, includeOption);
    }

    public void selectReviewStatus(String status) {

        selectFromDropdown(reviewStatusDropdown, status);
    }


    public void setFromDate(String day, String month, String year) {
        selectDateFromCalendar(fromCalendarIcon, day, month, year);
    }

    public void setToDate(String day, String month, String year) {
        selectDateFromCalendar(toCalendarIcon, day, month, year);
    }

    public void clickSearch() {
        searchButton.click();
    }

    public void clickReset() {
        resetButton.click();
    }

    public void searchEmployeeReviews(String empInput, String empName, String jobTitle,
                                      String subUnit, String includeOption, String status,
                                      String fromDay, String fromMonth, String fromYear,
                                      String toDay, String toMonth, String toYear) {
        enterEmployeeName(empInput, empName);
        selectJobTitle(jobTitle);
        selectSubUnit(subUnit);
        selectInclude(includeOption);
        selectReviewStatus(status);
        setFromDate(fromDay, fromMonth, fromYear);
        setToDate(toDay, toMonth, toYear);
        clickSearch();
    }

    public boolean isInvalidErrorDisplayedForEmployeeName() {
        try {
            WebElement errorElem = driver.findElement(By.xpath("//label[text()='Employee Name']/following::span[contains(@class,'oxd-input-field-error-message')][1]"));
            return errorElem.isDisplayed() && errorElem.getText().trim().equalsIgnoreCase("Invalid");
        } catch (Exception e) {
            return false;
        }
    }

    public String waitForToastAndGetMessage() {
        By toastLocator = By.cssSelector(".oxd-toast-content");
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5));
            WebElement toastElement = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(toastLocator));
            return toastElement.getText().trim();
        } catch (Exception e) {
            return null;
        }
    }
}
