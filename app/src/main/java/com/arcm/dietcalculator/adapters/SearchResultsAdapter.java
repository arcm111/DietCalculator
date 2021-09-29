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
import com.arcm.dietcalculator.Utilities;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    public static final String TAG = "MediaListAdapter";

    private final LayoutInflater inflater;
    private List<FoodItem> items = new ArrayList<>();
    private ItemClickListener listener;

    public SearchResultsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem item = items.get(position);
        if (holder.id == null || !holder.id.equals(item.fdcId)) {
            holder.id = item.fdcId;
            holder.name.setText(item.getFoodName());
            holder.calsTextView.setText(Utilities.formatNumber(item.getCalories()));
            holder.carbsTextView.setText(Utilities.formatNumber(item.getCarbohydrates()));
            holder.proteinTextView.setText(Utilities.formatNumber(item.getProtein()));
            holder.fatTextView.setText(Utilities.formatNumber(item.getFat()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public FoodItem getItem(int position) {
        if (position >= items.size()) return null;
        return items.get(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<FoodItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String id;
        public final TextView calsTextView;
        public final TextView proteinTextView;
        public final TextView carbsTextView;
        public final TextView fatTextView;
        public final TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.item_name);
            this.calsTextView = itemView.findViewById(R.id.cals_value);
            this.carbsTextView = itemView.findViewById(R.id.carbs_value);
            this.proteinTextView = itemView.findViewById(R.id.protein_value);
            this.fatTextView = itemView.findViewById(R.id.fat_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(getItem(getBindingAdapterPosition()));
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(FoodItem item);
    }
}
