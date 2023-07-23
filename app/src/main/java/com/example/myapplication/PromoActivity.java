package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PromoActivity extends AppCompatActivity {

    RecyclerView foodRecycler;
    FoodAdapter_Column foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        //dummy data food
        //List<Food> foodList = new ArrayList<>();
//        foodList.add(new Food("Fruit salad mix", "Free delivery", "22.500", "18.500", "5 left", R.drawable.assorted_sliced_fruits_in_white_ceramic_bowl_10927301));
//        foodList.add(new Food("Fruit salad mix", "Free delivery", "22.500", "18.500", "5 left", R.drawable.assorted_sliced_fruits_in_white_ceramic_bowl_10927301));

        // request data
        // Show Food in home page
        RequestQueue queue = Volley.newRequestQueue(PromoActivity.this);

        String endpoint = "shop/food?limit=10&page=1&sort=ctime";
        String url = ENV.URL_BASE + endpoint;

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
                                    foodList.add(food);
                                }

                                // Set up the RecyclerView with the foodList
                                setFoodRecycler(foodList);
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

        queue.add(jsonObjectRequest);
        queue.getCache().clear();

        // button event
        // Back to previous page
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setFoodRecycler(List<Food> foodList){
        foodRecycler = findViewById(R.id.foodColumn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        foodRecycler.setLayoutManager(layoutManager);
        foodAdapter = new FoodAdapter_Column(this, foodList);
        foodRecycler.setAdapter(foodAdapter);
    }
}