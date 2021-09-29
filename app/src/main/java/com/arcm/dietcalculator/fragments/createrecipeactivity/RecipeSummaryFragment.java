package com.arcm.dietcalculator.fragments.createrecipeactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcm.dietcalculator.database.IngredientItem;
import com.arcm.dietcalculator.Utilities;
import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.databinding.FragmentRecipeSummaryBinding;
import com.arcm.dietcalculator.viewmodels.RecipeViewModel;

public class RecipeSummaryFragment extends Fragment {
    private FragmentRecipeSummaryBinding binding;
    private RecipeViewModel rvm;

    public RecipeSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        this.binding = FragmentRecipeSummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rvm = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        Nutrients recipeNutrients = getRecipeNutrients();
        binding.caloriesValue.setText(Utilities.formatCalories(recipeNutrients.cals));
        binding.carbsValue.setText(Utilities.formatGrams(recipeNutrients.carbs));
        binding.sugarValue.setText(Utilities.formatGrams(recipeNutrients.sugar));
        binding.fibreValue.setText(Utilities.formatGrams(recipeNutrients.fibre));
        binding.fatValue.setText(Utilities.formatGrams(recipeNutrients.fat));
        binding.saturatedFatValue.setText(Utilities.formatGrams(recipeNutrients.saturatedFat));
        binding.proteinValue.setText(Utilities.formatGrams(recipeNutrients.protein));
        binding.saveButton.setOnClickListener(this::onSaveButtonClicked);
    }
    
    private Nutrients getRecipeNutrients() {
        Nutrients nutrients = new Nutrients();
        for (IngredientItem ingredient : rvm.getIngredients()) {
            nutrients.cals += ingredient.getCalories();
            nutrients.carbs += ingredient.getCarbohydrates();
            nutrients.sugar += ingredient.getSugar();
            nutrients.fibre += ingredient.getFibre();
            nutrients.fat += ingredient.getFat();
            nutrients.saturatedFat += ingredient.getSaturatedFat();
            nutrients.protein += ingredient.getProtein();
        }
        return nutrients;
    }

    private void onSaveButtonClicked(View view) {
        rvm.saveToDatabase(requireActivity().getApplication());
        requireActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}