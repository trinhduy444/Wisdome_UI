package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.model.ENV;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class UserInforActivity extends AppCompatActivity {

    private EditText dateEditText,email, address,phoneNumber,userName,nameEditTextActivity,genderEdixText;
    private AlertDialog alertDialog;

    private RadioButton radioButtonMale,radioButtonFemale;
    private TextView nameProfile;
    private Button update_Profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        //Anh xa cac view
        nameProfile = findViewById(R.id.nameProfile);

        radioButtonMale = findViewById(R.id.radioMale);
        radioButtonFemale = findViewById(R.id.radioFemale);
        dateEditText = findViewById(R.id.dateEditText);
        email = findViewById(R.id.emailEditText);
        address = findViewById(R.id.address_editText);
        phoneNumber = findViewById(R.id.phoneEditText);
        userName = findViewById(R.id.nameEditText);

        update_Profile = findViewById(R.id.update_Profile);


        //Lay accessToken de post toi info user
        String jsonDataReceive = getIntent().getStringExtra("data");
        String shopEmail;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonDataReceive);
            JSONObject metadata = jsonObject.getJSONObject("metadata");

            JSONObject shop;
            if (metadata.has("shop")) {
                shop = metadata.getJSONObject("shop");
            } else {
                shop = metadata; // Assign the entire "metadata" object to "shop"
            }

            shopEmail = shop.getString("shop_email");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        //API
        String endpointGetInfo = "user/getInfor";
        String url = ENV.URL_BASE + endpointGetInfo;

        RequestQueue queue = Volley.newRequestQueue(UserInforActivity.this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("shop_email", shopEmail);
            Log.d("dataGetInfor", jsonBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("jsonBody", jsonBody.toString());
                try {
                    if(response.get("statusCode").equals(200)){
                        JSONObject metadata = response.getJSONObject("metadata");
                        dateEditText.setText(metadata.getString("shop_birtday"));
                        email.setText(shopEmail);

                        String gender;
                        gender = metadata.getString("shop_gerder");
                        if(gender.equals("1")){
                            radioButtonMale.setChecked(true);
                        } else if (gender.equals("0")) {
                            radioButtonFemale.setChecked(true);
                        }

                        address.setText(metadata.getString("shop_address"));
                        phoneNumber.setText(metadata.getString("shop_phoneNumber"));
                        userName.setText(metadata.getString("shop_userName"));
                        nameProfile.setText(metadata.getString("shop_firstName"));
                    }
                }catch (JSONException e){
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Erro when try get Information of user");
                return;
            }
        });
        queue.add(jsonObjectRequest);
        queue.getCache().clear();

        // Update Profile
        update_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(UserInforActivity.this);

                // API URL
                String urlUpdate = ENV.URL_BASE + "user/updateInfor";

                // Create JSON object for the request body
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("shop_email",shopEmail);
                    jsonBody.put("shop_userName", userName.getText().toString());
                    if(radioButtonMale.isChecked()){
                        jsonBody.put("shop_gerder","1");
                    }
                    else if(radioButtonFemale.isChecked()){
                        jsonBody.put("shop_gerder","0");
                    }
                    jsonBody.put("shop_birtday", dateEditText.getText().toString());
                    jsonBody.put("shop_phoneNumber", phoneNumber.getText().toString());
                    jsonBody.put("shop_address", address.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send Request Update to Server
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlUpdate, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("statusCode") == 200) {
                                Log.d("Success Update", "UPDATE SUCCESS");
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserInforActivity.this);
                                builder.setTitle("Success");
                                builder.setMessage("Update User Success");
                                builder.setPositiveButton("OK", null); // You can add an OnClickListener if needed

                                SharedPreferences sharedPreferences =
                                        getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("dataBackup",response.toString());
                                edit.apply();

                                // Create and show the dialog
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            } else {
                                Toast.makeText(UserInforActivity.this, "Update Information Fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        Toast.makeText(UserInforActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Access the RequestQueue through your singleton class.
                queue.add(jsonObjectRequest);
                queue.getCache().clear();
            }
        });
    }

    public void onClickPickDate(View view) {
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                String formattedDate =  selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                dateEditText.setText(formattedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
}

