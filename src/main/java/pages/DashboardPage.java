package pages;

import abstractComponents.AbstractComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.myinfo.PersonalDetailsPage;

public class DashboardPage extends AbstractComponent {

    WebDriver driver;

    public DashboardPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//h6[text()='Dashboard']")
    WebElement dashboardHeader;

    @FindBy(xpath = "//span[text()='Admin']")
    WebElement adminMenu;

    @FindBy(xpath = "//span[text()='PIM']")
    WebElement pimMenu;

    @FindBy(xpath = "//span[text()='Leave']")
    WebElement leaveMenu;

    @FindBy(xpath = "//span[text()='Time']")
    WebElement timeMenu;

    @FindBy(xpath = "//span[text()='Recruitment']")
    WebElement recruitmentMenu;

    @FindBy(xpath = "//span[text()='My Info']")
    WebElement myInfoMenu;

    @FindBy(xpath = "//span[text()='Performance']")
    WebElement performanceMenu;

    // My Info Tabs
    @FindBy(xpath = "//a[text()='Contact Details']")
    WebElement contactDetailsTab;

    @FindBy(xpath = "//a[text()='Dependents']")
    WebElement dependentsTab;

    @FindBy(xpath = "//a[text()='Emergency Contacts']")
    WebElement emergencyContactsTab;

    @FindBy(xpath = "//a[text()='Immigration']")
    WebElement immigrationTab;

    @FindBy(xpath = "//a[text()='Memberships']")
    WebElement membershipsTab;

    @FindBy(xpath = "//a[text()='Qualifications']")
    WebElement qualificationsTab;

    public boolean isAt() {
        waitForElementToAppear(dashboardHeader);
        return dashboardHeader.isDisplayed();
    }

    public AdminPage goToAdmin() {
        adminMenu.click();
        return new AdminPage(driver);
    }

    public PIMPage goToPIM() {
        pimMenu.click();
        return new PIMPage(driver);
    }

    public void goToLeave() {
        leaveMenu.click();
    }

    public void goToTime() {
        timeMenu.click();
    }

    public RecruitmentPage goToRecruitment() {
        recruitmentMenu.click();
        return new RecruitmentPage(driver);
    }

    public PersonalDetailsPage goToMyInfo() {
        myInfoMenu.click();
        return new PersonalDetailsPage(driver);
    }
    
    public pages.myinfo.ContactDetailsPage navigateToContactDetails() {
        myInfoMenu.click();
        contactDetailsTab.click();
        return new pages.myinfo.ContactDetailsPage(driver);
    }
    
    public pages.myinfo.DependentsPage navigateToDependents() {
        myInfoMenu.click();
        dependentsTab.click();
        return new pages.myinfo.DependentsPage(driver);
    }
    
    public pages.myinfo.EmergencyContactsPage navigateToEmergencyContacts() {
        myInfoMenu.click();
        emergencyContactsTab.click();
        return new pages.myinfo.EmergencyContactsPage(driver);
    }
    
    public pages.myinfo.ImmigrationPage navigateToImmigration() {
        myInfoMenu.click();
        immigrationTab.click();
        return new pages.myinfo.ImmigrationPage(driver);
    }
    
    public pages.myinfo.MembershipsPage navigateToMemberships() {
        myInfoMenu.click();
        membershipsTab.click();
        return new pages.myinfo.MembershipsPage(driver);
    }
    
    public pages.myinfo.QualificationsPage navigateToQualifications() {
        myInfoMenu.click();
        qualificationsTab.click();
        return new pages.myinfo.QualificationsPage(driver);
    }

    public void goToPerformance() {
        performanceMenu.click();
    }
}
