package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    private Button btnRegisters;

    Button btn_login;
    EditText et_userName, et_passWord;
                    // END CODE OF NHUT
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//
        et_userName = findViewById(R.id.usernameLogin);
        et_passWord = findViewById(R.id.passwordLogin);
        btnRegisters = findViewById(R.id.etSignUp);
        btn_login = findViewById(R.id.loginbtn);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

//                String url = "https://delivery-9thd.onrender.com/api/v1/auth/login";
                String endpoint = "auth/login";
                String url = ENV.URL_BASE + endpoint;
                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("email", et_userName.getText().toString());
                    jsonBody.put("password", et_passWord.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.get("statusCode").equals(200)){
                                        Log.d("response","Login Page: Loging Success");
//                                        Log.d("response","Login Page: Loging Success"+response.toString());

                                        ///////////////

                                        // get access token
                                        JSONObject metadata = response.getJSONObject("metadata");
                                        String acToken = metadata.getString("accessToken");
                                        Log.d("access token :",acToken);

                                        // get refresh token
                                        String rfToken = metadata.getString("refreshToken");
                                        Log.d("refresh token :",rfToken);

                                        // use shared preference - mob private
                                        SharedPreferences sharedPreferences =
                                                getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
                                        SharedPreferences.Editor edit = sharedPreferences.edit();
                                        edit.putString("accessToken",acToken);
                                        edit.putString("refreshToken",rfToken);
                                        edit.apply();

                                        /////////////
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.putExtra("data", response.toString());
                                        Log.d("data","data in Login" + response.toString());
                                        startActivity(intent);
                                    }
                                    else{
                                        Log.d("response",response.get("message").toString());
                                        Toast.makeText(LoginActivity.this,"Login Fail", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", error.toString());
                                Toast.makeText(LoginActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                // Access the RequestQueue through your singleton class.
                queue.add(jsonObjectRequest);
                queue.getCache().clear();
            }
        });


        btnRegisters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}