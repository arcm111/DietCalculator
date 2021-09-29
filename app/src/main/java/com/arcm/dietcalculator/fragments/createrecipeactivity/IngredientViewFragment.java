package com.arcm.dietcalculator.fragments.createrecipeactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcm.dietcalculator.database.IngredientItem;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.database.FoodRepository;
import com.arcm.dietcalculator.databinding.FragmentIngredientViewBinding;
import com.arcm.dietcalculator.viewmodels.RecipeViewModel;

public class IngredientViewFragment extends Fragment {
    public static final String TAG = "IngredientViewFragment";
    public static final String ARG_FOOD_ID = "food-item-id";

    private FragmentIngredientViewBinding binding;
    private long foodId;
    private IngredientItem ingredient;
    private RecipeViewModel rvm;

    public IngredientViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            long id = bundle.getLong(ARG_FOOD_ID);
            Log.d(TAG, "Argument " + ARG_FOOD_ID + ": " + id);
            this.foodId = id;
        }
        this.rvm = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        // Inflate the layout for this fragment
        this.binding = FragmentIngredientViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FoodRepository db = new FoodRepository(requireActivity().getApplication());
        if (foodId > 0) {
            db.getFoodItemById(foodId, this::onIngredientFetched);
        } else showFatalErrorDialog();
        binding.recipeWeight.setOnValueChangedListener(this::onIngredientWeightChanged);
        binding.saveIngredientButton.setOnClickListener(this::onSaveIngredientButtonClicked);
    }

    private void onIngredientWeightChanged(float oldVal, float newVal) {
        if (ingredient != null) ingredient.setWeight(newVal);
    }

    private void onIngredientFetched(FoodItem item) {
        if (item != null) {
            this.ingredient = new IngredientItem(item);
            binding.foodName.setText(item.getFoodName());
            binding.foodBrand.setText(item.getBrand());
            binding.ingredientGroup.setImageResource(DiaryItem.getGroupIconResource(item.getGroup()));
            binding.caloriesValue.setText(item.getCalsAsText());
            binding.carbsValue.setText(item.getCarbsAsText());
            binding.sugarValue.setText(item.getSugarAsText());
            binding.fibreValue.setText(item.getFibreAsText());
            binding.fatValue.setText(item.getFatAsText());
            binding.saturatedFatValue.setText(item.getSaturatedFatAsText());
            binding.proteinValue.setText(item.getProteinAsText());
        } else showFatalErrorDialog();
    }

    private void showFatalErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Error");
        builder.setMessage("Sorry the item you selected cannot be found!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", this::onDialogDismissed);
        builder.create().show();
    }

    private void onDialogDismissed(DialogInterface dialog, int i) {
        NavController controller = NavHostFragment.findNavController(IngredientViewFragment.this);
        controller.navigate(R.id.action_IngredientViewFragment_to_SelectIngredientsFragment);
    }

    private void onSaveIngredientButtonClicked(View view) {
        if (ingredient != null) rvm.ingredients.addItem(ingredient);
        NavController controller = NavHostFragment.findNavController(IngredientViewFragment.this);
        controller.navigate(R.id.action_IngredientViewFragment_to_IngredientsFragment);
    }
}