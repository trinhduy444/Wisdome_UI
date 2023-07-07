package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

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
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);

        // Load and display the image using Picasso
        Picasso.get().load(food.getFoodImage()).into(holder.foodImage);
//        holder.foodImage.setImageResource(foodList.get(position).getFoodImage());
        holder.foodName.setText(foodList.get(position).getFoodName());
        holder.foodDescription.setText(foodList.get(position).getFoodDescription());
        holder.foodOriginalPrice.setText(foodList.get(position).getFoodOriginalPrice());
        holder.foodDiscountedPrice.setText(foodList.get(position).getFoodDiscountedPrice());
        holder.foodAmount.setText(foodList.get(position).getFoodAmount());

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    public static final class FoodViewHolder extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView foodName, foodDescription, foodOriginalPrice, foodDiscountedPrice, foodAmount;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage= itemView.findViewById(R.id.foodImagePromo);
            foodName = itemView.findViewById(R.id.foodNamePromo);
            foodDescription = itemView.findViewById(R.id.foodDescriptionPromo);
            foodOriginalPrice = itemView.findViewById(R.id.foodOriginalPricePromo);
            foodDiscountedPrice = itemView.findViewById(R.id.foodDiscountedPricePromo);
            foodAmount = itemView.findViewById(R.id.foodAmountPromo);
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
