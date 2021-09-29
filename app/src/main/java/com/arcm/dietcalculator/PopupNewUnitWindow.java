package com.arcm.dietcalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.arcm.dietcalculator.database.UnitEntry;

public class PopupNewUnitWindow extends PopupWindow {
    public static final String TAG = "PopupNewUnitWindow";
    private static final int width = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final int height = LinearLayout.LayoutParams.MATCH_PARENT;

    private final Context context;
    private final EditText nameEdittext;
    private final EditText valueEdittext;

    private OnSavedListener listener;

    public PopupNewUnitWindow(Context context, ViewGroup parent) {
        super(context);

        this.context = context;
        setWidth(width);
        setHeight(height);
        setFocusable(true);
        setBackgroundDrawable(null);

        View contentView = inflateLayout(context, parent);
        this.nameEdittext = contentView.findViewById(R.id.unit_name_edittext);
        this.valueEdittext = contentView.findViewById(R.id.unit_value_edittext);
        Button saveButton = contentView.findViewById(R.id.save_button);
        Button cancelButton = contentView.findViewById(R.id.cancel_button);
        saveButton.setOnClickListener(this::onSaveButtonClicked);
        cancelButton.setOnClickListener(this::onCancelButtonClicked);

        setContentView(contentView);
    }

    public PopupNewUnitWindow(Context context, ViewGroup parent, UnitEntry unit) {
        this(context, parent);
        nameEdittext.setText(unit.unitTitle);
        valueEdittext.setText(String.valueOf((int) unit.unitSize));
    }

    private View inflateLayout(Context context, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.popup_window_new_unit, parent, false);
    }

    private void onSaveButtonClicked(View view) {
        String name = Utilities.sentenceCapitalise(nameEdittext.getText().toString().trim());
        int value = Utilities.toInteger(valueEdittext.getText().toString().trim());
        if (name.isEmpty()) {
            Utilities.showErrorDialog(context, "Please provide a none-empty unit's name!");
        } else if (value == 0) {
            Utilities.showErrorDialog(context, "Please provide a valid unit weight in grams!");
        } else {
            UnitEntry unit = new UnitEntry(-1, name, value);
            if (listener != null) {
                listener.onSaved(unit);
            }
            dismiss();
        }
    }

    private void onCancelButtonClicked(View view) {
        dismiss();
    }

    public void setOnSavedListener(OnSavedListener listener) {
        this.listener = listener;
    }

    public interface OnSavedListener {
        void onSaved(UnitEntry unit);
    }
}
