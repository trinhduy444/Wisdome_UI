package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapter.FoodAdapter_Column;
import com.example.myapplication.model.ENV;
import com.example.myapplication.model.Food;
import com.example.myapplication.parser.ShoppingCartParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CartActivity extends AppCompatActivity {

    RecyclerView foodRecycler;
    FoodAdapter_Column foodAdapter;
    EditText edtx_Note;

//    Integer totalPrice;
    TextView TW_toPay,TW_itemTotal,TW_placeOrder;

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("shoppingCart")) {
                // request food
                RequestQueue queue = Volley.newRequestQueue(CartActivity.this);

                // call
                JsonObjectRequest jsonObjectRequest = requestFood();
                queue.add(jsonObjectRequest); // call food => show food
                queue.getCache().clear();

                Log.e("text listen","run2");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        /**
         *  check cart have item
         *  no => do nothing
         *  yes => next()
         *  get id in cart and request food
         *  set food and render
         *  calc total price
         *  listener item change => render total price
         */
        TW_itemTotal = findViewById(R.id.textView_itemTotal);
        TW_placeOrder = findViewById(R.id.textView_placeOrder);
        TW_toPay = findViewById(R.id.textView_toPay);
        edtx_Note = findViewById(R.id.Edtx_Note);

        Log.e("activity cart","on create");

        // button event ------------------------------------------------
        // Back to previous page
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        LinearLayout lnCheckout = findViewById(R.id.lnCheckout);
        lnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                createOrders();createOrders();
                saveNote(edtx_Note.getText().toString());
                Intent intent = new Intent(CartActivity.this, PaymentMethodActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void saveNote(String noteContent){
        SharedPreferences sharedPreferences =
                getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("note", noteContent);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("activity cart","on Resume");
        List<String> cartFood = getCart();
        // check
        if(cartFood.size() > 0){ // cart have food
            // request food
            RequestQueue queue = Volley.newRequestQueue(CartActivity.this);

            // call
            JsonObjectRequest jsonObjectRequest = requestFood();
            queue.add(jsonObjectRequest); // call food => show food
            queue.getCache().clear();
        }

        // listen - checkout-------------
        SharedPreferences sharedPreferences = getSharedPreferences("my_sharedPreference", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unlisten - checkout-------------
        Log.e("activity cart","on Pause");
        SharedPreferences sharedPreferences = getSharedPreferences("my_sharedPreference", Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    private void setFoodRecycler(List<Food> foodList){
        foodRecycler = findViewById(R.id.foodColumn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        foodRecycler.setLayoutManager(layoutManager);
        foodAdapter = new FoodAdapter_Column(this, foodList);
        foodRecycler.setAdapter(foodAdapter);
    }
    @NonNull
    private JsonObjectRequest requestFood(){
        String endpoint = "shop/food?limit=10&page=1&sort=ctime";
        String url = ENV.URL_BASE + endpoint;

        // request food
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("statusCode").equals(200)) {

                                List<Food> foodList = new ArrayList<>();
                                List<String> cartFood = getCart();
                                JSONArray metadataArray = response.getJSONArray("metadata");

                                // loop foodList
                                for (int i = 0; i < metadataArray.length(); i++) {
                                    JSONObject foodObject = metadataArray.getJSONObject(i);
                                    String id = foodObject.getString("_id");

                                    // check food in cart ?
                                    if(cartFood.contains(id)){ // yes
                                        // Parse food data from JSON response
                                        String foodName = foodObject.getString("food_name");
                                        String foodDescription = foodObject.getString("food_description");
                                        String foodImageURL = "http://delivery-9thd.onrender.com/" + foodObject.getString("food_image");
                                        String foodPrice = foodObject.getString("food_price");
                                        String foodAmount = "10";
                                        Log.d("link image", foodImageURL);

                                        // Create a new Food object and add it to the foodList
                                        Food food = new Food(foodName, foodDescription, foodPrice, foodPrice, foodAmount, foodImageURL);

                                        // set id food
                                        food.setFoodId(foodObject.getString("_id"));
                                        foodList.add(food);
                                    }
                                }

                                setFoodRecycler(foodList);
                                renderTotalPay(foodList);
                            } else {
                                Log.d("response", response.get("message").toString());
                                Toast.makeText(CartActivity.this, "Fail to Load Food", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("error", "Get foods Fail");
                            throw new RuntimeException(e);
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", error.toString());
                                Toast.makeText(CartActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
        return jsonObjectRequest;
    }
    public List<String> getCart(){
        // get cart
        SharedPreferences sharedPreferences =
                getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
        String shoppingCart = sharedPreferences.getString("shoppingCart", "Cart dosen't exist");
//        Log.d("SharedPreferences", "Cart gom co: " + shoppingCart);

        List<String> cartFood = new ArrayList<String>();

        if(!shoppingCart.equals("Cart dosen't exist")){
            try {
                // get food cart
                JSONObject obj = new JSONObject(shoppingCart);
                JSONArray array = obj.getJSONArray("shopingCart_foods");
                int count = array.length();
                if(count > 0){ // not empty
                    // loop cart
                    for (int i = 0; i < count; i++) {
                        JSONObject foodObject = array.getJSONObject(i);
                        cartFood.add(foodObject.getString("cart_foodId"));
                    }
                }
            } catch (Throwable t) {
                Log.e("cartActivity", "Could not parse malformed JSON:");
            }
        }

        return cartFood;
    }

    @SuppressLint("SetTextI18n")
    public void renderTotalPay(List<Food> foodList){
        // get cart
        SharedPreferences sharedPreferences =
                getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
        String shoppingCart = sharedPreferences.getString("shoppingCart", "Cart dosen't exist");

        //
        int countItem = 0;
        int sumPrice = 0;

        if(!shoppingCart.equals("Cart dosen't exist") && foodList.size() > 0){
            try {
                // get food cart
                JSONObject obj = new JSONObject(shoppingCart);
                JSONArray array = obj.getJSONArray("shopingCart_foods");
                int count = array.length();
                if(count > 0){ // not empty
                    // loop cart
                    for (int i = 0; i < count; i++) {
                        JSONObject foodObject = array.getJSONObject(i);
                        // food in cart
                        String idFood = foodObject.getString("cart_foodId");
                        int quantity = foodObject.getInt("quantity");
                        countItem += quantity;

                        // loop food list ==> find price food
                        for(Food fd : foodList){
                            if(idFood.equals(fd.getFoodId())){
                                sumPrice += (quantity * Integer.parseInt(fd.getFoodOriginalPrice()));
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("totalPrice", sumPrice);
                                editor.apply();

                                break;
                            }
                        }

                    }
                }

            } catch (Throwable t) {
                Log.e("cartActivity", "Could not parse malformed JSON:");
            }
        }

        // render
        TW_toPay.setText(Integer.toString(sumPrice));
        TW_placeOrder.setText(Integer.toString(sumPrice));
        TW_itemTotal.setText(Integer.toString(countItem));
    }

}