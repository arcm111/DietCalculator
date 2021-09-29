package com.arcm.dietcalculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.database.FoodItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FoodGroupsGridAdapter extends RecyclerView.Adapter<FoodGroupsGridAdapter.ViewHolder> {
    public final static String TAG = "FoodGroupsGridAdapter";
    private static final FoodItem.Group[] groupsValues = FoodItem.Group.values();

    private ItemClickListener listener;
    private final List<FoodGroup> groups;
    private final LayoutInflater inflater;

    public FoodGroupsGridAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.groups = new ArrayList<>();

        int orange = R.drawable.background_group_orange;
        int red = R.drawable.background_group_red;
        int green = R.drawable.background_group_green;
        int purple = R.drawable.background_group_purple;
        int blue = R.drawable.background_group_blue;
        int transparent = android.R.color.transparent;
        groups.add(new FoodGroup("Vegetables", R.drawable.ic_group_vegetables, green));
        groups.add(new FoodGroup("Fruits", R.drawable.ic_group_fruits, orange));
        groups.add(new FoodGroup("Meat", R.drawable.ic_group_meat, red));
        groups.add(new FoodGroup("Nuts", R.drawable.ic_group_nuts, red));
        groups.add(new FoodGroup("Legumes", R.drawable.ic_group_legumes, green));
        groups.add(new FoodGroup("Pulses", R.drawable.ic_group_pulses, green));
        groups.add(new FoodGroup("Dairy", R.drawable.ic_group_dairy, blue));
        groups.add(new FoodGroup("Bakery", R.drawable.ic_group_bakery, orange));
        groups.add(new FoodGroup("Drinks", R.drawable.ic_group_drinks, purple));
        groups.add(new FoodGroup("Snacks", R.drawable.ic_group_snacks, purple));
        groups.add(new FoodGroup("Supplements", R.drawable.ic_group_supplements, blue));
        groups.add(new FoodGroup("Cooking", R.drawable.ic_group_cooking, blue));
        groups.add(new FoodGroup("Fast Food", R.drawable.ic_group_fast_food, red));
        groups.add(new FoodGroup("Grains", R.drawable.ic_group_grains, orange));
        groups.add(new FoodGroup("Deserts", R.drawable.ic_group_deserts, purple));
        groups.add(new FoodGroup("Others", R.drawable.ic_group_others, transparent));
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_grid_food_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        FoodGroup g = groups.get(position);
        holder.iconImageView.setImageResource(g.icon);
        holder.iconImageView.setBackgroundResource(g.background);
        holder.nameTextView.setText(g.name);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void setOnClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView iconImageView;
        private final TextView nameTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.iconImageView = itemView.findViewById(R.id.group_icon_imageview);
            this.nameTextView = itemView.findViewById(R.id.name_textveiw);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onGroupSelected(groupsValues[getBindingAdapterPosition()]);
            }
        }
    }

    private static class FoodGroup {
        public final String name;
        public final @DrawableRes int icon;
        public final @DrawableRes int background;
        public FoodGroup(String name, @DrawableRes int iconId, @DrawableRes int backgroundId) {
            this.name = name;
            this.icon = iconId;
            this.background = backgroundId;
        }
    }

    public interface ItemClickListener {
        void onGroupSelected(FoodItem.Group group);
    }
}
