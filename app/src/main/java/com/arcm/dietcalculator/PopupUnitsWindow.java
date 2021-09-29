package com.arcm.dietcalculator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.database.UnitEntry;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PopupUnitsWindow extends PopupWindow {
    public static final String TAG = "UnitsPopupWindow";
    private static final int width = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final int height = LinearLayout.LayoutParams.MATCH_PARENT;

    private final Context context;

    private final LinearLayout cont;
    private final EditText unitLabelEdittext;
    private final EditText unitWeightEdittext;
    private final Button unitSaveButton;
    private final ImageView unitAddButton;

    private final String newUnitButtonText;
    private final String modifyUnitButtonText;

    private final UnitsAdapter adapter;

    private List<UnitEntry> units;
    private UnitsSaveListener listener;

    public PopupUnitsWindow(Context context, ViewGroup parent) {
        super(context);

        this.context = context;

        setWidth(width);
        setHeight(height);
        setFocusable(true);
        setBackgroundDrawable(null);

        this.newUnitButtonText = context.getString(R.string.create_unit);
        this.modifyUnitButtonText = context.getString(R.string.modify_unit);
        this.units = new ArrayList<>();
        this.adapter = new UnitsAdapter(context);

        View contentView = inflateLayout(context, parent);
        this.cont = contentView.findViewById(R.id.create_unit_cont);
        this.unitLabelEdittext = contentView.findViewById(R.id.unit_label_edittext);
        this.unitWeightEdittext = contentView.findViewById(R.id.unit_calories_edittext);
        this.unitSaveButton = contentView.findViewById(R.id.create_unit_button);
        unitSaveButton.setOnClickListener(this::createUnitClickListener);
        cont.setVisibility(View.GONE);

        this.unitAddButton = contentView.findViewById(R.id.unit_button);
        unitAddButton.setOnClickListener(this::addUnitClickListener);

        RecyclerView rv = contentView.findViewById(R.id.ingredients_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);

        Button saveAllButton = contentView.findViewById(R.id.save_button);
        saveAllButton.setOnClickListener(this::onSaveListener);

        Button cancelButton = contentView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this::onCancelListener);

        setContentView(contentView);
    }

    private void addUnitClickListener(View view) {
        if (cont.getVisibility() == View.VISIBLE) {
            hideUnitContainer();
        } else showUnitContainer();
    }

    private void createUnitClickListener(View view) {
        if (unitLabelEdittext.getText().toString().isEmpty()) {
            showErrorDialog("Unit Label Cannot be empty!");
        } else if (!isValidUnitWeight(unitWeightEdittext.getText().toString())) {
            showErrorDialog("Please specify a valid unit weight in grams!");
        } else {
            String title = unitLabelEdittext.getText().toString().trim();
            float weight = Float.parseFloat(unitWeightEdittext.getText().toString().trim());
            UnitEntry unit = new UnitEntry(-1, Utilities.sentenceCapitalise(title), weight);
            units.add(unit);
            adapter.notifyItemInserted(units.size() - 1);
            clearUnitContainer();
            hideUnitContainer();
        }
    }

    private boolean isValidUnitWeight(String s) {
        try {
            float f = Float.parseFloat(s);
            if (f >= 0) return true;
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorMessage);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    private void resetUnitContainer() {
        clearUnitContainer();
        unitSaveButton.setText(newUnitButtonText);
        unitSaveButton.setOnClickListener(this::createUnitClickListener);
    }

    private void clearUnitContainer() {
        unitLabelEdittext.setText("");
        unitWeightEdittext.setText("");
    }

    private void showUnitContainer() {
        cont.setVisibility(View.VISIBLE);
        unitAddButton.animate().rotationBy(45).start();
    }

    private void hideUnitContainer() {
        cont.setVisibility(View.GONE);
        unitAddButton.animate().rotationBy(-45).start();
        unitLabelEdittext.onEditorAction(EditorInfo.IME_ACTION_DONE);
        unitWeightEdittext.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    private View inflateLayout(Context context, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.popup_window_unit_options, parent, false);
    }

    private void onCancelListener(View view) {
        dismiss();
    }

    private void onSaveListener(View view) {
        if (listener != null) listener.onSave(units);
        dismiss();
    }

    public void setOnUnitsSaveListener(UnitsSaveListener listener) {
        this.listener = listener;
    }

    private class UnitsAdapter extends RecyclerView.Adapter<UnitsAdapter.ViewHolder> {
        private final Context context;
        private final LayoutInflater inflater;

        public UnitsAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item_unit_editable_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            UnitEntry unit = units.get(holder.getBindingAdapterPosition());

            if (unit.unitTitle.equals("g")) {
                holder.labelTextview.setText("g");
                holder.optionsImageview.setVisibility(View.GONE);
            } else {
                String txt = unit.unitTitle + " (" + Utilities.formatNumber(unit.unitSize) + " g)";
                holder.position = holder.getBindingAdapterPosition();
                holder.labelTextview.setText(txt);
                holder.optionsImageview.setOnClickListener(holder::showPopupMenu);
            }
        }

        @Override
        public int getItemCount() {
            return units.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder
                implements PopupMenu.OnMenuItemClickListener {
            public final TextView labelTextview;
            public final ImageView optionsImageview;

            public int position;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                this.labelTextview = itemView.findViewById(R.id.unit_label_textview);
                this.optionsImageview = itemView.findViewById(R.id.unit_options_imageview);
            }

            private void showPopupMenu(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.getMenuInflater().inflate(R.menu.menu_unit_list_item_options, menu.getMenu());
                menu.setOnMenuItemClickListener(this);
                menu.show();
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.edit_optoin) {
                    UnitEntry unit = units.get(position);
                    unitLabelEdittext.setText(unit.unitTitle);
                    unitWeightEdittext.setText(String.valueOf(unit.unitSize));
                    unitSaveButton.setText(modifyUnitButtonText);
                    unitSaveButton.setOnClickListener(this::modifyUnitClickListener);
                    showUnitContainer();
                    return true;
                } else if (menuItem.getItemId() == R.id.delete_option) {
                    units.remove(position);
                    adapter.notifyItemRemoved(position);
                    return true;
                }
                return false;
            }

            private void modifyUnitClickListener(View view) {
                UnitEntry unit = units.get(position);
                unit.unitTitle = unitLabelEdittext.getText().toString();
                unit.unitSize = Float.parseFloat(unitWeightEdittext.getText().toString());
                adapter.notifyItemChanged(position);
                hideUnitContainer();
                resetUnitContainer();
            }
        }
    }

    public interface UnitsSaveListener {
        void onSave(List<UnitEntry> units);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUnits(List<UnitEntry> units) {
        this.units = units;
        adapter.notifyDataSetChanged();
    }
}
