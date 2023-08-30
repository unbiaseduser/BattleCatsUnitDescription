package com.sixtyninefourtwenty.common.utils;

import androidx.appcompat.widget.SearchView;

@FunctionalInterface
public interface ChangeOnlyQueryTextListener extends SearchView.OnQueryTextListener {

    @Override
    default boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    default boolean onQueryTextChange(String newText) {
        onQueryTextChangeCustom(newText);
        return true;
    }

    void onQueryTextChangeCustom(String newText);

    static SearchView.OnQueryTextListener changeOnly(ChangeOnlyQueryTextListener listener) {
        return listener;
    }

}
