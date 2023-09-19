package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.hypermaxpriority;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.sixtyninefourtwenty.bcud.R;
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

public final class HypermaxPriorityFragment extends TabLayoutViewPagerFragment {
    private AppSettingsViewModel appSettingsViewModel;

    @Override
    protected RecyclerView.Adapter<?> getPagerAdapter(@NonNull LayoutInflater inflater, ViewGroup container) {
        return new ListFragmentStateAdapter(requireActivity(),
                List.of(HypermaxPriorityTabTextsFragment::new,
                        HypermaxPriorityTabSpecialFragment::new,
                        HypermaxPriorityTabRareFragment::new,
                        HypermaxPriorityTabSuperRareFragment::new));
    }

    @Override
    protected void setupTabs(TabLayout.Tab tab, int pos) {
        switch (pos) {
            case 0 -> tab.setText(R.string.tab_advent_intro);
            case 1 -> tab.setText(R.string.tab_special);
            case 2 -> tab.setText(R.string.rares);
            case 3 -> tab.setText(R.string.super_rares);
        }
    }

    @Override
    protected void setup(@NonNull View view, @Nullable Bundle savedInstanceState) {
        appSettingsViewModel = new ViewModelProvider(requireActivity()).get(AppSettingsViewModel.class);
        final var model = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        requireActivity().addMenuProvider(new HypermaxPriorityFragmentMenu(model), getViewLifecycleOwner());
    }

    @Override
    public void onPause() {
        super.onPause();
        appSettingsViewModel.persistListViewMode();
    }

    @AllArgsConstructor
    private final class HypermaxPriorityFragmentMenu implements MenuProvider {
        private final SearchViewModel searchViewModel;

        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_udp_hypermax, menu);
            var search = menu.findItem(R.id.udp_hp_search);
            var searchView = Menus.requireActionView(search, SearchView.class);
            searchViewModel.setToSearchViewIfPresent(search);
            searchView.setQueryHint(getString(R.string.search_hint));
            searchView.setSubmitButtonEnabled(false);
            searchView.setOnQueryTextListener(changeOnly(searchViewModel::setQuery));
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
                openWebsite("https://thanksfeanor.pythonanywhere.com/hypermaxpriority");
                return true;
            }
            return false;
        }
    }

}
