package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewAddCardActivity extends AppCompatActivity {

    ImageButton addCard_btnBack;
    TextView btn_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add_cadd);

        addCard_btnBack = findViewById(R.id.addCard_btnBack);
        btn_success = findViewById(R.id.btn_success);

        addCard_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewAddCardActivity.this, SuccessActivity.class);
                startActivity(intent);
            }
        });
    }
}