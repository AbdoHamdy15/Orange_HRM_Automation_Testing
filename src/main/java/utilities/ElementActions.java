package utilities;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ElementActions {
    private WebDriver driver;
    private Waits waits;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    // ==================== CLICK METHODS ====================

    // Click element with wait
    @Step("Clicking on the element: {locator}")
    public void click(By locator) {
        WebElement element = findElement(locator);
        try {
            element.click();
        } catch (Exception e) {
            // If regular click fails, try JavaScript click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
        LogsUtil.info("Clicked on the element: ", locator.toString());
    }

    // ==================== TEXT INPUT METHODS ====================

    // Clear field reliably using Ctrl+A + Delete
    @Step("Clearing field reliably: {locator}")
    public void clearField(By locator) {
        WebElement element = findElement(locator);
        element.click();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        LogsUtil.info("Cleared field : ", locator.toString());
    }

    // Type text into element
    @Step("Sending data: {data} to the element: {locator}")
    public void type(By locator, String data) {
        WebElement element = findElement(locator);
        element.sendKeys(data);
        LogsUtil.info("Data entered: ", data, " in the field: ", locator.toString());
    }

    // ==================== TEXT RETRIEVAL METHODS ====================

    // Get text from element
    @Step("Getting text from the element: {locator}")
    public String getText(By locator) {
        waits.waitForElementVisible(locator);
        WebElement element = findElement(locator);
        String text = element.getText().trim();
        LogsUtil.info("Getting text from the element: ", locator.toString(), " Text: ", text);
        return text;
    }

    // ==================== DROPDOWN METHODS ====================

    // Select from custom dropdown (non-select elements) - for OrangeHRM
    @Step("Selecting from custom dropdown: {optionText}")
    public void selectFromDropdown(By dropdownLocator, String optionText) {
        WebElement dropdown = findElement(dropdownLocator);
        dropdown.click();
        
        By optionLocator = By.xpath("//div[@role='listbox']//span[text()='" + optionText + "']");
        waits.waitForElementVisible(optionLocator);
        WebElement option = findElement(optionLocator);
        option.click();
        
        LogsUtil.info("Selected from custom dropdown: " + optionText);
    }

    // Select from dropdown by label text
    @Step("Selecting from dropdown by label: {labelText} with option: {optionText}")
    public void selectFromDropdownByLabel(By dropdownListLocator, String labelText, String optionText) {
        if (optionText != null && !optionText.isEmpty()) {
            java.util.List<WebElement> dropdowns = driver.findElements(dropdownListLocator);
            WebElement targetDropdown = dropdowns.stream()
                    .filter(dropdown -> dropdown.findElement(By.cssSelector(".oxd-label")).getText().contains(labelText))
                    .findFirst().orElse(null);

            if (targetDropdown != null) {
                targetDropdown.click();
                By optionLocator = By.xpath("//div[@role='listbox']//span[text()='" + optionText + "']");
                waits.waitForElementVisible(optionLocator);
                WebElement option = findElement(optionLocator);
                option.click();
                LogsUtil.info("Selected from dropdown with label '" + labelText + "': " + optionText);
            } else {
                LogsUtil.error("Dropdown with label '" + labelText + "' not found");
            }
        }
    }

    // ==================== CHECKBOX AND RADIO METHODS ====================

    // Check checkbox
    @Step("Checking checkbox: {locator}")
    public void check(By locator) {
        WebElement element = findElement(locator);
        if (!element.isSelected()) {
            element.click();
        }
        LogsUtil.info("Checked checkbox: ", locator.toString());
    }

    // Uncheck checkbox
    @Step("Unchecking checkbox: {locator}")
    public void uncheck(By locator) {
        WebElement element = findElement(locator);
        if (element.isSelected()) {
            element.click();
        }
        LogsUtil.info("Unchecked checkbox: ", locator.toString());
    }

    // Toggle checkbox
    @Step("Toggling checkbox: {locator}")
    public void toggle(By locator) {
        WebElement element = findElement(locator);
        element.click();
        LogsUtil.info("Toggled checkbox: ", locator.toString());
    }

    // Check if element is selected
    public boolean isSelected(By locator) {
        WebElement element = findElement(locator);
        return element.isSelected();
    }

    // Select radio button by label text (works with enums)
    @Step("Selecting radio button by label: {optionLabel}")
    public void selectRadioByLabel(String optionLabel) {
        By radioLocator = By.xpath("//label[.//span[text()='" + optionLabel + "']]//input[@type='radio']");
        WebElement radio = findElement(radioLocator);
        if (!radio.isSelected()) {
            radio.click();
            LogsUtil.info("Selected radio button with label: ", optionLabel);
        } else {
            LogsUtil.info("Radio button with label already selected: ", optionLabel);
        }
    }

    // ==================== AUTOCOMPLETE METHODS ====================

    // Enter autocomplete field with suggestion selection
    @Step("Entering autocomplete field with text: {inputText} and selecting: {nameToSelect}")
    public void enterAutoSuggestField(By inputFieldLocator, String inputText, String nameToSelect) {
        WebElement field = findElement(inputFieldLocator);
        waits.waitForElementVisible(inputFieldLocator);
        
        // Clear field reliably
        field.click();
        field.sendKeys(Keys.CONTROL + "a");
        field.sendKeys(Keys.DELETE);
        
        // Type the text
        field.sendKeys(inputText);

        try {
            // Wait for suggestions to appear - try different locators
            waits.waitForElementVisible(By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown'] | //ul[@class='oxd-autocomplete-dropdown']"));
            
            // Wait for suggestions to load using existing method
            waits.waitForElementVisible(By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown']//div | //ul[@class='oxd-autocomplete-dropdown']//li"));
            
            java.util.List<WebElement> options = driver.findElements(By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown']//div | //ul[@class='oxd-autocomplete-dropdown']//li"));

            LogsUtil.info("Found " + options.size() + " suggestions");

            // Wait for "Searching...." to disappear and real suggestions to appear
            waitForSearchingToDisappear();
            
            // Get updated suggestions after "Searching...." disappeared
            options = driver.findElements(By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown']//div | //ul[@class='oxd-autocomplete-dropdown']//li"));
            
            for (WebElement option : options) {
                String text = option.getText().trim();
                if (!text.equals("Searching....") && !text.isEmpty() && text.equalsIgnoreCase(nameToSelect)) {
                    LogsUtil.info("Selected suggestion: " + text);
                    option.click();
                    return;
                }
            }

            throw new RuntimeException("Option '" + nameToSelect + "' not found in suggestions");
        } catch (Exception e) {
            LogsUtil.info("No suggestions found, typing directly: " + inputText);
            // If suggestions don't appear, just type the text directly
            field.click();
            field.sendKeys(Keys.CONTROL + "a");
            field.sendKeys(Keys.DELETE);
            field.sendKeys(inputText);
        }
    }

    // Helper method to wait for "Searching...." to disappear
    private void waitForSearchingToDisappear() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver1 -> {
                    java.util.List<WebElement> elements = driver1.findElements(By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown']//div | //ul[@class='oxd-autocomplete-dropdown']//li"));
                    for (WebElement element : elements) {
                        if (element.getText().trim().equals("Searching....")) {
                            return false; // Still searching
                        }
                    }
                    return true; // No more "Searching...."
                });
        } catch (Exception e) {
            LogsUtil.info("Timeout waiting for 'Searching....' to disappear");
        }
    }

    // ==================== DATE HANDLING METHODS ====================

    // Select date by sendKeys (expects yyyy-dd-mm format)
    @Step("Selecting date by sendKeys: {formattedDate}")
    public void selectDateBySendKeys(By dateInputLocator, String formattedDate) {
        scrollToElement(dateInputLocator);
        WebElement dateInputField = findElement(dateInputLocator);
        waits.waitForElementVisible(dateInputLocator);
        
        // Click to focus
        dateInputField.click();
        
        // Clear using Ctrl+A then Delete
        dateInputField.sendKeys(Keys.CONTROL + "a");
        dateInputField.sendKeys(Keys.DELETE);
        
        // Send the new date
        dateInputField.sendKeys(formattedDate);
        
        // Press Tab to ensure the date is properly set
        dateInputField.sendKeys(Keys.TAB);
        
        LogsUtil.info("Selected date: " + formattedDate);
    }

    // Select date from calendar picker
    @Step("Selecting date from calendar: {day}/{month}/{year}")
    public void selectDateFromCalendar(By calendarIconLocator, String day, String month, String year) {
        scrollToElement(calendarIconLocator);
        click(calendarIconLocator);
        
        // Wait for calendar to appear
        By calendarLocator = By.cssSelector(".oxd-date-input-calendar");
        waits.waitForElementVisible(calendarLocator);
        
        // Select month
        selectMonthFromCalendar(month);
        
        // Select year
        selectYearFromCalendar(year);
        
        // Select day
        By dayLocator = By.xpath("//div[contains(@class,'oxd-calendar-date') and text()='" + day + "']");
        click(dayLocator);
        
        LogsUtil.info("Selected date from calendar: " + day + "/" + month + "/" + year);
    }

    // Helper method to select month from calendar
    private void selectMonthFromCalendar(String month) {
        By monthDropdownLocator = By.cssSelector(".oxd-calendar-selector-month-selected");
        click(monthDropdownLocator);
        
        java.util.List<WebElement> months = driver.findElements(By.xpath("//div[@role='listbox']//span"));
        boolean monthFound = false;
        
        for (WebElement m : months) {
            if (m.getText().equalsIgnoreCase(month)) {
                m.click();
                monthFound = true;
                break;
            }
        }
        
        if (!monthFound) {
            LogsUtil.warn("Month '" + month + "' not found in dropdown options");
        }
    }

    // Helper method to select year from calendar
    private void selectYearFromCalendar(String year) {
        By yearDropdownLocator = By.cssSelector(".oxd-calendar-selector-year-selected");
        click(yearDropdownLocator);
        
        java.util.List<WebElement> years = driver.findElements(By.xpath("//div[@role='listbox']//span"));
        boolean yearFound = false;
        
        for (WebElement y : years) {
            if (y.getText().equalsIgnoreCase(year)) {
                y.click();
                yearFound = true;
                break;
            }
        }
        
        if (!yearFound) {
            LogsUtil.warn("Year '" + year + "' not found in dropdown options");
        }
    }

    // ==================== FILE ATTACHMENT METHODS ====================

    // Add attachment with comment
    @Step("Adding attachment: {filePath} with comment: {comment}")
    public void addAttachment(By addButtonLocator, By fileInputLocator, By commentInputLocator, By saveButtonLocator, String filePath, String comment) {
        click(addButtonLocator);
        waits.waitForElementVisible(fileInputLocator);
        
        uploadFile(fileInputLocator, filePath);
        
        clearField(commentInputLocator);
        type(commentInputLocator, comment);
        
        click(saveButtonLocator);
        
        LogsUtil.info("Added attachment: " + filePath + " with comment: " + comment);
    }

    // ==================== ELEMENT STATE METHODS ====================

    // Check if element is displayed
    public boolean isDisplayed(By locator) {
        try {
            WebElement element = findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if element is enabled
    public boolean isEnabled(By locator) {
        try {
            WebElement element = findElement(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== SCROLL METHODS ====================

    // Scroll to element
    @Step("Scrolling to the element: {locator}")
    public void scrollToElement(By locator) {
        WebElement element = findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        // Scroll a bit more to ensure element is not covered by topbar
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -100);");
        LogsUtil.info("Scrolled to the element: ", locator.toString());
    }

    // ==================== HELPER METHODS ====================

    // Upload file
    @Step("Uploading file: {filePath} to element: {locator}")
    public void uploadFile(By locator, String filePath) {
        WebElement element = findElement(locator);
        waits.waitForElementClickable(locator);
        element.sendKeys(filePath);
        LogsUtil.info("Uploaded file: ", filePath, " to element: ", locator.toString());
    }

    // Find element (no wait, no logging)
    public WebElement findElement(By locator) {
        LogsUtil.info("Finding element: ", locator.toString());
        return driver.findElement(locator);
    }
} 