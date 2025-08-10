package tests.features;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.*;
import pages.LoginPage;

@Epic("OrangeHRM Application")
@Feature("Authentication")
@Story("User Login")
@Listeners(listeners.TestNGListeners.class)
public class LoginTest {

    private static final String VALID_USERNAME = "Admin1";
    private static final String VALID_PASSWORD = "admin123";
    private GUIDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
    }

    @Test(priority = 1, groups = {"auth", "login", "smoke"})
    @Story("Valid Login")
    @Description("Test successful login with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void validLogin() {
        LoginPage loginPage = new LoginPage(driver);
        
        loginPage.navigateToLoginPage()
                .assertLoginPageDisplayed()
                .login(VALID_USERNAME, VALID_PASSWORD);
    }

    @Test(dataProvider = "invalidLoginData", priority = 2, groups = {"auth", "login"})
    @Story("Invalid Login")
    @Description("Test login with invalid credentials and verify error messages")
    @Severity(SeverityLevel.NORMAL)
    public void invalidLoginTest(String username, String password, String expectedError, String errorType) {
        LoginPage loginPage = new LoginPage(driver);
        
        loginPage.navigateToLoginPage()
                .invalidLogin(username, password);
        
        if ("invalid".equalsIgnoreCase(errorType)) {
            loginPage.assertInvalidCredentialsError(expectedError);
        } else {
            loginPage.assertRequiredFieldError(expectedError);
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][] {
                {"WrongUser", "admin123", "Invalid credentials", "invalid"},
                {"Admin", "wrongpass", "Invalid credentials", "invalid"},
                {"admin", "admin123", "Invalid credentials", "invalid"},
                {"", "admin123", "Required", "required"},
                {"Admin", "", "Required", "required"},
                {"", "", "Required", "required"}
        };
    }
} 
