#  🌐Magento API Rest Assured Automation Framework

An end-to-end API testing framework built using **Java**, **Rest Assured**, and **TestNG**, covering Customer and Cart services for Magento. Designed with modular architecture, grouped test execution (Smoke, Sanity, Regression), reusable service layers, and CI/CD readiness.

  

# 🎯 1. **Test Objective**

  

Build a **positive-path** API automation framework to validate Magento’s **Customer** and **Cart** REST APIs with focus on:

  

- Automating core business workflows: customer creation, authentication, and cart operations

- Leveraging dynamic test data for robustness and reusability

- Generating developer-friendly reports: timestamped archival reports plus a static, Jenkins-friendly report

  

---

  

## **🚀 Framework Highlights**

| Feature             | Description                                                    |
|---------------------|----------------------------------------------------------------|
| 🔧 Modular Design    | Well-structured layers: client, services, utilities, constants |
| 📜 Extent Reports   | Rich HTML reports with system metadata and embedded screenshots |
| 💬 Logging & Debugging | Integrated `log4j2` with rolling file strategy for detailed tracing |
| 🧪 End-to-End Coverage | Realistic test scenarios covering full customer-cart workflows |
| ⚙️ Configurable      | Centralized `config.properties` with type-safe access via enums |
| 🚀 CI-Ready         | Timestamped reports and seamless headless execution in Jenkins |
| 🔑 Token Management  | Singleton pattern with service layer managing auth tokens      |
| 🛠️ Payload Strategy  | Combination of external JSON templates and POJO-based payload builders |
| 🎲 Dynamic Data      | Faker library integration for realistic, unique test data generation |
| 🔄 Test Execution    | TestNG-driven with priority and grouping for flexible test runs |


---

  

# 🏁 2. Pre-setup

  

### 🧩 2.1 Maven Project Structure

  

- Initialize a standard **Maven project** with the following folder layout:

-  `src/main/java` — Application source code (API clients, services, models, utils)

-  `src/test/java` — Test classes and test base setup

-  `src/test/resources` — Configuration files such as `config.properties` and `log4j2.xml`

---
### 🔑 2.2 Key Dependencies (`pom.xml`)
  

Add the following libraries to your `pom.xml`  

-  **RestAssured** — for REST API HTTP request handling

-  **TestNG** — for test execution and orchestration

-  **Jackson Databind** — for JSON serialization and deserialization

-  **Log4j2** — advanced logging capabilities with rolling files

-  **Faker** — generate realistic dynamic test data

-  **Apache Commons IO** — for convenient file and stream utilities
---

### 📂 2.3 Folder Structure

```bash

magneto-api-framework
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── api/
│   │   │       ├── auth/
│   │   │       │   └── TokenManager.java
│   │   │       ├── client/
│   │   │       │   ├── CartClient.java
│   │   │       │   └── CustomerClient.java
│   │   │       ├── config/
│   │   │       │   └── Routes.java
│   │   │       ├── model/
│   │   │       │   └── UpdateCartItem.java (POJO)
│   │   │       ├── payload/
│   │   │       │   ├── PayloadGenerator.java
│   │   │       │   └── PayloadLoader.java
│   │   │       ├── services/
│   │   │       │   ├── CartService.java
│   │   │       │   └── CustomerService.java
│   │   │       └── utils/
│   │   │           ├── ReadProperty.java
│   │   │           ├── LoggerUtil.java
│   │   │           ├── FakerUtil.java
│   │   │           ├── ReportManager.java
│   │   │           ├── RetryAnalayzer.java
│   │   │           ├── RetryListener.java
│   │   │           └── TestContext.java
│   ├── test/
│   │   ├── java/
│   │   │   └── api/
│   │   │       ├── testBase/
│   │   │       │   └── BaseTest.java
│   │   │       ├── testCases/
│   │   │       │   ├── TC_CartFlowTest.java
│   │   │       │   └── TC_CustomerFlowTest.java
│   │   │       └── testSuite/
│   │   │           └── Functional-Suite.xml
│   └── resources/
│       ├── payloadTemplates/
│       │   └──CartItemTemplate.json
│       ├── TestCaseSheet.xlsx
│       ├── config.properties
│       └── log4j2.xml
│
├── logs/
│   └── rollingLogfile(timestamped)
├── reports/
│   └── html-report/
│       ├── index.html (static report)
│       └── dynamincTimeStamp_reports.html (archives)
└── pom.xml
  
```

### 📋 2.4 **Test Case Development & Mapping**

Manual test cases are added under test/resources as TestCaseSheet.xml
 

| Test Case ID   | Title                           | Test Category      | Expected Response Fields               |
|----------------|---------------------------------|--------------------|----------------------------------------|
| API-CUST-01    | Create customer with valid data | Smoke, Regression  | `Customer_ID`                          |
| API-CUST-02    | Generate customer JWT token     | Smoke, Regression  | `Customer_Bearer_Token`                |
| API-CUST-03    | View the customer               | Smoke, Regression  | `Customer_ID`, profile details         |
| API-CART-01    | Create cart for customer        | Sanity, Regression | `Cart_ID`                              |
| API-CART-02    | Add product to cart             | Sanity, Regression | `Item_ID`, product details             |
| API-CART-03    | View items in cart              | Sanity, Regression | Cart summary including `Item_ID`       |
| API-CART-04    | Update cart item quantity       | Regression         | Updated `Item_ID`, updated quantity    |
| API-CART-05    | Delete cart item                | Regression         | Success message, `Item_ID` removed     |


---

>  **DESIGN PHASE**

>  

# 🏗️ 3. Architecture Setup
  

### 🎟️ 3.1 Authentication Strategy
| Auth Type     | Use Case                  | Implementation                                         |
|---------------|---------------------------|--------------------------------------------------------|
| **Admin Token**   | Customer creation         | Hardcoded Bearer token in `config.properties` (practice only) |
| **Customer JWT**  | Login, Cart actions (`/mine`) | Dynamically generated via login API (`AuthClient.getCustomerToken()`)  |

  -   **Admin token** is loaded once from the config file for simplicity.
    
-   **Customer token** is generated at runtime and injected dynamically into API requests.



---

  

### ⚙️ 3.2 Configuration Strategy

  

- Maintain `BASE_URI`, `ADMIN_TOKEN`, and all API endpoints as `public static final` constants inside the `Routes` class.

  

---

  

###  📃 3.3 Payload Management Strategy

  

**Option 1: External JSON Templates**

  

- Store payload JSON files in `api.payload`.

- Use `PayloadLoader.loadPayload("filename.json")` to read templates.

- Replace placeholders dynamically using string `.replace()` calls.

- Generate dynamic values with `PayloadGenerator` backed by `FakerUtils`.

  

**Option 2: POJO Serialization**

  

- Create POJO classes under `api.model` (e.g., `UpdateCartItem`).

- Serialize POJOs to JSON strings using Jackson’s `ObjectMapper.writeValueAsString()`.

- Wrap POJOs inside maps when necessary for nested payloads.

  

---

  

### 📩  3.4 Client Layer (`api.client`)

  

- Contains API-specific classes like `CustomerClient` and `CartClient`.

- Uses RestAssured to build and execute HTTP requests.

- Fetches endpoints from `Routes`.

- Accepts payload and tokens as method parameters.

- Returns raw `Response` objects for validation.

  

This layer acts like the “Page Object Model” of your API framework.

  

---

  

### ⬇️ 3.5 Service Layer (`api.services`)

  

- Encapsulates business workflows by chaining Client calls.

- Example services:

-  `CustomerService`: create customer → generate token → get details

-  `CartService`: create cart → add item → view cart → update item → delete item

- Uses shared state in `TestContext` (e.g., tokens, cartId, itemId) to pass data between calls.

  

---

  

### 📑 3.6 Test Classes (`api.testCases`)

  

-  **TC_CustomerFlowTest**

- Tests customer-related APIs

- Groups: `smoke`, `regression`

-  **TC_CartFlowTest**

- Depends on `TestContext.customerToken`

- Validates cart operations

- Groups: `sanity`, `regression`, `smoke_cart`

-  **BaseTest (`api.testBase`)**

- Contains setup/teardown and shared utilities

- Reads base URI and tokens from `config.properties` via `ConfigReader`

- Initializes logging for subclasses

-  `@BeforeSuite`: initializes Customer token in `TestContext`

-  `@BeforeClass`: sets base URI

- Logs test start/end at method level

-  **Test Suites (`api.testSuite`)**

- Organize tests into XML suites by category (smoke, sanity, regression, etc.)

  ---

  

### 🗞️ 3.7 Test Data & Config Files

  

-  **Payload Templates** — JSON files for reusable request bodies.

-  **config.properties** — Base URI, Admin token, and other env variables.

-  **log4j2.xml** — Logging configuration with rolling file strategy.

  

---

  

> DEVELOP PHASE

>

  

# ⚓ 4. Utilities Setup

  

###  📖 4.1 **ConfigReader**

  

- Implements **Singleton pattern** to ensure a single properties instance.

- Private constructor initializes the `Properties` object by loading from `config.properties`.

- Public static `getInstance()` method provides global access.

-  `getProperty(String key)` fetches values by key, centralizing config access.

  

---

  

### 🎭 4.2 **FakerUtil**

  

- Holds a private static final instance of `Faker` for generating realistic test data.

- Exposes public getter methods for reusable random data generation (names, emails, etc.).

- Keeps test data creation clean and decoupled from test logic.

  

---

  

### 🗃️ 4.3 **LoggerUtil**

  

- Wraps `LogManager.getLogger(Class<?>)` to initialize Log4j2 loggers.

- Used in base classes and utilities for consistent logging across tests and clients.

- Helps track API calls, responses, and debugging info efficiently.

  

---

  

### 🎬 4.4 **ReportManager**

  

**Dual Extent Reports Setup (implements `ITestListener`)**

  

- Generates **two synchronized Extent reports** every test run:

1.  **Timestamped report:**

- Filepath: `./reports/Test-Report-<timestamp>.html`

- Purpose: archive historical runs for audit and traceability.

2.  **Static report:**

- Filepath: `./reports/index.html`

- Purpose: overwritten every run, used by Jenkins `Publish HTML Reports` to show latest test results seamlessly.

- Core setup snippet:

```java

sparkArchive = new  ExtentSparkReporter("./reports/" + repName);

sparkStatic = new  ExtentSparkReporter("./reports/index.html");

report.attachReporter(sparkArchive, sparkStatic);

```

- Lifecycle callbacks implemented:

-  **onStart:** Initialize reporters, configure theme, set system info.

-  **onTestStart:** Create new `ExtentTest`, assign test categories.

-  **onTestSuccess/Failure/Skipped:** Log corresponding test status.

-  **onFinish:** Flush and write reports to disk.

  

---

  

###  🔁 4.5 **RetryAnalyzer**

  

- Implements `IRetryAnalyzer`.

- Overrides `retry(ITestResult result)` to **retry failed tests exactly once**.

- Enables transient failure resilience without manual reruns.

  

---

  

### ⏺️ 4.6 **RetryListener**

  

- Implements `IAnnotationTransformer`.

- Overrides `transform` method to **dynamically assign the `RetryAnalyzer`** to all `@Test` methods.

- Automates retry logic injection without polluting test code.

  

---

  

### 📜  4.7 **TestContext**

  

- Acts as **shared in-memory storage** for inter-test communication.

- Holds key variables during test execution:

-  `customerToken` — JWT token for authenticated API calls.

-  `cartId` — Unique identifier for the customer’s shopping cart.

-  `addedItemId` — ID of the product item added to the cart.

- Ensures clean state sharing without static/global variable misuse.

  

---

  

# 🔰**5. Exec**ution Setup
  

### 5.1 **TestNG Suite XML Files**
 

- Organize test execution using **TestNG XML suites** under `src/test/java/api/testSuite/`.

- Separate suites for different test categories:

-  `Smoke-Suite.xml` — critical tests for quick sanity checks

-  `Sanity-Suite.xml` — focused tests on key flows (e.g., cart)

-  `Regression-Suite.xml` — full positive path coverage

-  `Functional-Suite.xml` — end-to-end comprehensive runs

- Each suite defines `<groups>` and `<classes>` for precise test filtering.

- Enables flexible execution in local runs and CI pipelines.
  
---

### 5.2 **Running Tests via Maven**

- Use Maven Surefire plugin configured in `pom.xml` to run tests via CLI:

```bash

mvn clean test -DsuiteXmlFile=src/test/java/api/testSuite/Smoke-Suite.xml

```

- Pass suite XML as a parameter (`suiteXmlFile`) for dynamic test selection.

- Supports parallel execution, retry logic, and detailed reporting out-of-the-box.

  

---

  

### 5.3 **Token Initialization Flow**

  

-  **Customer JWT token** is generated once **before the test suite** runs (`@BeforeSuite` in `BaseTest`).

- Token is stored in `TestContext` and reused in all relevant tests to avoid redundant logins.

- This ensures API calls requiring authentication run smoothly and maintain session state.

--- 

### **5.4 Execution Flow Example** (Add Item to Cart)


```

TestNG runs AddItemToCartTest

↓

Generate dynamic customer email with timestamp

↓

Call createCustomer() with admin token

↓

Generate customer JWT token

↓

Create empty cart with customer token

↓

Add product to cart with customer token

↓

Assert status code + response fields

  

```

  

---

 
# ☁️ 6. Maintenance & Integration

  

### 6.1 Git for SCM

  

```bash

git  init

git  add [README.md](http://readme.md/)

git  commit  -m  "first commit"

git  branch  -M  main

git  remote  add  origin <repo_url>

git  push  -u  origin  main

```

  

### **6.2 Jenkins Integration**

  

**New Item → Maven Project**

  

Under **Source Code Management**:

 
```bash

Git  Repo  URL  →  your  GitHub  repo

Branch  Specifier:  */main  or  */master

```  

Under **Build**:

```bash

Root  POM:  path/to/pom.xml

Goals  and  Options:  clean  test

```  

**Build Now** 

- Watch the Console Output to track the test execution.  

Configure **Publish HTML Reports**:  

- HTML directory: `reports`

- Index page: `index.html`

- This ensures Jenkins always picks the **latest static report**

- Archived timestamped reports remain for offline review

---
## 🧾 How to Run

### 🔧 Setup & Installation

**Clone the repo:**
```bash
git clone https://github.com/naveenkumarqa/magneto-api-framework.git
cd magneto-api-framework
```

**Install dependencies**
```bash 
mvn clean install
```
**Run any suite via:**
```bash
mvn clean test -DsuiteXmlFile=src/test/java/api/testSuite/Smoke-Suite.xml
```
*💡 Replace `Smoke-Suite.xml` with `Sanity-Suite.xml`, `Regression-Suite.xml`, or `Functional-Suite.xml` as needed.*

---
## 🙌 Author
**Naveen Kumar** 
SDET II | QA Automation Engineer
[GitHub](https://github.com/naveenkumarqa) | [LinkedIn](https://linkedin.com/in/naveenkumarqa) | [Portfolio](https://bento.me/naveenkumarqa)
