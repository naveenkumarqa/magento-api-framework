#  Magento API Test Automation Framework

An end-to-end API testing framework built using **Java**, **Rest Assured**, and **TestNG**, covering Customer and Cart services for Magento. Designed with modular architecture, grouped test execution (Smoke, Sanity, Regression), reusable service layers, and CI/CD readiness.

---

## 🚀 Tech Stack

- **Java 8+**
- **Rest Assured**
- **TestNG**
- **Extent Report** 
- **Log4j2** (for logging)
- **Jackson** (for JSON serialization)
- **Faker** (for test data generation)
-  **Maven** (build and dependency management)

---

## 📁 Project Structure
```text
src/  
├── main/  
│ └── java/  
│ ├── api.auth/ # Token generation/management  
│ ├── api.client/ # API request clients (CustomerClient, CartClient)  
│ ├── api.model/ # POJOs for request payloads  
│ ├── api.payload/ # JSON templates  
│ ├── api.services/ # Business flow chaining (CustomerService, CartService)  
│ └── api.utils/ # Utilities (FakerUtils, PayloadLoader, ConfigReader)  
├── test/  
│ └── java/  
│ ├── api.testBase/ # BaseTest class, TestContext  
│ ├── api.testCases/ # TestNG test classes (CustomerFlowTest, CartFlowTest)  
│ └── api.testSuite/ # Suite XML files (Smoke, Sanity, Regression, Functional)  
└── test/  
└── resources/  
└── log4j2.xml # Logging configuration
```
---

## 🔁 **Token Handling Strategy**

* **Admin token** kept in `config.properties`, accessed via `ConfigReader`
* **Customer token** dynamically generated using:

  * Payload from `PayloadGenerator`
  * API call from `TokenManager.generateCustomerToken()`
* **Shared via** `TestContext.customerToken` across tests and services

---

## 🏗 **Creating Payloads**

Two types:

* **External JSON Templates** (for easy editing)
* **POJO-based serialization** using `ObjectMapper` (for dynamic payloads)

Example JSON-driven:

* `CartItemTemplate.json` → uses `{{sku_id}}` and `{{cart_id}}` placeholders
* Loaded via `PayloadLoader.loadPayload("filename.json")`

Example POJO-driven:

* `UpdateCartItem.java` → converted to JSON using Jackson
* Supports nested structure via wrapper class (e.g., `Map<String, Object>` with `"cartItem"` key)

---

## 📦 **Client Classes (api.client)**

Each API has a dedicated client:

* `CustomerClient.java`

  * `createCustomer(payload, token)`
  * `generateToken(payload)`
  * `getCustomerDetails(customerToken)`

* `CartClient.java`

  * `createCart(customerToken)`
  * `addItemToCart(customerToken, payload)`
  * `viewCartItems(customerToken)`
  * `updateCartItem(customerToken, payload, itemId)`
  * `deleteCartItem(customerToken, itemId)`

All use `RestAssured.given()` with headers, payloads, and base URI from `Routes`.

---

## 🧪 **TestContext Sharing**

Shared class (`TestContext`) to persist state between tests:

* `customerToken`
* `cartId`
* `addedItemId`

Used instead of passing data manually across test methods or services.

---

## 🔁 **API Services (api.services)**

* `CustomerService`

  * Calls customer creation, token generation, and retrieval
  * Uses `PayloadGenerator`, `CustomerClient`
  * All steps are API chained

* `CartService`

  * Generates customer token using `TokenManager`
  * Performs create, add, view, update, delete operations in order
  * Uses `PayloadLoader`, `CartClient`, `UpdateCartItem` POJO
  * Manages itemId and cartId using `TestContext`

---

## 🧪 **TestNG Test Classes (api.testCases)**

* `CustomerFlowTest`

  * Create customer
  * Generate token
  * Get customer details

* `CartFlowTest`

  * @BeforeClass: Fetch token from `TestContext`
  * Create cart, add item, view cart, update, delete
  * Each test uses `priority` and `groups` annotations

---

## 🔁  **Execution Management (testng.xml)**

### Sample: E2E Sanity Suite

```xml
<suite name="Magneto-Cart-E2E-Sanity-APISuite">
  <test name="Sanity Suite" preserve-order="true">
    <classes>
      <class name="api.testCases.CustomerFlowTest"/>
      <class name="api.testCases.CartFlowTest"/>
    </classes>
  </test>
</suite>
```

---

## 🔄 **Retry Analyzer Setup**

* Create `RetryAnalyzer` class implementing `IRetryAnalyzer`
* Retry failed test cases 1 or 2 times
* Plug into `BaseTest` via `@Listeners` or TestNG config

---

## 🔍  **Logging & Configuration**

* Use `Logger` from Log4j2 inside `BaseTest`
* All config values fetched using `ConfigReader.getInstance().getProperty("key")`
* `BaseTest` handles:

  * Logger init
  * Token init via `@BeforeSuite`
  * Common error handling (optional)

---


## 📋 **Test Case Mapping**

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
