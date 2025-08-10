package drivers;


import org.openqa.selenium.WebDriver;
import utilities.BrowserActions;
import utilities.ElementActions;
import utilities.LogsUtil;
import utilities.Validations;

import static org.testng.Assert.fail;


public class GUIDriver {
    //code
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    WebDriver driver;

    public GUIDriver() {
        // Read browser from TestNG parameters or default to firefox
        String browserName = System.getProperty("browser", "chrome");
        try {
            driver = getDriver(browserName).startDriver();
            setDriver(driver);
        } catch (Exception e) {
            LogsUtil.error("Failed to initialize driver with browser: " + browserName);
            LogsUtil.error("Error: " + e.getMessage());
            // Fallback to edge if the specified browser fails
            try {
                driver = new EdgeFactory().startDriver();
                setDriver(driver);
                LogsUtil.info("Fallback to Edge browser successful");
            } catch (Exception fallbackError) {
                LogsUtil.error("Fallback to Edge also failed: " + fallbackError.getMessage());
                throw new RuntimeException("Failed to initialize WebDriver", fallbackError);
            }
        }
    }

    public GUIDriver(String browserName) {
        driver = getDriver(browserName).startDriver();
        setDriver(driver);
    }

    public static WebDriver getInstance() {
        return driverThreadLocal.get();
    }

    public WebDriver get() {
        if (driverThreadLocal.get() == null) {
            LogsUtil.error("Driver is null");
            fail("Driver is null");
            return null;
        }
        return driverThreadLocal.get();
    }

    private AbstractDriver getDriver(String browserName) {
        //code
        return switch (browserName.toLowerCase()) {
            case "chrome" -> new ChromeFactory();
            case "firefox" -> new FirefoxFactory();
            case "edge" -> new EdgeFactory();
            default -> throw new IllegalArgumentException();
        };

    }

    private void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    public ElementActions element() {
        return new ElementActions(get());
    }

    public BrowserActions browser() {
        return new BrowserActions(get());
    }

    public Validations validate() {
        return new Validations(get());
    }

    // Teardown method to close browser after each test
    public void teardown() {
        try {
            if (driverThreadLocal.get() != null) {
                LogsUtil.info("Closing browser...");
                driverThreadLocal.get().quit();
                driverThreadLocal.remove();
            }
        } catch (Exception e) {
            LogsUtil.error("Error during teardown: " + e.getMessage());
        }
    }

}
