package pages.myinfo;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import enums.DocumentType;

public class ImmigrationPage extends AbstractComponent {

    WebDriver driver;

    public ImmigrationPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    // ========== METHODS ==========

    public void setDocumentType(DocumentType documentType) {
        // If document type is PASSPORT, don't do anything since it's the default
        if (documentType == DocumentType.PASSPORT) {
            System.out.println("Document type is PASSPORT (default), skipping selection");
            return;
        }
        selectRadioOptionByLabel(documentType.getLabel());
    }

    public void selectIssuedBy(String country) {
        WebElement issuedByDropdown = driver.findElement(By.xpath("//label[text()='Issued By']/following::div[contains(@class,'oxd-select-wrapper')]"));
        waitForElementToAppear(issuedByDropdown);
        selectFromDropdown(issuedByDropdown, country);
    }

    public void setEligibleReviewDate(String day, String month, String year) {
        WebElement reviewDateInput = driver.findElement(By.xpath("//label[text()='Eligible Review Date']/following::input[1]"));
        waitForElementToAppear(reviewDateInput);
        String formattedDate = year + "-" + month + "-" + day;
        selectDateBySendKeys(reviewDateInput, formattedDate);
    }

    public void clickSave() {
        WebElement saveButton = driver.findElement(By.xpath("//button[normalize-space()='Save']"));
        waitForElementToAppear(saveButton);
        saveButton.click();
    }

    public void uploadAttachment(String filePath, String comment) {
        WebElement addAttachmentButton = driver.findElement(By.xpath("//h6[text()='Attachments']/following::button[normalize-space()='Add'][1]"));
        WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
        WebElement commentInput = driver.findElement(By.xpath("//label[text()='Comment']/following::textarea[1]"));
        WebElement attachmentSaveButton = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//button[normalize-space()='Save']"));
        waitForElementToAppear(addAttachmentButton);
        addAttachment(addAttachmentButton, fileInput, commentInput, attachmentSaveButton, filePath, comment);
    }

    public void clickAddImmigration() {
        WebElement addButton = driver.findElement(By.xpath("//h6[text()='Assigned Immigration Records']/following::button[normalize-space()='Add'][1]"));
        waitForElementToAppear(addButton);
        addButton.click();
    }

    public void fillImmigrationDetails(String documentType, String documentNumber, String issuedDay, String issuedMonth, String issuedYear, String expiryDay, String expiryMonth, String expiryYear) {
        // Set document type
        if (documentType.equals("Passport")) {
            setDocumentType(DocumentType.PASSPORT);
        } else if (documentType.equals("Visa")) {
            setDocumentType(DocumentType.VISA);
        }
        
        // Fill document number
        WebElement documentNumberInput = driver.findElement(By.xpath("//label[text()='Number']/following::input[1]"));
        waitForElementToAppear(documentNumberInput);
        clearFieldReliably(documentNumberInput);
        documentNumberInput.sendKeys(documentNumber);
        
        // Set issued date
        WebElement issuedDateInput = driver.findElement(By.xpath("//label[text()='Issued Date']/following::input[1]"));
        waitForElementToAppear(issuedDateInput);
        String issuedDate = issuedYear + "-" + issuedMonth + "-" + issuedDay;
        selectDateBySendKeys(issuedDateInput, issuedDate);
        
        // Set expiry date
        WebElement expiryDateInput = driver.findElement(By.xpath("//label[text()='Expiry Date']/following::input[1]"));
        waitForElementToAppear(expiryDateInput);
        String expiryDate = expiryYear + "-" + expiryMonth + "-" + expiryDay;
        selectDateBySendKeys(expiryDateInput, expiryDate);
    }

    public void addImmigration(String documentType, String documentNumber, String issuedDay, String issuedMonth, String issuedYear, String expiryDay, String expiryMonth, String expiryYear) {
        clickAddImmigration();
        fillImmigrationDetails(documentType, documentNumber, issuedDay, issuedMonth, issuedYear, expiryDay, expiryMonth, expiryYear);
        clickSave();
    }
    
    public String waitForToastAndGetMessage() {
        return super.waitForToastAndGetMessage();
    }
    
    public boolean isErrorMessageDisplayed(String errorText) {
        return super.isErrorMessageDisplayed(errorText);
    }

}
