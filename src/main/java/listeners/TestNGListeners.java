package listeners;

import drivers.GUIDriver;
import org.testng.*;
import utilities.*;

import java.util.Map;


import java.io.File;

import static utilities.PropertiesUtils.loadProperties;

public class TestNGListeners implements IExecutionListener, ITestListener, IInvokedMethodListener {

    File allure_results = new File("test-outputs/allure-results");
    File logs = new File("test-outputs/Logs");
    File screenshots = new File("test-outputs/screenshots");


    @Override
    public void onExecutionStart() {
        LogsUtil.info("Test Execution started");
        loadProperties();

        // Preserve history folder
        File historyDir = new File(allure_results, "history");
        File tempHistoryDir = new File("temp-history");
        if (historyDir.exists()) {
            FilesUtils.copyDirectory(historyDir, tempHistoryDir);
        }

        FilesUtils.cleanDirectory(allure_results);

        // Restore history folder
        if (tempHistoryDir.exists()) {
            FilesUtils.copyDirectory(tempHistoryDir, historyDir);
            FilesUtils.deleteDirectory(tempHistoryDir);
        }

        FilesUtils.cleanDirectory(logs);
        FilesUtils.cleanDirectory(screenshots);
        FilesUtils.createDirectory(allure_results);
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
        }
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        LogsUtil.info("Test case", result.getName(), "passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogsUtil.info("Test case", result.getName(), "failed");
        
        // Take screenshot for Jira
        ScreenshotsUtils.takeScreenshot(GUIDriver.getInstance(), "failed-" + result.getName());
        
        // Report to Jira using new JiraManager
        JiraManager.reportFailure(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogsUtil.info("Test case", result.getName(), "skipped");
    }

}
