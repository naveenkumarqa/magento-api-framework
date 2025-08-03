package api.services;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import api.client.CartClient;
import api.payload.PayloadLoader;
import api.utils.LoggerUtil;
import api.utils.TestContext;
import io.restassured.response.Response;

public class CartService {

	private static String customerToken;

	static Logger log = LoggerUtil.getLogger(CartService.class);

	@Test()
	public static void performCartFlow() throws Exception {

		// Step 1: Create Cart
		Response createCartRes = CartClient.createCart(customerToken);
		log.info("Create Cart Customer token");
		createCartRes.then().log().body();
		Assert.assertEquals(createCartRes.getStatusCode(), 200, "Cart creation failed");

		// Save cart ID to context
		String cartId = createCartRes.getBody().asString().replace("\"", "");
		log.info("Cart Id genenrated");
		TestContext.cartId = cartId;

		// Step 2: Add Item to Cart
		String addItemPayload = PayloadLoader.getAddItemPayload("24-MB01", cartId);
		Response addItemRes = CartClient.addItemToCart(customerToken, addItemPayload);
		log.info("Create Cart Customer token");
		addItemRes.then().log().body();
		Assert.assertEquals(addItemRes.getStatusCode(), 200, "Add to cart failed");

		// Save item ID for update/delete
		int itemId = addItemRes.jsonPath().getInt("item_id");
		TestContext.addedItemId = itemId;

		// Step 3: View Cart
		Response viewCartRes = CartClient.viewCartItems(customerToken);
		viewCartRes.then().log().body();
		Assert.assertEquals(viewCartRes.getStatusCode(), 200, "View cart failed");

		// Step 4: Update Cart Item
		String updatePayload = PayloadLoader.getUpdateCartPayload("24-MB01", cartId, 1);  // reduce qty to 1
		Response updateRes = CartClient.updateCartItem(customerToken, updatePayload, itemId);
		updateRes.then().log().body();
		Assert.assertEquals(updateRes.getStatusCode(), 200, "Update cart item failed");

		// Step 5: Delete Cart Item
		Response deleteRes = CartClient.deleteCartItem(customerToken, itemId);
		deleteRes.then().log().body();
		Assert.assertEquals(deleteRes.getStatusCode(), 200, "Delete cart item failed");
	}
}