package tests;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.AdminPage;
import pages.AddUserPage;
import java.util.HashMap;
import java.util.List;

public class AddUserTest extends BaseTest {

    private AddUserPage addUserPage;

    @BeforeMethod
    public void navigateToAddUser() {
        DashboardPage dashboard = loginPage.login("Admin", "admin123");
        AdminPage adminPage = dashboard.goToAdmin();
        addUserPage = adminPage.clickAdd();
    }

    @Test(dataProvider = "addUserData")
    public void testAddUser(HashMap<String, String> user) throws InterruptedException {
        // Fill the form
        addUserPage.selectUserRole(user.get("userRole"));
        addUserPage.enterEmployeeName(user.get("employeeNameInput"), user.get("employeeNameToSelect"));
        addUserPage.selectStatus(user.get("status"));
        addUserPage.enterUsername(user.get("username"));
        addUserPage.enterPassword(user.get("password"));
        addUserPage.enterConfirmPassword(user.get("confirmPassword"));
        addUserPage.clickSave();

        // Assertions
        String expectedError = user.getOrDefault("expectedError", "");
        if (expectedError != null && !expectedError.isEmpty()) {
            String actualError = null;
            switch (expectedError) {
                case "Required":
                    actualError = "Required";
                    break;
                case "UsernameExists":
                    actualError = "Already exists";
                    break;
                case "UsernameMinLength":
                    actualError = "Should be at least 5 characters";
                    break;
                case "UsernameMaxLength":
                    actualError = "Should not exceed 40 characters";
                    break;
                case "PasswordMinLength":
                    actualError = "Should have at least 7 characters";
                    break;
                case "PasswordMaxLength":
                    actualError = "Should not exceed 64 characters";
                    break;
                case "PasswordMismatch":
                    actualError = "Passwords do not match";
                    break;
                case "PasswordMustContainNumber":
                    actualError = "Your password must contain minimum 1 number";
                    break;
                case "PasswordMustContainLowercase":
                    actualError = "Your password must contain minimum 1 lower-case letter";
                    break;
                case "Invalid":
                    actualError = "Invalid";
                    break;
                default:
                    actualError = expectedError;
            }
            assert addUserPage.isErrorDisplayed(actualError) : "Expected error message: " + actualError;
        } else {
            assert addUserPage.isSuccessToastDisplayed() : "Success toast should be displayed";
        }
    }

    @DataProvider(name = "addUserData")
    public Object[][] addUserData() {
        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "/src/test/resources/addUserData.json");
        return data.stream().map(emp -> new Object[]{emp}).toArray(Object[][]::new);
    }
} 