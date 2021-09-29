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

import com.arcm.dietcalculator.database.IngredientItem;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.database.DiaryItem;

import java.util.ArrayList;
import java.util.List;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private List<IngredientItem> ingredients = new ArrayList<>();
    private OnIngredientClickedListener listener;

    public IngredientsListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IngredientItem item = ingredients.get(position);
        holder.icon.setImageResource(DiaryItem.getGroupIconResource(item.getGroup()));
        if (holder.nameTextview != null) holder.nameTextview.setText(item.getName());
        if (holder.brandTextview != null) holder.brandTextview.setText(item.getBrand());
        if (holder.caloriesTextview != null) holder.caloriesTextview.setText(item.getCaloriesAsText());
        if (holder.weightTextview != null) holder.weightTextview.setText(item.getWeightAsText());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView nameTextview;
        private final TextView brandTextview;
        private final TextView caloriesTextview;
        private final TextView weightTextview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.icon = itemView.findViewById(R.id.ingredient_group_icon);
            this.nameTextview = itemView.findViewById(R.id.ingredient_name);
            this.brandTextview = itemView.findViewById(R.id.ingredient_brand);
            this.caloriesTextview = itemView.findViewById(R.id.ingredient_calories);
            this.weightTextview = itemView.findViewById(R.id.ingredient_weight);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onIngredientClicked(ingredients.get(getBindingAdapterPosition()));
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<IngredientItem> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public void addItem(IngredientItem ingredient) {
        ingredients.add(ingredient);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeItem(int position) {
        if (position < getItemCount()) {
            ingredients.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setOnIngredientClickedListener(OnIngredientClickedListener listener) {
        this.listener = listener;
    }

    public interface OnIngredientClickedListener {
        void onIngredientClicked(IngredientItem ingredient);
    }
}
