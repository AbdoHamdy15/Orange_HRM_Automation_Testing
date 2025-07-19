package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class LeavePage extends AbstractComponent {

    WebDriver driver;

    public LeavePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ====== Fields ======
    @FindBy(xpath = "//label[text()='From Date']/following::input[1]")
    WebElement fromDateInput;

    @FindBy(xpath = "//label[text()='To Date']/following::input[1]")
    WebElement toDateInput;

    @FindBy(xpath = "//label[text()='Employee Name']/following::input[1]")
    WebElement employeeNameInput;

    @FindBy(css = ".oxd-input-group")
    List<WebElement> dropdownList;

    @FindBy(css = "input[type='checkbox']")
    WebElement includePastEmployeesToggle;

    @FindBy(xpath = "//button[normalize-space()='Search']")
    WebElement searchButton;

    @FindBy(xpath = "//button[normalize-space()='Reset']")
    WebElement resetButton;

    public void enterFromDate(String formattedDate) {
        selectDateBySendKeys(fromDateInput, formattedDate);
    }

    public void enterToDate(String formattedDate) {
        selectDateBySendKeys(toDateInput, formattedDate);
    }


    public void enterEmployeeName(String inputText, String nameToSelect) {
        enterAutoSuggestField(employeeNameInput, inputText, nameToSelect);
    }

    public void setIncludePastEmployees(boolean include) {
        boolean isSelected = includePastEmployeesToggle.isSelected();
        if (include != isSelected) {
            includePastEmployeesToggle.click();
        }
    }

    public void clickSearch() {
        searchButton.click();
    }

    public void clickReset() {
        resetButton.click();
    }

    public void searchLeave(String fromDate, String toDate, String status, String leaveType, String nameToType,
                            String nameToSelect, String subUnit, boolean includePastEmployees) {

        enterFromDate(fromDate);
        enterToDate(toDate);

        // Only select status if a specific value is provided
        if (status != null && !status.trim().isEmpty()) {
            selectStatusDropdown(status);
        }

        if (leaveType != null) {
            selectLeaveTypeDropdown(leaveType);
        }

        if (nameToType != null && nameToSelect != null) {
            enterEmployeeName(nameToType, nameToSelect);
        }

        if (subUnit != null) {
            selectSubUnitDropdown(subUnit);
        }

        setIncludePastEmployees(includePastEmployees);
        clickSearch();
    }

    // Add this method to get the Info/No Records Found toast message
    public String waitForToastAndGetMessage() {
        By toastLocator = By.cssSelector(".oxd-toast-content");
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                    java.time.Duration.ofSeconds(5));
            WebElement toastElement = wait
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(toastLocator));
            return toastElement.getText().trim();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isInvalidErrorDisplayedForEmployeeName() {
        try {
            WebElement errorElem = driver.findElement(By.xpath(
                    "//label[text()='Employee Name']/following::span[contains(@class,'oxd-input-field-error-message')][1]"));
            return errorElem.isDisplayed() && errorElem.getText().trim().equalsIgnoreCase("Invalid");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRequiredErrorDisplayedForStatus() {
        try {
            WebElement errorElem = driver.findElement(By.xpath(
                    "//label[contains(text(),'Show Leave with Status')]/following::span[contains(@class,'oxd-input-field-error-message')][1]"));
            return errorElem.isDisplayed() && errorElem.getText().trim().equalsIgnoreCase("Required");
        } catch (Exception e) {
            return false;
        }
    }

    public void clearStatusSelection() {
        try {
            // Use the working XPath for the clear button
            WebElement clearBtn = driver.findElement(By.xpath("//i[contains(@class,'--clear')]"));
            clearBtn.click();
            System.out.println("[LeavePage] Successfully cleared status selection");
        } catch (Exception e) {
            System.out.println("[LeavePage] Could not clear status selection: " + e.getMessage());
            // If the clear button is not present, do nothing
        }
    }

    public List<WebElement> getDropdownList() {
        return dropdownList;
    }

    public void selectStatusDropdown(String typeText) {

        WebElement statusDropDown = dropdownList.stream()
                .filter(dropdown -> dropdown.findElement(By.cssSelector(".oxd-label")).getText().contains("Status"))
                .findFirst().orElse(null);

        statusDropDown.click();

        WebElement option = driver.findElement(By.xpath("//div[@role='listbox']//span[text()='" + typeText + "']"));
        option.click();

    }

    public void selectLeaveTypeDropdown(String typeText) {

        WebElement leaveTypeDropDown = dropdownList.stream()
                .filter(dropdown -> dropdown.findElement(By.cssSelector(".oxd-label")).getText().contains("Leave Type"))
                .findFirst().orElse(null);

        leaveTypeDropDown.click();

        WebElement option = driver.findElement(By.xpath("//div[@role='listbox']//span[text()='" + typeText + "']"));
        option.click();

    }

    public void selectSubUnitDropdown(String typeText) {

        WebElement subUnitDropDown = dropdownList.stream()
                .filter(dropdown -> dropdown.findElement(By.cssSelector(".oxd-label")).getText().equalsIgnoreCase("Sub Unit"))
                .findFirst().orElse(null);

        subUnitDropDown.click();

        WebElement option = driver.findElement(By.xpath("//div[@role='listbox']//span[text()='" + typeText + "']"));
        option.click();

    }
}
