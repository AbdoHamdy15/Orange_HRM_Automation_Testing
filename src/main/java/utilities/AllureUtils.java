package utilities;

import io.qameta.allure.Allure;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.readString;

public class AllureUtils {

    public static final String ALLURE_RESULTS_PATH = "test-outputs/allure-results";
    static String REPORT_PATH = "test-outputs/allure-report";
    static String USER_HOME = System.getProperty("user.home");
    static String ALLURE_PATH = USER_HOME + File.separator + ".m2" + File.separator + "repository"
            + File.separator + "allure" + File.separator + "allure-2.33.0" + File.separator
            + "bin" + File.separator + "allure";

    private AllureUtils() {
        super();
    }

    public static void attacheLogsToAllureReport() {
        try {
            File logFile = FilesUtils.getLatestFile(LogsUtil.LOGS_PATH);
            if (!logFile.exists()) {
                LogsUtil.warn("Log file does not exist: " + LogsUtil.LOGS_PATH);
                return;
            }
            Allure.addAttachment("logs.log", readString(Path.of(logFile.getPath())));
            LogsUtil.info("Logs attached to Allure report");
        } catch (Exception e) {
            LogsUtil.error("Failed to attach logs to Allure report: " + e.getMessage());
        }
    }

    public static void copyHistory() {
        try {
            File oldReportHistory = new File(REPORT_PATH + "/history");
            File newHistoryDir = new File(ALLURE_RESULTS_PATH + "/history");

            if (oldReportHistory.exists()) {
                FilesUtils.copyFolder(oldReportHistory, newHistoryDir);
                LogsUtil.info("Allure history copied successfully");
            } else {
                LogsUtil.warn("No previous Allure history found to copy");
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to copy Allure history: " + e.getMessage());
        }
    }

    public static void createEnvironmentProperties() {
        try {
            File envFile = new File(ALLURE_RESULTS_PATH + "/environment.properties");
            String envContent =
                    "Browser=Chrome\n" +
                            "OS=Windows 10 Pro\n" +
                            "Java Version=22\n" +
                            "Test Environment=OrangeHRM Demo\n" +
                            "Framework=TestNG\n" +
                            "Executor=Abdelrahman Hamdy\n";

            Files.write(envFile.toPath(), envContent.getBytes());
            LogsUtil.info("environment.properties created successfully");
        } catch (Exception e) {
            LogsUtil.error("Failed to create environment.properties: " + e.getMessage());
        }
    }

    public static void generateAllureReport() {
        // Copy history & set environment before generating report
        copyHistory();
        createEnvironmentProperties();

        if (PropertiesUtils.getPropertyValue("os.name").toLowerCase().contains("win")) {
            String WIN = ALLURE_PATH + ".bat";
            TerminalUtils.executeCommand(WIN, "generate", ALLURE_RESULTS_PATH, "-o", REPORT_PATH, "clean", "--single-file");
            LogsUtil.info("Allure report generated successfully on Windows");
        } else {
            TerminalUtils.executeCommand(ALLURE_PATH, "generate", ALLURE_RESULTS_PATH, "-o", REPORT_PATH, "clean", "--single-file");
            LogsUtil.info("Allure report generated successfully on " + PropertiesUtils.getPropertyValue("os.name"));
        }
    }

    public static String renameReport() {
        File oldName = new File(REPORT_PATH + File.separator + "index.html");
        if (!oldName.exists()) {
            LogsUtil.warn("Allure report file does not exist: " + oldName.getPath());
            return null;
        }
        File newName = new File("Report_" + TimestampUtils.getTimestamp() + ".html");
        FilesUtils.renameFile(oldName, newName);
        return newName.getName();
    }

    public static void openReport(String fileName) {
        String reportPath = REPORT_PATH + File.separator + fileName;
        File reportFile = new File(reportPath);

        if (!reportFile.exists()) {
            LogsUtil.warn("Allure report file does not exist: " + reportPath);
            return;
        }

        if (PropertiesUtils.getPropertyValue("openAllureAutomatically").equalsIgnoreCase("true")) {
            if (PropertiesUtils.getPropertyValue("os.name").toLowerCase().contains("win")) {
                TerminalUtils.executeCommand("cmd.exe", "/c", "start", reportPath);
            } else {
                TerminalUtils.executeCommand("open", reportPath);
            }
        }
    }

    public static void attachScreenshotToAllure(String screenshotName, String screenshotPath) {
        try {
            File screenshotFile = new File(screenshotPath);
            if (screenshotFile.exists()) {
                Allure.addAttachment(screenshotName, "image/png", Files.newInputStream(Path.of(screenshotPath)), "png");
                LogsUtil.info("Screenshot attached to Allure report: " + screenshotPath);
            } else {
                LogsUtil.warn("Screenshot file does not exist: " + screenshotPath);
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to attach screenshot to Allure report: " + e.getMessage());
        }
    }

    public static void configureAllureTrend() {
        try {
            File allureResultsDir = new File(ALLURE_RESULTS_PATH);
            if (!allureResultsDir.exists()) {
                allureResultsDir.mkdirs();
            }
            File historyDir = new File(ALLURE_RESULTS_PATH + "/history");
            if (!historyDir.exists()) {
                historyDir.mkdirs();
            }
            LogsUtil.info("Allure trend tracking configured");
        } catch (Exception e) {
            LogsUtil.error("Failed to configure Allure trend: " + e.getMessage());
        }
    }

    public static void setEnvironmentVariables() {
        try {
            System.setProperty("allure.results.directory", ALLURE_RESULTS_PATH);
            System.setProperty("allure.report.directory", REPORT_PATH);
            LogsUtil.info("Environment variables configured via allure.properties");
        } catch (Exception e) {
            LogsUtil.error("Failed to set environment variables: " + e.getMessage());
        }
    }

    public static void setExecutorInfo() {
        try {
            LogsUtil.info("Executor information configured via allure.properties");
        } catch (Exception e) {
            LogsUtil.error("Failed to set executor info: " + e.getMessage());
        }
    }

    public static void createEnvironmentJson() {
        try {
            File allureResultsDir = new File(ALLURE_RESULTS_PATH);
            if (!allureResultsDir.exists()) {
                allureResultsDir.mkdirs();
            }
            File environmentFile = new File(ALLURE_RESULTS_PATH + "/environment.json");
            String environmentJson = "{\n" +
                    "  \"Browser\": \"Chrome\",\n" +
                    "  \"OS\": \"Windows 10 Pro\",\n" +
                    "  \"Java Version\": \"22\",\n" +
                    "  \"Test Environment\": \"OrangeHRM Demo\",\n" +
                    "  \"Framework\": \"TestNG\",\n" +
                    "  \"Tester\": \"Abdelrahman Hamdy\"\n" +
                    "}";
            java.nio.file.Files.write(environmentFile.toPath(), environmentJson.getBytes());
            LogsUtil.info("Environment.json created successfully");
        } catch (Exception e) {
            LogsUtil.error("Failed to create environment.json: " + e.getMessage());
        }
    }
}
