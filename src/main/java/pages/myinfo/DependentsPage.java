package pages.myinfo;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DependentsPage extends AbstractComponent {

    WebDriver driver;

    public DependentsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void clickAddDependent() {
        WebElement addDependentButton = driver.findElement(By.xpath("//h6[text()='Assigned Dependents']/following::button[normalize-space()='Add'][1]"));
        waitForElementToAppear(addDependentButton);
        addDependentButton.click();
    }

    public void fillDependentDetails(String name, String relationship, String day, String month, String year) {
        WebElement nameInput = driver.findElement(By.xpath("//label[text()='Name']/following::input[1]"));
        waitForElementToAppear(nameInput);
        clearFieldReliably(nameInput);
        nameInput.sendKeys(name);
        
        WebElement relationshipDropdown = driver.findElement(By.xpath("//label[text()='Relationship']/following::div[contains(@class,'oxd-select-wrapper')]"));
        waitForElementToAppear(relationshipDropdown);
        selectFromDropdown(relationshipDropdown, relationship);
        
        WebElement dobInput = driver.findElement(By.xpath("//label[text()='Date of Birth']/following::input[1]"));
        waitForElementToAppear(dobInput);
        String formattedDate = year + "-" + month + "-" + day;
        selectDateBySendKeys(dobInput, formattedDate);
    }

    public void clickSave() {
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        waitForElementToAppear(saveButton);
        saveButton.click();
    }

    public void addDependent(String name, String relationship, String day, String month, String year) {
        clickAddDependent();
        fillDependentDetails(name, relationship, day, month, year);
        clickSave();
    }

    public void uploadAttachment(String filePath, String comment) {
        WebElement addAttachmentButton = driver.findElement(By.xpath("//h6[text()='Attachments']/following::button[normalize-space()='Add'][1]"));
        WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
        WebElement commentInput = driver.findElement(By.xpath("//label[text()='Comment']/following::textarea[1]"));
        WebElement attachmentSaveButton = driver.findElement(By.xpath("//button[normalize-space()='Save']"));
        waitForElementToAppear(addAttachmentButton);
        addAttachment(addAttachmentButton, fileInput, commentInput, attachmentSaveButton, filePath, comment);
    }
    
    public String waitForToastAndGetMessage() {
        return super.waitForToastAndGetMessage();
    }
    
    public boolean isErrorMessageDisplayed(String errorText) {
        return super.isErrorMessageDisplayed(errorText);
    }
}
