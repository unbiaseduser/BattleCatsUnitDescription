package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.talentpriority;

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
import com.sixtyninefourtwenty.bcud.utils.fragments.TabLayoutViewPagerFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.SearchViewModel;
import com.sixtyninefourtwenty.common.utils.Menus;
import com.sixtyninefourtwenty.stuff.adapters.ListFragmentStateAdapter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public final class TalentPriorityFragment extends TabLayoutViewPagerFragment {
    private SearchViewModel model;
    private final MenuProvider provider = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_talent_priority, menu);
            final var search = menu.findItem(R.id.talent_priority_search);
            final var searchView = Menus.requireActionView(search, SearchView.class);
            model.setToSearchViewIfPresent(search);
            searchView.setQueryHint(requireContext().getString(R.string.search_hint));
            searchView.setSubmitButtonEnabled(false);
            searchView.setOnQueryTextListener(changeOnly(model::setQuery));
            searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) search.collapseActionView();
            });
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.talent_priority_go_to_website) {
                openWebsite("https://thanksfeanor.pythonanywhere.com/talentpriority");
                return true;
            }
            return false;
        }
    };

    @Override
    protected RecyclerView.Adapter<?> getPagerAdapter(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container) {
        return new ListFragmentStateAdapter(requireActivity(),
                List.of(TalentPriorityTabTextsFragment::new,
                        TalentPriorityTabNonUberFragment::new,
                        TalentPriorityTabUberFragment::new));
    }

    @Override
    protected void setupTabs(TabLayout.Tab tab, int pos) {
        switch (pos) {
            case 0 -> tab.setText(R.string.tab_advent_intro);
            case 1 -> tab.setText(R.string.tab_non_uber);
            case 2 -> tab.setText(R.string.ubers);
        }
    }

    @Override
    protected void setup(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
    }

}
