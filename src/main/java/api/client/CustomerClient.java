package api.client;

import api.config.Routes;
import api.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CustomerClient {

	static String Base_URI = ConfigReader.getInstance().getProperty("base.uri");
	
    public static Response createCustomer(String payload, String adminToken) {
        return RestAssured.given()
                .baseUri(Base_URI)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(payload)
            .when()
                .post(Routes.CREATE_CUSTOMER);
    }

    public static Response generateToken(String tokenPayload) {
        return RestAssured.given()
                .baseUri(Base_URI)
                .contentType(ContentType.JSON)
                .body(tokenPayload)
            .when()
                .post(Routes.GENERATE_TOKEN);
    }

    public static Response getCustomerDetails(String customerToken) {
        return RestAssured.given()
                .baseUri(Base_URI)
                .header("Authorization", "Bearer " + customerToken)
                .contentType(ContentType.JSON)
            .when()
                .get(Routes.GET_CUSTOMER);
    }
}