package com.example.myapplication.parser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ShoppingCartParser {
    public static class CartItem {
        String cartFoodId;
        int quantity;
        public CartItem(String cartFoodId, int quantity) {
            this.cartFoodId = cartFoodId;
            this.quantity = quantity;
        }
    }

    public static CartItem[] parseCartItems(String shoppingCart) {
        try {
            JSONObject cartJson = new JSONObject(shoppingCart);
            JSONArray foodArray = cartJson.getJSONArray("shopingCart_foods");

            CartItem[] cartItems = new CartItem[foodArray.length()];

            for (int i = 0; i < foodArray.length(); i++) {
                JSONObject foodObject = foodArray.getJSONObject(i);
                String cartFoodId = foodObject.getString("cart_foodId");
                int quantity = foodObject.getInt("quantity");
                cartItems[i] = new CartItem(cartFoodId, quantity);
            }

            return cartItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null; // Return null in case of any parsing errors
    }

}
