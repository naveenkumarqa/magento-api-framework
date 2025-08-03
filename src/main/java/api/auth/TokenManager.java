package api.auth;

import api.client.CustomerClient;
import api.payload.PayloadGenerator;
import api.payload.PayloadLoader;
import api.utils.ConfigReader;
import io.restassured.response.Response;

public class TokenManager {

    public static String generateCustomerToken() throws Exception {
        // Generate fresh customer data 
        PayloadGenerator.generateCustomerData(); // sets static fields
        
        String customerPayload = PayloadLoader.getCustomerPayload();
        String tokenPayload = PayloadLoader.getTokenPayload();
        
        String adminToken = ConfigReader.getInstance().getProperty("admin.token");

        // Create customer using admin token
        Response createRes = CustomerClient.createCustomer(customerPayload, adminToken);
        if (createRes.getStatusCode() != 200) {
            throw new RuntimeException("Customer creation failed");
        }

        // Generate token
        Response tokenRes = CustomerClient.generateToken(tokenPayload);
        String token = tokenRes.getBody().asString().replace("\"", "");
        return token;
    }
}