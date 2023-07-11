package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegisBack;
    private Button btnRegisters;
    EditText username, email, phonenumber, password;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Route to UI
        username =  findViewById(R.id.username);
        email =  findViewById(R.id.email);
        phonenumber =  findViewById(R.id.phoneNumber);
        password =  findViewById(R.id.password);
        btnRegisBack = findViewById(R.id.btnBack);
        btnRegisters = findViewById(R.id.btnRegister1);

        // Sig up new Account
        btnRegisters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

                // Link API
                String url = "https://delivery-9thd.onrender.com/api/v1/auth/signUpUser";
                // Data request
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("firstName","-");
                    jsonBody.put("lastName","-");
                    jsonBody.put("userName", username.getText().toString());
                    jsonBody.put("email", email.getText().toString());
                    jsonBody.put("phoneNumber", phonenumber.getText().toString());
                    jsonBody.put("password", password.getText().toString());
                    jsonBody.put("address", "-");
//                    jsonBody.put("birtDay", "0/00/0000");

                } catch (JSONException e) {
                    // Check error of JSONObject
                    Log.d("error","JSONObject Error");
                    e.printStackTrace();
                }
                Log.d("data sigup", jsonBody.toString());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.get("statusCode").equals(500)){
                                        Log.d("message", response.toString());
                                    }
                                    if(response.get("statusCode").equals(201)){
                                        Toast toast = Toast.makeText(RegisterActivity.this,"Create User Success", Toast.LENGTH_LONG);
                                        toast.show();
                                        Log.d("response",response.get("message").toString()+ " IT mean Success");
                                        AlertDialog dialog = createDialog();
                                        dialog.show();
                                    }
                                    else{
                                        Log.d("error", "Can't Sign Up");
                                        Log.d("message", response.toString());
                                        Log.d("response",response.get("message").toString());
                                        Toast.makeText(RegisterActivity.this,"Crate User Fail", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Log.d("re", "Bi loi");
                                    Log.d("message", response.toString());
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", error.toString());
                                Toast.makeText(RegisterActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                // Access the RequestQueue through your singleton class.
                queue.add(jsonObjectRequest);
                queue.getCache().clear();
            }
        });

        // Back to previous page
        btnRegisBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    AlertDialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SigUp Success");
        builder.setMessage("Do you want to do?");
        builder.setPositiveButton("Return Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Stay Here", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}