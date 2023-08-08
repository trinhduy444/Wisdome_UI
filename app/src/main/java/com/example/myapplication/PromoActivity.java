package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapter.FoodAdapter_Column;
import com.example.myapplication.model.ENV;
import com.example.myapplication.model.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromoActivity extends AppCompatActivity {

    RecyclerView foodRecycler;
    FoodAdapter_Column foodAdapter;
    List<Food> tempFoodlist;
    TextView textView_count,textView_price,textView5;
    LinearLayout lnCheckout;

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("shoppingCart")) {
                String shoppingCart = sharedPreferences.getString("shoppingCart", "");
                handleCheckoutBar(shoppingCart);
                Log.e("text listen","run");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        lnCheckout = findViewById(R.id.lnCheckout);
        textView_count = findViewById(R.id.textViewCheckout_count);
        textView_price = findViewById(R.id.textViewCheckout_price);
        textView5 = findViewById(R.id.textView5);

        Log.e("activity listen","on create");
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

        //
        lnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PromoActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PromoActivity.this, DetailRestroActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("activity promo","on Resume");

        // Show Food in home page
        RequestQueue queue = Volley.newRequestQueue(PromoActivity.this);

        // request
        JsonObjectRequest jsonObjectRequest = requestFood();
        queue.add(jsonObjectRequest); // call food => show food
        queue.getCache().clear();

        // listen - checkout-------------
        SharedPreferences sharedPreferences = getSharedPreferences("my_sharedPreference", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unListen - checkout-------------
        Log.e("activity promo","on Pause");
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
    private void handleCheckoutBar(@NonNull String shoppingCart){
        if(!shoppingCart.equals("Cart dosen't exist")){
            try {
                // get food cart
                JSONObject obj = new JSONObject(shoppingCart);
                JSONArray array = obj.getJSONArray("shopingCart_foods");

                //
                int countFood = 0;
                int sumPrice = 0;

                // loop cart
                for (int i = 0; i < array.length(); i++) {
                    // food in cart
                    JSONObject foodObject = array.getJSONObject(i);
                    String idFood = foodObject.getString("cart_foodId");
                    int quantity = foodObject.getInt("quantity");
                    countFood += quantity;

                    // loop temp data ==> find price food
                    for(Food fd : tempFoodlist){
                        if(idFood.equals(fd.getFoodId())){
                            sumPrice += (quantity * Integer.parseInt(fd.getFoodOriginalPrice()));
                            break;
                        }
                    }
                }

                // logic checkout bar
                // show
                if(countFood != 0){
                    showCheckoutBar(countFood,sumPrice);
                }
                // hidden
                if(countFood == 0){
                    hiddenCheckoutBar();
                }

            } catch (Exception ex) {
                Log.e("promo activity", ex.toString());
            }
        }
    }
    private void showCheckoutBar(int countFood, int sumPrice){
        // show bar
        lnCheckout.setVisibility(View.VISIBLE);
        // render count and price
        textView_price.setText(Integer.toString(sumPrice )+ " vnd");
        textView_count.setText(Integer.toString(countFood )+ " Item");

    }
    private void hiddenCheckoutBar(){
        // hidden bar
        lnCheckout.setVisibility(View.GONE);
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
                                JSONArray metadataArray = response.getJSONArray("metadata");
                                for (int i = 0; i < metadataArray.length(); i++) {
                                    JSONObject foodObject = metadataArray.getJSONObject(i);

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
                                // set temp for query
                                tempFoodlist = foodList;

                                // call cart - checkout
                                RequestQueue queue = Volley.newRequestQueue(PromoActivity.this);
                                // call cart => show checkout, cart save 2 shared preference key = shoppingCart
                                JsonObjectRequest jsonObjectRequest1 = requestCart();
                                queue.add(jsonObjectRequest1);
                                queue.getCache().clear();

                            } else {
                                Log.d("response", response.get("message").toString());
                                Toast.makeText(PromoActivity.this, "Fail to Load Food", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(PromoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
        return jsonObjectRequest;
    }

    @NonNull
    private JsonObjectRequest requestCart(){
        // url
        String endpoint = "shoppingCart/getAllFood";
        String url = ENV.URL_BASE + endpoint;

        // get token
        // Get the shared preferences object
        SharedPreferences sharedPreferences =
                getSharedPreferences("my_sharedPreference",MODE_PRIVATE);

        // Retrieve the value for a key
        String accessToken = sharedPreferences.getString("accessToken", "");
        String refreshToken = sharedPreferences.getString("refreshToken", "");

        // request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override // success
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("statusCode").equals(200)){
                        Log.e("cart response :",response.toString());

                        String shoppingCart = response.getString("metadata");

                        // save shared preference
                        // use shared preference - mob private
                        SharedPreferences sharedPreferences =
                                getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("shoppingCart",shoppingCart);
                        edit.apply();

                        // Set up the RecyclerView with the foodList
                        setFoodRecycler(tempFoodlist);

                        /***
                         * checkout - logic
                         */
                        if(!shoppingCart.equals("Cart dosen't exist")){
                                // get food cart
                                JSONObject obj = response.getJSONObject("metadata");
                                JSONArray array = obj.getJSONArray("shopingCart_foods");

                                //
                                int countFood = 0;
                                int sumPrice = 0;

                            if(array.length() > 0) { // not empty
                                // loop cart
                                for (int i = 0; i < array.length(); i++) {
                                    // food in cart
                                    JSONObject foodObject = array.getJSONObject(i);
                                    String idFood = foodObject.getString("cart_foodId");
                                    int quantity = foodObject.getInt("quantity");
                                    countFood += quantity;

                                    // loop temp data ==> find price food
                                    for(Food fd : tempFoodlist){
                                        if(idFood.equals(fd.getFoodId())){
                                            sumPrice += (quantity * Integer.parseInt(fd.getFoodOriginalPrice()));
                                            break;
                                        }
                                    }
                                }
                            }
                                // logic checkout bar
                                // show
                                if(countFood != 0){
                                    showCheckoutBar(countFood,sumPrice);
                                }
                                // hidden
                                if(countFood == 0){
                                    hiddenCheckoutBar();
                                }
                        }
                    }
                    else{
                        Log.d("response",response.get("message").toString());
                        Toast.makeText(PromoActivity.this,"Login Fail", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override // failed
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                Toast.makeText(PromoActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("authorization","Bearer " + accessToken);
                headers.put("Cookie", "refreshToken=" + refreshToken);
                return headers;
            }
        };

        return jsonObjectRequest;
    }
}