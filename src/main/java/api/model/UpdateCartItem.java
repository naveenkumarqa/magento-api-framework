package api.model;

public class UpdateCartItem {
    private String sku;
    private int qty;
    private String quote_id;

    // Constructors
    public UpdateCartItem() {}   //	Needed by Jackson during serialization
 
    public UpdateCartItem(String sku, int qty, String quote_id) {		// Used for creating objects in code
        this.sku = sku;
        this.qty = qty;
        this.quote_id = quote_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }
}