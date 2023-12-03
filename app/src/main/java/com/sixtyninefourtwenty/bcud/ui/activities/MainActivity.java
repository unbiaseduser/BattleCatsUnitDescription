package com.sixtyninefourtwenty.bcud.ui.activities;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.ActivityMainBinding;
import com.sixtyninefourtwenty.bcud.utils.AppPreferences;
import com.sixtyninefourtwenty.bcud.utils.Constants;
import com.sixtyninefourtwenty.customactionmode.AbstractActionMode;
import com.sixtyninefourtwenty.customactionmode.FadingActionMode;
import com.sixtyninefourtwenty.theming.preferences.ActivityThemingWithPreferences;

import org.checkerframework.checker.nullness.qual.NonNull;

import lombok.Getter;

public final class MainActivity extends AppCompatActivity {

    private AppPreferences prefs;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    @Getter
    private AbstractActionMode actionMode;

    @NonNull
    public CoordinatorLayout getRootView() {
        return binding.getRoot();
    }

    @NonNull
    public Toolbar getToolbar() {
        return binding.appBarMain.toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createNotificationChannels();
        prefs = AppPreferences.get(this);
        setFirstInstallSettings();
        ActivityThemingWithPreferences.applyThemingWithPreferences(this,
                R.style.Theme_BattleCatsUnitDescription,
                R.style.Theme_BattleCatsUnitDescription_Material3_Android11,
                R.style.Theme_BattleCatsUnitDescription_Material3);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_udp, R.id.nav_help_pins, R.id.nav_guides, R.id.nav_settings, R.id.nav_about, R.id.nav_misc)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        actionMode = new FadingActionMode(binding.appBarMain.actionModeToolbar, getOnBackPressedDispatcher());
        final var navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        loadData();
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isOpen()) {
                    binding.drawerLayout.close();
                } else {
                    remove();
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        final var navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void createNotificationChannels() {
        NotificationManagerCompat.from(this)
                .createNotificationChannel(new NotificationChannelCompat.Builder(Constants.FAVORITES_BACKUP_NOTIFICATION_CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
                        .setName(getString(R.string.favorites_backup))
                        .setDescription(getString(R.string.favorites_notification_channel_desc))
                        .build());
    }

    private void loadData() {
        MyApplication.get(this).eagerInit();
    }

    private void setFirstInstallSettings() {
        AppPreferences.initializePreferences(this);
        if (prefs.getFirstLaunch()) {
            prefs.setFirstLaunch(false);
        }
    }

}
