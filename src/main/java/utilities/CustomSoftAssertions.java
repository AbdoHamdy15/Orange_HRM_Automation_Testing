package utilities;

import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

public class CustomSoftAssertions extends SoftAssert {

    public static CustomSoftAssertions softAssertion = new CustomSoftAssertions();

    public static void customAssertAll(ITestResult result) {
        try {
            softAssertion.assertAll("Custom Soft Assertion");
        } catch (AssertionError e) {
            System.out.println("Custom Soft Assertion Failed: " + e.getMessage());
            result.setStatus(ITestResult.FAILURE);
            result.setThrowable(e);
        } finally {
            reInitializeSoftAssert();
        }
    }

    private static void reInitializeSoftAssert() {
        softAssertion = new CustomSoftAssertions();
    }
} 