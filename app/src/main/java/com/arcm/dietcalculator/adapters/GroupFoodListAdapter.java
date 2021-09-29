package com.arcm.dietcalculator.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GroupFoodListAdapter extends RecyclerView.Adapter<GroupFoodListAdapter.ViewHolder>{
    private List<FoodItem> items = new ArrayList<>();
    private final LayoutInflater inflater;

    public ItemClickListener listener;

    public GroupFoodListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_food_group_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GroupFoodListAdapter.ViewHolder holder, int position) {
        FoodItem item = items.get(position);
        holder.titleTextview.setText(item.getFoodName());
        holder.brandTextview.setText(item.getBrand());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<FoodItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView titleTextview;
        private final TextView brandTextview;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.titleTextview = itemView.findViewById(R.id.title_textview);
            this.brandTextview = itemView.findViewById(R.id.ingredient_brand);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemClicked(items.get(getBindingAdapterPosition()));
            }
        }
    }

    public interface ItemClickListener {
        void onItemClicked(FoodItem item);
    }
}
