package com.sixtyninefourtwenty.bcud.viewmodels;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateHandleSupport;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.objects.Combo;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.objects.filters.combo.FilterComboByName;
import com.sixtyninefourtwenty.bcud.objects.filters.combo.FilterComboByType;
import com.sixtyninefourtwenty.bcud.objects.filters.combo.FilterComboByUnit;
import com.sixtyninefourtwenty.bcud.objects.filters.unit.FilterUnitByName;
import com.sixtyninefourtwenty.bcud.objects.filters.unit.FilterUnitByType;
import com.sixtyninefourtwenty.bcud.repository.UnitData;
import com.sixtyninefourtwenty.bcud.repository.helper.ComboNameSupplier;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationSupplier;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import lombok.Getter;

@NonNullTypesByDefault
public final class ComboDataViewModel extends ViewModel {

    private static final String CURRENTLY_USED_FILTERS_KEY = "currently_used_filters";
    private static final String CURRENT_QUERY_BY_NAME_KEY = "current_query_by_name";
    private static final String CURRENT_QUERY_BY_UNIT_KEY = "current_query_by_unit";
    private static final String CURRENT_SEARCH_SCOPE_KEY = "current_search_scope";
    private static final String CURRENT_COMBO_LIST_KEY = "current_combo_list";

    private final SavedStateHandle savedStateHandle;
    private final UnitData dataSet;

    public ComboDataViewModel(SavedStateHandle savedStateHandle, UnitData data) {
        this.savedStateHandle = savedStateHandle;
        dataSet = data;
        currentlyUsedFilters = requireNonNullElse(savedStateHandle.get(CURRENTLY_USED_FILTERS_KEY), EnumSet.noneOf(Combo.Type.class));
        currentQueryByName = requireNonNullElse(savedStateHandle.get(CURRENT_QUERY_BY_NAME_KEY), "");
        currentQueryByUnit.setValue(requireNonNullElse(savedStateHandle.get(CURRENT_QUERY_BY_UNIT_KEY), ""));
        currentSearchScope = requireNonNullElse(savedStateHandle.get(CURRENT_SEARCH_SCOPE_KEY), EnumSet.noneOf(UnitBaseData.Type.class));
        currentComboList.setValue(ImmutableList.copyOf(requireNonNullElse(savedStateHandle.get(CURRENT_COMBO_LIST_KEY), dataSet.getAllCombos().toArray(new Combo[0]))));
    }

    private final Set<Combo.Type> currentlyUsedFilters;

    public boolean comboFiltersContainsType(Combo.Type type) {
        return currentlyUsedFilters.contains(type);
    }

    public void addTypeToComboFilters(Combo.Type type) {
        currentlyUsedFilters.add(type);
        savedStateHandle.set(CURRENTLY_USED_FILTERS_KEY, currentlyUsedFilters);
    }

    public void removeTypeFromComboFilters(Combo.Type type) {
        currentlyUsedFilters.remove(type);
        savedStateHandle.set(CURRENTLY_USED_FILTERS_KEY, currentlyUsedFilters);
    }

    /**
     * -- GETTER --
     *  Persists combo name query across searches.
     */
    @Getter
    private String currentQueryByName;

    public void setCurrentQueryByName(String currentQueryByName) {
        savedStateHandle.set(CURRENT_QUERY_BY_NAME_KEY, currentQueryByName);
        this.currentQueryByName = requireNonNull(currentQueryByName);
    }

    private final MutableLiveData<String> currentQueryByUnit = new MutableLiveData<>("");

    /**
     * Persists unit name in combo query across searches.
     */
    public LiveData<String> getCurrentQueryByUnit() {
        return currentQueryByUnit;
    }

    public void setCurrentQueryByUnit(String query) {
        savedStateHandle.set(CURRENT_QUERY_BY_UNIT_KEY, query);
        currentQueryByUnit.setValue(requireNonNull(query));
    }

    private final Set<UnitBaseData.Type> currentSearchScope;

    public boolean unitSearchScopeContainsType(UnitBaseData.Type type) {
        return currentSearchScope.contains(type);
    }

    public void addTypeToUnitSearchScope(UnitBaseData.Type type) {
        currentSearchScope.add(type);
        savedStateHandle.set(CURRENT_SEARCH_SCOPE_KEY, currentSearchScope);
    }

    public void removeTypeFromUnitSearchScope(UnitBaseData.Type type) {
        currentSearchScope.remove(type);
        savedStateHandle.set(CURRENT_SEARCH_SCOPE_KEY, currentSearchScope);
    }

    private final MutableLiveData<ImmutableList<Combo>> currentComboList = new MutableLiveData<>();

    public LiveData<ImmutableList<Combo>> getCurrentComboList() {
        return currentComboList;
    }

    public void setCurrentComboList(List<Combo> list) {
        savedStateHandle.set(CURRENT_COMBO_LIST_KEY, list.toArray(new Combo[0]));
        currentComboList.setValue(ImmutableList.copyOf(list));
    }

    public ImmutableList<Combo> filterCombos(UnitExplanationSupplier explanationSupplier, ComboNameSupplier comboNameSupplier) {
        final var filters = new HashSet<Predicate<Combo>>(3);
        FilterComboByType.safeAddTo(filters, currentlyUsedFilters);
        FilterComboByName.safeAddTo(filters, currentQueryByName, comboNameSupplier);
        if (!requireNonNull(currentQueryByUnit.getValue()).isEmpty()) {
            final var unitFilters = new HashSet<Predicate<Unit>>(2);
            FilterUnitByName.safeAddTo(unitFilters, currentQueryByUnit.getValue(), explanationSupplier); //guaranteed to succeed
            FilterUnitByType.safeAddTo(unitFilters, currentSearchScope);
            final var filteredUnits = dataSet.filterUnits(dataSet.getAllUnits(), unitFilters);
            filters.add(new FilterComboByUnit(filteredUnits));
        }
        return dataSet.filterCombos(filters);
    }

    public ImmutableList<Combo> findCombosContainingUnit(Unit unit) {
        return dataSet.findCombosContainingUnit(unit);
    }

    public void resetSearchFilterCombosState() {
        currentQueryByName = "";
        currentQueryByUnit.setValue("");
        currentlyUsedFilters.clear();
        currentSearchScope.clear();
        currentComboList.setValue(dataSet.getAllCombos());
        savedStateHandle.keys().forEach(savedStateHandle::remove);
    }

    public static final ViewModelInitializer<ComboDataViewModel> INITIALIZER = new ViewModelInitializer<>(ComboDataViewModel.class, creationExtras -> {
        final var application = creationExtras.get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
        assert application != null;
        return new ComboDataViewModel(SavedStateHandleSupport.createSavedStateHandle(creationExtras), ((MyApplication) application).getUnitData());
    });

    public static ComboDataViewModel get(ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner, ViewModelProvider.Factory.from(INITIALIZER)).get(ComboDataViewModel.class);
    }
}
