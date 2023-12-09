package com.sixtyninefourtwenty.bcud.utils.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.sixtyninefourtwenty.common.utils.TabLayoutViewPagerScreen;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class TabLayoutViewPagerFragment extends BaseFragment<@NonNull View> {
    protected abstract RecyclerView.Adapter<?> getPagerAdapter(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);
    protected boolean shouldTabsBeScrollable() {
        return false;
    }
    protected abstract void setupTabs(TabLayout.Tab tab, int pos);

    @Override
    protected final @NonNull View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return new TabLayoutViewPagerScreen(requireContext(), shouldTabsBeScrollable(), getPagerAdapter(inflater, container), this::setupTabs).getRoot();
    }

    @Override
    protected void setup(@NonNull View view, @Nullable Bundle savedInstanceState) {}
}
