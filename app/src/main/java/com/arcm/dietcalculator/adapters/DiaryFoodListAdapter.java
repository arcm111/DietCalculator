package com.arcm.dietcalculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.R;

import java.util.List;

public class DiaryFoodListAdapter extends RecyclerView.Adapter<DiaryFoodListAdapter.ViewHolder> {
    public static final String TAG = "MediaListAdapter";
    private final LayoutInflater inflater;
    private List<DiaryItem> items;
    private ItemClickListener listener;

    public DiaryFoodListAdapter(Context context, List<DiaryItem> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_diary_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaryItem item = items.get(position);
        String id = String.valueOf(item.getId());
        if (holder.id == null || !holder.id.equals(id)) {
            holder.id = id;
            holder.nameTextView.setText(item.getName());
            holder.brandTextView.setText(item.getBrand());
            holder.calsTextView.setText(item.getCalsAsText());
            holder.weightTextView.setText(item.getWeightAsText());
            holder.iconImageView.setImageResource(item.getGroupIconResource());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public DiaryItem getItem(int position) {
        if (position >= items.size()) return null;
        return items.get(position);
    }

    public void remove(int position) {
        if (position < items.size()) items.remove(position);
    }

    public void updateItems(List<DiaryItem> items) {
        this.items = items;
    }

    public void setClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String id = null;
        public final TextView calsTextView;
        public final TextView nameTextView;
        public final TextView weightTextView;
        public final TextView brandTextView;
        public final ImageView iconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.ingredient_name);
            this.calsTextView = itemView.findViewById(R.id.ingredient_calories);
            this.weightTextView = itemView.findViewById(R.id.ingredient_weight);
            this.brandTextView = itemView.findViewById(R.id.ingredient_brand);
            this.iconImageView = itemView.findViewById(R.id.ingredient_group_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, getBindingAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
