package com.arcm.dietcalculator.fragments.SettingsActivity;

import android.os.Bundle;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.Utilities;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;

public class MainSettingsFragment extends Fragment {
    private static final @AnimRes int t1 = android.R.anim.slide_in_left;
    private static final @AnimRes int t2 = android.R.anim.slide_out_right;
    private static final @IdRes int cont = android.R.id.content;

    private SetupViewModel svm;
    private FragmentManager fm;
    private TextView ageTextview;
    private TextView heightTextview;
    private TextView weightTextview;
    private TextView activitiesTextview;
    private TextView targetCaloriesTextview;
    private TextView targetWeightChangeTextview;
    private ImageView genderImage;

    public MainSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fm = getParentFragmentManager();
        this.svm = new ViewModelProvider(requireActivity()).get(SetupViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.ageTextview = view.findViewById(R.id.age_textview);
        this.heightTextview = view.findViewById(R.id.height_textview);
        this.weightTextview = view.findViewById(R.id.ingredient_weight);
        this.activitiesTextview = view.findViewById(R.id.activity_level);
        this.genderImage = view.findViewById(R.id.profile_image);
        this.targetCaloriesTextview = view.findViewById(R.id.target_calories_textview);
        this.targetWeightChangeTextview = view.findViewById(R.id.target_weight_textview);

        svm.getAgeLiveData().observe(requireActivity(), this::onAgeChanged);
        svm.getHeightLiveData().observe(requireActivity(), this::onHeightChanged);
        svm.getWeightLiveData().observe(requireActivity(), this::onWeightChanged);
        svm.getFactorLiveData().observe(requireActivity(), this::onActivitiesChanged);
        svm.getGenderLiveData().observe(requireActivity(), this::onGenderChanged);
        svm.getTargetCaloriesLiveDate().observe(requireActivity(), this::onTargetCaloriesChanged);
        svm.getTargetWeightChangeLiveData().observe(requireActivity(), this::onTargetWeightChanged);

        LinearLayout details = view.findViewById(R.id.personal_details_settings);
        details.setOnClickListener(this::startDetailsSettingsFragment);
        LinearLayout activities = view.findViewById(R.id.activities_settings);
        activities.setOnClickListener(this::startActivitiesSettingsFragment);
        LinearLayout targets = view.findViewById(R.id.targets_settings);
        targets.setOnClickListener(this::startTargetsSettingsFragment);
        LinearLayout macros = view.findViewById(R.id.macros_settings);
        macros.setOnClickListener(this::startMacrosSettingsFragment);
        LinearLayout myItems = view.findViewById(R.id.my_food_items);
        myItems.setOnClickListener(this::startMyItemsSettingsFragment);
        LinearLayout myRecipes = view.findViewById(R.id.my_recipes);
        myRecipes.setOnClickListener(this::startMyRecipesSettingsFragment);
    }
    
    private void onAgeChanged(int age) {
        ageTextview.setText(String.valueOf(age));
    }
    
    private void onHeightChanged(int height) {
        heightTextview.setText(String.valueOf(height));
    }
    
    private void onWeightChanged(double weight) {
        weightTextview.setText(Utilities.formatDecimal(weight));
    }
    
    private void onActivitiesChanged(SetupViewModel.ActivityFactor activities) {
        activitiesTextview.setText(activities.toString());
    }

    private void onGenderChanged(SetupViewModel.Sex gender) {
        if (gender == SetupViewModel.Sex.FEMALE) {
            genderImage.setImageResource(R.drawable.ic_female_profile_icon);
        } else genderImage.setImageResource(R.drawable.ic_male_profile_icon);
    }

    private void onTargetCaloriesChanged(int targetCalories) {
        targetCaloriesTextview.setText(Utilities.formatCalories(targetCalories));
    }

    private void onTargetWeightChanged(float weight) {
        targetWeightChangeTextview.setText(Utilities.formatKilograms(weight));
    }

    private void startDetailsSettingsFragment(View view) {
        startFragment(new MetricsSettingsFragment());
    }

    private void startActivitiesSettingsFragment(View view) {
        startFragment(new ActivitiesLevelSettingsFragment());
    }

    private void startTargetsSettingsFragment(View view) {
        startFragment(new TargetsSettingsFragment());
    }

    private void startMacrosSettingsFragment(View view) {
        startFragment(new MacrosSettingsFragment());
    }

    private void startMyItemsSettingsFragment(View view) {
        startFragment(new MyItemsFragment());
    }

    private void startMyRecipesSettingsFragment(View view) {
        startFragment(new MyRecipesFragment());
    }

    private void startFragment(Fragment f) {
        fm.beginTransaction().setCustomAnimations(t1, t2).add(cont, f).addToBackStack(null).commit();
    }
}