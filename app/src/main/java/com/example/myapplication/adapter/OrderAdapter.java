package com.example.myapplication.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Order;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList; // Order là một lớp chứa thông tin đơn hàng

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        double totalPrice = order.getTotalPrice();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0 VNĐ");
        String formattedTotalPrice = decimalFormat.format(totalPrice);

        // Đặt thông tin từ order vào các thành phần trong card
        holder.textShopName.setText(order.getShop_name());
        holder.textStatus.setText(order.getStatus());
        holder.textTotalPrice.setText("Total: " + formattedTotalPrice);
        holder.textDate.setText(order.getDate_order());
        holder.textTime.setText(order.getTime_order());



    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        // Khai báo các thành phần trong card (TextView, ImageView,...)
        private TextView textShopName, textStatus, textTotalPrice, textDate, textTime;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Khởi tạo các thành phần trong card
            textShopName = itemView.findViewById(R.id.textShopName);
            textStatus = itemView.findViewById(R.id.textStatus);
            textTotalPrice = itemView.findViewById(R.id.textTotalPrice);
            textDate = itemView.findViewById(R.id.textDate);
            textTime = itemView.findViewById(R.id.textTime);
        }
        public void bind(Order order) {
            double totalPrice = order.getTotalPrice();
            DecimalFormat decimalFormat = new DecimalFormat("#,##0 VNĐ");
            String formattedTotalPrice = decimalFormat.format(totalPrice);
            textShopName.setText(order.getShop_name());
            textStatus.setText(order.getStatus());
            textTotalPrice.setText("Total: " + formattedTotalPrice);
            textDate.setText(order.getDate_order());
            textTime.setText(order.getTime_order());
        }
    }
}
