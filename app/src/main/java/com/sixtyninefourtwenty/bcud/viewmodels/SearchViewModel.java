package com.sixtyninefourtwenty.bcud.viewmodels;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.sixtyninefourtwenty.bcud.utils.annotations.ActivityScopedViewModel;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@ActivityScopedViewModel(reason = "Communication between multiple fragments")
@NonNullTypesByDefault
public final class SearchViewModel extends ViewModel {

    private static final String QUERY_KEY = "query";

    private final SavedStateHandle savedStateHandle;
    private final MutableLiveData<String> query = new MutableLiveData<>("");

    public SearchViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        query.setValue(requireNonNullElse(savedStateHandle.get(QUERY_KEY), ""));
    }

    public void setQuery(String search) {
        query.setValue(requireNonNull(search));
        savedStateHandle.set(QUERY_KEY, search);
    }

    public String getQueryValue() {
        return requireNonNull(query.getValue());
    }

    public LiveData<String> getQuery() {
        return query;
    }

    public void setToSearchViewIfPresent(MenuItem menuItem) {
        if (!getQueryValue().isEmpty() && menuItem.getActionView() instanceof SearchView sv) {
            menuItem.expandActionView();
            sv.setQuery(getQueryValue(), false);
        }
    }

    public static SearchViewModel get(ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner).get(SearchViewModel.class);
    }

}
