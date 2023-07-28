package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.model.ENV;
import com.example.myapplication.model.Food;
import com.example.myapplication.adapter.FoodAdapter_Row;
import com.example.myapplication.model.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import com.example.myapplication.adapter.FoodAdapter_Row;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView foodRecycler;
    FoodAdapter_Row foodAdapter;
    RelativeLayout showUserSettingButton,showHomeButton,showCartButton;
    TextView seeAllPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        String jsonDataReceive = getIntent().getStringExtra("data");
        Log.d("Data","data receive from login page" + jsonDataReceive);

        // Show Food in home page
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);

        String endpoint = "shop/food?limit=10&page=1&sort=ctime";
        String url = ENV.URL_BASE + endpoint;
//      String url = "https://delivery-9thd.onrender.com/api/v1/shop/food?limit=10&page=1&sort=ctime";
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

                                    // check id food
                                    //Log.d("response", foodObject.getString("_id"));

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
                                Toast.makeText(HomeActivity.this, "Fail to Load Food", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

        queue.add(jsonObjectRequest);
        queue.getCache().clear();

        // Direct to Setting layout
        showUserSettingButton = findViewById(R.id.showUserSetting);
        showUserSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserSettingActivity.class);
                intent.putExtra("data", jsonDataReceive);
                startActivity(intent);
            }
        });
        // Direct to all Promo ( Button " See All  " )
        seeAllPromo = findViewById(R.id.allPromo);
        seeAllPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PromoActivity.class);
                startActivity(intent);
            }
        });

        // set button listener
        showCartButton = findViewById(R.id.showCartButton2);
        showCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ToolbarBottomActivity.this, "ok run", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setFoodRecycler(List<Food> foodList){
        foodRecycler = findViewById(R.id.foodRow);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        foodRecycler.setLayoutManager(layoutManager);
        foodAdapter = new FoodAdapter_Row(this, foodList);
        foodRecycler.setAdapter(foodAdapter);
    }




}