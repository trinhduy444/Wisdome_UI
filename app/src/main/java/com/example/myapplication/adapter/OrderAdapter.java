package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.example.myapplication.OrderDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderDetail;

import java.text.DecimalFormat;
import java.util.List;

import javax.security.auth.callback.Callback;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList; // Order là một lớp chứa thông tin đơn hàng
    private OnItemClickListener itemClickListener;

    public OrderAdapter(List<Order> orderList ){
        this.orderList = orderList;
    }

    public interface OnItemClickListener {
        void onItemClick(String orderId);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
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
        holder.textDate.setText("Ngày đặt: "+ order.getDate_order());
        holder.textTime.setText(order.getTime_order());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(order.getOrder_Id());
                    System.out.println("Item clicked: " + order.getOrder_Id());

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        // Khai báo các thành phần trong card (TextView, ImageView,...)
        private TextView textShopName, textStatus, textTotalPrice, textDate, textTime;
        private  CardView cardView;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            // Khởi tạo các thành phần trong card
            cardView= itemView.findViewById(R.id.cardViewOrder);

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
