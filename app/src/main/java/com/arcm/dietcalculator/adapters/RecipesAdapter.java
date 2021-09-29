package com.arcm.dietcalculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.Utilities;
import com.arcm.dietcalculator.database.RecipeItem;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    public static final String TAG = "MediaListAdapter";
    private final LayoutInflater inflater;
    private List<RecipeItem> items;
    private ItemClickListener listener;

    public RecipesAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeItem item = items.get(position);
        String id = String.valueOf(item.getId());
        if (holder.id == null || !holder.id.equals(id)) {
            holder.id = id;
            holder.nameTextView.setText(item.getName());
            holder.authorTextView.setText(item.getAuthor());
            holder.calsTextView.setText(Utilities.formatCalories(item.getCalories()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public RecipeItem getItem(int position) {
        if (position >= items.size()) return null;
        return items.get(position);
    }

    public void remove(int position) {
        if (position < items.size()) items.remove(position);
    }

    public void updateItems(List<RecipeItem> items) {
        this.items = items;
    }

    public void setClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String id = null;
        public final TextView calsTextView;
        public final TextView nameTextView;
        public final TextView authorTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.recipe_name);
            this.calsTextView = itemView.findViewById(R.id.recipe_calories);
            this.authorTextView = itemView.findViewById(R.id.recipe_author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClicked(v, getBindingAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClicked(View view, int position);
    }

}
