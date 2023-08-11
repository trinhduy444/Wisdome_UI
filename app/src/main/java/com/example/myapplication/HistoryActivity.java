package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.model.ENV;
import com.example.myapplication.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    TextView textOrderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getAllOrders();
    }
    private void getAllOrders() {
        SharedPreferences sharedPreferences =
                getSharedPreferences("my_sharedPreference",MODE_PRIVATE);

        // Retrieve the value for a key
        String accessToken = sharedPreferences.getString("accessToken", "");
        String refreshToken = sharedPreferences.getString("refreshToken", "");

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String urlGetAllOrders = ENV.URL_BASE + "order/getAllOrders";
            textOrderCount = findViewById(R.id.textOrderCount);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetAllOrders, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý dữ liệu như đã mô tả trong phần trước
                            try {
                                JSONArray ordersArray = response.getJSONArray("metadata");

                                List<Order> orderList = new ArrayList<>();
                                for (int i = 0; i < ordersArray.length(); i++) {
                                    JSONObject orderObject = ordersArray.getJSONObject(i);


                                    JSONArray foodNameArray = orderObject.getJSONArray("food_name");
                                    JSONArray foodAmountArray = orderObject.getJSONArray("food_amount");
                                    List<String> foodNames = new ArrayList<>();
                                    List<Integer> foodAmounts = new ArrayList<>();
                                    for (int j = 0; j < foodNameArray.length(); j++) {
                                        foodNames.add(foodNameArray.getString(j));
                                        foodAmounts.add(foodAmountArray.getInt(j));
                                    }
                                    String dateTime = orderObject.getString("date_order");
                                    String date = dateTime.substring(0, 10);

                                    Order order = new Order(
                                            orderObject.getString("_id"),
                                            orderObject.getString("customer_id"),
                                            orderObject.getString("shop_name"),
                                            orderObject.getString("status"),
                                            orderObject.getDouble("totalPrice"),
                                            date,
                                            orderObject.getString("time_order"),
                                            foodNames,
                                            foodAmounts
                                    );
                                    orderList.add(order);
                                }
                                textOrderCount.setText("Số đơn hàng của bạn: "+ordersArray.length());
                                orderAdapter = new OrderAdapter(orderList);
                                recyclerView.setAdapter(orderAdapter);
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
                    headers.put("authorization","Bearer " + accessToken);
                    headers.put("Cookie", "refreshToken=" + refreshToken);
                    return headers;
                }
            };

            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
