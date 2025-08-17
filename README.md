# OrangeHRM-Automation

## Project Overview
OrangeHRM-Automation is a robust, data-driven test automation framework for the [OrangeHRM](https://opensource-demo.orangehrmlive.com/) web application. It leverages Selenium WebDriver, TestNG, and the Page Object Model (POM) design pattern to ensure maintainable, scalable, and readable test code. The project integrates Allure and Extent Reports for advanced test reporting and supports cross-browser execution.

## Features
- **Framework:** Java, Selenium WebDriver, TestNG, Page Object Model (POM)
- **Reporting:** Allure Reports, Extent Reports
- **Cross-browser Support:** Chrome, Firefox, Edge
- **Data-driven Testing:** JSON-based test data
- **Comprehensive Coverage:** Management, Search, My Info, and End-to-End workflows
- **Logging:** Log4j2 for detailed execution logs
- **Jira Integration:** (Configurable in `environment.properties`)

## Project Structure
```
OrangeHRM-Automation/
├── pom.xml                  # Maven project file with dependencies
├── src/
│   ├── main/
│   │   ├── java/            # Main Java source code
│   │   │   ├── drivers/     # WebDriver factories and management
│   │   │   ├── enums/       # Enum types (Status, Gender, etc.)
│   │   │   ├── listeners/   # TestNG and retry listeners
│   │   │   ├── pages/       # Page Object classes (Login, Dashboard, etc.)
│   │   │   └── utilities/   # Utilities (actions, assertions, logging)
│   │   └── resources/       # Config files (allure, environment, log4j2)
│   └── test/
│       ├── java/            # Test classes
│       │   └── tests/
│       │       ├── e2e/     # End-to-End workflow tests
│       │       └── features/# Feature-specific tests
│       └── resources/       # Test data (JSON)
├── testng-*.xml             # TestNG suite files
└── test-outputs/            # Allure results, reports, logs, screenshots
```

## Getting Started
### Prerequisites
- Java 14 or higher
- Maven 3.6+
- Chrome, Firefox, or Edge browser installed

### Setup
1. **Clone the repository:**
   ```sh
   git clone <repo-url>
   cd OrangeHRM-Automation
   ```
2. **Install dependencies:**
   ```sh
   mvn clean install
   ```

### Configuration
- **Base URLs and credentials:**
  Edit `src/main/resources/environment.properties` as needed.
- **Jira Integration:**
  Configure Jira settings in `environment.properties` (optional).
- **Logging:**
  Log4j2 configuration is in `src/main/resources/log4j2.properties`.

## Running Tests
### Using Maven
- **All tests:**
  ```sh
  mvn clean test
  ```
- **Specific suite:**
  ```sh
  mvn clean test -DsuiteXmlFile=testng-e2e.xml
  ```
- **Cross-browser:**
  ```sh
  mvn clean test -Dbrowser=firefox
  ```
  (Supported: `chrome` (default), `firefox`, `edge`)

### Using TestNG XML
You can run any of the provided TestNG suite files, e.g.:
```sh
mvn clean test -DsuiteXmlFile=testng-smoke.xml
```

## Test Data
- Test data is stored in JSON files under `src/test/resources/` (e.g., `addEmployeeData.json`).
- Data-driven tests use these files via TestNG DataProviders.

## Reporting
### Allure Report
1. **Generate Allure report:**
   ```sh
   mvn allure:report
   ```
2. **Open Allure report:**
   ```sh
   mvn allure:serve
   ```
- Allure results are stored in `test-outputs/allure-results/`.
- HTML reports are generated in `test-outputs/allure-report/`.

## Logging
- Execution logs are saved in `test-outputs/Logs/`.

## Sample Test Summary
- **Total Tests:** 114
- **Passed:** 11
- **Failed:** 3
- **Skipped:** 0
- **Pass Rate:** 97.4%


