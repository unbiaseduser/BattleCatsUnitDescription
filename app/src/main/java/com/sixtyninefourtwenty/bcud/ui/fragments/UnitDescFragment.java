package com.sixtyninefourtwenty.bcud.ui.fragments;

import static com.sixtyninefourtwenty.stuff.listeners.ChangeOnlyQueryTextListener.changeOnly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.ui.fragments.unitdesc.UnitDescTabAdventDropsFragment;
import com.sixtyninefourtwenty.bcud.ui.fragments.unitdesc.UnitDescTabCFSpecialsFragment;
import com.sixtyninefourtwenty.bcud.ui.fragments.unitdesc.UnitDescTabLegendRareFragment;
import com.sixtyninefourtwenty.bcud.ui.fragments.unitdesc.UnitDescTabRareFragment;
import com.sixtyninefourtwenty.bcud.ui.fragments.unitdesc.UnitDescTabStoryLegendsFragment;
import com.sixtyninefourtwenty.bcud.ui.fragments.unitdesc.UnitDescTabSuperRareFragment;
import com.sixtyninefourtwenty.bcud.ui.fragments.unitdesc.UnitDescTabUberFragment;
import com.sixtyninefourtwenty.bcud.utils.AppPreferences;
import com.sixtyninefourtwenty.bcud.utils.fragments.TabLayoutViewPagerFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.AppSettingsViewModel;
import com.sixtyninefourtwenty.bcud.viewmodels.SearchViewModel;
import com.sixtyninefourtwenty.common.utils.Menus;
import com.sixtyninefourtwenty.stuff.adapters.ListFragmentStateAdapter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

import lombok.AllArgsConstructor;

public final class UnitDescFragment extends TabLayoutViewPagerFragment {
    private AppSettingsViewModel appSettingsViewModel;

    @Override
    protected RecyclerView.Adapter<?> getPagerAdapter(@NonNull LayoutInflater inflater, ViewGroup container) {
        return new ListFragmentStateAdapter(requireActivity(), List.of(
                UnitDescTabStoryLegendsFragment::new,
                UnitDescTabCFSpecialsFragment::new,
                UnitDescTabAdventDropsFragment::new,
                UnitDescTabRareFragment::new,
                UnitDescTabSuperRareFragment::new,
                UnitDescTabUberFragment::new,
                UnitDescTabLegendRareFragment::new
        ));
    }

    @Override
    protected boolean shouldTabsBeScrollable() {
        return true;
    }

    @Override
    protected void setupTabs(TabLayout.Tab tab, int pos) {
        switch (pos) {
            case 0 -> tab.setText(R.string.story_legends);
            case 1 -> tab.setText(R.string.cf_specials);
            case 2 -> tab.setText(R.string.advent_drops);
            case 3 -> tab.setText(R.string.rares);
            case 4 -> tab.setText(R.string.super_rares);
            case 5 -> tab.setText(R.string.ubers);
            case 6 -> tab.setText(R.string.legend_rares);
        }
    }

    @Override
    protected void setup(@NonNull View view, @Nullable Bundle savedInstanceState) {
        appSettingsViewModel = AppSettingsViewModel.get(requireActivity());
        final var model = SearchViewModel.get(requireActivity());
        requireActivity().addMenuProvider(new UDPFragmentMenu(model, appSettingsViewModel), getViewLifecycleOwner());
    }

    @Override
    public void onPause() {
        super.onPause();
        appSettingsViewModel.persistListViewMode();
    }

    @AllArgsConstructor
    private final class UDPFragmentMenu implements MenuProvider {
        private final SearchViewModel model;
        private final AppSettingsViewModel appSettingsViewModel;
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_udp_hypermax, menu);
            final var search = menu.findItem(R.id.udp_hp_search);
            final var searchView = Menus.requireActionView(search, SearchView.class);
            model.setToSearchViewIfPresent(search);
            searchView.setQueryHint(getString(R.string.search_hint));
            searchView.setSubmitButtonEnabled(false);
            searchView.setOnQueryTextListener(changeOnly(model::setQuery));
            searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    search.collapseActionView();
                }
            });
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.udp_hp_toggle_list_mode) {
                appSettingsViewModel.setListViewMode(appSettingsViewModel.getListViewMode().getValue() == AppPreferences.ListViewMode.LIST ? AppPreferences.ListViewMode.GRID : AppPreferences.ListViewMode.LIST);
                return true;
            } else if (id == R.id.udp_hp_go_to_website) {
                openWebsite("https://thanksfeanor.pythonanywhere.com/UDP");
                return true;
            }
            return false;
        }
    }

}
