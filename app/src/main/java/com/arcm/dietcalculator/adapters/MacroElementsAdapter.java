package com.arcm.dietcalculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MacroElementsAdapter extends RecyclerView.Adapter<MacroElementsAdapter.ViewHolder> {
    public static final String TAG = "MacroElementsAdapter";

    private final LayoutInflater inflater;
    private final Nutrients.Type macroType;

    private List<DiaryItem> items;
    private double totalValue;

    public MacroElementsAdapter(Context context, List<DiaryItem> items, Nutrients.Type type) {
        this.inflater = LayoutInflater.from(context);
        this.macroType = type;
        this.items = items;
        this.totalValue = getTotalValue(items);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_macro_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        DiaryItem item = items.get(position);
        int progress = (int) Math.round((item.getValueByType(macroType) / totalValue) * 100);
        holder.titleTextview.setText(item.getName());
        holder.brandTextview.setText(item.getBrand());
        holder.valueTextview.setText(item.getValueByTypeAsText(macroType));
        holder.valueProgressbar.setProgress(progress);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateValues(List<DiaryItem> items) {
        this.totalValue = getTotalValue(items);
        this.items = items;
    }

    private double getTotalValue(List<DiaryItem> items) {
        double total = 0;
        for (DiaryItem item : items) total += item.getValueByType(macroType);
        return total;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextview;
        private final TextView brandTextview;
        private final TextView valueTextview;
        private final ProgressBar valueProgressbar;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.titleTextview = itemView.findViewById(R.id.title_textview);
            this.brandTextview = itemView.findViewById(R.id.ingredient_brand);
            this.valueTextview = itemView.findViewById(R.id.value_textview);
            this.valueProgressbar = itemView.findViewById(R.id.value_progressbar);
        }
    }
}
