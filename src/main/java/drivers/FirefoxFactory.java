package drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxFactory extends AbstractDriver implements WebDriverOptionsAbstract<FirefoxOptions> {
    @Override
    public FirefoxOptions getOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        // Firefox-specific arguments
        firefoxOptions.addArguments("--width=1920");
        firefoxOptions.addArguments("--height=1080");
        firefoxOptions.addArguments("--disable-extensions");
        firefoxOptions.addArguments("--disable-notifications");
        //firefoxOptions.addArguments("--headless"); // Uncomment if you want to run in headless mode
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        firefoxOptions.setAcceptInsecureCerts(true);
        return firefoxOptions;
    }

    @Override
    public WebDriver startDriver() {
        return new FirefoxDriver(getOptions());
    }
}
