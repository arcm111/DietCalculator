package com.arcm.dietcalculator.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class SelectIngredientsAdapter extends RecyclerView.Adapter<SelectIngredientsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private List<FoodItem> items;
    private OnIngredientSelectedListener listener;

    public SelectIngredientsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem item = items.get(position);
        holder.icon.setImageResource(DiaryItem.getGroupIconResource(item.getGroup()));
        holder.nameTextview.setText(item.getFoodName());
        holder.brandTextview.setText(item.getBrand());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView nameTextview;
        private final TextView brandTextview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.icon = itemView.findViewById(R.id.food_group_icon);
            this.nameTextview = itemView.findViewById(R.id.food_name);
            this.brandTextview = itemView.findViewById(R.id.food_brand);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                FoodItem item = items.get(getBindingAdapterPosition());
                listener.onIngredientSelected(item);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<FoodItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnIngredientSelectedListener(OnIngredientSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnIngredientSelectedListener {
        void onIngredientSelected(FoodItem item);
    }
}
