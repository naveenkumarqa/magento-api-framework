package api.services;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.client.CustomerClient;
import api.payload.PayloadGenerator;
import api.payload.PayloadLoader;
import api.utils.ConfigReader;
import api.utils.TestContext;
import io.restassured.response.Response;

public class CustomerService {

	@Test()
	public static void performCustomerFlow() throws Exception {

		// Ensure dynamic fields like email/password are generated
		PayloadGenerator.generateCustomerData();

		// Step 1: Load customer creation payload		
		String customerPayload = PayloadLoader.getCustomerPayload();
		String adminToken = ConfigReader.getInstance().getProperty("admin.token");

		// Step 2: Create customer
		Response createResponse = CustomerClient.createCustomer(customerPayload, adminToken);
		createResponse.then().log().body();
		Assert.assertEquals(createResponse.getStatusCode(), 200, "Customer creation failed");

		// Step 3: Prepare payload for token
		String tokenPayload = PayloadLoader.getTokenPayload(); // Uses email & password from generator

		// Step 4: Generate token
		Response tokenRes = CustomerClient.generateToken(tokenPayload);
		tokenRes.then().log().body();
		Assert.assertEquals(tokenRes.getStatusCode(), 200, "Token generation failed");

		// Step 5: Save token to context
		String token = tokenRes.getBody().asString().replace("\"", "");
		TestContext.customerToken = token;

		// Step 6: Get customer details
		Response getRes = CustomerClient.getCustomerDetails(token);
		getRes.then().log().body();
		Assert.assertEquals(getRes.getStatusCode(), 200, "Fetching customer details failed");
	}
}