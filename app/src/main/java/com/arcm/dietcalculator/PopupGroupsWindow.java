package com.arcm.dietcalculator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.adapters.PopupGroupsGridAdapter;
import com.arcm.dietcalculator.database.FoodItem;

public class PopupGroupsWindow extends PopupWindow implements PopupGroupsGridAdapter.ItemClickListener {
    public static final String TAG = "PopupGroupsWindow";
    private static final int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static final int height = LinearLayout.LayoutParams.WRAP_CONTENT;

    public PopupGroupsWindow(Context context, ViewGroup parent) {
        super(context);

        setWidth(width);
        setHeight(height);
        setFocusable(true);
        setBackgroundDrawable(null);

        View contentView = inflateLayout(context, parent);
        RecyclerView rv = contentView.findViewById(R.id.groups_recyclerView);
        rv.setLayoutManager(new GridLayoutManager(context, 3));
        PopupGroupsGridAdapter adapter = new PopupGroupsGridAdapter(context);
        adapter.setOnClickListener(this);
        rv.setAdapter(adapter);

        setContentView(contentView);
    }

    private View inflateLayout(Context context, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.popup_window_group_selector, parent, false);
    }

    @Override
    public void onGroupSelected(FoodItem.Group group) {
        Log.d(TAG, "selected item: " + group.ordinal());
        dismiss();
    }
}
