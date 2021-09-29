package com.arcm.dietcalculator.fragments.AddNewFoodItemActivity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.FoodDatabaseViewModel;
import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.DietDate;
import com.arcm.dietcalculator.PopupGroupsWindow;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.USDARequests;
import com.arcm.scalenumberpicker.ScaleNumberPicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class NutrientsFragment extends Fragment {
    public static final String TAG = "NutrientsFragment";
    public static final String FDCID_PARAM = "fdcId";
    public static final String ID_PARAM = "food-id";
    public static final String DATE_PARAM = "date";
    public static final String MEAL_TYPE_PARAM = "meal-type";
    public static final String FOOD_ID_PARAM = "food-item-id";

    private DietDate date;
    private String fdcid;
    private long id;
    private DiaryItem.MealType mealType;
    private TextView nameTextView;
    private LinearLayout loadingCont;
    private FloatingActionButton saveButton;
    private ScaleNumberPicker weightScale;
    private NutrientsViewModel nvm;
    private FoodDatabaseViewModel db;
    private FoodItem.Group selectedGroup = FoodItem.Group.OTHER;

    private final String[] tabsTitles = {FoodNutrientsFragment.TITLE, NutrientsTargetsFragment.TITLE};

    public NutrientsFragment() {
        // Required empty public constructor
    }

    public static NutrientsFragment newInstance(String id, DietDate date, DiaryItem.MealType type) {
        NutrientsFragment fragment = new NutrientsFragment();
        Bundle args = new Bundle();
        args.putString(FDCID_PARAM, id);
        args.putSerializable(DATE_PARAM, date);
        args.putSerializable(MEAL_TYPE_PARAM, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static NutrientsFragment newInstance(long id, DietDate date, DiaryItem.MealType type) {
        NutrientsFragment fragment = new NutrientsFragment();
        Bundle args = new Bundle();
        args.putLong(ID_PARAM, id);
        args.putSerializable(DATE_PARAM, date);
        args.putSerializable(MEAL_TYPE_PARAM, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle extras = getArguments();
            this.fdcid = extras.getString(FDCID_PARAM);
            this.id = extras.getLong(ID_PARAM);
            this.date = (DietDate) extras.getSerializable(DATE_PARAM);
            this.mealType = (DiaryItem.MealType) extras.getSerializable(MEAL_TYPE_PARAM);
        } else Log.e(TAG, "no arguments were found!");
        this.nvm = new ViewModelProvider(requireActivity()).get(NutrientsViewModel.class);
        nvm.date = date;
        this.db = new ViewModelProvider(requireActivity()).get(FoodDatabaseViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutrients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.nameTextView = view.findViewById(R.id.food_name_editText);
        this.saveButton = view.findViewById(R.id.save_new_floatbutton);
        this.loadingCont = view.findViewById(R.id.loading_cont);
        this.weightScale = view.findViewById(R.id.item_weight_scale);
        loadingCont.setVisibility(View.VISIBLE);

        if (fdcid != null) {
            getDiaryFoodItemFromUSDA(requireActivity(), fdcid);
        } else getDiaryFoodItemFromDatabase(requireActivity(), id);

        ViewPager2 viewPager = view.findViewById(R.id.sections_viewpager);
        TabLayout tabLayout = view.findViewById(R.id.sections_tab_layout);
        viewPager.setAdapter(new SectionsPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabsTitles[position])).attach();

        weightScale.setOnValueChangedListener((n1, n2) -> nvm.setWeight(Math.round(n2)));

        ImageView groupsSelector = view.findViewById(R.id.group_imageview);
        groupsSelector.setOnClickListener((groupItemView -> {
            PopupGroupsWindow window = new PopupGroupsWindow(requireContext(), (ViewGroup) view) {
                @Override
                public void onGroupSelected(FoodItem.Group group) {
                    super.onGroupSelected(group);
                    selectedGroup = group;
                    groupsSelector.setImageResource(DiaryItem.getGroupIconResource(group));
                }
            };
            window.showAsDropDown(groupsSelector, 0, 10);
        }));
    }

    private void getDiaryFoodItemFromUSDA(@NotNull final Activity parent, String fdcId) {
        USDARequests.FoodItemRequest request =  new USDARequests.FoodItemRequest(fdcId) {
            @Override
            protected void onResponse(FoodItem item) {
                parent.runOnUiThread(() -> displayNutrients(item));
            }
        };
        request.execute();
    }

    private void getDiaryFoodItemFromDatabase(@NotNull final Activity parent, long id) {
        db.getFoodItemById(id, result -> parent.runOnUiThread(() -> displayNutrients(result)));
    }
    
    private void displayNutrients(FoodItem item) {
        Log.d(TAG, item.getNutrients().toString());
        nvm.nutrientsPer100 = item.getNutrients();
        nvm.setWeight(100);
        nameTextView.setText(item.getFoodName());
        loadingCont.setVisibility(View.GONE);
        saveButton.setOnClickListener(view -> requireActivity().runOnUiThread(() ->
                saveNewDiaryFoodItemToDatabaseAndExit(item)));
    }

    private void saveNewDiaryFoodItemToDatabaseAndExit(FoodItem item) {
        item.setGroup(selectedGroup);
        DiaryItem diaryItem = new DiaryItem(item, date, mealType);
        diaryItem.setUnitsCount(weightScale.getValue());
        if (fdcid != null) {
            db.insertFoodItem(diaryItem.foodItem, id -> insertDiaryItemToDatabase(id, diaryItem));
        } else db.insertDiaryItem(diaryItem);
        requireActivity().finish();
    }

    private void insertDiaryItemToDatabase(long foodId, DiaryItem diaryItem) {
        diaryItem.diaryEntry.foodId = foodId;
        db.insertDiaryItem(diaryItem);
    }

    /**
     * Adapter for the viewPager2.
     * Allows swiping between the two sections fragments.
     */
    private static class SectionsPagerAdapter extends FragmentStateAdapter {
        public static final int FRAGMENTS_COUNT = 2;

        private final Fragment[] fragments;

        public SectionsPagerAdapter(@NonNull @NotNull Fragment fragment) {
            super(fragment);
            this.fragments = new Fragment[FRAGMENTS_COUNT];
            fragments[0] = new FoodNutrientsFragment();
            fragments[1] = new NutrientsTargetsFragment();
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return fragments[position];
        }

        @Override
        public int getItemCount() {
            return FRAGMENTS_COUNT;
        }
    }

    /**
     * ViewModel to synchronize the foodItem's nutrients changes occurring from
     * changing the foodItem's weight.
     * Changes are synchronized with sections fragments.
     */
    public static class NutrientsViewModel extends ViewModel {
        private final Nutrients nutrients = new Nutrients();
        private final MutableLiveData<Integer> weight = new MutableLiveData<>();

        public DietDate date;
        public Nutrients nutrientsPer100;
        
        public int getWeight() {
            if (weight.getValue() == null) return 0;
            return weight.getValue();
        }

        public MutableLiveData<Integer> getWeightMutableLiveData() {
            return weight;
        }

        public void setWeight(int w) {
            if (nutrientsPer100 != null) updateNutrients(w);
            weight.setValue(w);
        }
        
        private void updateNutrients(int w) {
			nutrients.cals = (nutrientsPer100.cals / 100.0f) * w;
			nutrients.carbs = (nutrientsPer100.carbs / 100.0f) * w;
			nutrients.protein = (nutrientsPer100.protein / 100.0f) * w;
			nutrients.fat = (nutrientsPer100.fat / 100.0f) * w;
			nutrients.saturatedFat = (nutrientsPer100.saturatedFat / 100.0f) * w;
			nutrients.sugar = (nutrientsPer100.sugar / 100.0f) * w;
			nutrients.fibre = (nutrientsPer100.fibre / 100.0f) * w;
		}
		
		public Nutrients getNutrients() {
            return nutrients;
        }
    }
}