package com.arcm.dietcalculator.fragments.AddNewFoodItemActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.adapters.BlankFoodListAdapter;
import com.arcm.dietcalculator.adapters.GroupFoodListAdapter;
import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.viewmodels.AddNewFoodItemViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment implements GroupFoodListAdapter.ItemClickListener {
    public static final String TAG = "GroupListFragment";
    private static final String ARG_GROUP = "food-group";

    private FoodItem.Group group = FoodItem.Group.OTHER;
    private RecyclerView rv;
    private SearchView sv;
    private GroupFoodListAdapter adapter;
    private AddNewFoodItemViewModel avm;
    private BlankFoodListAdapter blankAdapter;

    public static GroupListFragment newInstance(FoodItem.Group group) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }

    public GroupListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.group = (FoodItem.Group) getArguments().getSerializable(ARG_GROUP);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        toolbar.setTitle(FoodItem.getGroupName(group));
        MenuItem searchViewItem = toolbar.getMenu().findItem(R.id.search_view);
        this.sv = (SearchView) searchViewItem.getActionView();
        setListener(sv);

        this.blankAdapter = new BlankFoodListAdapter(requireActivity());
        this.adapter = new GroupFoodListAdapter(requireActivity());

        this.rv = view.findViewById(R.id.items_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rv.setAdapter(blankAdapter);

        this.avm = new ViewModelProvider(requireActivity()).get(AddNewFoodItemViewModel.class);
        avm.foodItems.observe(requireActivity(), this::onItemsChanged);
    }

    private void onItemsChanged(List<FoodItem> items) {
        List<FoodItem> filtered = new ArrayList<>();
        for (FoodItem item : items) {
            if (item.getGroup() == group) filtered.add(item);
        }

        if (filtered.size() > 0) {
            adapter.setItemClickListener(this);
            rv.setAdapter(adapter);
            adapter.updateItems(filtered);
        } else rv.setAdapter(blankAdapter);
    }

    public void setListener(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "query: " + newText);
                return false;
            }
        });
    }

    private void startNutrientsFragment(FoodItem item) {
        NutrientsFragment f = NutrientsFragment.newInstance(item.getId(), avm.date, avm.mealType);
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
    }

    @Override
    public void onItemClicked(FoodItem item) {
        Log.d(TAG, "item selected: \n" + item.toString());
        sv.clearFocus();
        startNutrientsFragment(item);
    }
}