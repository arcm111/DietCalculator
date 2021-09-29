package com.arcm.dietcalculator.fragments.SetupActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;

public class ActivitiesLevelFragment extends Fragment {
    public final static String TAG = "ActivitiesLevelFragment";

    private final String[] desc = new String[6];
    private SetupViewModel svm;
    private RadioButton sedentaryRadioButton;
    private RadioButton lightRadioButton;
    private RadioButton moderateRadioButton;
    private RadioButton highRadioButton;
    private RadioButton extremeRadioButton;
    private TextView selectedActivityDescription;

    public ActivitiesLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.svm = new ViewModelProvider(requireActivity()).get(SetupViewModel.class);
        desc[0] = getResources().getString(R.string.activity_sedentary);
        desc[1] = getResources().getString(R.string.activity_light);
        desc[2] = getResources().getString(R.string.activity_moderate);
        desc[3] = getResources().getString(R.string.activity_high);
        desc[4] = getResources().getString(R.string.activity_extreme);
        desc[5] = "Unknown";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities_level, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup activityRadioGroup = view.findViewById(R.id.activity_factor_radioGroup);
        this.sedentaryRadioButton = view.findViewById(R.id.sedentary_radioButton);
        this.lightRadioButton = view.findViewById(R.id.light_radioButton);
        this.moderateRadioButton = view.findViewById(R.id.moderate_radioButton);
        this.highRadioButton = view.findViewById(R.id.high_radioButton);
        this.extremeRadioButton = view.findViewById(R.id.extreme_radioButton);
        this.selectedActivityDescription = view.findViewById(R.id.activities_level_desc);

        RadioGroup.OnCheckedChangeListener listener = (radioGroup, i) -> {
            if (i == R.id.sedentary_radioButton) {
                svm.setFactor(SetupViewModel.ActivityFactor.SEDENTARY);
            } else if (i == R.id.light_radioButton) {
                svm.setFactor(SetupViewModel.ActivityFactor.LIGHT);
            } else if (i == R.id.moderate_radioButton) {
                svm.setFactor(SetupViewModel.ActivityFactor.MODERATE);
            } else if (i == R.id.high_radioButton) {
                svm.setFactor(SetupViewModel.ActivityFactor.HIGH);
            } else if (i == R.id.extreme_radioButton) {
                svm.setFactor(SetupViewModel.ActivityFactor.EXTREME);
            }
            selectedActivityDescription.setText(desc[svm.getFactor().ordinal()]);
        };
        
        setSelectedRadioButton(svm.getFactor());
        // set listener after selecting the checked radio button so
        // that the listener doesn't get triggered
        activityRadioGroup.setOnCheckedChangeListener(listener);
    }
    
    private void setSelectedRadioButton(SetupViewModel.ActivityFactor factor) {
        switch (factor) {
            case LIGHT:
                lightRadioButton.setChecked(true);
                break;
            case SEDENTARY:
                sedentaryRadioButton.setChecked(true);
                break;
            case MODERATE:
                moderateRadioButton.setChecked(true);
                break;
            case HIGH:
                highRadioButton.setChecked(true);
                break;
            case EXTREME:
                extremeRadioButton.setChecked(true);
                break;
        }
        selectedActivityDescription.setText(desc[svm.getFactor().ordinal()]);
    }
}