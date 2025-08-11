package listeners;

import drivers.GUIDriver;
import org.openqa.selenium.WebDriver;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utilities.AllureUtils;
import utilities.CustomSoftAssertions;
import utilities.FilesUtils;
import utilities.JiraManager;
import utilities.LogsUtil;
import utilities.PropertiesUtils;
import utilities.ScreenshotsUtils;

import java.io.File;
import java.util.Map;


import static utilities.PropertiesUtils.loadProperties;

public class TestNGListeners implements IExecutionListener, ITestListener, IInvokedMethodListener {

    File allure_results = new File("test-outputs/allure-results");
    File allure_history = new File("test-outputs/allure-history");
    File logs = new File("test-outputs/Logs");
    File screenshots = new File("test-outputs/screenshots");


    @Override
    public void onExecutionStart() {
        LogsUtil.info("Test Execution started");
        loadProperties();

        // Preserve history folder from the new history directory
        File tempHistoryDir = new File("temp-history");
        if (allure_history.exists()) {
            FilesUtils.copyDirectory(allure_history, tempHistoryDir);
        }

        FilesUtils.cleanDirectory(allure_results);

        // Restore history folder to the new history directory
        if (tempHistoryDir.exists()) {
            FilesUtils.copyDirectory(tempHistoryDir, allure_history);
            FilesUtils.deleteDirectory(tempHistoryDir);
        }

        FilesUtils.cleanDirectory(logs);
        FilesUtils.cleanDirectory(screenshots);
        FilesUtils.createDirectory(allure_results);
        FilesUtils.createDirectory(allure_history);
        FilesUtils.createDirectory(logs);
        FilesUtils.createDirectory(screenshots);
    }


    @Override
    public void onTestStart(ITestResult result) {
        // Set browser parameter from TestNG to System property
        ITestContext context = result.getTestContext();
        Map<String, String> parameters = context.getCurrentXmlTest().getAllParameters();
        if (parameters.containsKey("browser")) {
            System.setProperty("browser", parameters.get("browser"));
            LogsUtil.info("Browser set to: " + parameters.get("browser"));
        }
    }


    @Override
    public void onExecutionFinish() {
        LogsUtil.info("Test Execution finished");
        
        // Close Jira reporter
        JiraManager.closeJiraReporter();
        
        AllureUtils.generateAllureReport();
        String reportName = AllureUtils.renameReport();
        LogsUtil.info("Allure report generated: " + reportName);
    }


    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            CustomSoftAssertions.customAssertAll(testResult);
            AllureUtils.attacheLogsToAllureReport();
            
            // Take screenshot and attach to Allure if test failed
            if (testResult.getStatus() == ITestResult.FAILURE) {
                ScreenshotsUtils.takeScreenshot(GUIDriver.getInstance(), "failed-" + testResult.getName());
                String screenshotPath = "test-outputs/screenshots/failed-" + testResult.getName() + ".png";
                AllureUtils.attachScreenshotToAllure("Screenshot", screenshotPath);
            }
            
            // Clean up WebDriver after each test method
            cleanupWebDriver();
        }
    }

    /**
     * Clean up WebDriver instance for current thread
     */
    private void cleanupWebDriver() {
        try {
            WebDriver driver = GUIDriver.getInstance();
            if (driver != null) {
                LogsUtil.info("Cleaning up WebDriver for thread: " + Thread.currentThread().getName());
                driver.quit();
                LogsUtil.info("WebDriver cleaned up successfully for thread: " + Thread.currentThread().getName());
            }
        } catch (Exception e) {
            LogsUtil.error("Error cleaning up WebDriver: " + e.getMessage());
        }
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        LogsUtil.info("Test case", result.getName(), "passed");
        // Clean up WebDriver after successful test
        cleanupWebDriver();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogsUtil.info("Test case", result.getName(), "failed");
        
        // Take screenshot for Jira
        ScreenshotsUtils.takeScreenshot(GUIDriver.getInstance(), "failed-" + result.getName());
        
        // Report to Jira using new JiraManager
        JiraManager.reportFailure(result);
        
        // Clean up WebDriver after failed test
        cleanupWebDriver();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogsUtil.info("Test case", result.getName(), "skipped");
        // Clean up WebDriver after skipped test
        cleanupWebDriver();
    }

}
