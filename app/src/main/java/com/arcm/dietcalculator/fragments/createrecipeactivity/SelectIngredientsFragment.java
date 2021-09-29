package com.arcm.dietcalculator.fragments.createrecipeactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.adapters.BlankFoodListAdapter;
import com.arcm.dietcalculator.adapters.SelectIngredientsAdapter;
import com.arcm.dietcalculator.database.FoodDatabaseViewModel;
import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.databinding.FragmentSelectIngredientsBinding;

import java.util.List;

public class SelectIngredientsFragment extends Fragment
        implements SelectIngredientsAdapter.OnIngredientSelectedListener {
    public final static String TAG = "SelectIngredientsFrag";
    private FragmentSelectIngredientsBinding binding;
    private BlankFoodListAdapter blankAdapter;
    private SelectIngredientsAdapter ingredientsAdapter;

    public SelectIngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        this.binding = FragmentSelectIngredientsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FoodDatabaseViewModel db = new ViewModelProvider(requireActivity()).get(FoodDatabaseViewModel.class);
        this.blankAdapter = new BlankFoodListAdapter(requireContext());
        this.ingredientsAdapter = new SelectIngredientsAdapter(requireContext());
        binding.selectIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.selectIngredientsRecyclerView.setAdapter(blankAdapter);
        db.getAllFoodItems().observe(requireActivity(), this::onFoodItemsChanged);
    }

    private void onFoodItemsChanged(List<FoodItem> items) {
        Log.i(TAG, "Items changed: " + items.size());
        if (items.size() > 0) {
            binding.selectIngredientsRecyclerView.setAdapter(ingredientsAdapter);
            ingredientsAdapter.setOnIngredientSelectedListener(this);
            ingredientsAdapter.updateItems(items);
        } else binding.selectIngredientsRecyclerView.setAdapter(blankAdapter);
    }

    @Override
    public void onIngredientSelected(FoodItem item) {
        Log.i(TAG, "Selected Ingredient: " + item.toString());
        Bundle bundle = new Bundle();
        bundle.putLong(IngredientViewFragment.ARG_FOOD_ID, item.getId());
        NavController controller = NavHostFragment.findNavController(SelectIngredientsFragment.this);
        controller.navigate(R.id.action_SelectIngredientsFragment_to_ingredientViewFragment, bundle);
    }
}