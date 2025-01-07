package com.example.hotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {

    private List<Discount> discountList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public DiscountAdapter(List<Discount> discountList) {
        this.discountList = discountList;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        Discount discount = discountList.get(position);

        // Set the details
        holder.txtDiscountID.setText("Discount ID: " + discount.getDiscountID());
        holder.txtDiscountName.setText("Discount Name: " + discount.getDiscountName());
        holder.txtDescription.setText("Description: " + discount.getDescription());
        holder.txtDiscountPercentage.setText("Discount Percentage: " + discount.getDiscountPercentage() + "%");
        holder.txtStartDate.setText("Start Date: " + discount.getStartDate());
        holder.txtEndDate.setText("End Date: " + discount.getEndDate());
        holder.txtApplicableRoomType.setText("Applicable For: " + discount.getApplicableRoomType());

        try {
            Date endDate = dateFormat.parse(discount.getEndDate());
            Date currentDate = new Date();
            if (endDate != null && endDate.after(currentDate)) {
                holder.txtStatus.setText("Status: Pending");
                holder.txtStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            } else {
                holder.txtStatus.setText("Status: Expired");
                holder.txtStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    static class DiscountViewHolder extends RecyclerView.ViewHolder {
        TextView txtDiscountID, txtDiscountName, txtDescription, txtDiscountPercentage, txtStartDate, txtEndDate, txtApplicableRoomType, txtStatus;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDiscountID = itemView.findViewById(R.id.textDiscountID);
            txtDiscountName = itemView.findViewById(R.id.textDiscountName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDiscountPercentage = itemView.findViewById(R.id.txtDiscountPercentage);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
            txtApplicableRoomType = itemView.findViewById(R.id.txtApplicableRoomType);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
