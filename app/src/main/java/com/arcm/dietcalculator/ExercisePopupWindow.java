package com.arcm.dietcalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.arcm.dietcalculator.database.ExerciseEntry;

public class ExercisePopupWindow extends PopupWindow {
    public static final String TAG = "ExercisePopupWindow";
    private static final int width = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final int height = LinearLayout.LayoutParams.MATCH_PARENT;

    private final Context context;
    private final DietDate date;

    private final EditText exerciseLabelEdittext;
    private final EditText exerciseCaloriesEdittext;

    private OnSaveListener listener;

    private ExerciseEntry exerciseEntryToBeModified;

    public ExercisePopupWindow(Context context, ViewGroup parent, DietDate date, boolean modify) {
        super(context);

        this.context = context;
        this.date = date;

        setWidth(width);
        setHeight(height);
        setFocusable(true);
        setBackgroundDrawable(null);

        View contentView = inflateLayout(context, parent);
        this.exerciseLabelEdittext = contentView.findViewById(R.id.unit_name_edittext);
        this.exerciseCaloriesEdittext = contentView.findViewById(R.id.unit_value_edittext);

        if (modify) {
            ImageView icon = contentView.findViewById(R.id.exercise_button);
            icon.setImageResource(R.drawable.ic_edit_black);
        }

        Button saveButton = contentView.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this::onSaveListener);

        Button cancelButton = contentView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this::onCancelListener);

        setContentView(contentView);
    }

    public void setItemToBeModified(ExerciseEntry entry) {
        this.exerciseEntryToBeModified = entry;
        exerciseLabelEdittext.setText(entry.label);
        exerciseCaloriesEdittext.setText(String.valueOf(entry.caloriesBurnt));
    }

    private boolean isValidInteger(String s) {
        try {
            int f = Integer.parseInt(s);
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

    private View inflateLayout(Context context, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.popup_window_exercise_options, parent, false);
    }

    private void onCancelListener(View view) {
        dismiss();
    }

    private void onSaveListener(View view) {
        if (exerciseLabelEdittext.getText().toString().isEmpty()) {
            showErrorDialog("Exercise label cannot be empty!");
        } else if (!isValidInteger(exerciseCaloriesEdittext.getText().toString())) {
            showErrorDialog("Please specify a valid calories value!");
        } else {
            if (listener != null) {
                String label = exerciseLabelEdittext.getText().toString().trim();
                int calories = Integer.parseInt(exerciseCaloriesEdittext.getText().toString().trim());
                if (exerciseEntryToBeModified != null) {
                    exerciseEntryToBeModified.label = label;
                    exerciseEntryToBeModified.caloriesBurnt = calories;
                    listener.onSave(exerciseEntryToBeModified);
                } else {
                    ExerciseEntry entry = new ExerciseEntry(date, label, calories);
                    listener.onSave(entry);
                }
            } else Log.d(TAG, "OnSaveListener is not set!");
        }
        dismiss();
    }

    public void setOnSaveListener(OnSaveListener listener) {
        this.listener = listener;
    }

    public interface OnSaveListener {
        void onSave(ExerciseEntry entry);
    }
}
