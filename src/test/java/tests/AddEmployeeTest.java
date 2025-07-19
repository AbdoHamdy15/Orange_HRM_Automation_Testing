package tests;

import base.BaseTest;
import enums.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.PIMPage;
import pages.AddEmployeePage;
import java.util.HashMap;
import java.util.List;
import org.testng.ITest;

public class AddEmployeeTest extends BaseTest implements ITest {
    private AddEmployeePage addEmployeePage;
    private PIMPage pimPage;
    private ThreadLocal<String> testName = new ThreadLocal<>();

    @BeforeMethod
    public void navigateToAddEmployee() {
        DashboardPage dashboard = loginPage.login("Admin", "admin123");
        pimPage = dashboard.goToPIM();
        addEmployeePage = pimPage.clickAddEmployee();
    }

    @DataProvider(name = "addEmployeeData")
    public Object[][] addEmployeeData() {
        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "/src/test/resources/addEmployeeData.json");
        return data.stream().map(emp -> new Object[]{emp}).toArray(Object[][]::new);
    }

    @Test(dataProvider = "addEmployeeData")
    public void testAddEmployee(HashMap<String, String> employee) {
        testName.set(employee.get("description"));
        String description = employee.getOrDefault("description", "");
        addEmployeePage.enterName(employee.get("firstName"), employee.get("middleName"), employee.get("lastName"));
        addEmployeePage.enterEmployeeId(employee.get("employeeId"));
//        addEmployeePage.uploadPicture(employee.get("imagePath"));

        if (Boolean.parseBoolean(String.valueOf(employee.get("withLogin")))) {
            addEmployeePage.enableLoginDetails();
            String username = employee.get("username");
            String password = employee.get("password");
            String confirmPassword = employee.getOrDefault("confirmPassword", password);
            addEmployeePage.enterLoginDetails(username, password, confirmPassword);
            addEmployeePage.setStatus(Status.valueOf(employee.get("status")));
        }

        addEmployeePage.clickSaveWithoutWaiting();

        String expectedError = employee.get("expectedError");
        if (expectedError != null && !expectedError.isEmpty()) {
            String errorText = getErrorTextForType(expectedError, employee);
            Assert.assertTrue(addEmployeePage.isErrorMessageDisplayed(errorText), description + " - Error message should be displayed: " + errorText);
        } else {
            String successMessage = addEmployeePage.waitForToastAndGetMessage();
            Assert.assertTrue(successMessage.contains("Successfully Saved"), description + " - Employee should be saved successfully");
        }
    }

    private String getErrorTextForType(String expectedError, HashMap<String, String> employee) {
        switch (expectedError) {
            case "Required":
                return "Required";
            case "MaxLength":
                return "Should not exceed 30 characters";
            case "EmployeeIdExists":
                return "Employee Id already exists";
            case "EmployeeIdMaxLength":
                return "Should not exceed 10 characters";
            case "UsernameExists":
                return "Username already exists";
            case "UsernameMaxLength":
                return "Should not exceed 40 characters";
            case "UsernameMinLength":
                return "Should be at least 5 characters";
            case "PasswordMinLength":
                return "Should have at least 7 characters";
            case "PasswordMaxLength":
                return "Should not exceed 64 characters";
            case "PasswordMismatch":
                return "Passwords do not match";
            case "PasswordMustContainNumber":
                return "must contain minimum 1 number";
            case "PasswordMustContainLowercase":
                return "must contain minimum 1 lower-case letter";
            default:
                return expectedError;
        }
    }

    @Override
    public String getTestName() {
        return testName.get();
    }
}
