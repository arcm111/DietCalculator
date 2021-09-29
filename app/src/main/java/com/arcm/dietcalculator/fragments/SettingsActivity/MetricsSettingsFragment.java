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
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;
import com.arcm.scalenumberpicker.ScaleNumberPicker;
import com.polyak.iconswitch.IconSwitch;

public class MetricsSettingsFragment extends Fragment {
    public final static String TAG = "MetricsSettingsFragment";

    private NumberPicker ageNumberPicker;
    private ScaleNumberPicker weighSNP;
    private ScaleNumberPicker heightSNP;
    private IconSwitch genderSwitch;
    private ImageView genderImage;

    private SetupViewModel svm;

    public MetricsSettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_metrics_settings, container, false);
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

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this::onSaveButtonClicked);

        initValues();
        setupListeners();
    }

    private void setupListeners() {
        genderSwitch.setCheckedChangeListener(this::onGenderChanged);
        ageNumberPicker.setOnValueChangedListener(this::onAgeChanged);
        weighSNP.setOnValueChangedListener(this::onWeightChanged);
        heightSNP.setOnValueChangedListener(this::onHeightChanged);
    }

    private void initValues() {
        if (!svm.isFemale()) {
            genderSwitch.setChecked(IconSwitch.Checked.LEFT);
            updateGenderImage(false);
        } else {
            genderSwitch.setChecked(IconSwitch.Checked.RIGHT);
            updateGenderImage(true);
        }
        if (svm.getAge() > 0) ageNumberPicker.setValue(svm.getAge());
        if (svm.getWeight() > 0) weighSNP.setValue((int) svm.getWeight());
        if (svm.getHeight() > 0) heightSNP.setValue(svm.getHeight());
    }

    private void onAgeChanged(NumberPicker np, int oldVal, int newVal) {
        svm.setAge(newVal);
    }

    private void onGenderChanged(IconSwitch.Checked current) {
        boolean isFemale = (current == IconSwitch.Checked.RIGHT);
        updateGenderImage(isFemale);
        svm.setGender(isFemale ? SetupViewModel.Sex.FEMALE : SetupViewModel.Sex.MALE);
    }

    private void onWeightChanged(float oldVal, float newVal) {
        svm.setWeight((double) newVal);
    }

    private void onHeightChanged(float oldVal, float newVal) {
        svm.setHeight((int) newVal);
    }

    private void updateGenderImage(boolean isFemale) {
        if (isFemale) {
            genderImage.setImageResource(R.drawable.ic_female_body_image);
        } else genderImage.setImageResource(R.drawable.ic_male_body_image);
    }

    private void onSaveButtonClicked(View view) {
        svm.saveSettings(requireActivity());
        requireActivity().onBackPressed();
    }
}