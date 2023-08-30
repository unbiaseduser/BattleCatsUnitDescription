package com.sixtyninefourtwenty.common.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import java.util.function.ObjIntConsumer;

@NonNullTypesByDefault
public final class TabLayoutViewPagerScreen {

    private final LinearLayout root;

    public View getRoot() {
        return root;
    }

    public TabLayoutViewPagerScreen(Context context, boolean shouldTabsBeScrollable, RecyclerView.Adapter<?> pagerAdapter, ObjIntConsumer<TabLayout.Tab> config) {
        final var tabs = new TabLayout(context);
        final var numOfTabs = pagerAdapter.getItemCount();
        for (int i = 0; i < numOfTabs; i++) {
            tabs.addTab(tabs.newTab());
        }
        if (shouldTabsBeScrollable) {
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        final var pager = new ViewPager2(context);
        pager.setOffscreenPageLimit(numOfTabs);
        pager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabs, pager, config::accept).attach();
        root = new LinearLayout(context);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.setOrientation(LinearLayout.VERTICAL);
        root.addView(tabs, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(pager, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
