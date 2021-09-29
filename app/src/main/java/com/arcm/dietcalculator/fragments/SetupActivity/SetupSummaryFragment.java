package com.arcm.dietcalculator.fragments.SetupActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;

import java.text.DecimalFormat;

public class SetupSummaryFragment extends Fragment {
    public static final String TAG = "SetupSummaryFragment";
    private final DecimalFormat decimalFormat = new DecimalFormat("0.##");
    private SetupViewModel svm;

    public SetupSummaryFragment() {
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
        return inflater.inflate(R.layout.fragment_setup_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageView gender = view.findViewById(R.id.gender_imageview);
        final TextView age = view.findViewById(R.id.ingredient_brand);
        final TextView height = view.findViewById(R.id.height_textview);
        final TextView weight = view.findViewById(R.id.weight_textView);
        final TextView activities = view.findViewById(R.id.activities_textview);
        final TextView targetCalories = view.findViewById(R.id.target_calories_textview);
        final TextView targetWeight = view.findViewById(R.id.target_weight_textview);
        final TextView carbsPercent = view.findViewById(R.id.carbs_percent_text);
        final TextView proteinPercent = view.findViewById(R.id.protein_percent_text);
        final TextView fatPercent = view.findViewById(R.id.fat_percent_text);
        final TextView carbsValue = view.findViewById(R.id.carbs_value_textView);
        final TextView proteinValue = view.findViewById(R.id.protein_value_textView);
        final TextView fatValue = view.findViewById(R.id.fat_value_textView);

        age.setText(toString(svm.getAge()));
        height.setText(getString(R.string.cm_value, svm.getHeight()));
        weight.setText(getString(R.string.kg_double_value, svm.getWeight()));
        activities.setText(svm.getFactor().toString());
        targetCalories.setText(toString(svm.getTargetCalories()));
        targetWeight.setText(formatDecimal(svm.getTargetWeightChange()));
        carbsPercent.setText(getString(R.string.percent_value, svm.getCarbsMacro()));
        proteinPercent.setText(getString(R.string.percent_value, svm.getProteinMacro()));
        fatPercent.setText(getString(R.string.percent_value, svm.getFatMacro()));
        carbsValue.setText(getString(R.string.gram_value, svm.getTargetCarbsValue()));
        proteinValue.setText(getString(R.string.gram_value, svm.getTargetProteinValue()));
        fatValue.setText(getString(R.string.gram_value, svm.getTargetFatValue()));
        
        final LifecycleOwner owner = requireActivity();

        svm.getAgeLiveData().observe(owner, integer -> age.setText(toString(integer)));
        svm.getHeightLiveData().observe(owner, integer -> height.setText(toString(integer)));
        svm.getWeightLiveData().observe(owner, val -> weight.setText(formatDecimal(val)));
        svm.getFactorLiveData().observe(owner, factor -> activities.setText(factor.toString()));

        svm.getCarbsMacroLiveData().observe(owner, integer -> {
            carbsPercent.setText(getString(R.string.percent_value, integer));
            carbsValue.setText(getString(R.string.gram_value, svm.getTargetCarbsValue()));
        });

        svm.getProteinMacroLiveData().observe(owner, integer -> {
            proteinPercent.setText(getString(R.string.percent_value, integer));
            proteinValue.setText(getString(R.string.gram_value, svm.getTargetProteinValue()));
        });

        svm.getFatMacroLiveData().observe(owner, integer -> {
            fatPercent.setText(getString(R.string.percent_value, integer));
            fatValue.setText(getString(R.string.gram_value, svm.getTargetFatValue()));
        });
        
        svm.getMaintenanceCaloriesLiveData().observe(owner, val -> {
            targetCalories.setText(toString(svm.getTargetCalories()));
            carbsValue.setText(getString(R.string.gram_value, svm.getTargetCarbsValue()));
            proteinValue.setText(getString(R.string.gram_value, svm.getTargetProteinValue()));
            fatValue.setText(getString(R.string.gram_value, svm.getTargetFatValue()));
        });

        svm.getTargetWeightChangeLiveData().observe(owner, value -> {
            targetCalories.setText(toString(svm.getTargetCalories()));
            targetWeight.setText(formatDecimal(svm.getTargetWeightChange()));
            carbsValue.setText(getString(R.string.gram_value, svm.getTargetCarbsValue()));
            proteinValue.setText(getString(R.string.gram_value, svm.getTargetProteinValue()));
            fatValue.setText(getString(R.string.gram_value, svm.getTargetFatValue()));
        });

        svm.getGenderLiveData().observe(owner, value -> {
            if (value == SetupViewModel.Sex.FEMALE) {
                gender.setImageResource(R.drawable.ic_female_avatar_large);
            } else gender.setImageResource(R.drawable.ic_male_avatar_large);
        });
    }
    
    private String formatDecimal(float decimal) {
        return decimalFormat.format(decimal);
    }
    
    private String formatDecimal(double decimal) {
        return decimalFormat.format(decimal);
    }
    
    private String toString(int n) {
        return String.valueOf(n);
    }
}