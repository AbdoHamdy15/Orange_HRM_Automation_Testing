package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pages.LoginPage;  // غيّر الباكدج حسب اسم الـ package بتاعك

public class BaseTest {

    public WebDriver driver;
    public LoginPage loginPage;

    public WebDriver initializeDriver() {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "/src/main/java/resources/GlobalData.properties"
        )) {
            prop.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }

        String browserName = prop.getProperty("browser");

        if (browserName.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("--incognito");
            options.addArguments("--disable-save-password-bubble");
            driver = new ChromeDriver(options);
        } else if (browserName.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));

        return driver;
    }

    @BeforeMethod(alwaysRun = true)
    public LoginPage launchApplication() {
        driver = initializeDriver();
        driver.get("https://opensource-demo.orangehrmlive.com/");
        loginPage = new LoginPage(driver);
        return loginPage;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public List<HashMap<String, String>> getJsonData(String jsonFilePath) {
        try {
            String jsonContent = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data", e);
        }
    }

    public String getScreenshotPath(WebDriver driver, String testCaseName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/reports/" + testCaseName + ".png";
        FileUtils.copyFile(source, new File(destination));
        return destination;
    }
}