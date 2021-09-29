package com.arcm.dietcalculator.fragments.createrecipeactivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.database.IngredientItem;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.Utilities;
import com.arcm.dietcalculator.adapters.BlankFoodListAdapter;
import com.arcm.dietcalculator.adapters.IngredientsListAdapter;
import com.arcm.dietcalculator.databinding.FragmentIngredientsBinding;
import com.arcm.dietcalculator.viewmodels.RecipeViewModel;

import java.util.List;

public class IngredientsFragment extends Fragment {
    public final static String TAG = "IngredientsFragment";

    private FragmentIngredientsBinding binding;
    private BlankFoodListAdapter blankAdapter;
    private IngredientsListAdapter ingredientsAdapter;
    private RecyclerView rv;
    private RecipeViewModel rvm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.blankAdapter = new BlankFoodListAdapter(requireContext());
        this.ingredientsAdapter = new IngredientsListAdapter(requireContext());
        binding.ingredientsRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.ingredientsRecyclerview.setAdapter(blankAdapter);
        binding.ingredientsAddButton.setOnClickListener(this::onAddIngredientButtonClicked);
        binding.nextButton.setOnClickListener(this::onNextButtonClicked);

        this.rv = binding.ingredientsRecyclerview;
        this.rvm = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        rvm.ingredients.observe(requireActivity(), this::onIngredientsListChanged);
    }

    private void onIngredientsListChanged(List<IngredientItem> ingredients, int size) {
        if (size > 0) {
            if (rv == null) Log.e(TAG, "Can't find recyclerview!");
            rv.setAdapter(ingredientsAdapter);
            ingredientsAdapter.setOnIngredientClickedListener(this::onIngredientItemClicked);
            ingredientsAdapter.updateItems(ingredients);
        } else rv.setAdapter(blankAdapter);
    }

    private void onIngredientItemClicked(IngredientItem ingredient) {
        Log.i(TAG, "Selected ingredient: " + ingredient.toString());
    }

    private void onNextButtonClicked(View view) {
        if (rvm.getIngredients().size() >= 2) {
            NavController controller = NavHostFragment.findNavController(IngredientsFragment.this);
            controller.navigate(R.id.action_IngredientsFragment_to_SummaryFragment);
        } else Utilities.showErrorDialog(requireContext(), getString(R.string.ingredients_too_few_error));
    }

    private void onAddIngredientButtonClicked(View view) {
        NavController controller = NavHostFragment.findNavController(IngredientsFragment.this);
        controller.navigate(R.id.action_IngredientsFragment_to_SelectIngredientsFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}