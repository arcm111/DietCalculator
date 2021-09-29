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

import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.R;
import com.arcm.scalenumberpicker.ScaleNumberPicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CreateFoodItemAdapter extends RecyclerView.Adapter<CreateFoodItemAdapter.ViewHolder> {
    public static final String TAG = "CreateFoodItemAdapter";
    private static final int[] backgrounds = {
            R.drawable.background_nutrient_calories_thumb,
            R.drawable.background_nutrient_carbohydrates_thumb,
            R.drawable.background_nutrient_sugar_thumb,
            R.drawable.background_nutrient_fibre_thumb,
            R.drawable.background_nutrient_fat_thumb,
            R.drawable.background_nutrient_saturated_fat_thumb,
            R.drawable.background_nutrient_protein_thumb
    };
    private static final int[] icons = {
            R.drawable.ic_calories_white,
            R.drawable.ic_carbohydrates_white,
            R.drawable.ic_sugar_white,
            R.drawable.ic_fibre_white,
            R.drawable.ic_fat_white,
            R.drawable.ic_saturated_fat_white,
            R.drawable.ic_protein_white,
    };

    private final List<NutrientItem> items;
    private final LayoutInflater inflater;
    private final Nutrients nutrients;
    private ScaleNumberPicker selectedPicker;

    public CreateFoodItemAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        String[] labels = context.getResources().getStringArray(R.array.new_item_nutrients_names);
        Nutrient[] types = Nutrient.values();
        for (int i = 0; i < 7; i++) {
            items.add(new NutrientItem(labels[i], types[i], backgrounds[i], icons[i]));
        }
        this.nutrients = new Nutrients();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_adjustable_nutrient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        NutrientItem item = items.get(position);
        holder.nameTextview.setText(item.name);
        holder.valueTextview.setText(String.valueOf(item.value));
        holder.scalePicker.setValue(item.value);
        holder.thumbImageview.setBackgroundResource(item.bgColor);
        holder.thumbImageview.setImageResource(item.icon);
        holder.type = item.type;
//        holder.setItemClickListener(() -> notifyItemChanged(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View itemView;
        public final TextView nameTextview;
        public final TextView valueTextview;
        public final ScaleNumberPicker scalePicker;
        public final ImageView thumbImageview;
        public Nutrient type;

        private ItemClickListener listener;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.nameTextview = itemView.findViewById(R.id.name);
            this.valueTextview = itemView.findViewById(R.id.calories_value);
            this.scalePicker = itemView.findViewById(R.id.scalepicker);
            this.thumbImageview = itemView.findViewById(R.id.thumb);
            scalePicker.setOnValueChangedListener((oldValue, newValue) -> {
                valueTextview.setText(String.valueOf(newValue));
                updateNutrients(newValue);
            });
            itemView.setOnClickListener(this);
        }

        private void updateNutrients(float val) {
            switch (type) {
                case CALS:
                    nutrients.cals = val;
                    break;
                case CARBS:
                    nutrients.carbs = val;
                    break;
                case SUGAR:
                    nutrients.sugar = val;
                    break;
                case FIBRE:
                    nutrients.fibre = val;
                    break;
                case FAT:
                    nutrients.fat = val;
                    break;
                case SFAT:
                    nutrients.saturatedFat = val;
                    break;
                case PROTEIN:
                    nutrients.protein = val;
                    break;
            }
        }

        public void setItemClickListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (selectedPicker != null) {
                selectedPicker.setVisibility(View.GONE);
            }
            selectedPicker = scalePicker;
            selectedPicker.setVisibility(View.VISIBLE);
            if (listener != null) listener.onItemClicked();
        }
    }

    public interface ItemClickListener {
        void onItemClicked();
    }

    private static class NutrientItem {
        public String name;
        public float value;
        public Nutrient type;
        public @DrawableRes int bgColor;
        public @DrawableRes int icon;

        public NutrientItem(String name, Nutrient type, @DrawableRes int bgColor, @DrawableRes int icon) {
            this.name = name;
            this.value = 0;
            this.bgColor = bgColor;
            this.icon = icon;
            this.type = type;
        }
    }

    private enum Nutrient {
        CALS,
        CARBS,
        SUGAR,
        FIBRE,
        FAT,
        SFAT,
        PROTEIN
    }

    public Nutrients getNutrients() {
        return nutrients;
    }
}