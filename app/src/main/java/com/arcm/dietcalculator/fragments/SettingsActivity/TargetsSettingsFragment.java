package com.arcm.dietcalculator.fragments.SettingsActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;
import com.arcm.scalenumberpicker.ScaleNumberPicker;

public class TargetsSettingsFragment extends Fragment {
    public static final String TAG = "TargetsSettingsFragment";

    private SetupViewModel svm;
    private ScaleNumberPicker caloriesScale;
    private ScaleNumberPicker weightScale;

    public TargetsSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.svm = new ViewModelProvider(requireActivity()).get(SetupViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_targets_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.caloriesScale = view.findViewById(R.id.calories_snp);
        this.weightScale = view.findViewById(R.id.weight_snp);

        svm.getMaintenanceCaloriesLiveData().observe(requireActivity(), this::updateValues);
        caloriesScale.setOnValueChangedListener(this::onTargetCaloriesChanged);
        weightScale.setOnValueChangedListener(this::onWeightTargetChanged);

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this::onSaveButtonClicked);

        Toolbar toolbar = view.findViewById(R.id.navigation_toolbar);
        toolbar.setNavigationOnClickListener(this::onNavigationIconClicked);
    }

    private void updateValues(int maintenanceCalories) {
        caloriesScale.setValue(svm.getTargetCalories());
        weightScale.setValue(svm.getTargetWeightChange());
    }

    private void onTargetCaloriesChanged(float oldVal, float newVal) {
        svm.setTargetCalories((int) newVal);
        weightScale.setValue(svm.getTargetWeightChange());
    }

    private void onWeightTargetChanged(float oldVal, float newVal) {
        svm.setTargetWeightChange(newVal);
        caloriesScale.setValue(svm.getTargetCalories());
    }

    private void onSaveButtonClicked(View view) {
        svm.saveSettings(requireActivity());
        requireActivity().onBackPressed();
    }

    private void onNavigationIconClicked(View view) {
        requireActivity().onBackPressed();
    }
}