package api.testCases;

import api.client.CartClient;
import api.payload.PayloadLoader;
import api.testBase.BaseTest;
import api.utils.TestContext;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CartFlowTest extends BaseTest {

	private static String cartId;          
    private static int addedItemId;
    private static String custToken;
    private static String skuId = "24-MB01";

    
    @BeforeClass 
    public void custTokeninit() {
    	custToken = TestContext.customerToken;
        Assert.assertNotNull(custToken, "Customer token is null");
        
    }

    @Test(priority = 1, groups={"sanity"})
    public void testCreateCart() {
    	custTokeninit();
    	log.info("POST--> CREATE CART");
    	
        Response res = CartClient.createCart(custToken);
        res.then().log().body();

        Assert.assertEquals(res.getStatusCode(), 200);

        // Extract cartId from response (usually plain text)
        cartId = res.getBody().asString().replace("\"", ""); 
        TestContext.cartId = cartId;
        log.info("Cart ID is --------------------> "+ cartId);
    }

    @Test(priority = 2, groups={"sanity"})
    public void testAddItemToCart() throws Exception {
    	log.info("POST--> ADD CART ITEMS");
        String addItemPayload = PayloadLoader.getAddItemPayload(skuId, cartId); // Use cartId here

        Response res = CartClient.addItemToCart(custToken, addItemPayload);
        res.then().log().body();

        Assert.assertEquals(res.getStatusCode(), 200, "Add to cart failed.");

        // Save item ID for delete step
        addedItemId = res.jsonPath().getInt("item_id");
        TestContext.addedItemId = addedItemId;
        log.info("Item id is ------------> "+ addedItemId);
    }

    @Test(priority = 3, groups={"sanity"})
    public void testViewCartItems() {
    	log.info("GET--> VIEW CART");
        Response res = CartClient.viewCartItems(custToken);
        res.then().log().body();

        Assert.assertEquals(res.getStatusCode(), 200);
        Assert.assertTrue(res.getBody().asString().contains(String.valueOf(addedItemId)));
    }
    
    @Test(priority = 4)
    public void testUpdatCartItem() throws Exception {
    	log.info("PUT--> UPDATE CART");
    	String updateItemPayload = PayloadLoader.getUpdateCartPayload(skuId, cartId, 1);
    	log.info("--------------------------->>>>>>>>>>>>>>>>>>>>>");
    	log.info(updateItemPayload);
    	
    	Response res = CartClient.updateCartItem(custToken, updateItemPayload , addedItemId);
    	res.then().log().body();
    	
    	Assert.assertEquals(res.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testDeleteCartItem() {
    	log.info("DELETE--> DELETE CART");
        Response res = CartClient.deleteCartItem(custToken, addedItemId);
        res.then().log().body();

        Assert.assertEquals(res.getStatusCode(), 200);
    }
}