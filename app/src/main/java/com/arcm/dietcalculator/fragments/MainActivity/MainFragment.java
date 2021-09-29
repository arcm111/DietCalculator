package com.arcm.dietcalculator.fragments.MainActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arcm.caloriescaption.CaloriesCaption;
import com.arcm.caloriesprogressbar.CaloriesProgressBar;
import com.arcm.dietcalculator.CreateRecipeActivity;
import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.SettingsActivity;
import com.arcm.dietcalculator.viewmodels.MainViewModel;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.DietSettings;
import com.arcm.nutrientsgraph.NutrientsGraph;

import java.util.Calendar;
import java.util.List;

public class MainFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = "MainFragment";

    private Nutrients targets;
    private Nutrients consumed;

    private CaloriesProgressBar calsProgressBar;
    private CaloriesCaption calsCaption;
    private NutrientsGraph nutrientsGraph;

    private MainViewModel mvm;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mvm = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        this.targets = DietSettings.getNutrientsTargets(requireContext());
        this.consumed = new Nutrients();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.calsProgressBar = view.findViewById(R.id.caloriesProgressBar);
        this.calsCaption = view.findViewById(R.id.caloriesCaption);
        this.nutrientsGraph = view.findViewById(R.id.nutrientsGraph);

        nutrientsGraph.setValues(new int[6]);
        ImageView profileButton = view.findViewById(R.id.profile_imageview);
        profileButton.setOnClickListener(this::startSettingsActivity);
        ImageView progressButton = view.findViewById(R.id.progress_imageview);
        progressButton.setOnClickListener(this::startSetupActivity);
        LinearLayout dateCont = view.findViewById(R.id.date_cont);
        dateCont.setOnClickListener(this::onDateContainerClicked);


        MainViewModel mvm = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mvm.diaryItems.observe(requireActivity(), this::updateValuesFromDatabase);
        mvm.targetCalories.observe(requireActivity(), this::updateTargets);
    }

    private void onDateContainerClicked(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), this, year, month, day);
        dialog.show();
    }

    private void updateTargets(int targetCalories) {
        this.targets = mvm.getNutrientsTargets();
        calsProgressBar.setMaxValue(targetCalories);
        calsCaption.setTargetCalories(targetCalories);
        nutrientsGraph.setTargetValues(targets.toIntArray());
    }

    private void startSetupActivity(View view) {
//        startActivity(new Intent(requireActivity(), SetupActivity.class));
        startActivity(new Intent(requireActivity(), CreateRecipeActivity.class));
    }

    private void startSettingsActivity(View view) {
        startActivity(new Intent(requireActivity(), SettingsActivity.class));
    }

    private void updateValuesFromDatabase(List<DiaryItem> foodItems) {
        resetValues();
        for (DiaryItem item: foodItems) {
            consumed.cals += item.getCals();
            consumed.carbs += item.getCarbs();
            consumed.protein += item.getProtein();
            consumed.fat += item.getFat();
            consumed.fibre += item.getFibre();
            consumed.sugar += item.getSugar();
            consumed.saturatedFat += item.getSaturatedFat();
        }
        updateValues();
    }

    private void resetValues() {
        this.consumed = new Nutrients();
    }

    private void updateValues() {
        calsProgressBar.setValue((int) Math.round(targets.cals - consumed.cals));
        calsCaption.setConsumedCalories((int) Math.round(consumed.cals));
        nutrientsGraph.setValues(consumed.toIntArray());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.i(TAG, i + "/" + (i1 + 1) + "/" + i2);
    }
}