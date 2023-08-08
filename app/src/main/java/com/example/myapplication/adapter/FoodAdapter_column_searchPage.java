package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailRestroActivity;
import com.example.myapplication.HomeActivity;
import com.example.myapplication.PromoActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter_column_searchPage extends RecyclerView.Adapter<FoodAdapter_column_searchPage.FoodViewHolder> {

    Context context;
    List<Food> foodList;

    public FoodAdapter_column_searchPage(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_column_item_search, parent, false);
        return new FoodAdapter_column_searchPage.FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter_column_searchPage.FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        // Load and display the image using Picasso
        Picasso.get().load(food.getFoodImage()).into(holder.foodImage);
        holder.foodName.setText(foodList.get(position).getFoodName());
        holder.foodDescription.setText(foodList.get(position).getFoodDescription());
        holder.foodDiscountedPrice.setText(foodList.get(position).getFoodDiscountedPrice());
        holder.foodAmount.setText(foodList.get(position).getFoodAmount());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // view holder class
    public static final class FoodViewHolder extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView foodName, foodDescription, foodDiscountedPrice, foodAmount;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage= itemView.findViewById(R.id.foodImagePromo);
            foodName = itemView.findViewById(R.id.foodNamePromo);
            foodDescription = itemView.findViewById(R.id.foodDescriptionPromo);
            foodDiscountedPrice = itemView.findViewById(R.id.foodDiscountedPricePromo);
            foodAmount = itemView.findViewById(R.id.foodAmountPromo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DetailRestroActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
