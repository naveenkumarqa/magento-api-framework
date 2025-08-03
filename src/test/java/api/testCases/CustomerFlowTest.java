package api.testCases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.client.CustomerClient;
import api.payload.PayloadGenerator;
import api.payload.PayloadLoader;
import api.testBase.BaseTest;
import api.utils.ConfigReader;
import api.utils.TestContext;
import io.restassured.response.Response;

public class CustomerFlowTest extends BaseTest {

    private static String custToken;
    private static String adminToken;

    @Test(priority = 1, groups={"sanity"})
    public void testCreateCustomer() throws Exception {
    	log.info("POST--> CREATE CUSTOMER");
        PayloadGenerator.generateCustomerData();

        String customerPayload = PayloadLoader.getCustomerPayload();
        adminToken = ConfigReader.getInstance().getProperty("admin.token");

        Response res = CustomerClient.createCustomer(customerPayload, adminToken);
        res.then().log().body();

        Assert.assertEquals(res.getStatusCode(), 200);
        Assert.assertTrue(res.getBody().asString().contains(PayloadGenerator.getEmail()));
        
    }

    @Test(priority = 2, groups={"sanity"})
    public void testGenerateCustomerToken() throws IOException {
    	log.info("POST--> CREATE CUSTOMER TOKEN");
        String tokenPayload = PayloadLoader.getTokenPayload();

        Response tokenRes = CustomerClient.generateToken(tokenPayload);
        tokenRes.then().log().body();

        Assert.assertEquals(tokenRes.getStatusCode(), 200);
        custToken = tokenRes.getBody().asString().replace("\"", ""); 
        TestContext.customerToken = custToken;
        log.info("Customer token is ------------> "+ custToken);
    }

    @Test(priority = 3, groups={"sanity"})
    public void testGetCustomerDetails() {
    	log.info("POST--> GET CUSTOMER USING TOKEN");
        Response res = CustomerClient.getCustomerDetails(custToken);
        res.then().log().body();

        Assert.assertEquals(res.getStatusCode(), 200);
        Assert.assertEquals(res.jsonPath().get("email"), PayloadGenerator.getEmail());
    }
}