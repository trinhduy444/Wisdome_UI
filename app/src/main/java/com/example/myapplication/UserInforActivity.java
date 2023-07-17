package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class UserInforActivity extends AppCompatActivity {

    ImageView ReturnPreviousButton;
    EditText email, address,phoneNumber,nameEditTextActivity,genderEdixText,dateEditText,helloUser;
    TextView nameProfileTextView, logOutButton;
    Button update_Profile_Button;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);

        // Data receive from login page
        String jsonDataReceive = getIntent().getStringExtra("data");

        // Parameter
        String shopEmail;
        JSONObject jsonObject;
        // Get  " shop_email " from data login page
        try {
            jsonObject = new JSONObject(jsonDataReceive);
            JSONObject metadata = jsonObject.getJSONObject("metadata");
            JSONObject shop = metadata.getJSONObject("shop");
             shopEmail = shop.getString("shop_email");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        // Define Edit Text, button....
        nameProfileTextView = findViewById(R.id.nameProfile);
        nameEditTextActivity = findViewById(R.id.nameEditText);
        genderEdixText = findViewById(R.id.genderEditText);
        dateEditText = findViewById(R.id.dateEditText);
        phoneNumber = findViewById(R.id.phoneEditText);
        email = findViewById(R.id.emailEditText);
        address = findViewById(R.id.address_editText);
        helloUser = findViewById(R.id.nameProfile);


        // Link API
        String endpointGetInfo = "user/getInfor";
        String url = ENV.URL_BASE + endpointGetInfo;

        String endpointLogout = "auth/logout";
        String urlLogOut =ENV.URL_BASE + endpointLogout;
//        String url = "https://delivery-9thd.onrender.com/api/v1/user/getInfor";
//        String urlLogOut = "https://delivery-9thd.onrender.com/api/v1/auth/logout";

        // Queue Request
        RequestQueue queue = Volley.newRequestQueue(UserInforActivity.this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("shop_email", shopEmail);
            Log.d("dataGetInfor", jsonBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("jsonBody", jsonBody.toString());
                        try {
                            if(response.get("statusCode").equals(200)){
                                JSONObject metadata = response.getJSONObject("metadata");
                                nameEditTextActivity.setText(metadata.getString("shop_userName"));
                                genderEdixText.setText(metadata.getString("shop_gerder"));
                                dateEditText.setText(metadata.getString("shop_birtday"));
                                phoneNumber.setText(metadata.getString("shop_phoneNumber"));
                                address.setText(metadata.getString("shop_address"));
                                email.setText(shopEmail);
                                helloUser.setText("Hello " + metadata.getString("shop_lastName"));
                            }
                            else{
                                Log.d("Error", "Erro when try get Information of user");
                                return;
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);
        queue.getCache().clear();


        // Back Previous Pages
        ReturnPreviousButton = findViewById(R.id.ReturnPrevious);
        ReturnPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Log out
        logOutButton = findViewById(R.id.logOut);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest jsonObjectRequestLogout = new JsonObjectRequest
                        (Request.Method.POST, urlLogOut, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.get("statusCode").equals(200)){
                                       Intent intentLogOut = new Intent(UserInforActivity.this, LoginActivity.class);
                                       startActivity(intentLogOut);
                                    }
                                    else{
                                        Log.d("Server response", " Can't Log Out");
                                        return;
                                    }
                                } catch (JSONException e) {
                                    Log.d("Error", "Function Log Out error");
                                    throw new RuntimeException(e);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Intent intentLogOut = new Intent(UserInforActivity.this, LoginActivity.class);
                                startActivity(intentLogOut);
                                Log.d("Error", error.toString() + "This Error of onClick of function Log Out, CHECK IT");
                            }
                        });
                queue.add(jsonObjectRequestLogout);

            }
        });

        // Button " UPDATE " information
        update_Profile_Button = findViewById(R.id.update_Profile);
        update_Profile_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(UserInforActivity.this);

                // Link API
                String url = "https://delivery-9thd.onrender.com/api/v1/user/updateInfor";
                JSONObject jsonBody = new JSONObject();
                try {
                    // Put data to Request
                    jsonBody.put("shop_userName", nameEditTextActivity.getText());
                    jsonBody.put("shop_gerder", genderEdixText.getText());
                    jsonBody.put("shop_birtday", dateEditText.getText());
                    jsonBody.put("shop_phoneNumber", phoneNumber.getText());
                    jsonBody.put("shop_email",shopEmail);
                    jsonBody.put("shop_address", address.getText());
                    String userLastNameChange = helloUser.getText().toString().substring(6);

                    jsonBody.put("shop_lastName", userLastNameChange);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.get("statusCode").equals(200)){
                                        Log.d("Success Update", "UPDATE SUCESS");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(UserInforActivity.this);
                                        builder.setTitle("Success");
                                        builder.setMessage("Update User Success");
                                        builder.setPositiveButton("OK", null); // You can add an OnClickListener if needed

                                        // Create and show the dialog
                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    }
                                    else{
                                        Toast.makeText(UserInforActivity.this,"Update Information Fail", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", error.toString());
                                Toast.makeText(UserInforActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                // Access the RequestQueue through your singleton class.
                queue.add(jsonObjectRequest);
                queue.getCache().clear();
            }
        });
    }
}

