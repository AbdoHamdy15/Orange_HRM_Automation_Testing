package drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import utilities.LogsUtil;

import java.util.Map;

public class EdgeFactory extends AbstractDriver implements WebDriverOptionsAbstract<EdgeOptions> {
    @Override
    public EdgeOptions getOptions() {
        EdgeOptions edgeOptions = new EdgeOptions();
        
        edgeOptions.addArguments("--start-maximized");
        edgeOptions.addArguments("--disable-extensions");
        edgeOptions.addArguments("--disable-infobars");
        edgeOptions.addArguments("--disable-notifications");
        edgeOptions.addArguments("--remote-allow-origins=*");
        //edgeoptions.addArguments("--headless"); // Uncomment if you want to run in headless mode
        
        Map<String, Object> edgePrefs = Map.of(
                "profile.default_content_setting_values.notifications", 2,
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "autofill.profile_enabled", false);
        edgeOptions.setExperimentalOption("prefs", edgePrefs);
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        
        return edgeOptions;
    }

    @Override
    public WebDriver startDriver() {
        LogsUtil.info("Starting Edge driver...");
        try {
            EdgeDriver driver = new EdgeDriver(getOptions());
            LogsUtil.info("Edge driver started successfully");
            return driver;
        } catch (Exception e) {
            LogsUtil.error("Failed to start Edge driver: " + e.getMessage());
            throw e;
        }
    }
}
