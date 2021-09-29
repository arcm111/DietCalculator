package com.arcm.dietcalculator;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.arcm.dietcalculator.databinding.ActivityCreateRecipeBinding;

public class CreateRecipeActivity extends AppCompatActivity {
    private final @IdRes int CONTENT_ID = R.id.nav_host_fragment_content_create_recipe;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCreateRecipeBinding binding = ActivityCreateRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundResource(R.color.colour_purple_toolbar);

        NavController navController = Navigation.findNavController(this, CONTENT_ID);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, CONTENT_ID);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}