package com.arcm.dietcalculator.fragments.AddNewFoodItemActivity;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcm.dietcalculator.adapters.DiaryFoodListAdapter;
import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.DietDate;
import com.arcm.dietcalculator.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FoodListFragment extends Fragment implements DiaryFoodListAdapter.ItemClickListener {
    public static final String TAG = "FoodListFragment";
    private static final String ARG_PARAM1 = "date";
    private List<DiaryItem> items = new ArrayList<>();
    private DietDate date;
    private RecyclerView rv;
    private DiaryFoodListAdapter adapter;

    public FoodListFragment() {
        // Required empty public constructor
    }

    public static FoodListFragment newInstance(DietDate date) {
        FoodListFragment fragment = new FoodListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.date = (DietDate) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton btn = view.findViewById(R.id.add_floatbutton);
        btn.setOnClickListener(view1 -> switchToSearchFragment());
        TextView datetv = view.findViewById(R.id.date_textview);
        datetv.setText(date.toShortDateString());
        this.rv = view.findViewById(R.id.foods_recyclerView);
        setupRecyclerView(rv);
        displayItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        displayItems();
    }

    private void setupRecyclerView(RecyclerView rv) {
        int flags = ItemTouchHelper.LEFT;
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, flags) {

            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
//            Toast.makeText(ListActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getBottom() - itemView.getTop();

                // Draw the red delete background
                ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.error));
                background.setBounds(
                        itemView.getRight() + (int) dX,
                        itemView.getTop(),
                        itemView.getRight(),
                        itemView.getBottom()
                );
                background.draw(canvas);

                // Calculate position of delete icon
                Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_white_24dp);
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
//            Toast.makeText(ListActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                adapter.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);
    }

    private void displayItems() {
//        DietDatabase db = new DietDatabase(getContext());
//        this.items = db.getItemsByDate(date);
        Log.d(TAG, "number of table rows: " + items.size());
        for (DiaryItem item : items) {
            Log.d(TAG, item.toString());
        }
        if (adapter == null) {
            this.adapter = new DiaryFoodListAdapter(getContext(), items);
            adapter.setClickListener(this);
            rv.setAdapter(adapter);
        } else {
            adapter.updateItems(items);
            adapter.notifyDataSetChanged();
        }
    }

    private void switchToSearchFragment() {
        SearchFragment f = new SearchFragment();
        if (getActivity() != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().add(android.R.id.content, f).addToBackStack(TAG).commit();
        } else Log.e(TAG, "Failed to get parent activity!");
    }

    private void startNutrientsFragment(String id) {
        if (getActivity() != null) {
            NutrientsFragment f = NutrientsFragment.newInstance(id, date, DiaryItem.MealType.BREAKFAST);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
        } else Log.e(TAG, "Cannot find parent activity!");
    }

    @Override
    public void onItemClick(View view, int position) {
        DiaryItem item = items.get(position);
        Log.d(TAG, item.getId() + "");
        startNutrientsFragment(item.foodItem.fdcId);
    }
}