package api.testBase;

import api.utils.ConfigReader;
import api.auth.TokenManager;
import api.utils.TestContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

import io.restassured.RestAssured;

import java.lang.reflect.Method;

public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected static final String BASE_URI = ConfigReader.getInstance().getProperty("base.uri");

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() throws Exception {
        TestContext.customerToken = TokenManager.generateCustomerToken();
        log.info("Customer token initialized: " + TestContext.customerToken);
    }

    @BeforeClass(alwaysRun = true)
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        log.info("Base URI set to: " + BASE_URI);
    }

    @BeforeMethod(alwaysRun = true)
    public void logStart(Method method) {
        log.info("Starting Test: " + method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void logEnd(Method method) {
        log.info("Finished Test: " + method.getName());
    }
}