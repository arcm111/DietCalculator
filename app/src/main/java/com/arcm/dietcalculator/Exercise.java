package com.arcm.dietcalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.adapters.BlankFoodListAdapter;
import com.arcm.dietcalculator.adapters.DiaryExerciseListAdapter;
import com.arcm.dietcalculator.database.ExerciseEntry;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Exercise implements DiaryExerciseListAdapter.ItemClickListener {
    public static final String TAG = "Exercise";

    private final Context context;
    private final DietDate date;
    private final DiaryExerciseListAdapter adapter;
    private final BlankFoodListAdapter blankAdapter;
    private final RecyclerView rv;

    private List<ExerciseEntry> items;
    private OnSaveExerciseListener saveListener;
    private OnUpdateExerciseListener updateListener;

    public Exercise(Context context, DietDate date, RecyclerView rv, ImageView addButton) {
        this.context = context;
        this.date = date;
        this.items = new ArrayList<>();
        this.blankAdapter = new BlankFoodListAdapter(context);
        this.adapter = new DiaryExerciseListAdapter(context, items);
        adapter.setClickListener(this);
        this.rv = rv;
        rv.setAdapter(blankAdapter);
        setupRecyclerView(rv);
        addButton.setOnClickListener(this::addNewDiaryExerciseItem);
    }

    public void addItems(List<ExerciseEntry> items) {
        this.items = items;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyItemsChanged() {
        if (items.isEmpty()) {
            rv.setAdapter(blankAdapter);
        } else {
            rv.setAdapter(adapter);
            adapter.updateItems(items);
            adapter.notifyDataSetChanged();
        }
    }

    private void setupRecyclerView(RecyclerView rv) {
        final int swipeFlags = ItemTouchHelper.LEFT;
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, swipeFlags) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
                if (items.isEmpty()) return makeMovementFlags(0, 0);
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getBottom() - itemView.getTop();

                // Draw the red delete background
                ColorDrawable background = new ColorDrawable(context.getResources().getColor(R.color.error));
                background.setBounds(
                        itemView.getRight() + (int) dX,
                        itemView.getTop(),
                        itemView.getRight(),
                        itemView.getBottom()
                );
                background.draw(canvas);

                // Calculate position of delete icon
                Drawable icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24dp);
                if (icon != null) {
                    int inHeight = icon.getIntrinsicHeight();
                    int inWidth = icon.getIntrinsicWidth();
                    int iconTop = itemView.getTop() + (itemHeight - inHeight) / 2;
                    int iconMargin = (itemHeight - inHeight) / 2;
                    int iconLeft = itemView.getRight() - iconMargin - inWidth;
                    int iconRight = itemView.getRight() - iconMargin;
                    int iconBottom = iconTop + inHeight;

                    // Draw the delete icon
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    icon.draw(canvas);
                } else Log.e(TAG, "delete icon not found!");
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getBindingAdapterPosition();
                items.remove(position);
                notifyItemsChanged();
            }
        };
        rv.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);
    }

    private void addNewDiaryExerciseItem(View view) {
        ExercisePopupWindow window = new ExercisePopupWindow(context,
                (ViewGroup) view.getRootView(), date, false);
        window.setOnSaveListener(this::onSaveExercise);
        window.showAtLocation(view, Gravity.TOP | Gravity.START, 0, 0);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "item: " + position);
        ExerciseEntry entry = items.get(position);
        ExercisePopupWindow window = new ExercisePopupWindow(context,
                (ViewGroup) view, date, true);
        window.setItemToBeModified(entry);
        window.setOnSaveListener(this::onUpdateExercise);
        window.showAtLocation(view, Gravity.TOP | Gravity.START, 0, 0);
    }

    private void onSaveExercise(ExerciseEntry entry) {
        if (saveListener != null) {
            saveListener.onSave(entry);
        }
    }

    private void onUpdateExercise(ExerciseEntry entry) {
        if (updateListener != null) {
            updateListener.onUpdate(entry);
        }
    }

    public interface OnSaveExerciseListener {
        void onSave(ExerciseEntry entry);
    }

    public interface OnUpdateExerciseListener {
        void onUpdate(ExerciseEntry entry);
    }

    public void setSaveListener(Exercise.OnSaveExerciseListener saveListener) {
        this.saveListener = saveListener;
    }

    public void setUpdateListener(Exercise.OnUpdateExerciseListener updateListener) {
        this.updateListener = updateListener;
    }
}
