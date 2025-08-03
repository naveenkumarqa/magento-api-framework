package api.client;


import api.config.Routes;
import api.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CartClient {
	
	static String Base_URI = ConfigReader.getInstance().getProperty("base.uri");

    public static Response createCart(String customerToken) {
        return RestAssured.given()
                .baseUri(Base_URI)
                .header("Authorization", "Bearer " + customerToken)
                .contentType(ContentType.JSON)
            .when()
                .post(Routes.CREATE_CART);
    }

    public static Response addItemToCart(String customerToken, String payload) {
        return RestAssured.given()
                .baseUri(Base_URI)
                .header("Authorization", "Bearer " + customerToken)
                .contentType(ContentType.JSON)
                .body(payload)
            .when()
                .post(Routes.ADD_ITEM);
    }

    public static Response viewCartItems(String customerToken) {
        return RestAssured.given()
                .baseUri(Base_URI)
                .header("Authorization", "Bearer " + customerToken)
                .contentType(ContentType.JSON)
            .when()
                .get(Routes.GET_ITEMS);
    }
    
    public static Response updateCartItem(String customerToken, String payload, int itemId) {
    	return RestAssured.given()
    			.baseUri(Base_URI)
    			.header("Authorization", "Bearer " + customerToken)
    			.contentType(ContentType.JSON)
    			.body(payload)
    		.when()
    			.put(String.format(Routes.UPDATE_ITEMS, itemId));
    }

    public static Response deleteCartItem(String customerToken, int itemId) {
        return RestAssured.given()
                .baseUri(Base_URI)
                .header("Authorization", "Bearer " + customerToken)
                .contentType(ContentType.JSON)
            .when()
                .delete(String.format(Routes.DELETE_ITEM, itemId));
    }
}