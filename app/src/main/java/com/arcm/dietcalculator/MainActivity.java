package com.arcm.dietcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.ExerciseEntry;
import com.arcm.dietcalculator.database.FoodDatabaseViewModel;
import com.arcm.dietcalculator.fragments.MainActivity.MacroFragment;
import com.arcm.dietcalculator.fragments.MainActivity.MainFragment;
import com.arcm.dietcalculator.fragments.MainActivity.DiaryFragment;
import com.arcm.dietcalculator.viewmodels.MainViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = "MainActivity";
    private static final int TABS_COUNT = 3;

    private ViewPager2 vPager;
    private MainViewModel mvm;
    private FoodDatabaseViewModel db;
    private LiveData<List<DiaryItem>> foodListLiveData;
    private LiveData<List<ExerciseEntry>> exerciseListLiveData;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        runSetupIfRequired();
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        this.db = new ViewModelProvider(this).get(FoodDatabaseViewModel.class);
        this.mvm = new ViewModelProvider(this).get(MainViewModel.class);
        mvm.loadSettings(this);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MacroFragment());
        fragments.add(new MainFragment());
        fragments.add(new DiaryFragment());

        this.vPager = findViewById(R.id.main_viewPager);
        vPager.setAdapter(new MainPagerAdapter(this, fragments));
        vPager.setCurrentItem(1, false);
        vPager.setOffscreenPageLimit(3);
//        vPager.setUserInputEnabled(false);

        TabLayout tableLayout = findViewById(R.id.bottom_menu_tablayout);
        int[] icons = {R.drawable.ic_menu_macros, R.drawable.ic_menu_home, R.drawable.ic_menu_diary};
        new TabLayoutMediator(tableLayout, vPager, (tab, position) -> tab.setIcon(icons[position])).attach();
        mvm.date.observe(this, this::onDateChanged);
        mvm.setDate(DietDate.today());

        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.i(TAG, "Settings changed: " + s);
        mvm.loadSettings(this);
    }

    @Override
    public void onBackPressed() {
        if (vPager.getCurrentItem() == 1) {
            super.onBackPressed();
        } else vPager.setCurrentItem(1);
    }

    private void onDateChanged(DietDate date) {
        if (foodListLiveData != null && foodListLiveData.hasActiveObservers()) {
            foodListLiveData.removeObservers(this);
        }
        if (exerciseListLiveData != null && exerciseListLiveData.hasActiveObservers()) {
            exerciseListLiveData.removeObservers(this);
        }
        this.foodListLiveData = db.getDiaryItemsAsLiveData(date);
        this.exerciseListLiveData = db.getExercisesByDate(date);
        foodListLiveData.observe(this, this::onDiaryListChanged);
        exerciseListLiveData.observe(this, this::onExerciseListChanged);
    }

    private void onDiaryListChanged(List<DiaryItem> items) {
        for (DiaryItem item : items) Log.d(TAG, item.toString());
        mvm.setFoodItems(items);
    }

    private void onExerciseListChanged(List<ExerciseEntry> items) {
        for (ExerciseEntry item : items) Log.d(TAG, item.toString());
        mvm.setExerciseItems(items);
    }

    private void runSetupIfRequired() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstRun = pref.getBoolean(DietSettings.FIRST_RUN, true);
        if (firstRun) startSetupActivity();
    }

    private void startSetupActivity() {
        startActivity(new Intent(this, SetupActivity.class));
        finish();
    }

    private static class MainPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragments;

        public MainPagerAdapter(FragmentActivity fa, List<Fragment> fragments) {
            super(fa);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return TABS_COUNT;
        }
    }

    public DietDate getDate() {
        return DietDate.today();
    }
}