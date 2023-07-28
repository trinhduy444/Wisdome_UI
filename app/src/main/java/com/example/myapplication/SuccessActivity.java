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
import android.widget.FrameLayout;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuccessActivity extends AppCompatActivity {

    FrameLayout sheet;
    TextView textView_seeAll,textView_continueBuy;

    RecyclerView foodRecycler;
    FoodAdapter_Column foodAdapter;
    List<Food> tempFoodlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        textView_seeAll = findViewById(R.id.textView_seeAll);
        textView_continueBuy = findViewById(R.id.textView_continueBuy);

        sheet = findViewById(R.id.sheet);
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(sheet);
        bottomSheetBehavior.setPeekHeight(140);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // navigation event
        textView_seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessActivity.this, PromoActivity.class);
                startActivity(intent);
            }
        });

        textView_continueBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("activity detail","on Resume");

        // Show Food in home page
        RequestQueue queue = Volley.newRequestQueue(SuccessActivity.this);

        // request
        JsonObjectRequest jsonObjectRequest = requestFood();
        queue.add(jsonObjectRequest); // call food => show food
        queue.getCache().clear();

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
                                RequestQueue queue = Volley.newRequestQueue(SuccessActivity.this);
                                // call cart => show checkout, cart save 2 shared preference key = shoppingCart
                                JsonObjectRequest jsonObjectRequest1 = requestCart();
                                queue.add(jsonObjectRequest1);
                                queue.getCache().clear();

                            } else {
                                Log.d("response", response.get("message").toString());
                                Toast.makeText(SuccessActivity.this, "Fail to Load Food", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(SuccessActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                    }
                    else{
                        Log.d("response",response.get("message").toString());
                        Toast.makeText(SuccessActivity.this,"Login Fail", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override // failed
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                Toast.makeText(SuccessActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
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