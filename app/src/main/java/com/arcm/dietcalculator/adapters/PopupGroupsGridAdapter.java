package com.arcm.dietcalculator.adapters;

import android.content.Context;
import android.content.res.TypedArray;
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

public class PopupGroupsGridAdapter extends RecyclerView.Adapter<PopupGroupsGridAdapter.ViewHolder> {
    public final static String TAG = "FoodGroupsGridAdapter";

    private ItemClickListener listener;
    private final List<FoodGroup> groups;
    private final LayoutInflater inflater;

    public PopupGroupsGridAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.groups = new ArrayList<>();

        String[] names = context.getResources().getStringArray(R.array.food_groups);
        TypedArray icons = context.getResources().obtainTypedArray(R.array.groups_icons_drawables);
        FoodItem.Group[] values = FoodItem.Group.values();

        for (int i = 0; i < values.length; i++) {
            if (values[i] == FoodItem.Group.COOKING) continue;
            groups.add(new FoodGroup(names[i], values[i], icons.getResourceId(i, 0)));
        }

        icons.recycle();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_popup_food_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        FoodGroup g = groups.get(position);
        holder.iconImageView.setImageResource(g.icon);
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
                listener.onGroupSelected(groups.get(getBindingAdapterPosition()).group);
            }
        }
    }

    private static class FoodGroup {
        public final String name;
        public final @DrawableRes int icon;
        public final FoodItem.Group group;

        public FoodGroup(String name, FoodItem.Group group, @DrawableRes int iconId) {
            this.name = name;
            this.group = group;
            this.icon = iconId;
        }
    }

    public interface ItemClickListener {
        void onGroupSelected(FoodItem.Group group);
    }
}
