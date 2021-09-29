package com.arcm.dietcalculator.fragments.AddNewFoodItemActivity;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.arcm.dietcalculator.adapters.FoodGroupsGridAdapter;
import com.arcm.dietcalculator.adapters.GroupFoodListAdapter;
import com.arcm.dietcalculator.adapters.SearchResultsAdapter;
import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.USDARequests;
import com.arcm.dietcalculator.viewmodels.AddNewFoodItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Food search fragment.
 * Look up foods using online databases (currently only usda database available).
 */
public class SearchFragment extends Fragment implements SearchResultsAdapter.ItemClickListener,
        FoodGroupsGridAdapter.ItemClickListener, SearchView.OnQueryTextListener {
    public static final String TAG = "SearchFragment";

    private SearchResultsAdapter searchResultsAdapter;
    private SearchView sv;
    private RecyclerView rv;
    private RecyclerView gv;

    private AddNewFoodItemViewModel avm;
    private List<FoodItem> items = new ArrayList<>();
    private final List<FoodItem> filtered = new ArrayList<>();

    private GroupFoodListAdapter historyAdapter;
    private boolean searchSubmitted = false;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.rv = view.findViewById(R.id.search_results);
        this.searchResultsAdapter = new SearchResultsAdapter(requireContext());
        this.historyAdapter = new GroupFoodListAdapter(requireContext());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(historyAdapter);
        historyAdapter.setItemClickListener(this::onHistoryItemClick);
        historyAdapter.updateItems(items);
        rv.setVisibility(View.GONE);

        this.gv = view.findViewById(R.id.food_groups_grid);
        gv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        FoodGroupsGridAdapter gridAdapter = new FoodGroupsGridAdapter(getContext());
        gridAdapter.setOnClickListener(this);
        gv.setAdapter(gridAdapter);

        this.sv = view.findViewById(R.id.serach_view);
        sv.setOnQueryTextListener(this);

        ImageView createItemBtn = view.findViewById(R.id.add_new_item);
        createItemBtn.setOnClickListener(this::startCreateNewDiaryFoodItemFragment);
//        createItemBtn.setOnClickListener(this::startCreateNewRecipeItemFragment);

        this.avm = new ViewModelProvider(requireActivity()).get(AddNewFoodItemViewModel.class);
        avm.foodItems.observe(requireActivity(), this::onItemsChanged);
    }

    private void onItemsChanged(List<FoodItem> items) {
        this.items = items;
        if (!searchSubmitted) {
            filterHistory(sv.getQuery().toString());
        }
    }

    private void toggleSearchResults(boolean show) {
        if (show) {
            gv.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        } else {
            gv.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.searchSubmitted = true;
        searchResultsAdapter.setClickListener(this);
        rv.addItemDecoration(new CellMarginDecoration(10));
        rv.setAdapter(searchResultsAdapter);
        String q = requireAllKeys(query);
        USDARequests.SearchRequest request = new USDARequests.SearchRequest(q, 40) {
            @Override
            protected void onResponse(List<FoodItem> items) {
                Log.d(TAG, "onSuccess from main activity");
                requireActivity().runOnUiThread(() -> displaySearchResults(items));
            }
        };
        request.execute();
        sv.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        toggleSearchResults(!newText.isEmpty());
        if (!searchSubmitted) filterHistory(newText);
        return false;
    }

    private void filterHistory(String s) {
        filtered.clear();
        for (FoodItem item : items) {
            if (item.getFoodName().toLowerCase().startsWith(s.toLowerCase())) {
                filtered.add(item);
            }
        }
        Log.d(TAG, "filtered items: " + filtered.size() + "/" + items.size());
        historyAdapter.updateItems(filtered);
    }

    private void displaySearchResults(List<FoodItem> items) {
        Log.d(TAG, "Found " + items.size() + " items");
        searchResultsAdapter.updateItems(items);
        sv.clearFocus();
    }

    private static class CellMarginDecoration extends RecyclerView.ItemDecoration {
        private final int margin;

        public CellMarginDecoration(int margin) {
            this.margin = margin;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = margin;
            outRect.right = margin;
            outRect.bottom = margin;
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = margin;
            } else outRect.top = 0;
        }
    }

    private String requireAllKeys(String query) {
        String[] words = query.split(" ");
        if (words.length == 1) return query;
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append("+").append(word).append(" ");
        }
        builder.setLength(builder.length() - 1);
        Log.d(TAG, "[" + builder.toString() + "]");
        return builder.toString();
    }

    private void startCreateNewDiaryFoodItemFragment(View view) {
        sv.clearFocus();
        CreateNewFoodItemFragment f = new CreateNewFoodItemFragment();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
    }

    private void startCreateNewRecipeItemFragment(View view) {
        sv.clearFocus();
        CreateNewRecipeFragment f = new CreateNewRecipeFragment();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
    }

    private void startGroupListFragment(FoodItem.Group group) {
        GroupListFragment f = GroupListFragment.newInstance(group);
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
    }

    private void startNutrientsFragment(String fdcId) {
        NutrientsFragment f = NutrientsFragment.newInstance(fdcId, avm.date, avm.mealType);
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
    }

    private void startNutrientsFragment(long id) {
        NutrientsFragment f = NutrientsFragment.newInstance(id, avm.date, avm.mealType);
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
    }

    private void onHistoryItemClick(FoodItem item) {
        Log.d(TAG, "history item clicked: " + item.getFoodName());
        startNutrientsFragment(item.getId());
    }

    @Override
    public void onItemClick(FoodItem item) {
        Log.d(TAG, item.toString());
        sv.clearFocus();
        startNutrientsFragment(item.fdcId);
    }

    @Override
    public void onGroupSelected(FoodItem.Group group) {
        Log.d(TAG, "Group selected: " + FoodItem.getGroupName(group));
        sv.clearFocus();
        startGroupListFragment(group);
    }
}