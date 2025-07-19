package pages.myinfo;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EmergencyContactsPage extends AbstractComponent {

    WebDriver driver;

    public EmergencyContactsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    // --- Actions ---

    public void clickAddEmergencyContact() {
        WebElement addEmergencyButton = driver.findElement(By.xpath("//h6[text()='Assigned Emergency Contacts']/following::button[normalize-space()='Add'][1]"));
        waitForElementToAppear(addEmergencyButton);
        addEmergencyButton.click();
    }

    public void fillEmergencyContact(String name, String relationship, String homePhone, String mobile, String workPhone) {
        WebElement nameInput = driver.findElement(By.xpath("//label[text()='Name']/following::input[1]"));
        waitForElementToAppear(nameInput);
        clearFieldReliably(nameInput);
        nameInput.sendKeys(name);

        WebElement relationshipInput = driver.findElement(By.xpath("//label[text()='Relationship']/following::input[1]"));
        waitForElementToAppear(relationshipInput);
        clearFieldReliably(relationshipInput);
        relationshipInput.sendKeys(relationship);

        WebElement homePhoneInput = driver.findElement(By.xpath("//label[text()='Home Telephone']/following::input[1]"));
        waitForElementToAppear(homePhoneInput);
        clearFieldReliably(homePhoneInput);
        homePhoneInput.sendKeys(homePhone);

        WebElement mobilePhoneInput = driver.findElement(By.xpath("//label[text()='Mobile']/following::input[1]"));
        waitForElementToAppear(mobilePhoneInput);
        clearFieldReliably(mobilePhoneInput);
        mobilePhoneInput.sendKeys(mobile);

        WebElement workPhoneInput = driver.findElement(By.xpath("//label[text()='Work Telephone']/following::input[1]"));
        waitForElementToAppear(workPhoneInput);
        clearFieldReliably(workPhoneInput);
        workPhoneInput.sendKeys(workPhone);
    }

    public void clickSave() {
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        waitForElementToAppear(saveButton);
        saveButton.click();
    }

    public void addEmergencyContact(String name, String relationship, String homePhone, String mobile, String workPhone) {
        clickAddEmergencyContact();
        fillEmergencyContact(name, relationship, homePhone, mobile, workPhone);
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
