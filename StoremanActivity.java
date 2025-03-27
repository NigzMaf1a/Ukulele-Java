package com.example.theukuleleband.modules.storeman;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.example.theukuleleband.R;
import com.example.theukuleleband.modules.shared.BaseActivity;

import java.util.Objects;

public class StoremanActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeman);

        // Call BaseActivity's method to set up bottom navigation
        super.setupBottomNav();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false); // Hide default title

        // Set custom title for the toolbar


        // Initialize navigation components
        setupNavigation(toolbar);
    }

    private void setupNavigation(Toolbar toolbar) {
        drawerLayout = findViewById(R.id.storeman_activity);
        navigationView = findViewById(R.id.storeman_nav_view);
        navController = Navigation.findNavController(this, R.id.storeman_nav_host_fragment);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.storeman_nav_dashboard,
                R.id.storeman_nav_addequip,
                R.id.storeman_nav_inventory,
                R.id.storeman_nav_orderequip,
                R.id.storeman_nav_approvedelivery,
                R.id.storeman_nav_receipt,
                R.id.storeman_nav_report
        ).setOpenableLayout(drawerLayout).build();

        // Set up ActionBar toggle for the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Connect NavController with NavigationView and ActionBar
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
