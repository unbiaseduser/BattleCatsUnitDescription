package com.sixtyninefourtwenty.bcud.viewmodels;

import static java.util.Objects.requireNonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateHandleSupport;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.utils.AppPreferences;
import com.sixtyninefourtwenty.bcud.utils.annotations.ActivityScopedViewModel;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import lombok.Getter;

@NonNullTypesByDefault
@ActivityScopedViewModel(reason = "Communication between multiple fragments")
public final class AppSettingsViewModel extends ViewModel {

    private static final String LIST_VIEW_MODE_KEY = "list_view_mode";
    private static final String ADVENT_VIEW_MODE_KEY = "advent_view_mode";

    private final SavedStateHandle savedStateHandle;
    private final AppPreferences prefs;

    public AppSettingsViewModel(AppPreferences prefs, SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.prefs = prefs;
        listViewMode = savedStateHandle.getLiveData(LIST_VIEW_MODE_KEY, prefs.getListViewMode());
        adventViewMode = savedStateHandle.getLiveData(ADVENT_VIEW_MODE_KEY, prefs.getAdventViewMode());
    }

    @Getter
    private final LiveData<AppPreferences.ListViewMode> listViewMode;

    public void setListViewMode(AppPreferences.ListViewMode mode) {
        savedStateHandle.set(LIST_VIEW_MODE_KEY, mode);
    }

    public void persistListViewMode() {
        prefs.setListViewMode(requireNonNull(listViewMode.getValue()));
    }

    @Getter
    private final LiveData<AppPreferences.AdventViewMode> adventViewMode;

    public void setAdventViewMode(AppPreferences.AdventViewMode mode) {
        savedStateHandle.set(ADVENT_VIEW_MODE_KEY, mode);
    }

    public void persistAdventViewMode() {
        prefs.setAdventViewMode(requireNonNull(adventViewMode.getValue()));
    }

    private static final ViewModelInitializer<AppSettingsViewModel> INITIALIZER = new ViewModelInitializer<>(AppSettingsViewModel.class, creationExtras -> {
        final var application = creationExtras.get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
        assert application != null;
        return new AppSettingsViewModel(MyApplication.get(application).getPrefs(), SavedStateHandleSupport.createSavedStateHandle(creationExtras));
    });

    public static AppSettingsViewModel get(ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner, ViewModelProvider.Factory.from(INITIALIZER)).get(AppSettingsViewModel.class);
    }

}
