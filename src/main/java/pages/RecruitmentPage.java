package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RecruitmentPage extends AbstractComponent {

    WebDriver driver;

    public RecruitmentPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//label[text()='Job Title']/following::div[@class='oxd-select-wrapper'][1]")
    WebElement jobTitleDropdown;

    @FindBy(xpath = "//label[text()='Vacancy']/following::div[@class='oxd-select-wrapper'][1]")
    WebElement vacancyDropdown;

    @FindBy(xpath = "//label[text()='Hiring Manager']/following::div[@class='oxd-select-wrapper'][1]")
    WebElement hiringManagerDropdown;

    @FindBy(xpath = "//label[text()='Status']/following::div[@class='oxd-select-wrapper'][1]")
    WebElement statusDropdown;

    @FindBy(xpath = "//label[text()='Candidate Name']/following::input[1]")
    WebElement candidateNameInput;

    @FindBy(xpath = "//input[@placeholder='From']")
    WebElement fromDateInput;

    @FindBy(xpath = "//input[@placeholder='To']")
    WebElement toDateInput;

    @FindBy(xpath = "//button[normalize-space()='Search']")
    WebElement searchButton;

    @FindBy(xpath = "//button[normalize-space()='Reset']")
    WebElement resetButton;

    @FindBy(xpath = "//button[normalize-space()='Add']")
    WebElement addButton;

    @FindBy(xpath = "//input[@placeholder='From']/following::i[contains(@class, 'oxd-date-input-icon')][1]")
    WebElement fromCalendarIcon;

    @FindBy(xpath = "//input[@placeholder='To']/following::i[contains(@class, 'oxd-date-input-icon')][1]")
    WebElement toCalendarIcon;

    public void selectJobTitle(String title) {
        selectFromDropdown(jobTitleDropdown, title);
    }

    public void selectVacancy(String vacancy) {
        selectFromDropdown(vacancyDropdown, vacancy);
    }

    public void selectHiringManager(String manager) {
        selectFromDropdown(hiringManagerDropdown, manager);
    }

    public void selectStatus(String status) {
        selectFromDropdown(statusDropdown, status);
    }

    public void enterCandidateName(String inputText, String nameToSelect) {
        enterAutoSuggestField(candidateNameInput, inputText, nameToSelect);
    }

    public void setFromDate(String date) {
        selectDateBySendKeys(fromDateInput, date);
    }

    public void setToDate(String date) {
        selectDateBySendKeys(toDateInput, date);
    }

    public void clickSearch() {
        searchButton.click();
    }

    public void clickReset() {
        resetButton.click();
    }

    public AddCandidatePage clickAdd() {
        addButton.click();
        return new AddCandidatePage(driver);
    }

    public void searchCandidates(String jobTitle, String vacancy, String manager, String status,
                                 String candidateInput, String candidateName,
                                 String fromDay, String fromMonth, String fromYear,
                                 String toDay, String toMonth, String toYear) {

        selectJobTitle(jobTitle);
        selectVacancy(vacancy);
        selectHiringManager(manager);
        selectStatus(status);
        enterCandidateName(candidateInput, candidateName);
        setFromDate(fromDay + "-" + fromMonth + "-" + fromYear);
        setToDate(toDay + "-" + toMonth + "-" + toYear);
        clickSearch();
    }

    // Checks if the 'Invalid' error is displayed under the candidate name field
    public boolean isInvalidErrorDisplayedForCandidateName() {
        try {
            WebElement errorElem = driver.findElement(By.xpath("//label[text()='Candidate Name']/following::span[contains(@class,'oxd-input-field-error-message')][1]"));
            return errorElem.isDisplayed() && errorElem.getText().trim().equalsIgnoreCase("Invalid");
        } catch (Exception e) {
            return false;
        }
    }

    // Checks if the search results table is displayed
    public boolean isSearchResultsTableDisplayed() {
        try {
            WebElement table = driver.findElement(By.cssSelector(".oxd-table"));
            return table.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
