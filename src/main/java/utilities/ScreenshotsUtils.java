package utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class ScreenshotsUtils {

    public static final String SCREENSHOTS_PATH = "test-outputs/screenshots";
    private ScreenshotsUtils() {
        super();
    }

    public static void takeScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Wait for page to be ready
            try {
                Thread.sleep(500); // Small wait to ensure page is ready
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File screenshotFile = new File(SCREENSHOTS_PATH + File.separator + screenshotName + ".png");
            FileUtils.copyFile(screenshot, screenshotFile);
            
            LogsUtil.info("Screenshot taken: " + screenshotFile.getPath());
        }
        catch (Exception e) {
            LogsUtil.error("Failed to take screenshot: " + e.getMessage());
        }
    }
}
