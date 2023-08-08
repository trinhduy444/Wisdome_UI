package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapter.FoodAdapter_column_searchPage;
import com.example.myapplication.model.ENV;
import com.example.myapplication.model.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFoodActivity extends AppCompatActivity {

    RecyclerView foodRecycler;
    FoodAdapter_column_searchPage foodAdapter;

    RelativeLayout showUserSettingButton,showHomeButton,showCartButton;

    EditText editText_searchFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        /**
         *  first visit => request get and show some food
         *  action search food ----------
         *  request food with key word
         *  get food
         *  render food
         */

        // request food
        RequestQueue queue = Volley.newRequestQueue(SearchFoodActivity.this);

        // call
        JsonObjectRequest jsonObjectRequest = requestFood();
        queue.add(jsonObjectRequest); // call food => show food
        queue.getCache().clear();

        // event search
        editText_searchFood = findViewById(R.id.editText_searchFood);
        editText_searchFood.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                // Check if the action is the "submit" action
                if (i == EditorInfo.IME_ACTION_SEND) {

                    String text = editText_searchFood.getText().toString();

                    // request food with key
                    RequestQueue queue = Volley.newRequestQueue(SearchFoodActivity.this);

                    // call
                    JsonObjectRequest jsonObjectRequest = requestFoodWithKey(text);
                    queue.add(jsonObjectRequest); // find food => show food
                    queue.getCache().clear();

                    return true;
                }
                // Return false if the event is not handled
                return false;
            }
        });

        editText_searchFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editText_searchFood.getText().toString();
                Log.e("dm",text);
                if(text.isEmpty()){
                    // request food
                    RequestQueue queue = Volley.newRequestQueue(SearchFoodActivity.this);

                    // call
                    JsonObjectRequest jsonObjectRequest = requestFood();
                    queue.add(jsonObjectRequest); // call food => show food
                    queue.getCache().clear();
                }
            }
        });

        // navi event
        // go CART
        showCartButton = findViewById(R.id.showCartButton2);
        showCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ToolbarBottomActivity.this, "ok run", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchFoodActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // go SETTING
        showUserSettingButton = findViewById(R.id.showUserSetting);
        showUserSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchFoodActivity.this, UserSettingActivity.class);
                startActivity(intent);
            }
        });

        // go HOME
        showHomeButton = findViewById(R.id.showHome);
        showHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchFoodActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setFoodRecycler(List<Food> foodList){
        foodRecycler = findViewById(R.id.foodRow);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        foodRecycler.setLayoutManager(layoutManager);
        foodAdapter = new FoodAdapter_column_searchPage(this, foodList);
        foodRecycler.setAdapter(foodAdapter);
    }

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

                                    // check id food
                                    //Log.d("response", foodObject.getString("_id"));

                                    // Parse food data from JSON response
                                    String foodName = foodObject.getString("food_name");
                                    String foodDescription = foodObject.getString("food_description");
                                    String foodImageURL = "http://delivery-9thd.onrender.com/" + foodObject.getString("food_image");
                                    String foodPrice = foodObject.getString("food_price");
                                    String foodAmount = "10";

                                    // Create a new Food object and add it to the foodList
                                    Food food = new Food(foodName, foodDescription, foodPrice, foodPrice, foodAmount, foodImageURL);
                                    foodList.add(food);
                                }

                                // Set up the RecyclerView with the foodList
                                setFoodRecycler(foodList);


                            } else {
                                Log.d("response", response.get("message").toString());
                                Toast.makeText(SearchFoodActivity.this, "Fail to Load Food", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(SearchFoodActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
        return jsonObjectRequest;
    }

    private JsonObjectRequest requestFoodWithKey(String keyWord){
        String endpoint = "shop/food/search?limit=10&page=1&keySearch=" + keyWord;
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

                                    // check id food
                                    //Log.d("response", foodObject.getString("_id"));

                                    // Parse food data from JSON response
                                    String foodName = foodObject.getString("food_name");
                                    String foodDescription = foodObject.getString("food_description");
                                    String foodImageURL = "http://delivery-9thd.onrender.com/" + foodObject.getString("food_image");
                                    String foodPrice = foodObject.getString("food_price");
                                    String foodAmount = "10";

                                    // Create a new Food object and add it to the foodList
                                    Food food = new Food(foodName, foodDescription, foodPrice, foodPrice, foodAmount, foodImageURL);
                                    foodList.add(food);
                                }

                                // Set up the RecyclerView with the foodList
                                setFoodRecycler(foodList);


                            } else {
                                Log.d("response", response.get("message").toString());
                                Toast.makeText(SearchFoodActivity.this, "Fail to Load Food", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(SearchFoodActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
        return jsonObjectRequest;
    }
}