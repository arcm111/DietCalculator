package com.arcm.dietcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.arcm.dietcalculator.fragments.SetupActivity.ActivitiesLevelFragment;
import com.arcm.dietcalculator.fragments.SetupActivity.CaloriesFragment;
import com.arcm.dietcalculator.fragments.SetupActivity.MacrosFragment;
import com.arcm.dietcalculator.fragments.SetupActivity.MetricsFragment;
import com.arcm.dietcalculator.fragments.SetupActivity.SetupSummaryFragment;
import com.arcm.dietcalculator.fragments.SetupActivity.WelcomeFragment;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends AppCompatActivity {
    public final static String TAG = "SetupActivity";
    private ImageView nextBtn;
    private ImageView prevBtn;
    private SetupViewModel svm;
    private ProgressBar pbar;

    private static final int NUM_PAGES = 6;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        this.svm = new ViewModelProvider(this).get(SetupViewModel.class);
        svm.loadSettings(this);

        // Instantiate a ViewPager2 and a PagerAdapter.
        this.viewPager = findViewById(R.id.pager);
        viewPager.setUserInputEnabled(false);
        this.pagerAdapter = new ScreenSlidePagerAdapter(this, getFragments());
        setupViewPager();

        this.nextBtn = findViewById(R.id.save_button);
        this.prevBtn = findViewById(R.id.prev_button);
        this.pbar = findViewById(R.id.progressBar);
        pbar.setMax(NUM_PAGES - 1);

        setupUi();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull @NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setupUi() {
        prevBtn.setOnClickListener(view -> {
            int position = viewPager.getCurrentItem() - 1;
            if (position >= 0) setCurrentFragment(position);
        });
        nextBtn.setOnClickListener(view -> {
            int position = viewPager.getCurrentItem() + 1;
            if (position == NUM_PAGES) {
                finishSetup();
            } else if (position < NUM_PAGES) setCurrentFragment(position);
        });
    }

    private void setupViewPager() {
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateNavigationButtons(position);
                pbar.setProgress(position);
            }
        });
        viewPager.setOffscreenPageLimit(NUM_PAGES - 1);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private void setCurrentFragment(int position) {
        viewPager.setCurrentItem(position);
        updateNavigationButtons(position);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new WelcomeFragment());
        fragments.add(new MetricsFragment());
        fragments.add(new ActivitiesLevelFragment());
        fragments.add(new CaloriesFragment());
        fragments.add(new MacrosFragment());
        fragments.add(new SetupSummaryFragment());
        return fragments;
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragments;

        public ScreenSlidePagerAdapter(FragmentActivity fa, List<Fragment> fragments) {
            super(fa);
            this.fragments = fragments;
        }

        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    private void updateNavigationButtons(int position) {
        if (position == 0) {
            prevBtn.setVisibility(View.GONE);
        } else if (position == NUM_PAGES - 1) {
            nextBtn.setImageResource(R.drawable.ic_finish);
        } else {
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setImageResource(R.drawable.ic_next);
        }
    }

    private void finishSetup() {
        savePreferences();
        startMainActivity();
    }

    private void savePreferences() {
        svm.saveSettings(this);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent, null);
    }
}
