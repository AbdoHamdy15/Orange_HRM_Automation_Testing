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
        String browserName = System.getProperty("browser", "chrome");
        LogsUtil.info("Initializing driver for browser: " + browserName);
        try {
            driver = getDriver(browserName).startDriver();
            setDriver(driver);
            LogsUtil.info("Driver initialized successfully for: " + browserName);
        } catch (Exception e) {
            LogsUtil.error("Failed to initialize driver with browser: " + browserName);
            LogsUtil.error("Error: " + e.getMessage());
            // Fallback to edge if the specified browser fails
            try {
                LogsUtil.info("Attempting fallback to Edge browser...");
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
        LogsUtil.info("Creating driver with specified browser: " + browserName);
        try {
            driver = getDriver(browserName).startDriver();
            setDriver(driver);
            LogsUtil.info("Driver created successfully for: " + browserName);
        } catch (Exception e) {
            LogsUtil.error("Failed to create driver for browser: " + browserName);
            LogsUtil.error("Error: " + e.getMessage());
            throw e;
        }
    }

    public static WebDriver getInstance() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            LogsUtil.warn("No driver instance found in ThreadLocal");
        }
        return driver;
    }

    public WebDriver get() {
        if (driverThreadLocal.get() == null) {
            LogsUtil.error("Driver is null in ThreadLocal");
            fail("Driver is null");
            return null;
        }
        return driverThreadLocal.get();
    }

    private AbstractDriver getDriver(String browserName) {
        return switch (browserName.toLowerCase()) {
            case "chrome" -> new ChromeFactory();
            case "firefox" -> new FirefoxFactory();
            case "edge" -> new EdgeFactory();
            default -> {
                LogsUtil.error("Unsupported browser: " + browserName);
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
            }
        };
    }

    public static void setDriver(WebDriver driver) {
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
                LogsUtil.info("Browser closed successfully");
                driverThreadLocal.remove();
            } else {
                LogsUtil.warn("No driver found in ThreadLocal to close");
            }
        } catch (Exception e) {
            LogsUtil.error("Error during teardown: " + e.getMessage());
        }
    }

}
