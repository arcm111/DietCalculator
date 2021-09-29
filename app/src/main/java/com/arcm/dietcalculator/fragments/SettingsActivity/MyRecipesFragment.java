package com.arcm.dietcalculator.fragments.SettingsActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcm.dietcalculator.adapters.BlankFoodListAdapter;
import com.arcm.dietcalculator.adapters.RecipesAdapter;
import com.arcm.dietcalculator.database.FoodDatabaseViewModel;
import com.arcm.dietcalculator.database.RecipeItem;
import com.arcm.dietcalculator.databinding.FragmentMyRecipesBinding;

import java.util.List;

public class MyRecipesFragment extends Fragment implements RecipesAdapter.ItemClickListener {
    public static final String TAG = "MyRecipesFragment";

    private FragmentMyRecipesBinding binding;
    private RecyclerView rv;
    private BlankFoodListAdapter blankAdapter;
    private RecipesAdapter recipesAdapter;
    private FoodDatabaseViewModel db;

    public MyRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        this.binding = FragmentMyRecipesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.db = new ViewModelProvider(requireActivity()).get(FoodDatabaseViewModel.class);
        this.rv = binding.myRecipesRecyclerview;
        this.blankAdapter = new BlankFoodListAdapter(requireContext());
        this.recipesAdapter = new RecipesAdapter(requireContext());
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(blankAdapter);
        db.getRecipesAdLiveDate().observe(requireActivity(), this::onRecipesListChanged);
    }

    private void onRecipesListChanged(List<RecipeItem> recipes) {
        if (recipes.size() > 0) {
            rv.setAdapter(recipesAdapter);
            recipesAdapter.setClickListener(this);
            recipesAdapter.updateItems(recipes);
        } else rv.setAdapter(blankAdapter);
    }

    @Override
    public void onItemClicked(View view, int position) {
        RecipeItem recipeItem = recipesAdapter.getItem(position);
        Log.i(TAG, recipeItem.toString());
    }
}