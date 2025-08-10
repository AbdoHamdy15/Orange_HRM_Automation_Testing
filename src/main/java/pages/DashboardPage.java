package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import utilities.ElementActions;
import utilities.Validations;
import utilities.Waits;

public class DashboardPage {

    // Variables
    private final GUIDriver driver;
    private final ElementActions elementActions;
    private final Validations validations;
    private final Waits waits;

    // Locators
    private final By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private final By adminMenu = By.xpath("//a[contains(@href,'admin')]");
    private final By pimMenu = By.xpath("//a[contains(@href,'pim')]");
    private final By leaveMenu = By.xpath("//a[contains(@href,'leave')]");
    private final By timeMenu = By.xpath("//a[contains(@href,'time')]");
    private final By recruitmentMenu = By.xpath("//a[contains(@href,'recruitment')]");
    private final By myInfoMenu = By.xpath("//a[@href='/web/index.php/pim/viewMyDetails'] | //span[text()='My Info']/parent::a | //a[contains(@class,'oxd-main-menu-item')]//span[contains(@class,'oxd-main-menu-item--icon--my-info')]/parent::a");
    private final By performanceMenu = By.xpath("//a[contains(@href,'Performance')]");

    // My Info Tabs
    private final By contactDetailsTab = By.xpath("//a[text()='Contact Details']");
    private final By dependentsTab = By.xpath("//a[text()='Dependents']");
    private final By emergencyContactsTab = By.xpath("//a[text()='Emergency Contacts']");
    private final By immigrationTab = By.xpath("//a[text()='Immigration']");
    private final By membershipsTab = By.xpath("//a[text()='Memberships']");
    private final By qualificationsTab = By.xpath("//a[text()='Qualifications']");

    // Logout
    private final By userDropdown = By.xpath("//span[@class='oxd-userdropdown-tab']");
    private final By logoutOption = By.xpath("//a[text()='Logout']");

    // Constructor
    public DashboardPage(GUIDriver driver) {
        this.driver = driver;
        this.elementActions = driver.element();
        this.validations = driver.validate();
        this.waits = new Waits(driver.get());
    }

    // Validations
    @Step("Assert dashboard is displayed")
    public DashboardPage assertDashboardDisplayed() {
        validations.validateTrue(elementActions.isDisplayed(dashboardHeader), "Dashboard should be displayed");
        return this;
    }

    // Navigation methods
    @Step("Navigate to Admin page")
    public AdminPage goToAdmin() {
        waits.navigateToPage(adminMenu, "Admin");
        return new AdminPage(driver);
    }

    @Step("Navigate to PIM page")
    public PIMPage goToPIM() {
        waits.navigateToPage(pimMenu, "PIM");
        return new PIMPage(driver);
    }

    @Step("Navigate to Leave page")
    public LeavePage goToLeave() {
        waits.navigateToPage(leaveMenu, "Leave");
        return new LeavePage(driver);
    }

    @Step("Navigate to Time page")
    public TimePage goToTime() {
        waits.navigateToPage(timeMenu, "Time");
        return new TimePage(driver);
    }

    @Step("Navigate to Recruitment page")
    public RecruitmentPage goToRecruitment() {
        waits.navigateToPage(recruitmentMenu, "Recruitment");
        return new RecruitmentPage(driver);
    }

    @Step("Navigate to My Info page")
    public pages.myinfo.PersonalDetailsPage goToMyInfo() {
        waits.navigateToPage(myInfoMenu, "Personal Details");
        return new pages.myinfo.PersonalDetailsPage(driver);
    }

    @Step("Navigate to Contact Details")
    public pages.myinfo.ContactDetailsPage navigateToContactDetails() {
        waits.waitForElementClickable(myInfoMenu);
        elementActions.click(myInfoMenu);
        waits.waitForElementClickable(contactDetailsTab);
        elementActions.click(contactDetailsTab);
        return new pages.myinfo.ContactDetailsPage(driver);
    }

    @Step("Navigate to Dependents")
    public pages.myinfo.DependentsPage navigateToDependents() {
        waits.waitForElementClickable(myInfoMenu);
        elementActions.click(myInfoMenu);
        waits.waitForElementClickable(dependentsTab);
        elementActions.click(dependentsTab);
        return new pages.myinfo.DependentsPage(driver);
    }

    @Step("Navigate to Emergency Contacts")
    public pages.myinfo.EmergencyContactsPage navigateToEmergencyContacts() {
        waits.waitForElementClickable(myInfoMenu);
        elementActions.click(myInfoMenu);
        waits.waitForElementClickable(emergencyContactsTab);
        elementActions.click(emergencyContactsTab);
        return new pages.myinfo.EmergencyContactsPage(driver);
    }

    @Step("Navigate to Immigration")
    public pages.myinfo.ImmigrationPage navigateToImmigration() {
        waits.waitForElementClickable(myInfoMenu);
        elementActions.click(myInfoMenu);
        waits.waitForElementClickable(immigrationTab);
        elementActions.click(immigrationTab);
        return new pages.myinfo.ImmigrationPage(driver);
    }

    @Step("Navigate to Memberships")
    public pages.myinfo.MembershipsPage navigateToMemberships() {
        waits.waitForElementClickable(myInfoMenu);
        elementActions.click(myInfoMenu);
        waits.waitForElementClickable(membershipsTab);
        elementActions.click(membershipsTab);
        return new pages.myinfo.MembershipsPage(driver);
    }

    @Step("Navigate to Qualifications")
    public pages.myinfo.QualificationsPage navigateToQualifications() {
        waits.waitForElementClickable(myInfoMenu);
        elementActions.click(myInfoMenu);
        waits.waitForElementClickable(qualificationsTab);
        elementActions.click(qualificationsTab);
        return new pages.myinfo.QualificationsPage(driver);
    }

    @Step("Navigate to Performance page")
    public PerformancePage goToPerformance() {
        waits.navigateToPage(performanceMenu, "Performance");
        return new PerformancePage(driver);
    }

    @Step("Logout from the application")
    public LoginPage logout() {
        elementActions.click(userDropdown);
        elementActions.click(logoutOption);
        return new LoginPage(driver);
    }

    // Legacy method for backward compatibility
    public boolean isAt() {
        return elementActions.isDisplayed(dashboardHeader);
    }
}
