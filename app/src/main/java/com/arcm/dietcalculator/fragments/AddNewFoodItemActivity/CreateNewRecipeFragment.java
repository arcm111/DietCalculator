package com.arcm.dietcalculator.fragments.AddNewFoodItemActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.arcm.dietcalculator.R;

import java.util.Locale;

public class CreateNewRecipeFragment extends Fragment {
    private String title;
    private String author;

    public CreateNewRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.recipe_toolbar);
        toolbar.setNavigationOnClickListener(this::onNavigationClicked);
    }

    private boolean onEditTitleMenuItemClick(MenuItem menuItem) {
        showEditTitleDialog(requireActivity());
        return true;
    }

    private void onNavigationClicked(View view) {
        requireActivity().onBackPressed();
    }

    private void showEditTitleDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(requireActivity());
        final View view = inflater.inflate(R.layout.dialog_recipe_edit_title, null);
        EditText titleEdittext = view.findViewById(R.id.recipe_title_edittext);
        EditText authorEdittext = view.findViewById(R.id.recipe_author_edittext);

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Edit Recipe's Title");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save", (dialog, which) -> {
            title = titleEdittext.getText().toString().toLowerCase(Locale.ROOT).trim();
            author = authorEdittext.getText().toString().toLowerCase(Locale.ROOT).trim();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", this::onDialogCanceled);
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void onDialogCanceled(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}