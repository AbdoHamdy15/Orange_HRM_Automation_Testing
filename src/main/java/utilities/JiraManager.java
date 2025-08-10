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
        String browser = System.getProperty("browser", "Unknown");
        if (story != null) {
            return browser + " Automated Test Failure: " + story.value();
        } else {
            return browser + " Automated Test Failure: " + testName;
        }
    }

    public static String createDescription(ITestResult testResult) {
        StringBuilder description = new StringBuilder();
        
        description.append("h2. Test Failure Details\n");
        description.append("*Test Name:* ").append(testResult.getName()).append("\n");
        description.append("*Test Class:* ").append(testResult.getTestClass().getName()).append("\n");
        description.append("*Test Method:* ").append(testResult.getMethod().getMethodName()).append("\n");
        description.append("*Failure Time:* ").append(new java.util.Date(testResult.getEndMillis())).append("\n");
        description.append("*Browser:* ").append(System.getProperty("browser", "Unknown")).append("\n");
        
        // Add exception details if available (simplified)
        if (testResult.getThrowable() != null) {
            description.append("\nh3. Error Details\n");
            description.append("*Error Message:* ").append(testResult.getThrowable().getMessage()).append("\n");
        }
        
        // Add test parameters if available
        if (testResult.getParameters() != null && testResult.getParameters().length > 0) {
            description.append("\nh3. Test Parameters\n");
            for (int i = 0; i < testResult.getParameters().length; i++) {
                description.append("*Parameter ").append(i + 1).append(":* ").append(testResult.getParameters()[i]).append("\n");
            }
        }
        
        // Add logs content if available
        String today = TimestampUtils.getTimestamp().substring(0, 10); // YYYY-MM-DD
        String logPath = "test-outputs/Logs/log_" + today + ".log";
        
        // Try to find the actual log file
        File logsDir = new File("test-outputs/Logs");
        File latestLog = null;
        if (logsDir.exists()) {
            File[] logFiles = logsDir.listFiles((dir, name) -> name.startsWith("log_") && name.endsWith(".log"));
            if (logFiles != null && logFiles.length > 0) {
                // Use the most recent log file
                latestLog = logFiles[logFiles.length - 1];
                logPath = latestLog.getPath();
                
                try {
                    String logContent = new String(java.nio.file.Files.readAllBytes(latestLog.toPath()));
                    description.append("\nh3. Test Logs\n");
                    description.append("{code}\n");
                    description.append(logContent).append("\n");
                    description.append("{code}\n");
                    LogsUtil.info("Logs added to Jira description: " + latestLog.getName());
                } catch (Exception e) {
                    LogsUtil.error("Failed to read log file: " + e.getMessage());
                }
            }
        }
        
        // Add screenshot attachment info
        String screenshotPath = "test-outputs/screenshots/failed-" + testResult.getName() + ".png";
        description.append("\nh3. Attachments\n");
        if (new File(screenshotPath).exists()) {
            description.append("*Screenshot:* ").append(screenshotPath).append("\n");
        }
        
        // Add log file info
        if (latestLog != null) {
            description.append("*Logs File:* ").append(latestLog.getPath()).append("\n");
        }
        
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
        String logPath = "test-outputs/Logs/log_" + TimestampUtils.getTimestamp().substring(0, 10) + ".log";
        
        // Check which files exist and report with multiple attachments
        java.util.List<String> attachments = new java.util.ArrayList<>();
        
        if (new File(screenshotPath).exists()) {
            attachments.add(screenshotPath);
        }
        if (new File(logPath).exists()) {
            attachments.add(logPath);
        }
        
        if (!attachments.isEmpty()) {
            reportFailureWithMultipleAttachments(projectKey, summary, description, attachments.toArray(new String[0]));
        } else {
            reportFailure(projectKey, summary, description, null);
        }
    }

    public static void reportFailureWithMultipleAttachments(String projectKey, String summary, String description, String... filePaths) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping bug report.");
            return;
        }
        
        try {
            reporter.reportBugWithMultipleAttachments(projectKey, summary, description, filePaths);
        } catch (Exception e) {
            LogsUtil.error("Failed to report bug with attachments to Jira: " + e.getMessage());
        }
    }

    public static void addComment(String issueKey, String comment) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping comment.");
            return;
        }
        
        try {
            reporter.addComment(issueKey, comment);
        } catch (Exception e) {
            LogsUtil.error("Failed to add comment to Jira: " + e.getMessage());
        }
    }

    public static void transitionIssue(String issueKey, String transitionName) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping transition.");
            return;
        }
        
        try {
            reporter.transitionIssue(issueKey, transitionName);
        } catch (Exception e) {
            LogsUtil.error("Failed to transition issue in Jira: " + e.getMessage());
        }
    }

    public static void assignIssue(String issueKey, String assignee) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping assignment.");
            return;
        }
        
        try {
            reporter.assignIssue(issueKey, assignee);
        } catch (Exception e) {
            LogsUtil.error("Failed to assign issue in Jira: " + e.getMessage());
        }
    }

    public static void updateIssueSummary(String issueKey, String newSummary) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping update.");
            return;
        }
        
        try {
            reporter.updateIssueSummary(issueKey, newSummary);
        } catch (Exception e) {
            LogsUtil.error("Failed to update issue summary in Jira: " + e.getMessage());
        }
    }

    public static void updateIssueDescription(String issueKey, String newDescription) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping update.");
            return;
        }
        
        try {
            reporter.updateIssueDescription(issueKey, newDescription);
        } catch (Exception e) {
            LogsUtil.error("Failed to update issue description in Jira: " + e.getMessage());
        }
    }

    public static com.fasterxml.jackson.databind.JsonNode getIssue(String issueKey) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping get issue.");
            return null;
        }
        
        try {
            return reporter.getIssue(issueKey);
        } catch (Exception e) {
            LogsUtil.error("Failed to get issue from Jira: " + e.getMessage());
            return null;
        }
    }

    public static void deleteIssue(String issueKey) {
        JiraReporter reporter = getInstance();
        if (reporter == null) {
            LogsUtil.warn("Jira integration not available. Skipping delete.");
            return;
        }
        
        try {
            reporter.deleteIssue(issueKey);
        } catch (Exception e) {
            LogsUtil.error("Failed to delete issue in Jira: " + e.getMessage());
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
