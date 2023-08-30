package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.elderepictfpriority;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.utils.fragments.TabLayoutViewPagerFragment;
import com.sixtyninefourtwenty.stuff.adapters.ListFragmentStateAdapter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public final class ElderEpicFragment extends TabLayoutViewPagerFragment {

    private final MenuProvider menu = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_guide_other, menu);
            menu.findItem(R.id.go_to_combos).setEnabled(false).setVisible(false);
            menu.findItem(R.id.text_search).setVisible(false).setEnabled(false);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.guide_other_go_to_website) {
                openWebsite("https://thanksfeanor.pythonanywhere.com/elderepicfruitpriority");
                return true;
            }
            return false;
        }
    };

    @Override
    protected RecyclerView.Adapter<?> getPagerAdapter(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container) {
        return new ListFragmentStateAdapter(requireActivity(),
                List.of(ElderEpicTabTextsFragment::new,
                        ElderEpicTabElderFragment::new,
                        ElderEpicTabEpicFragment::new));
    }

    @Override
    protected void setupTabs(TabLayout.Tab tab, int pos) {
        switch (pos) {
            case 0 -> tab.setText(R.string.tab_advent_intro);
            case 1 -> tab.setText(R.string.tab_elder);
            case 2 -> tab.setText(R.string.tab_epic);
        }
    }

    @Override
    protected void setup(@NonNull View view, @Nullable Bundle savedInstanceState) {
        requireActivity().addMenuProvider(menu, getViewLifecycleOwner());
    }

}
