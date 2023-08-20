package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.model.ENV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentMethodActivity extends AppCompatActivity {

    Button button4;

    View view4,view5,view6,view3;

    RadioButton radioButton2;

    ImageButton imageButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        button4 = findViewById(R.id.button4); // main
        radioButton2 = findViewById(R.id.radioButton2);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        view3 = findViewById(R.id.view3);
        imageButton3 = findViewById(R.id.imageButton3);

        // button cash payment
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton2.setChecked(true);
            }
        });

        // button credit payment
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaymentMethodActivity.this, NewAddCardActivity.class);
                startActivity(intent);
            }
        });

        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaymentMethodActivity.this, NewAddCardActivity.class);
                startActivity(intent);
            }
        });

        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaymentMethodActivity.this, NewAddCardActivity.class);
                startActivity(intent);
            }
        });

        // button go back
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // get total price
        SharedPreferences sharedPreferences =
                getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
        Integer totalPrice = sharedPreferences.getInt("totalPrice",0);

        button4.setText("Pay " + totalPrice + "VND");

        // create order
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrders();
                Intent intent = new Intent(PaymentMethodActivity.this, SuccessActivity.class);
                startActivity(intent);
            }
        });

    }

    public static Map<String, Integer> parseCartItems(String shoppingCart) {
        Map<String, Integer> cartItemsMap = new HashMap<>();

        try {
            JSONObject cartJson = new JSONObject(shoppingCart);
            JSONArray foodArray = cartJson.getJSONArray("shopingCart_foods");

            for (int i = 0; i < foodArray.length(); i++) {
                JSONObject foodObject = foodArray.getJSONObject(i);
                String cartFoodId = foodObject.getString("cart_foodId");
                int quantity = foodObject.getInt("quantity");
                cartItemsMap.put(cartFoodId, quantity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cartItemsMap;
    }

    private void createOrders(){

        SharedPreferences sharedPreferences =
                getSharedPreferences("my_sharedPreference",MODE_PRIVATE);

        // Retrieve the value for a key
        String accessToken = sharedPreferences.getString("accessToken", "");
        String refreshToken = sharedPreferences.getString("refreshToken", "");
        String shoppingCart = sharedPreferences.getString("shoppingCart", "Cart dosen't exist");
        Integer totalPrice = sharedPreferences.getInt("totalPrice",0);

        Map<String,Integer> cartItemsMap = parseCartItems(shoppingCart);
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String urlCreateOrder = ENV.URL_BASE + "order/createOrder";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("shop_name","Kingscross Restaurant");
            jsonObject.put("totalPrice",totalPrice);

            String currentDate = getCurrentDate();
            String currentTime = getCurrentTime();
            jsonObject.put("date_order",currentDate);
            jsonObject.put("time_order",currentTime);

            JSONArray foodIdsArray = new JSONArray();
            JSONArray food_amount = new JSONArray();
            for (String cartFoodId : cartItemsMap.keySet()) {
                foodIdsArray.put(cartFoodId);
                food_amount.put(cartItemsMap.get(cartFoodId));
            }
            jsonObject.put("food_name",foodIdsArray);
            jsonObject.put("food_amount",food_amount);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    urlCreateOrder,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getInt("statusCode") == 201) {
                                    Log.d("Success Order", "ORDER SUCCESSFULLY");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentMethodActivity.this);
                                    builder.setTitle("Success");
                                    builder.setMessage("Order Successfully");
                                    builder.setPositiveButton("OK", null); // You can add an OnClickListener if needed

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                } else {
                                    Toast.makeText(PaymentMethodActivity.this, "Update Informa+tion Fail", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error", error.toString());
                            Toast.makeText(PaymentMethodActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/json");
                    headers.put("authorization","Bearer " + accessToken);
                    headers.put("Cookie", "refreshToken=" + refreshToken);
                    return headers;
                }
            };

            // Thêm yêu cầu vào RequestQueue để thực hiện gửi đi
            requestQueue.add(jsonObjectRequest);
            requestQueue.getCache().clear();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date currentTime = new Date();
        return timeFormat.format(currentTime);
    }
}
