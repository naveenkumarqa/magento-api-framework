package api.payload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import api.model.UpdateCartItem;

public class PayloadLoader {

	public static String loadPayload(String fileName) throws IOException {
		return new String(Files.readAllBytes(Paths.get("./src/test/resources/payloads/" + fileName)));
	}

	public static String getCustomerPayload() throws IOException {
		String raw = loadPayload("CustomerTemplate.json");

		return raw
				.replace("{{email}}", PayloadGenerator.getEmail())
				.replace("{{firstname}}", PayloadGenerator.getFirstname())
				.replace("{{lastname}}", PayloadGenerator.getLastname())
				.replace("{{password}}", PayloadGenerator.getPassword());
	}

	public static String getTokenPayload() throws IOException {
		String raw = loadPayload("TokenTemplate.json");

		return raw
				.replace("{{email}}", PayloadGenerator.getEmail())
				.replace("{{password}}",PayloadGenerator.getPassword());
	}

	public static String getAddItemPayload(String sku, String cartId) throws IOException {
		String raw = loadPayload("CartItemTemplate.json");

		return raw
				.replace("{{sku_id}}", sku)
				.replace("{{cart_id}}", cartId);
	}

	public static String getUpdateCartPayload(String sku, String quoteId, int qty) throws Exception {
		UpdateCartItem updateItem = new UpdateCartItem(sku, qty, quoteId);

		// creates empty JSON structure under cartItem key
		Map<String,Object> updatePayload = new HashMap<>();
		updatePayload.put("cartItem", updateItem);
		
		// Use Jackson to Convert it to JSON
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(updatePayload);
	}
}