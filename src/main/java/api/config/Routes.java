package api.config;

public class Routes {
    
    // Base paths as prefix 
    public static final String CUSTOMER_PREFIX = "/rest/default/V1/customers";
    public static final String CART_PREFIX = "/rest/default/V1/carts";

    // CUSTOMER
    public static final String CREATE_CUSTOMER = CUSTOMER_PREFIX;
    public static final String GENERATE_TOKEN = "/rest/default/V1/integration/customer/token";
    public static final String GET_CUSTOMER = CUSTOMER_PREFIX + "/me";

    // CART
    public static final String CREATE_CART = CART_PREFIX + "/mine";
    public static final String ADD_ITEM = CART_PREFIX + "/mine/items";
    public static final String GET_ITEMS = CART_PREFIX + "/mine/items";
    public static final String DELETE_ITEM = CART_PREFIX + "/mine/items/%d";  // requires itemId
    public static final String UPDATE_ITEMS = CART_PREFIX + "/mine/items/%d";
}