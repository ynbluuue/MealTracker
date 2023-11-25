package com.example.mealtracker.FoodInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealtracker.R;

import java.util.List;

public class FoodRecordAdapter extends RecyclerView.Adapter<FoodRecordAdapter.ViewHolder> {
    private List<FoodRecord> foodRecords;

    public FoodRecordAdapter(List<FoodRecord> foodRecords) {
        this.foodRecords = foodRecords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodRecord foodRecord = foodRecords.get(position);

        // ViewHolder의 각 View에 데이터를 설정
        holder.dateTextView.setText(foodRecord.getDateTime());
        holder.foodNameTextView.setText(foodRecord.getFoodName());
    }

    @Override
    public int getItemCount() {
        if(foodRecords==null) return 0;
        return foodRecords.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView foodNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
        }
    }
}