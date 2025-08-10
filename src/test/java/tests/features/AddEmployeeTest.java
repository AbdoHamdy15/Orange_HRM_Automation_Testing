package tests.features;

import drivers.GUIDriver;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.PIMPage;
import pages.AddEmployeePage;
import pages.LoginPage;
import utilities.JsonUtils;
import enums.Status;
import java.util.HashMap;
import java.util.List;

@Epic("OrangeHRM Application")
@Feature("Employee Management")
@Story("Add Employee")
public class AddEmployeeTest {
    
    private GUIDriver driver;
    private AddEmployeePage addEmployeePage;

    public AddEmployeeTest(HashMap<String, String> employeeData) {
    }

    @BeforeMethod
    public void setup() {
        driver = new GUIDriver();
        
        // Login and navigate to Add Employee page
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboard = loginPage.navigateToLoginPage()
                .login("Admin", "admin123");
        
        PIMPage pimPage = dashboard.goToPIM();
        addEmployeePage = pimPage.clickAddEmployee();
    }

    @Test(dataProvider = "addEmployeeData", groups = {"management", "employee", "smoke", "sanity"})
    @Story("Add New Employee")
    @Description("Test adding employee with valid information from data provider")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddEmployee(HashMap<String, String> employee) {
        String description = employee.getOrDefault("description", "");
        
        // Fill the form using Method Chaining
        addEmployeePage.enterFirstName(employee.get("firstName"))
                       .enterMiddleName(employee.get("middleName"))
                       .enterLastName(employee.get("lastName"))
                       .enterEmployeeId(employee.get("employeeId"));
        
        // Upload picture if provided
        if (employee.get("imagePath") != null && !employee.get("imagePath").isEmpty()) {
            addEmployeePage.uploadPicture(employee.get("imagePath"));
        }

        // Handle login details if enabled
        if (Boolean.parseBoolean(String.valueOf(employee.get("withLogin")))) {
            addEmployeePage.enableLoginDetails()
                           .enterUsername(employee.get("username"))
                           .enterPassword(employee.get("password"))
                           .enterConfirmPassword(employee.getOrDefault("confirmPassword", employee.get("password")))
                           .setStatus(Status.valueOf(employee.get("status")));
        }

        // Click save and get PersonalDetailsPage
        pages.myinfo.PersonalDetailsPage personalDetailsPage = addEmployeePage.clickSave();

        // Assertions
        String expectedError = employee.getOrDefault("expectedError", "");
        if (expectedError != null && !expectedError.isEmpty()) {
            addEmployeePage.assertSpecificErrorDisplayed(expectedError);
        } else {
            addEmployeePage.assertSuccessToastDisplayed();
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.teardown();
        }
    }

    @DataProvider(name = "addEmployeeData")
    public Object[][] addEmployeeData() {
        List<HashMap<String, String>> data = JsonUtils.readJsonDataAsList("src/test/resources/addEmployeeData.json");
        return data.stream().map(emp -> new Object[]{emp}).toArray(Object[][]::new);
    }
}
