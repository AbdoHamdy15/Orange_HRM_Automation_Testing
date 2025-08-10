# Jenkins CI/CD Configuration Guide

## 🚀 **Pipeline Setup:**

### **1️⃣ Jenkins Job Configuration:**

#### **General Settings:**
- **Project Name:** OrangeHRM-Automation
- **Description:** Automated testing for OrangeHRM application
- **Discard old builds:** Keep last 10 builds

#### **Source Code Management:**
- **Git Repository:** Your GitHub/GitLab repo URL
- **Credentials:** Add your Git credentials
- **Branch Specifier:** */main

#### **Build Triggers:**
- **Poll SCM:** H/5 * * * * (every 5 minutes)
- **GitHub hook trigger:** Enable if using GitHub

### **2️⃣ Build Environment:**

#### **Tools Configuration:**
- **Maven:** Maven-3.9.6
- **JDK:** JDK-17
- **Allure Commandline:** allure-2.24.0

#### **Environment Variables:**
```bash
JAVA_HOME=/usr/lib/jvm/java-17-openjdk
MAVEN_HOME=/usr/share/maven
ALLURE_HOME=/usr/local/bin/allure
```

### **3️⃣ Build Steps:**

#### **Step 1: Checkout**
```bash
checkout scm
```

#### **Step 2: Build**
```bash
mvn clean compile
```

#### **Step 3: Test (Parallel)**
```bash
# Smoke Tests
mvn test -DsuiteXmlFile=testng-smoke.xml

# Cross Browser Tests  
mvn test -DsuiteXmlFile=testng-cross-browser.xml

# E2E Tests
mvn test -DsuiteXmlFile=testng-e2e.xml
```

#### **Step 4: Generate Allure Report**
```bash
mvn allure:report
```

### **4️⃣ Post-Build Actions:**

#### **Publish Test Results:**
- **Test report XMLs:** `**/test-outputs/*.xml`
- **Publish Allure Report:** `test-outputs/allure-report`

#### **Archive Artifacts:**
- **Files to archive:** `test-outputs/**/*`
- **Fingerprint all archived artifacts:** ✓

#### **Email Notifications:**
- **Recipients:** your-email@company.com
- **Send to individuals who broke the build:** ✓

### **5️⃣ Pipeline Script:**

Use the Jenkinsfile in the root directory or configure as Pipeline script.

## 🔧 **Required Jenkins Plugins:**

### **Essential Plugins:**
- **Git plugin**
- **Maven Integration plugin**
- **Allure Jenkins Plugin**
- **Email Extension Plugin**
- **Test Results Analyzer Plugin**
- **Pipeline plugin**
- **Workspace Cleanup Plugin**

### **Installation:**
1. Go to **Manage Jenkins** → **Manage Plugins**
2. Search and install required plugins
3. Restart Jenkins

## 📊 **Allure Report Integration:**

### **Allure Commandline Setup:**
1. Download Allure commandline
2. Extract to `/usr/local/bin/allure`
3. Add to PATH: `export PATH=$PATH:/usr/local/bin/allure`

### **Jenkins Allure Configuration:**
- **Allure Commandline:** `/usr/local/bin/allure`
- **Results path:** `test-outputs/allure-results`
- **Report path:** `test-outputs/allure-report`

## 🎯 **Pipeline Features:**

### **✅ Parallel Execution:**
- Smoke Tests
- Cross Browser Tests
- E2E Tests

### **✅ Reporting:**
- Allure Reports
- Test Results
- Screenshots
- Logs

### **✅ Notifications:**
- Email on Success/Failure
- Jira Integration (automatic bug creation)

### **✅ Artifacts:**
- Test Results
- Screenshots
- Logs
- Allure Reports

## 🚀 **Next Steps:**

1. **Configure Jenkins Job** using the above settings
2. **Install Required Plugins**
3. **Setup Allure Commandline**
4. **Configure Email Notifications**
5. **Test the Pipeline**

## 📧 **Email Configuration:**

### **SMTP Settings:**
- **SMTP server:** smtp.gmail.com (for Gmail)
- **SMTP port:** 587
- **Use TLS:** ✓
- **Username:** your-email@gmail.com
- **Password:** App password (not regular password)

### **Email Templates:**
- **Success Subject:** `Pipeline SUCCESS: ${JOB_NAME} [${BUILD_NUMBER}]`
- **Failure Subject:** `Pipeline FAILED: ${JOB_NAME} [${BUILD_NUMBER}]`
