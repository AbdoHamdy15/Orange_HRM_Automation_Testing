package tests.features;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.AdminPage;
import pages.AddUserPage;
import utilities.JsonUtils;

import java.util.HashMap;
import java.util.List;

@Epic("OrangeHRM Application")
@Feature("User Management")
@Story("Add User")
public class AddUserTest {

    private GUIDriver driver;
    private AddUserPage addUserPage;

    public AddUserTest(HashMap<String, String> userData) {
    }

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login first
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        // Navigate to Add User page
        AdminPage adminPage = dashboard.goToAdmin();
        addUserPage = adminPage.clickAdd();
    }

    @Test(dataProvider = "addUserData", groups = {"management", "user"})
    @Story("Add New User")
    @Description("Test adding user with valid information from data provider")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddUser(HashMap<String, String> user) {
        // Fill the form using Method Chaining
        addUserPage.selectUserRole(user.get("userRole"))
                   .enterEmployeeName(user.get("employeeNameInput"), user.get("employeeNameToSelect"))
                   .selectStatus(user.get("status"))
                   .enterUsername(user.get("username"))
                   .enterPassword(user.get("password"))
                   .enterConfirmPassword(user.get("confirmPassword"))
                   .clickSave();

        // Assertions
        String expectedError = user.getOrDefault("expectedError", "");
        if (expectedError != null && !expectedError.isEmpty()) {
            addUserPage.assertSpecificErrorDisplayed(expectedError);
        } else {
            addUserPage.assertSuccessToastDisplayed();
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }

    @DataProvider(name = "addUserData")
    public Object[][] addUserData() {
        List<HashMap<String, String>> data = JsonUtils.readJsonDataAsList("src/test/resources/addUserData.json");
        return data.stream().map(emp -> new Object[]{emp}).toArray(Object[][]::new);
    }


} 
