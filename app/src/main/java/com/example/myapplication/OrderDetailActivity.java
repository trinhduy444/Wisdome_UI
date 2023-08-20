package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.model.ENV;
import com.example.myapplication.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {
    private String orderId;

    DecimalFormat decimalFormat = new DecimalFormat("###,### VNĐ");

    private TextView txt_date_delivery,txt_time_delivery,
            txt_delivery_status,txt_address_restaurant,txt_address_custommer,txt_price,txt_price_delivery,txt_totalPrice;
    //private RatingBar rating_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

//        Toolbar toolbar = findViewById(R.id.toolbarOrderdetail);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        orderId = getIntent().getStringExtra("orderId");
        Bundle bundle = getIntent().getBundleExtra("orderBundle");

        getOrderDetail(bundle,orderId);

        // Xử lý sự kiện nút "Back"
        //toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void getOrderDetail(Bundle bundle, String orderId) {

        SharedPreferences sharedPreferences =
                getSharedPreferences("my_sharedPreference", MODE_PRIVATE);

        // Retrieve the value for a key
        String accessToken = sharedPreferences.getString("accessToken", "");
        String refreshToken = sharedPreferences.getString("refreshToken", "");

        try {
            String url = ENV.URL_BASE + "order/detail/getOderDetail";
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("order_id", orderId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý dữ liệu từ response
                            try {

                                JSONObject metadata = response.getJSONObject("metadata");

                                //Anh xa cac thong tin trong orderDetail
                                txt_date_delivery = findViewById(R.id.txt_date_delivery);
                                txt_time_delivery = findViewById(R.id.txt_time_delivery);
                                txt_delivery_status = findViewById(R.id.txt_delivery_status);
                                txt_address_restaurant = findViewById(R.id.txt_address_restaurant);
                                txt_address_custommer = findViewById(R.id.txt_address_custommer);
                                //rating_order = findViewById(R.id.rating_order);
                                txt_price = findViewById(R.id.txt_price);
                                txt_price_delivery = findViewById(R.id.txt_price_delivery);
                                txt_totalPrice = findViewById(R.id.txt_totalPrice);


                                //Lay du lieu tu endpoint
                                String orderId = metadata.getString("order_id");
                                String shipperId = metadata.getString("shipper_id");

                                String note = metadata.getString("note");
                                String addressRestaurant = metadata.getString("address_restaurant");
                                String addressCustomer = metadata.getString("address_customer");

                                String paymentName = metadata.getString("payment_name");
                                double rating = metadata.getDouble("rating");

                                // Lay data order tu bundle
                                if (bundle != null) {
                                    String status = bundle.getString("status");
                                    double totalPrice = bundle.getDouble("totalPrice");
                                    String date_order = bundle.getString("date_order");
                                    String time_order = bundle.getString("time_order");

                                    txt_date_delivery.setText(date_order);
                                    txt_time_delivery.setText(time_order);
                                    txt_delivery_status.setText(status);
                                    String formattedTotalPrice = decimalFormat.format(totalPrice);
                                    txt_totalPrice.setText(formattedTotalPrice);
                                    txt_price.setText(formattedTotalPrice);
                                    txt_price_delivery.setText(formattedTotalPrice);
                                    txt_price_delivery.setText("0 VNĐ");

                                }

                                txt_address_restaurant.setText(addressRestaurant);
                                txt_address_custommer.setText(addressCustomer);

                                float normalizedRating = (float) Math.min(Math.max(rating, 0.0), 5.0);
                                //rating_order.setRating(normalizedRating);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("authorization", "Bearer " + accessToken);
                    headers.put("Cookie", "refreshToken=" + refreshToken);
                    return headers;
                }
            };


            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
