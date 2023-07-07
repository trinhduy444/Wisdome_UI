package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.adapter.FoodAdapter_Column;
import com.example.myapplication.model.Food;

import java.util.ArrayList;
import java.util.List;

public class PromoActivity extends AppCompatActivity {

    RecyclerView foodRecycler;
    FoodAdapter_Column foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);


        //dummy data food
        List<Food> foodList = new ArrayList<>();
//        foodList.add(new Food("Fruit salad mix", "Free delivery", "22.500", "18.500", "5 left", R.drawable.assorted_sliced_fruits_in_white_ceramic_bowl_10927301));
//        foodList.add(new Food("Fruit salad mix", "Free delivery", "22.500", "18.500", "5 left", R.drawable.assorted_sliced_fruits_in_white_ceramic_bowl_10927301));

        setFoodRecycler(foodList);

        // Back to previous page
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setFoodRecycler(List<Food> foodList){
        foodRecycler = findViewById(R.id.foodColumn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        foodRecycler.setLayoutManager(layoutManager);
        foodAdapter = new FoodAdapter_Column(this, foodList);
        foodRecycler.setAdapter(foodAdapter);
    }
}