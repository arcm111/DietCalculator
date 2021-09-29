package com.arcm.dietcalculator.fragments.SetupActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;
import com.arcm.scalenumberpicker.ScaleNumberPicker;

public class CaloriesFragment extends Fragment {
    public static final String TAG = "CaloriesFragment";
    private SetupViewModel svm;

    private ScaleNumberPicker caloriesScale;
    private ScaleNumberPicker weightScale;

    public CaloriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.svm = new ViewModelProvider(requireActivity()).get(SetupViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.caloriesScale = view.findViewById(R.id.calories_snp);
        this.weightScale = view.findViewById(R.id.weight_snp);

        svm.getMaintenanceCaloriesLiveData().observe(requireActivity(), integer -> updateValues());

        updateValues();
        setupScalesListeners();
    }

    private void updateValues() {
        caloriesScale.setValue(svm.getTargetCalories());
        weightScale.setValue(svm.getTargetWeightChange());
    }

    private void setupScalesListeners() {
        caloriesScale.setOnValueChangedListener((oldValue, newValue) -> {
            float weightChange = (newValue - svm.getMaintenanceCalories()) / 1000.0f;
            svm.setTargetWeightChange(weightChange);
            weightScale.setValue(weightChange);
            caloriesScale.setValue((int) (svm.getMaintenanceCalories() + 1000.0f * weightChange));
        });

        weightScale.setOnValueChangedListener((oldValue, newValue) -> {
            svm.setTargetWeightChange(newValue);
            caloriesScale.setValue(svm.getTargetCalories());
        });
    }
}