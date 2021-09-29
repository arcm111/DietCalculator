package com.arcm.dietcalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.adapters.BlankFoodListAdapter;
import com.arcm.dietcalculator.adapters.DiaryFoodListAdapter;
import com.arcm.dietcalculator.database.DiaryItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Meal implements DiaryFoodListAdapter.ItemClickListener {
    public static final String TAG = "Meal";

    private final Context context;
    private final DietDate date;
    private final DiaryFoodListAdapter adapter;
    private final BlankFoodListAdapter blankAdapter;
    private final RecyclerView rv;
    private final List<DiaryItem> items;
    private final DiaryItem.MealType type;

    public Meal(Context context, DietDate date, RecyclerView rv, ImageView addButton,
                DiaryItem.MealType type) {
        this.context = context;
        this.date = date;
        this.type = type;
        this.items = new ArrayList<>();
        this.blankAdapter = new BlankFoodListAdapter(context);
        this.adapter = new DiaryFoodListAdapter(context, items);
        adapter.setClickListener(this);
        this.rv = rv;
        rv.setAdapter(blankAdapter);
        setupRecyclerView(rv);
        addButton.setOnClickListener(this::addNewDiaryFoodItem);
    }

    public void addItem(DiaryItem item) {
        items.add(item);
    }

    public void clearItems() {
        items.clear();
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

    private void addNewDiaryFoodItem(View view) {
        Intent intent = new Intent(context, AddNewFoodItemActivity.class);
        intent.putExtra(AddNewFoodItemActivity.DATE_EXTRA, date);
        intent.putExtra(AddNewFoodItemActivity.MEAL_TYPE_EXTRA, type);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "item: " + position);
    }
}
