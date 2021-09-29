package com.arcm.dietcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.arcm.dietcalculator.fragments.SettingsActivity.MainSettingsFragment;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SetupViewModel svm = new ViewModelProvider(this).get(SetupViewModel.class);
        svm.loadSettings(this);
        
        MainSettingsFragment f = new MainSettingsFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, f).commit();
    }
}