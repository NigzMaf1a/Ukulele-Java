package com.example.theukuleleband.modules.supplier;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
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

public class SupplierActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        setupToolbar();
        setupDrawerNavigation();
        super.setupBottomNav(); // Inherit bottom navigation from BaseActivity
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Supplier Module");
    }

    private void setupDrawerNavigation() {
        drawerLayout = findViewById(R.id.supplier_activity);
        NavigationView navigationView = findViewById(R.id.supplier_nav_view);
        navController = Navigation.findNavController(this, R.id.supplier_nav_host_fragment);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.supplier_nav_dashboard,
                R.id.supplier_nav_addsupply,
                R.id.supplier_nav_available,
                R.id.supplier_nav_report)
                .setOpenableLayout(drawerLayout)
                .build();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, findViewById(R.id.toolbar),
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
