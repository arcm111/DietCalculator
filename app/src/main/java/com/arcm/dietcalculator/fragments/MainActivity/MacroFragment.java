package com.arcm.dietcalculator.fragments.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcm.dietcalculator.DietSettings;
import com.arcm.dietcalculator.Utilities;
import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.viewmodels.MainViewModel;
import com.arcm.dietcalculator.R;
import com.arcm.dietpiechart.DietPieChart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MacroFragment extends Fragment {
    private DietPieChart macrosPieChart;
    private TextView carbsTargetTextview;
    private TextView proteinTargetTextview;
    private TextView fatTargetTextview;
    private TextView carbsValueTextview;
    private TextView proteinValueTextview;
    private TextView fatValueTextview;

    public MacroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_macro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.macrosPieChart = view.findViewById(R.id.macros_pieChart);
        this.carbsTargetTextview = view.findViewById(R.id.macro_carbs_target);
        this.proteinTargetTextview = view.findViewById(R.id.macro_protein_target);
        this.fatTargetTextview = view.findViewById(R.id.macro_fat_target);
        this.carbsValueTextview = view.findViewById(R.id.macro_carbs_value);
        this.proteinValueTextview = view.findViewById(R.id.macro_protein_value);
        this.fatValueTextview = view.findViewById(R.id.macro_fat_value);

        MainViewModel mvm = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mvm.diaryItems.observe(requireActivity(), this::updateCurrentMacros);
        mvm.targetMacros.observe(requireActivity(), this::updateTargetMacros);

        setMacrosValues(DietSettings.getMacros(requireActivity()));
    }

    private void updateCurrentMacros(List<DiaryItem> items) {
        if (items != null && items.size() > 0) {
            double cals = 0;
            double protein = 0;
            double fat = 0;
            for (DiaryItem item : items) {
                cals += item.getCals();
                protein += item.getProtein();
                fat += item.getFat();
            }
            int macroProtein = (int) Math.round(((protein * Nutrients.PROTEIN_CALS_VALUE) / cals) * 100);
            int macroFat = (int) Math.round(((fat * Nutrients.FAT_CALS_VALUE) / cals) * 100);
            int macroCarbs = 100 - macroFat - macroProtein;
            setMacrosValues(macroCarbs, macroProtein, macroFat);
        }
    }

    private void updateTargetMacros(Integer[] macros) {
        carbsTargetTextview.setText(Utilities.getPercent(macros[0]));
        proteinTargetTextview.setText(Utilities.getPercent(macros[1]));
        fatTargetTextview.setText(Utilities.getPercent(macros[2]));
    }

    private void setMacrosValues(int[] macros) {
        assert macros.length == 3;
        setMacrosValues(macros[0], macros[1], macros[2]);
    }

    private void setMacrosValues(int carbs, int protein, int fat) {
        macrosPieChart.setValues(carbs, protein, fat);
        carbsValueTextview.setText(Utilities.getPercent(carbs));
        proteinValueTextview.setText(Utilities.getPercent(protein));
        fatValueTextview.setText(Utilities.getPercent(fat));
    }
}