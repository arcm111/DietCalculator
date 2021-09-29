package com.arcm.dietcalculator.fragments.SettingsActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;
import com.arcm.dietpiechart.DietPieChart;


public class MacrosSettingsFragment extends Fragment {
    public final static String TAG = "MacrosFragment";

    private TextView carbsValueTextView;
    private TextView proteinsValueTextView;
    private TextView fatValueTextView;
    private TextView carbsPercentTextView;
    private TextView proteinsPercentTextView;
    private TextView fatPercentTextView;
    private SetupViewModel svm;

    public MacrosSettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_macros_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.carbsValueTextView = view.findViewById(R.id.carbs_value_textView);
        this.proteinsValueTextView = view.findViewById(R.id.protein_value_textView);
        this.fatValueTextView = view.findViewById(R.id.fat_value_textView);
        this.carbsPercentTextView = view.findViewById(R.id.carbs_percent_text);
        this.proteinsPercentTextView = view.findViewById(R.id.protein_percent_text);
        this.fatPercentTextView = view.findViewById(R.id.fat_percent_text);

        DietPieChart chart = view.findViewById(R.id.macros_pie_chart);
        chart.setOnValuesChangedListener(this::onMacrosChanged);
        chart.setLabels("Carbohydrates", "Protein", "Fat");
        chart.setValues(svm.getCarbsMacro(), svm.getProteinMacro(), svm.getFatMacro());

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this::onSaveButtonClicked);

        svm.getMaintenanceCaloriesLiveData().observe(requireActivity(), integer -> updateValues());
        svm.getTargetWeightChangeLiveData().observe(requireActivity(), value -> updateValues());

        updateValues();
    }

    private void onMacrosChanged(int[] values) {
        svm.setCarbsMacro(values[0]);
        svm.setProteinMacro(values[1]);
        svm.setFatMacro(values[2]);
        updateValues();
    }

    private void updateValues() {
        carbsValueTextView.setText(getString(R.string.gram_value, svm.getTargetCarbsValue()));
        proteinsValueTextView.setText(getString(R.string.gram_value, svm.getTargetProteinValue()));
        fatValueTextView.setText(getString(R.string.gram_value, svm.getTargetFatValue()));
        carbsPercentTextView.setText(getString(R.string.percent_value, svm.getCarbsMacro()));
        proteinsPercentTextView.setText(getString(R.string.percent_value, svm.getProteinMacro()));
        fatPercentTextView.setText(getString(R.string.percent_value, svm.getFatMacro()));
    }

    private void onSaveButtonClicked(View view) {
        svm.saveSettings(requireActivity());
        requireActivity().onBackPressed();
    }
}