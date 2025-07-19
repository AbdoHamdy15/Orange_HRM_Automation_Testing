package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;
import pages.DashboardPage;
import base.BaseTest;

public class LoginTest extends BaseTest {

    private static final String VALID_USERNAME = "Admin";
    private static final String VALID_PASSWORD = "admin123";

    @Test(priority = 1)
    public void validLogin() {
        DashboardPage dashboard = loginPage.login(VALID_USERNAME, VALID_PASSWORD);
        Assert.assertTrue(dashboard.isAt(), "Dashboard should be visible after successful login");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        // username, password, expectedError, scenario description
        String[][] rawData = new String[][] {
            {"WrongUser", "admin123", "Invalid credentials", "Invalid username"},
            {"Admin", "wrongpass", "Invalid credentials", "Invalid password"},
            {"", "admin123", "Required", "Empty username"},
            {"Admin", "", "Required", "Empty password"},
            {"", "", "Required", "Empty username and password"}
        };
        return java.util.Arrays.stream(rawData)
            .map(arr -> {
                java.util.HashMap<String, String> map = new java.util.HashMap<>();
                map.put("username", arr[0]);
                map.put("password", arr[1]);
                map.put("expectedError", arr[2]);
                map.put("scenario", arr[3]);
                return new Object[]{map};
            }).toArray(Object[][]::new);
    }

    @Test(dataProvider = "invalidLoginData", priority = 2)
    public void invalidLoginTest(java.util.HashMap<String, String> data) {
        System.out.println("Scenario: " + data.get("scenario"));

        if (data.get("username") != null && !data.get("username").isEmpty()) loginPage.enterUsername(data.get("username"));
        if (data.get("password") != null && !data.get("password").isEmpty()) loginPage.enterPassword(data.get("password"));
        loginPage.clickLogin();

        String expectedError = data.get("expectedError");
        String username = data.get("username");
        String password = data.get("password");
        if (username.isEmpty() && password.isEmpty()) {
            Assert.assertEquals(loginPage.getUsernameRequiredMessage().trim(), expectedError, "Username required message should be shown when username is empty");
            Assert.assertEquals(loginPage.getPasswordRequiredMessage().trim(), expectedError, "Password required message should be shown when password is empty");
        } else if (username.isEmpty()) {
            Assert.assertEquals(loginPage.getUsernameRequiredMessage().trim(), expectedError, "Username required message should be shown when username is empty");
        } else if (password.isEmpty()) {
            Assert.assertEquals(loginPage.getPasswordRequiredMessage().trim(), expectedError, "Password required message should be shown when password is empty");
        } else {
            Assert.assertEquals(loginPage.getErrorMessage().trim(), expectedError, "Invalid credentials message should be shown for invalid login");
        }
    }
} 