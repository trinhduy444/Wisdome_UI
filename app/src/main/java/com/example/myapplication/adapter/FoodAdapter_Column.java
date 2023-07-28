package com.example.myapplication.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.DetailRestroActivity;
import com.example.myapplication.HomeActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.PromoActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.ENV;
import com.example.myapplication.model.Food;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodAdapter_Column extends RecyclerView.Adapter<FoodAdapter_Column.FoodViewHolder> {

    Context context;
    List<Food> foodList;

    public FoodAdapter_Column(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_column_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder,int position) {
        Food food = foodList.get(position);
        // Load and display the image using Picasso
        Picasso.get().load(food.getFoodImage()).into(holder.foodImage);
//        holder.foodImage.setImageResource(foodList.get(position).getFoodImage());
        holder.foodName.setText(foodList.get(position).getFoodName());
        holder.foodDescription.setText(foodList.get(position).getFoodDescription());
        holder.foodOriginalPrice.setText(foodList.get(position).getFoodOriginalPrice());
        holder.foodDiscountedPrice.setText(foodList.get(position).getFoodDiscountedPrice());
        holder.foodAmount.setText(foodList.get(position).getFoodAmount());


        /**
         *  check food is in cart => render number food in cart
         * */
        // get shared preference
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("my_sharedPreference", MODE_PRIVATE);
        // Retrieve the value for a key
        String shoppingCart = sharedPreferences.getString("shoppingCart", "");

        if(!shoppingCart.equals("Cart dosen't exist")){
            try {
                // get food cart
                JSONObject obj = new JSONObject(shoppingCart);
                JSONArray array = obj.getJSONArray("shopingCart_foods");

                // loop cart
                for (int i = 0; i < array.length(); i++) {
                    JSONObject foodObject = array.getJSONObject(i);
                    // id food current
                    String foodId = foodList.get(position).getFoodId();

                    // check id
                    if(foodId.equals(foodObject.getString("cart_foodId"))){
                        // number food
                        String quantity = foodObject.getString("quantity");
                        // hide button add
                        holder.textView10_btnAdd.setVisibility(View.GONE);
                        // show group [-] [number] [+]
                        holder.lnLayout10.setVisibility(View.VISIBLE);
                        holder.imageView.setVisibility(View.VISIBLE);
                        // set quality food
                        holder.textView10.setText(quantity);
                        break;
                    }

                }
            } catch (Throwable t) {
                Log.e("food adapter", "Could not parse malformed JSON:");
            }
        }

        /**
         *  button event
         * */
        // increase food
        holder.textView10_btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quality = increaseFood(holder.textView10.getText().toString(),food.getFoodId());
                holder.textView10.setText(quality);
            }
        });

        // decrease food
        holder.textView10_btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quality =  decreaseFood(holder.textView10.getText().toString(),food.getFoodId());
                // case quality = 0
                if(Integer.parseInt(quality) == 0){
                    // hide group [-] [number] [+]
                    holder.lnLayout10.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.GONE);
                    // show btn add
                    holder.textView10_btnAdd.setVisibility(View.VISIBLE);
                }
                holder.textView10.setText(quality);
            }
        });

        // add food
        holder.textView10_btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String quality = addFood(food.getFoodId());
               // hide button add
                holder.textView10_btnAdd.setVisibility(View.GONE);
               // show group [-] [number] [+]
                holder.lnLayout10.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.VISIBLE);
                // set quality food
                holder.textView10.setText(quality);
            }
        });
    }

    /**
     * action request
     */
    public String increaseFood(String currentQuality,String foodId){
        int numberQuality = Integer.parseInt(currentQuality);
        numberQuality += 1;

        // request url
        RequestQueue queue = Volley.newRequestQueue(context);

        String endpoint = "shoppingCart/updateFood";
        String url = ENV.URL_BASE + endpoint;

        // request header
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("my_sharedPreference",MODE_PRIVATE);

        // Retrieve the value for a key
        String accessToken = sharedPreferences.getString("accessToken", "");
        String refreshToken = sharedPreferences.getString("refreshToken", "");

        // request body
        JSONObject jsonBody = new JSONObject();

        try {

            // add the "food" parameter as a JSON object
            JSONObject foodObject = new JSONObject();
            //
            foodObject.put("cart_foodId", foodId);
            foodObject.put("quantity", numberQuality);
            //
            jsonBody.put("food", foodObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // call
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.get("statusCode").equals(200)){

                                // get cart
                                String newShoppingCart = response.getString("metadata");

                                // save shared preference
                                // use shared preference - mob private
                                SharedPreferences sharedPreferences =
                                        context.getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("shoppingCart",newShoppingCart);
                                edit.apply();

                            }
                            else{
                                Log.d("food adapter response",response.get("message").toString());
                                Toast.makeText(context,"food increase Fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("food adapter Error", error.toString());
                        Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
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
        //
        queue.add(jsonObjectRequest);
        queue.getCache().clear();

        return Integer.toString(numberQuality);
    }

    public String decreaseFood(String currentQuality,String foodId){
        int numberQuality = Integer.parseInt(currentQuality);
        numberQuality -= 1;

        RequestQueue queue = Volley.newRequestQueue(context);

        // request header
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("my_sharedPreference",MODE_PRIVATE);

        // Retrieve the value for a key
        String accessToken = sharedPreferences.getString("accessToken", "");
        String refreshToken = sharedPreferences.getString("refreshToken", "");

        /**
         *  case decrease food == 0 => remove cart
         */

        // update food --------------------------------------
        String endpoint;
        if(numberQuality != 0){
            // request url
            endpoint = "shoppingCart/updateFood";
            String url = ENV.URL_BASE + endpoint;

            // request body
            JSONObject jsonBody = new JSONObject();

            try {

                // add the "food" parameter as a JSON object
                JSONObject foodObject = new JSONObject();
                //
                foodObject.put("cart_foodId", foodId);
                foodObject.put("quantity", numberQuality);
                //
                jsonBody.put("food", foodObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // call
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.get("statusCode").equals(200)){

                                    // get cart
                                    String newShoppingCart1 = response.getString("metadata");

                                    // save shared preference
                                    // use shared preference - mob private
                                    SharedPreferences sharedPreferences =
                                            context.getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString("shoppingCart",newShoppingCart1);
                                    edit.apply();

                                }
                                else{
                                    Log.d("food adapter response",response.get("message").toString());
                                    Toast.makeText(context,"food increase Fail", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("food adapter Error", error.toString());
                            Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
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
            //
            queue.add(jsonObjectRequest);

        }else { // delete food -------------------------------
            // request url
            endpoint = "shoppingCart/deleteFood";
            String url = ENV.URL_BASE + endpoint;

            // request body
            JSONObject jsonBody = new JSONObject();

            try {

                // add the "food" parameter as a JSON object
                JSONObject foodObject = new JSONObject();
                //
                foodObject.put("cart_foodId", foodId);
                //
                jsonBody.put("food", foodObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // call
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.get("statusCode").equals(200)){

                                    // get cart
                                    String newShoppingCart1 = response.getString("metadata");

                                    // save shared preference
                                    // use shared preference - mob private
                                    SharedPreferences sharedPreferences =
                                            context.getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString("shoppingCart",newShoppingCart1);
                                    edit.apply();

                                }
                                else{
                                    Log.d("food adapter response",response.get("message").toString());
                                    Toast.makeText(context,"food increase Fail", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("food adapter Error", error.toString());
                            Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
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
            //
            queue.add(jsonObjectRequest);
        }
        queue.getCache().clear();
        return Integer.toString(numberQuality);
    }

    public String addFood(String foodId){
        int numberQuality = 1;

        // request url
        RequestQueue queue = Volley.newRequestQueue(context);

        String endpoint = "shoppingCart/addFood";
        String url = ENV.URL_BASE + endpoint;

        // request header
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("my_sharedPreference",MODE_PRIVATE);

        // Retrieve the value for a key
        String accessToken = sharedPreferences.getString("accessToken", "");
        String refreshToken = sharedPreferences.getString("refreshToken", "");

        // request body
        JSONObject jsonBody = new JSONObject();

        try {

            // add the "food" parameter as a JSON object
            JSONObject foodObject = new JSONObject();
            //
            foodObject.put("cart_foodId", foodId);
            foodObject.put("quantity", numberQuality);
            //
            jsonBody.put("food", foodObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // call
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.get("statusCode").equals(201)){

                                // get cart
                                String newShoppingCart2 = response.getString("metadata");

                                // save shared preference
                                // use shared preference - mob private
                                SharedPreferences sharedPreferences =
                                        context.getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("shoppingCart",newShoppingCart2);
                                edit.apply();

                            }
                            else{
                                Log.d("food adapter response",response.get("message").toString());
                                Toast.makeText(context,"food increase Fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("food adapter Error", error.toString());
                        Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
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
        //
        queue.add(jsonObjectRequest);
        queue.getCache().clear();

        return "1";
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static final class FoodViewHolder extends RecyclerView.ViewHolder{

        ImageView foodImage,imageView;
        TextView foodName, foodDescription, foodOriginalPrice, foodDiscountedPrice, foodAmount,
                textView10,textView10_btnIncrease,textView10_btnDecrease,textView10_btnAdd;
        LinearLayout lnLayout10;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage= itemView.findViewById(R.id.foodImagePromo);
            foodName = itemView.findViewById(R.id.foodNamePromo);
            foodDescription = itemView.findViewById(R.id.foodDescriptionPromo);
            foodOriginalPrice = itemView.findViewById(R.id.foodOriginalPricePromo);
            foodDiscountedPrice = itemView.findViewById(R.id.foodDiscountedPricePromo);
            foodAmount = itemView.findViewById(R.id.foodAmountPromo);
            //
            textView10 = itemView.findViewById(R.id.textView10);
            textView10_btnIncrease = itemView.findViewById(R.id.textView10_btnIncrease);
            textView10_btnDecrease = itemView.findViewById(R.id.textView10_btnDecrease);
            textView10_btnAdd =itemView.findViewById(R.id.textView10_btnAddFood);

            lnLayout10 = itemView.findViewById(R.id.lnLayout10);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

//package com.example.myapplication.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.myapplication.R;
//import com.example.myapplication.model.Food;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class FoodAdapter_Column extends RecyclerView.Adapter<FoodAdapter_Column.FoodViewHolder> {
//
//    Context context;
//    List<Food> foodList;
//
//
//    public FoodAdapter_Column(Context context, List<Food> foodList) {
//        this.context = context;
//        this.foodList = foodList;
//    }
//
//    @NonNull
//    @Override
//    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.food_column_item, parent, false);
//        return new FoodViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
//        String imageURL = foodList.get(position).getFoodImage();
//
//        Picasso.get().load(imageURL).into(holder.foodImage);
////        holder.foodImage.setImageResource(foodList.get(position).getFoodImage());
//        holder.foodName.setText(foodList.get(position).getFoodName());
//        holder.foodDescription.setText(foodList.get(position).getFoodDescription());
//        holder.foodOriginalPrice.setText(foodList.get(position).getFoodOriginalPrice());
//        holder.foodDiscountedPrice.setText(foodList.get(position).getFoodDiscountedPrice());
//        holder.foodAmount.setText(foodList.get(position).getFoodAmount());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return foodList.size();
//    }
//
//
//    public static final class FoodViewHolder extends RecyclerView.ViewHolder{
//
//        ImageView foodImage;
//        TextView foodName, foodDescription, foodOriginalPrice, foodDiscountedPrice, foodAmount;
//
//        public FoodViewHolder(@NonNull View itemView) {
//            super(itemView);
//            foodImage= itemView.findViewById(R.id.foodImagePromo);
//            foodName = itemView.findViewById(R.id.foodNamePromo);
//            foodDescription = itemView.findViewById(R.id.foodDescriptionPromo);
//            foodOriginalPrice = itemView.findViewById(R.id.foodOriginalPricePromo);
//            foodDiscountedPrice = itemView.findViewById(R.id.foodDiscountedPricePromo);
//            foodAmount = itemView.findViewById(R.id.foodAmountPromo);
//        }
//    }
//}
