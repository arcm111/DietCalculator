package com.arcm.dietcalculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.database.ExerciseEntry;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.Utilities;

import java.util.List;

public class DiaryExerciseListAdapter extends RecyclerView.Adapter<DiaryExerciseListAdapter.ViewHolder> {
    public static final String TAG = "DiaryExerciseListAdapter";
    private final LayoutInflater inflater;
    private List<ExerciseEntry> entries;
    private ItemClickListener listener;

    public DiaryExerciseListAdapter(Context context, List<ExerciseEntry> items) {
        this.entries = items;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_diary_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciseEntry entry = entries.get(position);
        String id = String.valueOf(entry.id);
        if (holder.id == null || !holder.id.equals(id)) {
            holder.id = id;
            holder.nameTextView.setText(entry.label);
            holder.calsTextView.setText(Utilities.formatCalories(entry.caloriesBurnt));
        }
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public ExerciseEntry getItem(int position) {
        if (position >= entries.size()) return null;
        return entries.get(position);
    }

    public void remove(int position) {
        if (position < entries.size()) entries.remove(position);
    }

    public void updateItems(List<ExerciseEntry> items) {
        this.entries = items;
    }

    public void setClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String id = null;
        public final TextView calsTextView;
        public final TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.ingredient_name);
            this.calsTextView = itemView.findViewById(R.id.ingredient_calories);
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