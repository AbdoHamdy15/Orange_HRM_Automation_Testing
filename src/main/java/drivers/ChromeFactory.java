package drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utilities.LogsUtil;
import java.util.Map;

public class ChromeFactory extends AbstractDriver implements WebDriverOptionsAbstract<ChromeOptions> {
    @Override
    public ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();
        
        // Add Chrome arguments
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        //options.addArguments("--headless"); // Uncomment if you want to run in headless mode
        
        Map<String, Object> prefs = Map.of(
                "profile.default_content_setting_values.notifications", 2,
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "autofill.profile_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        
        return options;
    }

    @Override
    public WebDriver startDriver() {
        LogsUtil.info("Starting Chrome driver...");
        try {
            ChromeDriver driver = new ChromeDriver(getOptions());
            LogsUtil.info("Chrome driver started successfully");
            return driver;
        } catch (Exception e) {
            LogsUtil.error("Failed to start Chrome driver: " + e.getMessage());
            throw e;
        }
    }
}
