package com.arcm.dietcalculator.fragments.SetupActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;
import com.arcm.scalenumberpicker.ScaleNumberPicker;
import com.polyak.iconswitch.IconSwitch;

public class MetricsFragment extends Fragment {
    private final static String TAG = "MetricsFragment";
    private SetupViewModel svm;
    
    private NumberPicker ageNumberPicker;
    private ScaleNumberPicker weighSNP;
    private ScaleNumberPicker heightSNP;
    private IconSwitch genderSwitch;
    private ImageView genderImage;

    public MetricsFragment() {
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
        return inflater.inflate(R.layout.fragment_metrics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.ageNumberPicker = view.findViewById(R.id.age_number_picker);
        this.weighSNP = view.findViewById(R.id.weightScale);
        this.heightSNP = view.findViewById(R.id.heightScale);
        this.genderSwitch = view.findViewById(R.id.genderIconSwitch);
        this.genderImage = view.findViewById(R.id.gender_image);

        ageNumberPicker.setMinValue(1);
        ageNumberPicker.setMaxValue(100);
        ageNumberPicker.setValue(18);

        updateValues();
        setupListeners();
    }

    private void setupListeners() {
        genderSwitch.setCheckedChangeListener(current -> {
            if (current == IconSwitch.Checked.LEFT) {
                Log.d(TAG, "male checked");
                updateGenderImage(false);
                svm.setGender(SetupViewModel.Sex.MALE);
            } else if (current == IconSwitch.Checked.RIGHT) {
                Log.d(TAG, "female checked");
                updateGenderImage(true);
                svm.setGender(SetupViewModel.Sex.FEMALE);
            }
        });
        ageNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> svm.setAge(i1));
        weighSNP.setOnValueChangedListener((oldVal, newVal) -> svm.setWeight((double) newVal));
        heightSNP.setOnValueChangedListener((oldVal, newVal) -> svm.setHeight((int) newVal));
    }

    private void updateValues() {
        if (svm.getGender() != SetupViewModel.Sex.UNDEFINED) {
            if (svm.getGender() == SetupViewModel.Sex.MALE) {
                genderSwitch.setChecked(IconSwitch.Checked.LEFT);
                updateGenderImage(false);
            } else {
                genderSwitch.setChecked(IconSwitch.Checked.RIGHT);
                updateGenderImage(true);
            }
        }
        if (svm.getAge() > 0) ageNumberPicker.setValue(svm.getAge());
        if (svm.getWeight() > 0) weighSNP.setValue((int) svm.getWeight());
        if (svm.getHeight() > 0) heightSNP.setValue(svm.getHeight());
    }

    private void updateGenderImage(boolean isFemale) {
        if (isFemale) {
            genderImage.setImageResource(R.drawable.ic_female_body_image);
        } else genderImage.setImageResource(R.drawable.ic_male_body_image);
    }
}