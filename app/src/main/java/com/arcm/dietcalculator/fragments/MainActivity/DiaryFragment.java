package com.arcm.dietcalculator.fragments.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.ExerciseEntry;
import com.arcm.dietcalculator.database.FoodRepository;
import com.arcm.dietcalculator.Exercise;
import com.arcm.dietcalculator.Meal;
import com.arcm.dietcalculator.viewmodels.MainViewModel;
import com.arcm.dietcalculator.R;

import java.util.List;

public class DiaryFragment extends Fragment {
    public static final String TAG = "ProgressFragment";

    private Meal breakfast, lunch, dinner;
    private Exercise exercise;
    private FoodRepository db;


    public DiaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.db = new FoodRepository(requireActivity().getApplication());
        MainViewModel mvm = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        RecyclerView breakfastRv = view.findViewById(R.id.breakfast_recyclerview);
		ImageView breakfastAddBtn = view.findViewById(R.id.add_breakfast_imageview);
		this.breakfast = new Meal(requireContext(), mvm.getDate(), breakfastRv, breakfastAddBtn, DiaryItem.MealType.BREAKFAST);

        RecyclerView lunchRv = view.findViewById(R.id.lunch_recyclerview);
		ImageView lunchAddBtn = view.findViewById(R.id.add_lunch_imageview);
		this.lunch = new Meal(requireContext(), mvm.getDate(), lunchRv, lunchAddBtn, DiaryItem.MealType.LUNCH);

        RecyclerView dinnerRv = view.findViewById(R.id.dinner_recyclerview);
		ImageView dinnerAddBtn = view.findViewById(R.id.add_dinner_imageview);
		this.dinner = new Meal(requireContext(), mvm.getDate(), dinnerRv, dinnerAddBtn, DiaryItem.MealType.DINNER);

        RecyclerView exerciseRv = view.findViewById(R.id.exercise_recyclerview);
        ImageView exerciseBtn = view.findViewById(R.id.add_exercise_imageview);
        this.exercise = new Exercise(requireContext(), mvm.getDate(), exerciseRv, exerciseBtn);
        exercise.setSaveListener(this::saveExercise);
        exercise.setUpdateListener(this::updateExercise);

        mvm.diaryItems.observe(getViewLifecycleOwner(), this::displayFoodItems);
        mvm.exerciseItems.observe(getViewLifecycleOwner(), this::displayExerciseItems);
    }

    private void saveExercise(ExerciseEntry entry) {
        db.insertExerciseEntry(entry);
    }

    private void updateExercise(ExerciseEntry entry) {
        db.updateExerciseEntry(entry);
    }

    private void displayFoodItems(List<DiaryItem> items) {
        final Meal[] meals = new Meal[] {breakfast, lunch, dinner};
        for (Meal m : meals) m.clearItems();
        for (DiaryItem item : items) {
            if (item.getMealType() == DiaryItem.MealType.BREAKFAST) {
                breakfast.addItem(item);
            } else if (item.getMealType() == DiaryItem.MealType.LUNCH) {
                lunch.addItem(item);
            } else if (item.getMealType() == DiaryItem.MealType.DINNER) {
                dinner.addItem(item);
            }
        }
        for (Meal m : meals) m.notifyItemsChanged();
    }

    private void displayExerciseItems(List<ExerciseEntry> items) {
        exercise.addItems(items);
        exercise.notifyItemsChanged();
    }
}