package abstractComponents;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractComponent {

    WebDriver driver;
    WebDriverWait wait;

    public AbstractComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(6));
    }

    public void waitForElementToAppear(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToAppear(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementToDisappear(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void clearFieldReliably(WebElement field) {
        waitForElementToAppear(field);
        field.click();
        field.sendKeys(Keys.CONTROL + "a");
        field.sendKeys(Keys.DELETE);
    }

    public void selectRadioOptionByLabel(String optionLabel) {
        By radioOption = By.xpath("//label[.//span[text()='" + optionLabel + "']]//input[@type='radio']");
        WebElement radio = driver.findElement(radioOption);
        if (!radio.isSelected()) {
            radio.click();
        }
    }

    public void enterAutoSuggestField(WebElement inputField, String inputText, String nameToSelect) {
        clearFieldReliably(inputField);
        inputField.sendKeys(inputText);

        try {
            // Wait for suggestions to appear - try different locators
            waitForElementToAppear(By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown'] | //ul[@class='oxd-autocomplete-dropdown']"));
            
            // Wait for suggestions to load (not just appear)
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown']//div | //ul[@class='oxd-autocomplete-dropdown']//li")));
            
            List<WebElement> options = driver.findElements(By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown']//div | //ul[@class='oxd-autocomplete-dropdown']//li"));

            System.out.println("Found " + options.size() + " suggestions:");
            for (WebElement option : options) {
                System.out.println("  - " + option.getText());
            }

            // Wait for "Searching...." to disappear and real suggestions to appear
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown']//div | //ul[@class='oxd-autocomplete-dropdown']//li"), 
                "Searching...."
            )));
            
            // Get updated suggestions after "Searching...." disappeared
            options = driver.findElements(By.xpath("//div[@role='option'] | //div[@class='oxd-autocomplete-dropdown']//div | //ul[@class='oxd-autocomplete-dropdown']//li"));
            
            for (WebElement option : options) {
                String text = option.getText().trim();
                if (!text.equals("Searching....") && !text.isEmpty()) {
                    System.out.println("  - " + text);
                }
            }

            for (WebElement option : options) {
                if (option.getText().trim().equalsIgnoreCase(nameToSelect)) {
                    System.out.println("Selected: " + option.getText());
                    option.click();
                    return;
                }
            }

            throw new RuntimeException("Employee '" + nameToSelect + "' not found in suggestions.");
        } catch (Exception e) {
            System.out.println("No suggestions found, typing directly: " + inputText);
            // If suggestions don't appear, just type the text directly
            clearFieldReliably(inputField);
            inputField.sendKeys(inputText);
        }
    }

    public void selectDateBySendKeys(WebElement dateInputField, String formattedDate) {
        // Expects formattedDate in yyyy-MM-dd format (e.g. 2025-12-31)
        waitForElementToAppear(dateInputField);

        // Click first to ensure the field is focused and editable
        dateInputField.click();

        // Clear using Ctrl+A then Delete to ensure complete clearing
        dateInputField.sendKeys(Keys.CONTROL + "a");
        dateInputField.sendKeys(Keys.DELETE);

        // Send the new date
        dateInputField.sendKeys(formattedDate);

        // Press Tab to ensure the date is properly set
        dateInputField.sendKeys(Keys.TAB);
    }



    public void selectFromDropdown(WebElement dropdown, String optionText) {
        dropdown.click();
        By optionLocator = By.xpath("//div[@role='listbox']//span[text()='" + optionText + "']");
        waitForElementToAppear(optionLocator);
        driver.findElement(optionLocator).click();
    }

    public void selectDateFromCalendar(WebElement calendarIcon, String day, String month, String year) {
        calendarIcon.click();
        waitForElementToAppear(By.cssSelector(".oxd-date-input-calendar"));

        // Select month from dropdown
        WebElement monthDropdown = driver.findElement(By.cssSelector(".oxd-calendar-selector-month-selected"));
        monthDropdown.click();
        java.util.List<org.openqa.selenium.WebElement> months = driver.findElements(By.xpath("//div[@role='listbox']//span"));
        boolean monthFound = false;
        for (org.openqa.selenium.WebElement m : months) {
            if (m.getText().equalsIgnoreCase(month)) {
                m.click();
                monthFound = true;
                break;
            }
        }
        if (!monthFound) {
            System.out.println("WARNING: Month '" + month + "' not found in dropdown options!");
        }

        // Select year from dropdown
        WebElement yearDropdown = driver.findElement(By.cssSelector(".oxd-calendar-selector-year-selected"));
        yearDropdown.click();
        java.util.List<org.openqa.selenium.WebElement> years = driver.findElements(By.xpath("//div[@role='listbox']//span"));
        boolean yearFound = false;
        for (org.openqa.selenium.WebElement y : years) {
            if (y.getText().equalsIgnoreCase(year)) {
                y.click();
                yearFound = true;
                break;
            }
        }
        if (!yearFound) {
            System.out.println("WARNING: Year '" + year + "' not found in dropdown options!");
        }

        // Select the day
        WebElement dayButton = driver.findElement(By.xpath("//div[contains(@class,'oxd-calendar-date') and text()='" + day + "']"));
        dayButton.click();
    }


    public void addAttachment(WebElement addButton, WebElement fileInput, WebElement commentInput, WebElement saveButton, String filePath, String comment) {
        addButton.click();
        waitForElementToAppear(fileInput);

        fileInput.sendKeys(filePath);

        clearFieldReliably(commentInput);
        commentInput.sendKeys(comment);

        saveButton.click();
    }

    public boolean isErrorMessageDisplayed(String errorText) {
        try {
            List<WebElement> errorElems = driver.findElements(By.xpath("//*[contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-text--toast-message') or contains(@class,'error') or contains(@class,'invalid')]"));
            for (WebElement elem : errorElems) {
                if (elem.isDisplayed() && elem.getText().trim().equalsIgnoreCase(errorText)) {
                    return true;
                }
            }
            List<WebElement> allElems = driver.findElements(By.xpath("//*[text()='" + errorText + "']"));
            for (WebElement elem : allElems) {
                if (elem.isDisplayed()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String waitForToastAndGetMessage() {
        By toastLocator = By.cssSelector(".oxd-toast-content");
        try {
            WebDriverWait toastWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement toastElement = toastWait.until(ExpectedConditions.visibilityOfElementLocated(toastLocator));
            return toastElement.getText().trim();
        } catch (Exception e) {
            return null;
        }
    }
}
