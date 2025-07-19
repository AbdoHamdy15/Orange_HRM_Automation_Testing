package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TimePage extends AbstractComponent {

    WebDriver driver;

    public TimePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//label[text()='Employee Name']/following::input[1]")
    WebElement employeeNameInput;

    @FindBy(xpath = "//button[normalize-space()='View']")
    WebElement viewButton;

    public void enterEmployeeName(String inputText, String nameToSelect) {
        enterAutoSuggestField(employeeNameInput, inputText, nameToSelect);
    }

    public void clickView() {
        viewButton.click();
    }

    public void viewEmployeeTimesheet(String inputText, String nameToSelect) {
        enterEmployeeName(inputText, nameToSelect);
        clickView();
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

    public String getTimesheetPageMessage() {
        try {
            WebElement messageElem = driver.findElement(By.xpath("//p[contains(@class,'oxd-text') and contains(@class,'oxd-alert-content-text')]"));
            return messageElem.getText().trim();
        } catch (Exception e) {
            return null;
        }
    }
}
