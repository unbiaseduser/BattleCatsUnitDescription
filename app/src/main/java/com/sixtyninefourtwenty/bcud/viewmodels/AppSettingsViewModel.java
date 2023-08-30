package com.sixtyninefourtwenty.bcud.viewmodels;

import static java.util.Objects.requireNonNull;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.sixtyninefourtwenty.bcud.utils.AppPreferences;
import com.sixtyninefourtwenty.bcud.utils.annotations.ActivityScopedViewModel;

import org.checkerframework.checker.nullness.qual.NonNull;

@ActivityScopedViewModel(reason = "Communication between multiple fragments")
public final class AppSettingsViewModel extends AndroidViewModel {

    private final AppPreferences prefs;

    public AppSettingsViewModel(@NonNull Application application) {
        super(application);
        prefs = AppPreferences.get(application);
        listViewMode.setValue(prefs.getListViewMode());
        adventViewMode.setValue(prefs.getAdventViewMode());
    }

    private final MutableLiveData<AppPreferences.ListViewMode> listViewMode = new MutableLiveData<>();

    public LiveData<AppPreferences.ListViewMode> getListViewMode() {
        return listViewMode;
    }

    public void setListViewMode(AppPreferences.ListViewMode mode) {
        listViewMode.setValue(mode);
    }

    public void persistListViewMode() {
        prefs.setListViewMode(requireNonNull(listViewMode.getValue()));
    }

    private final MutableLiveData<AppPreferences.AdventViewMode> adventViewMode = new MutableLiveData<>();

    public void setAdventViewMode(AppPreferences.AdventViewMode mode) {
        adventViewMode.setValue(mode);
    }

    public LiveData<AppPreferences.AdventViewMode> getAdventViewMode() {
        return adventViewMode;
    }

    public void persistAdventViewMode() {
        prefs.setAdventViewMode(requireNonNull(adventViewMode.getValue()));
    }

    public static AppSettingsViewModel get(ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner).get(AppSettingsViewModel.class);
    }

}
