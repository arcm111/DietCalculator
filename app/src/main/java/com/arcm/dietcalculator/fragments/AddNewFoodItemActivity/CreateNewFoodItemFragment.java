package com.arcm.dietcalculator.fragments.AddNewFoodItemActivity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.FoodDatabaseViewModel;
import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.database.UnitEntry;
import com.arcm.dietcalculator.DietDate;
import com.arcm.dietcalculator.PopupGroupsWindow;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.PopupUnitsWindow;
import com.arcm.dietcalculator.Utilities;
import com.arcm.dietcalculator.viewmodels.AddNewFoodItemViewModel;
import com.arcm.scalenumberpicker.ScaleNumberPicker;

import java.util.ArrayList;
import java.util.List;

public class CreateNewFoodItemFragment extends Fragment {
    private static final String TAG = "CreateNewFoodItemFrag";

    private FoodDatabaseViewModel db;
    private DietDate date;
    private DiaryItem.MealType mealType;
    private EditText nameEdittext;
    private EditText brandEdittext;
    private FoodItem.Group selectedGroup = FoodItem.Group.OTHER;

    private ScaleNumberPicker selectedPicker;
    private final Nutrients nutrients = new Nutrients();
    private int screenWidth;

    private TextView unitsTextview;
    private List<UnitEntry> units;

    public CreateNewFoodItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddNewFoodItemViewModel avm = new ViewModelProvider(requireActivity()).get(AddNewFoodItemViewModel.class);
        this.date = avm.date;
        this.mealType = avm.mealType;
        this.db = new ViewModelProvider(requireActivity()).get(FoodDatabaseViewModel.class);
        DisplayMetrics metrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.screenWidth = metrics.widthPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_food_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.nameEdittext = view.findViewById(R.id.food_name_editText);
        this.brandEdittext = view.findViewById(R.id.brand_edittext);

        initItem(view, R.id.calories_value, R.id.calories_scalepicker, R.id.calories_cont, Nutrient.CALS);
        initItem(view, R.id.carbs_value, R.id.carbs_scalepicker, R.id.carbs_cont, Nutrient.CARBS);
        initItem(view, R.id.sugar_value, R.id.sugar_scalepicker, R.id.sugar_cont, Nutrient.SUGAR);
        initItem(view, R.id.fibre_value, R.id.fibre_scalepicker, R.id.fibre_cont, Nutrient.FIBRE);
        initItem(view, R.id.fat_value, R.id.fat_scalepicker, R.id.fat_cont, Nutrient.FAT);
        initItem(view, R.id.sfat_value, R.id.sfat_scalepicker, R.id.sfat_cont, Nutrient.SFAT);
        initItem(view, R.id.protein_value, R.id.protein_scalepicker, R.id.protein_cont, Nutrient.PROTEIN);

        Button btn = view.findViewById(R.id.save_button);
        btn.setOnClickListener(this::createNewFoodItem);

        ImageView thumb = view.findViewById(R.id.group_imageview);
        LinearLayout cont = view.findViewById(R.id.group_selector);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupGroupsWindow window = new PopupGroupsWindow(requireContext(), (ViewGroup) view) {
                    @Override
                    public void onGroupSelected(FoodItem.Group group) {
                        super.onGroupSelected(group);
                        selectedGroup = group;
                        thumb.setImageResource(DiaryItem.getGroupIconResource(group));
                    }
                };
                int[] pos = new int[2];
                cont.getLocationOnScreen(pos);
                pos[0] = screenWidth - cont.getWidth() - pos[0];
                pos[1] += cont.getHeight() + 10;
                Log.i(TAG, pos[0] + ":" + pos[1] + " " + window.getWidth());
                window.showAtLocation(cont, Gravity.TOP | Gravity.END, pos[0], pos[1]);
            }
        });
        ImageView editUnitsBtn = view.findViewById(R.id.edit_units_imageview);
        editUnitsBtn.setOnClickListener(view1 -> showUnitsPopupWindow(view));

        this.unitsTextview = view.findViewById(R.id.units_list_textview);
        this.units = new ArrayList<>();
        units.add(new UnitEntry(-1, "g", 1));
        updateUnits();
    }

    private void updateUnits() {
        StringBuilder builder = new StringBuilder();
        for (UnitEntry u : units) builder.append(u.unitTitle).append(", ");
        builder.setLength(builder.length() - 2);
        unitsTextview.setText(builder.toString());
    }

    private void onUnitsChanged(List<UnitEntry> units) {
        this.units = units;
        updateUnits();
    }

    private void showUnitsPopupWindow(View view) {
        PopupUnitsWindow window = new PopupUnitsWindow(requireContext(), (ViewGroup) view);
        window.setOnUnitsSaveListener(this::onUnitsChanged);
        window.updateUnits(units);
        window.showAtLocation(view, Gravity.TOP | Gravity.START, 0, 0);
    }

    private void initItem(View parent, @IdRes int vId, @IdRes int pId, @IdRes int cId, Nutrient type) {
        TextView valueTextview = parent.findViewById(vId);
        ScaleNumberPicker picker = parent.findViewById(pId);
        LinearLayout cont = parent.findViewById(cId);
        cont.setOnClickListener(view1 -> onNutrientItemSelected(picker));
        picker.setOnValueChangedListener((v1, v2) -> onNutrientValueChanged(v2, valueTextview, type));
    }
    
    private void onNutrientItemSelected(ScaleNumberPicker picker) {
        if (selectedPicker != null) {
            selectedPicker.setVisibility(View.GONE);
        }
        picker.setVisibility(View.VISIBLE);
        this.selectedPicker = picker;
    }
    
    private void onNutrientValueChanged(float v, TextView tv, Nutrient type) {
        switch (type) {
            case CALS:
                nutrients.cals = v;
                break;
            case CARBS:
                nutrients.carbs = v;
                break;
            case SUGAR:
				nutrients.sugar = v;
                break;
            case FIBRE:
				nutrients.fibre = v;
                break;
            case FAT:
				nutrients.fat = v;
                break;
            case SFAT:
				nutrients.saturatedFat = v;
                break;
            case PROTEIN:
				nutrients.protein = v;
                break;
        }

        if (type == Nutrient.CALS) {
            tv.setText(Utilities.formatCalories(v));
        } else tv.setText(Utilities.formatGrams(v));
    }

    private void createNewFoodItem(View view) {
        if (nameEdittext.getText().toString().isEmpty()) {
            showErrorDialog("Please type a name for your food item.");
        } else if (brandEdittext.getText().toString().isEmpty()) {
            showErrorDialog("Please type a brand for your food item.");
        } else if (!nutrients.isValid()) {
            showErrorDialog("Nutrients values error! Please make sure you entered correct values.");
        } else insertToDbAndStartNutrientsFragment(createItemFromInput());
    }

    private FoodItem createItemFromInput() {
        FoodItem item = new FoodItem();
        String name = nameEdittext.getText().toString();
        String brand = brandEdittext.getText().toString();
        item.setFoodName(name.trim());
        item.setBrand(brand.trim());
        item.setGroup(selectedGroup);
        item.setNutrients(nutrients);
        return item;
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void insertToDbAndStartNutrientsFragment(FoodItem item) {
        if (item != null) {
            db.insertFoodItem(item, this::onInsertComplete);
        }
    }

    private void onInsertComplete(long id) {
        NutrientsFragment f = NutrientsFragment.newInstance(id, date, mealType);
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
    }

    private enum Nutrient {
        CALS,
        CARBS,
        SUGAR,
        FIBRE,
        FAT,
        SFAT,
        PROTEIN
    }
}