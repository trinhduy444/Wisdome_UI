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

import java.util.List;
/*public class FoodAdapter_FoodPageColumn extends RecyclerView.Adapter<FoodAdapter_FoodPageColumn.FoodViewHolder> {

    Context context;
    List<Food> foodList;


    public FoodAdapter_FoodPageColumn(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_foodpage_column_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

        holder.foodImage.setImageResource(foodList.get(position).getImageURL());
        holder.price.setText(foodList.get(position).getPrice());
        holder.name.setText(foodList.get(position).getName());
        holder.description.setText(foodList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    public static final class FoodViewHolder extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView price,name,description;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage= itemView.findViewById(R.id.foodPageImage);
            price = itemView.findViewById(R.id.foodAttributes);
            name = itemView.findViewById(R.id.foodTitle);
            description = itemView.findViewById(R.id.temp);

        }
    }
}


 */