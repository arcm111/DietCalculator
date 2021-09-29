package com.arcm.dietcalculator.fragments.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcm.dietcalculator.adapters.MacroElementsAdapter;
import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.viewmodels.MainViewModel;
import com.arcm.dietcalculator.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MacroElementsFragment extends Fragment {
    private static final String TAG = "MacroElementsFragment";
    private static final String ARG_MACRO_TYPE = "macro-type";

    private Nutrients.Type macroType = Nutrients.Type.CARBOHYDRATES;
    private MacroElementsAdapter adapter;

    public MacroElementsFragment() {
        // Required empty public constructor
    }

    public static MacroElementsFragment newInstance(Nutrients.Type type) {
        MacroElementsFragment fragment = new MacroElementsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MACRO_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.macroType = (Nutrients.Type) getArguments().getSerializable(ARG_MACRO_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_macro_elments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.adapter = new MacroElementsAdapter(requireContext(), new ArrayList<>(), macroType);
        RecyclerView rv = view.findViewById(R.id.elements_recyclerview);
        rv.setAdapter(adapter);

        MainViewModel mvm = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mvm.diaryItems.observe(requireActivity(), this::updateElements);
    }

    private void updateElements(List<DiaryItem> items) {
        SortedMacros.sort(items, macroType);
        Log.d(TAG, "sorted items => " + items.toString());
        adapter.updateValues(items);
        adapter.notifyDataSetChanged();
    }

    private static class SortedMacros {
        public static void sort(List<DiaryItem> items, Nutrients.Type type) {
            switch (type) {
                case CARBOHYDRATES:
                    Collections.sort(items, carbsComparator());
                    break;
                case PROTEIN:
                    Collections.sort(items, proteinComparator());
                    break;
                case FAT:
                    Collections.sort(items, fatComparator());
                    break;
                case SATURATED_FAT:
                    Collections.sort(items, saturatedFatComparator());
                    break;
                case SUGAR:
                    Collections.sort(items, sugarComparator());
                    break;
                case FIBRE:
                    Collections.sort(items, fibreComparator());
                    break;
            }
        }
        
        private static Comparator<DiaryItem> carbsComparator() {
            return (t1, t2) -> toDouble(t2.getCarbs()).compareTo(toDouble(t1.getCarbs()));
        }
        
        private static Comparator<DiaryItem> proteinComparator() {
            return (t1, t2) -> toDouble(t2.getProtein()).compareTo(toDouble(t1.getProtein()));
        }
        
        private static Comparator<DiaryItem> fatComparator() {
            return (t1, t2) -> toDouble(t2.getFat()).compareTo(toDouble(t1.getFat()));
        }
        
        private static Comparator<DiaryItem> saturatedFatComparator() {
            return (t1, t2) -> toDouble(t2.getSaturatedFat()).compareTo(toDouble(t1.getSaturatedFat()));
        }
        
        private static Comparator<DiaryItem> sugarComparator() {
            return (t1, t2) -> toDouble(t2.getSugar()).compareTo(toDouble(t1.getSugar()));
        }
        
        private static Comparator<DiaryItem> fibreComparator() {
            return (t1, t2) -> toDouble(t2.getFibre()).compareTo(toDouble(t1.getFibre()));
        }
        
        private static Double toDouble(double d) {
            return d;
        }
    }
}