package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ToolbarBottomActivity  extends AppCompatActivity {
    RelativeLayout showUserSettingButton,showHomeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHomeButton = findViewById(R.id.showHome);
        showHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToolbarBottomActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
