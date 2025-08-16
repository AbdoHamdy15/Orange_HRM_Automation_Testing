package utilities;

import io.qameta.allure.Story;
import org.testng.ITestResult;

import java.io.File;

public class JiraManager {
    private static JiraReporter instance;

    private synchronized static JiraReporter getInstance() {
        if (instance == null) {
            String jiraUrl = PropertiesUtils.getPropertyValue("jira.url");
            String jiraEmail = PropertiesUtils.getPropertyValue("jira.username");
            String jiraToken = PropertiesUtils.getPropertyValue("jira.api.token");
            
            if (jiraUrl != null && jiraEmail != null && jiraToken != null) {
                instance = new JiraReporter(jiraUrl, jiraEmail, jiraToken);
                LogsUtil.info("JiraManager initialized successfully");
            } else {
                LogsUtil.warn("Jira credentials not found. Jira integration disabled.");
                return null;
            }
        }
        return instance;
    }

    public static String createSummary(String testName, Story story) {
        String browser = System.getProperty("browser", "chrome");
        String storyValue = story != null ? story.value() : testName;
        
        // Clean up test name (remove package prefix if present)
        String cleanTestName = testName;
        if (testName.contains(".")) {
            cleanTestName = testName.substring(testName.lastIndexOf(".") + 1);
        }
        
        // Create concise summary
        return String.format("[%s] %s - %s", browser.toUpperCase(), cleanTestName, storyValue);
    }

    private static String findLatestLogFile() {
        try {
            File logsDir = new File("test-outputs/Logs");
            if (!logsDir.exists()) {
                LogsUtil.warn("Logs directory does not exist: " + logsDir.getPath());
                return null;
            }
            
            File[] logFiles = logsDir.listFiles((dir, name) -> name.startsWith("log_") && name.endsWith(".log"));
            if (logFiles == null || logFiles.length == 0) {
                LogsUtil.warn("No log files found in: " + logsDir.getPath());
                return null;
            }
            
            // Find the most recent log file
            File latestLog = logFiles[0];
            for (File logFile : logFiles) {
                if (logFile.lastModified() > latestLog.lastModified()) {
                    latestLog = logFile;
                }
            }
            
            LogsUtil.info("Found latest log file: " + latestLog.getPath());
            return latestLog.getPath();
        } catch (Exception e) {
            LogsUtil.error("Error finding latest log file: " + e.getMessage());
            return null;
        }
    }

    public static String createDescription(ITestResult testResult) {
        StringBuilder description = new StringBuilder();
        
        description.append("h2. Test Failure Summary\n");
        description.append("*Test Name:* ").append(testResult.getName()).append("\n");
        description.append("*Test Class:* ").append(testResult.getTestClass().getName()).append("\n");
        description.append("*Browser:* ").append(System.getProperty("browser", "chrome")).append("\n");
        description.append("*Failure Time:* ").append(new java.util.Date(testResult.getEndMillis())).append("\n");
        
        // Add exception details if available
        if (testResult.getThrowable() != null) {
            description.append("\nh3. Error Details\n");
            String errorMessage = testResult.getThrowable().getMessage();
            if (errorMessage != null && errorMessage.length() > 500) {
                errorMessage = errorMessage.substring(0, 500) + "... [truncated]";
            }
            description.append("*Error Message:* ").append(errorMessage).append("\n");
            
            // Add stack trace (first few lines only)
            String stackTrace = java.util.Arrays.toString(testResult.getThrowable().getStackTrace());
            if (stackTrace.length() > 1000) {
                stackTrace = stackTrace.substring(0, 1000) + "... [truncated]";
            }
            description.append("*Stack Trace:* ").append(stackTrace).append("\n");
        }
        
        // Add test parameters if available (simplified)
        if (testResult.getParameters() != null && testResult.getParameters().length > 0) {
            description.append("\nh3. Test Parameters\n");
            for (int i = 0; i < testResult.getParameters().length; i++) {
                String param = String.valueOf(testResult.getParameters()[i]);
                if (param.length() > 100) {
                    param = param.substring(0, 100) + "... [truncated]";
                }
                description.append("*Parameter ").append(i + 1).append(":* ").append(param).append("\n");
            }
        }
        
        // Add essential test information
        description.append("\nh3. Test Information\n");
        description.append("*Test Duration:* ").append((testResult.getEndMillis() - testResult.getStartMillis()) / 1000.0).append(" seconds\n");
        description.append("*Test Status:* ").append(testResult.getStatus()).append("\n");
        
        // Add screenshot and logs info (without content)
        description.append("\nh3. Attachments\n");
        String screenshotPath = "test-outputs/screenshots/failed-" + testResult.getName() + ".png";
        if (new File(screenshotPath).exists()) {
            description.append("*Screenshot:* ").append(screenshotPath).append("\n");
        }
        
        // Add log file info (without content)
        String latestLogPath = findLatestLogFile();
        if (latestLogPath != null) {
            description.append("*Logs File:* ").append(latestLogPath).append("\n");
        }
        
        description.append("\nh3. Additional Information\n");
        description.append("For detailed logs and screenshots, please check the attached files.\n");
        description.append("Test executed on: ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version")).append("\n");
        
        return description.toString();
    }

    public static void reportFailure(String projectKey, String summary, String description, File screenshot) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping bug report.");
            return;
        }
        
        try {
            if (screenshot != null && screenshot.exists()) {
                reporter.reportBug(projectKey, summary, description, screenshot.getPath());
            } else {
                reporter.reportBug(projectKey, summary, description);
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to report bug to Jira: " + e.getMessage());
        }
    }

    public static void reportFailure(ITestResult testResult) {
        String projectKey = PropertiesUtils.getPropertyValue("jira.project.key");
        if (projectKey == null || projectKey.isEmpty()) {
            LogsUtil.warn("Jira project key not configured. Skipping bug report.");
            return;
        }
        
        String summary = createSummary(testResult.getName(), testResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Story.class));
        String description = createDescription(testResult);
        
        // Get screenshot and logs paths
        String screenshotPath = "test-outputs/screenshots/failed-" + testResult.getName() + ".png";
        
        // Fix log file path to match actual naming convention
        String latestLogPath = findLatestLogFile();
        
        LogsUtil.info("Checking attachments for Jira issue:");
        LogsUtil.info("Screenshot path: " + screenshotPath);
        LogsUtil.info("Log path: " + latestLogPath);
        
        // Check which files exist and report with multiple attachments
        java.util.List<String> attachments = new java.util.ArrayList<>();
        
        File screenshotFile = new File(screenshotPath);
        File logFile = new File(latestLogPath);
        
        if (screenshotFile.exists()) {
            attachments.add(screenshotPath);
            LogsUtil.info("Screenshot found and will be attached: " + screenshotPath);
        } else {
            LogsUtil.warn("Screenshot not found: " + screenshotPath);
        }
        
        if (logFile.exists()) {
            attachments.add(latestLogPath);
            LogsUtil.info("Log file found and will be attached: " + latestLogPath);
        } else {
            LogsUtil.warn("Log file not found: " + latestLogPath);
        }
        
        LogsUtil.info("Total attachments to be sent: " + attachments.size());
        
        if (!attachments.isEmpty()) {
            reportFailureWithMultipleAttachments(projectKey, summary, description, attachments.toArray(new String[0]));
        } else {
            LogsUtil.warn("No attachments found, sending issue without attachments");
            reportFailure(projectKey, summary, description, null);
        }
    }

    public static void reportFailureWithMultipleAttachments(String projectKey, String summary, String description, String... filePaths) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping bug report.");
            return;
        }
        
        LogsUtil.info("Reporting failure with multiple attachments to Jira:");
        LogsUtil.info("Project Key: " + projectKey);
        LogsUtil.info("Summary: " + summary);
        LogsUtil.info("Number of attachments: " + filePaths.length);
        
        for (int i = 0; i < filePaths.length; i++) {
            File file = new File(filePaths[i]);
            LogsUtil.info("Attachment " + (i + 1) + ": " + filePaths[i] + " (exists: " + file.exists() + ", size: " + file.length() + " bytes)");
        }
        
        try {
            reporter.reportBugWithMultipleAttachments(projectKey, summary, description, filePaths);
            LogsUtil.info("Successfully reported bug with attachments to Jira");
        } catch (Exception e) {
            LogsUtil.error("Failed to report bug with attachments to Jira: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void closeJiraReporter() {
        if (instance != null) {
            try {
                instance.close();
                instance = null;
                LogsUtil.info("JiraReporter closed successfully");
            } catch (Exception e) {
                LogsUtil.error("Error closing JiraReporter: " + e.getMessage());
            }
        }
    }
}
