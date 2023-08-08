package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSettingActivity extends AppCompatActivity {

    Button userProfile;

    RelativeLayout showHomeButton,showCartButton,showSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting_);

        // route to user Information
        userProfile = findViewById(R.id.editprofile);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingActivity.this, UserInforActivity.class);

                // first try get
                //String jsonDataReceive = getIntent().getStringExtra("data");

                // second try get
                SharedPreferences sharedPreferences =
                        getSharedPreferences("my_sharedPreference",MODE_PRIVATE);
                String jsonDataReceive = sharedPreferences.getString("dataBackup", "data in Login");

                intent.putExtra("data", jsonDataReceive);
                startActivity(intent);
            }
        });

        // go HOME
        showHomeButton = findViewById(R.id.showHome);
        showHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // go CART
        showCartButton = findViewById(R.id.showCartButton2);
        showCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ToolbarBottomActivity.this, "ok run", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserSettingActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // go SEARCH
        showSearchButton = findViewById(R.id.showSearchButton);
        showSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ToolbarBottomActivity.this, "ok run", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserSettingActivity.this, SearchFoodActivity.class);
                startActivity(intent);
            }
        });

    }

}