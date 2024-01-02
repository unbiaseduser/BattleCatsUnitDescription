package com.sixtyninefourtwenty.bcud.viewmodels;

import static java.util.Objects.requireNonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateHandleSupport;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.objects.filters.unit.FilterUnitByName;
import com.sixtyninefourtwenty.bcud.objects.filters.unit.FilterUnitByType;
import com.sixtyninefourtwenty.bcud.repository.UnitData;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationSupplier;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import lombok.Getter;

@NonNullTypesByDefault
public final class UnitDataViewModel extends ViewModel {

    private static final String CURRENT_UNIT_TO_DISPLAY_KEY = "current_unit_to_display";
    private static final String STORY_LEGEND_QUERY_KEY = "story_legend_query";
    private static final String CF_SPECIAL_QUERY_KEY = "cf_special_query";
    private static final String ADVENT_DROP_QUERY_KEY = "advent_drop_query";
    private static final String RARE_QUERY_KEY = "rare_query";
    private static final String SUPER_RARE_QUERY_KEY = "super_rare_query";
    private static final String UBER_QUERY_KEY = "uber_query";
    private static final String LEGEND_RARE_QUERY_KEY = "legend_rare_query";
    private static final String RARE_HP_QUERY_KEY = "rare_hp_query";
    private static final String RARE_HP_PRIORITY_KEY = "rare_hp_priority";
    private static final String SUPER_RARE_HP_QUERY_KEY = "super_rare_hp_query";
    private static final String SUPER_RARE_HP_PRIORITY_KEY = "super_rare_hp_priority";
    private static final String SPECIAL_HP_QUERY_KEY = "special_hp_query";
    private static final String SPECIAL_HP_PRIORITY_KEY = "special_hp_priority";
    private static final String NON_UBER_TP_QUERY_KEY = "non_uber_tp_query";
    private static final String NON_UBER_TP_PRIORITY_KEY = "non_uber_tp_priority";
    private static final String UBER_TP_QUERY_KEY = "uber_tp_query";
    private static final String UBER_TP_PRIORITY_KEY = "uber_tp_priority";

    public UnitDataViewModel(SavedStateHandle savedStateHandle, UnitData data, UnitExplanationSupplier explanationSupplier) {
        currentUnitToDisplay = savedStateHandle.getLiveData(CURRENT_UNIT_TO_DISPLAY_KEY);
        final var storyLegendQueryLiveData = savedStateHandle.getLiveData(STORY_LEGEND_QUERY_KEY, "");
        storyLegendsIntermediate = Transformations.map(storyLegendQueryLiveData, query -> Tuple.of(query, data.getMainList(UnitBaseData.Type.STORY_LEGEND)));
        storyLegends = Transformations.map(storyLegendsIntermediate, pair -> getSearchResults(pair._2, pair._1));
        final var cfSpecialsQueryLiveData = savedStateHandle.getLiveData(CF_SPECIAL_QUERY_KEY, "");
        cfSpecialsIntermediate = Transformations.map(cfSpecialsQueryLiveData, query -> Tuple.of(query, data.getMainList(UnitBaseData.Type.CF_SPECIAL)));
        cfSpecials = Transformations.map(cfSpecialsIntermediate, pair -> getSearchResults(pair._2, pair._1));
        final var adventDropsQueryLiveData = savedStateHandle.getLiveData(ADVENT_DROP_QUERY_KEY, "");
        adventDropsIntermediate = Transformations.map(adventDropsQueryLiveData, query -> Tuple.of(query, data.getMainList(UnitBaseData.Type.ADVENT_DROP)));
        adventDrops = Transformations.map(adventDropsIntermediate, pair -> getSearchResults(pair._2, pair._1));
        final var raresQueryLiveData = savedStateHandle.getLiveData(RARE_QUERY_KEY, "");
        raresIntermediate = Transformations.map(raresQueryLiveData, query -> Tuple.of(query, data.getMainList(UnitBaseData.Type.RARE)));
        rares = Transformations.map(raresIntermediate, pair -> getSearchResults(pair._2, pair._1));
        final var superRaresQueryLiveData = savedStateHandle.getLiveData(SUPER_RARE_QUERY_KEY, "");
        superRaresIntermediate = Transformations.map(superRaresQueryLiveData, query -> Tuple.of(query, data.getMainList(UnitBaseData.Type.SUPER_RARE)));
        superRares = Transformations.map(superRaresIntermediate, pair -> getSearchResults(pair._2, pair._1));
        final var ubersQueryLiveData = savedStateHandle.getLiveData(UBER_QUERY_KEY, "");
        ubersIntermediate = Transformations.map(ubersQueryLiveData, query -> Tuple.of(query, data.getMainList(UnitBaseData.Type.UBER)));
        ubers = Transformations.map(ubersIntermediate, pair -> getSearchResults(pair._2, pair._1));
        final var legendRaresQueryLiveData = savedStateHandle.getLiveData(LEGEND_RARE_QUERY_KEY, "");
        legendRaresIntermediate = Transformations.map(legendRaresQueryLiveData, query -> Tuple.of(query, data.getMainList(UnitBaseData.Type.LEGEND_RARE)));
        legendRares = Transformations.map(legendRaresIntermediate, pair -> getSearchResults(pair._2, pair._1));
        dataSet = data;
        this.explanationSupplier = explanationSupplier;
        this.savedStateHandle = savedStateHandle;
        final var rareHypermaxPriorityQueryLiveData = savedStateHandle.getLiveData(RARE_HP_QUERY_KEY, "");
        final var rareHypermaxPriorityTypeLiveData = savedStateHandle.getLiveData(RARE_HP_PRIORITY_KEY, Hypermax.Priority.MAX);
        rareHypermaxPriorityIntermediate = new MediatorLiveData<>();
        rareHypermaxPriorityIntermediate.addSource(rareHypermaxPriorityQueryLiveData, query -> rareHypermaxPriorityIntermediate.setValue(Tuple.of(query, dataSet.getHypermaxPriorityList(requireNonNull(rareHypermaxPriorityTypeLiveData.getValue()), Hypermax.UnitType.RARE))));
        rareHypermaxPriorityIntermediate.addSource(rareHypermaxPriorityTypeLiveData, type -> rareHypermaxPriorityIntermediate.setValue(Tuple.of(requireNonNull(rareHypermaxPriorityQueryLiveData.getValue()), dataSet.getHypermaxPriorityList(type, Hypermax.UnitType.RARE))));
        rareHypermaxPriority = Transformations.map(rareHypermaxPriorityIntermediate, pair -> getSearchResults(pair._2, pair._1));
        final var superRareHypermaxPriorityQueryLiveData = savedStateHandle.getLiveData(SUPER_RARE_HP_QUERY_KEY, "");
        final var superRareHypermaxPriorityTypeLiveData = savedStateHandle.getLiveData(SUPER_RARE_HP_PRIORITY_KEY, Hypermax.Priority.MAX);
        superRareHypermaxPriorityIntermediate = new MediatorLiveData<>();
        superRareHypermaxPriorityIntermediate.addSource(superRareHypermaxPriorityQueryLiveData, query -> superRareHypermaxPriorityIntermediate.setValue(Tuple.of(query, dataSet.getHypermaxPriorityList(requireNonNull(superRareHypermaxPriorityTypeLiveData.getValue()), Hypermax.UnitType.SUPER_RARE))));
        superRareHypermaxPriorityIntermediate.addSource(superRareHypermaxPriorityTypeLiveData, type -> superRareHypermaxPriorityIntermediate.setValue(Tuple.of(requireNonNull(superRareHypermaxPriorityQueryLiveData.getValue()), dataSet.getHypermaxPriorityList(type, Hypermax.UnitType.SUPER_RARE))));
        superRareHypermaxPriority = Transformations.map(superRareHypermaxPriorityIntermediate, pair -> getSearchResults(pair._2, pair._1));
        final var specialHypermaxPriorityQueryLiveData = savedStateHandle.getLiveData(SPECIAL_HP_QUERY_KEY, "");
        final var specialHypermaxPriorityTypeLiveData = savedStateHandle.getLiveData(SPECIAL_HP_PRIORITY_KEY, Hypermax.Priority.MAX);
        specialHypermaxPriorityIntermediate = new MediatorLiveData<>();
        specialHypermaxPriorityIntermediate.addSource(specialHypermaxPriorityQueryLiveData, query -> specialHypermaxPriorityIntermediate.setValue(Tuple.of(query, dataSet.getHypermaxPriorityList(requireNonNull(specialHypermaxPriorityTypeLiveData.getValue()), Hypermax.UnitType.SPECIAL))));
        specialHypermaxPriorityIntermediate.addSource(specialHypermaxPriorityTypeLiveData, type -> specialHypermaxPriorityIntermediate.setValue(Tuple.of(requireNonNull(specialHypermaxPriorityQueryLiveData.getValue()), dataSet.getHypermaxPriorityList(type, Hypermax.UnitType.SPECIAL))));
        specialHypermaxPriority = Transformations.map(specialHypermaxPriorityIntermediate, pair -> getSearchResults(pair._2, pair._1));
        final var nonUberTalentPriorityQueryLiveData = savedStateHandle.getLiveData(NON_UBER_TP_QUERY_KEY, "");
        final var nonUberTalentPriorityLiveData = savedStateHandle.getLiveData(NON_UBER_TP_PRIORITY_KEY, Talent.Priority.TOP);
        nonUberTalentPriorityIntermediate = new MediatorLiveData<>();
        nonUberTalentPriorityIntermediate.addSource(nonUberTalentPriorityQueryLiveData, query -> {
            final var talentPriority = requireNonNull(nonUberTalentPriorityLiveData.getValue());
            nonUberTalentPriorityIntermediate.setValue(Tuple.of(query, dataSet.getTalentPriorityList(talentPriority, Talent.UnitType.NON_UBER), talentPriority));
        });
        nonUberTalentPriorityIntermediate.addSource(nonUberTalentPriorityLiveData, priority -> nonUberTalentPriorityIntermediate.setValue(Tuple.of(requireNonNull(nonUberTalentPriorityQueryLiveData.getValue()), dataSet.getTalentPriorityList(priority, Talent.UnitType.NON_UBER), priority)));
        nonUberTalentPriority = Transformations.map(nonUberTalentPriorityIntermediate, triple -> Tuple.of(getSearchResults(triple._2, triple._1), triple._3));
        final var uberTalentPriorityQueryLiveData = savedStateHandle.getLiveData(UBER_TP_QUERY_KEY, "");
        final var uberTalentPriorityLiveData = savedStateHandle.getLiveData(UBER_TP_PRIORITY_KEY, Talent.Priority.TOP);
        uberTalentPriorityIntermediate = new MediatorLiveData<>();
        uberTalentPriorityIntermediate.addSource(uberTalentPriorityQueryLiveData, query -> {
            final var talentPriority = requireNonNull(uberTalentPriorityLiveData.getValue());
            uberTalentPriorityIntermediate.setValue(Tuple.of(query, dataSet.getTalentPriorityList(talentPriority, Talent.UnitType.UBER), talentPriority));
        });
        uberTalentPriorityIntermediate.addSource(uberTalentPriorityLiveData, priority -> uberTalentPriorityIntermediate.setValue(Tuple.of(requireNonNull(uberTalentPriorityQueryLiveData.getValue()), dataSet.getTalentPriorityList(priority, Talent.UnitType.UBER), priority)));
        uberTalentPriority = Transformations.map(uberTalentPriorityIntermediate, triple -> Tuple.of(getSearchResults(triple._2, triple._1), triple._3));
    }

    private final SavedStateHandle savedStateHandle;

    private final UnitData dataSet;

    private UnitExplanationSupplier explanationSupplier;

    @SuppressWarnings("unused")
    public void setExplanationSupplier(UnitExplanationSupplier explanationSupplier) {
        this.explanationSupplier = explanationSupplier;
        for (final var type : UnitBaseData.Type.values()) {
            setUnitDescQuery(type, "");
        }
        for (final var type : Hypermax.UnitType.values()) {
            setHypermaxPriorityQuery(type, "");
        }
        for (final var type : Talent.UnitType.values()) {
            setTalentPriorityQuery(type, "");
        }
    }

    @Getter
    private final LiveData<Unit> currentUnitToDisplay;

    public void setCurrentUnitToDisplay(Unit unit) {
        if (!unit.equals(currentUnitToDisplay.getValue())) {
            savedStateHandle.set(CURRENT_UNIT_TO_DISPLAY_KEY, unit);
        }
    }

    private final LiveData<Tuple2<String, ImmutableList<Unit>>> storyLegendsIntermediate;
    private final LiveData<ImmutableList<Unit>> storyLegends;
    private final LiveData<Tuple2<String, ImmutableList<Unit>>> cfSpecialsIntermediate;
    private final LiveData<ImmutableList<Unit>> cfSpecials;
    private final LiveData<Tuple2<String, ImmutableList<Unit>>> adventDropsIntermediate;
    private final LiveData<ImmutableList<Unit>> adventDrops;
    private final LiveData<Tuple2<String, ImmutableList<Unit>>> raresIntermediate;
    private final LiveData<ImmutableList<Unit>> rares;
    private final LiveData<Tuple2<String, ImmutableList<Unit>>> superRaresIntermediate;
    private final LiveData<ImmutableList<Unit>> superRares;
    private final LiveData<Tuple2<String, ImmutableList<Unit>>> ubersIntermediate;
    private final LiveData<ImmutableList<Unit>> ubers;
    private final LiveData<Tuple2<String, ImmutableList<Unit>>> legendRaresIntermediate;
    private final LiveData<ImmutableList<Unit>> legendRares;

    public void setUnitDescQuery(UnitBaseData.Type type, String query) {
        final var key = switch (type) {
            case STORY_LEGEND -> STORY_LEGEND_QUERY_KEY;
            case CF_SPECIAL -> CF_SPECIAL_QUERY_KEY;
            case ADVENT_DROP -> ADVENT_DROP_QUERY_KEY;
            case RARE -> RARE_QUERY_KEY;
            case SUPER_RARE -> SUPER_RARE_QUERY_KEY;
            case UBER -> UBER_QUERY_KEY;
            case LEGEND_RARE -> LEGEND_RARE_QUERY_KEY;
        };
        savedStateHandle.set(key, query);
    }

    public LiveData<ImmutableList<Unit>> getUnitDescLiveData(UnitBaseData.Type type) {
        return switch (type) {
            case STORY_LEGEND -> storyLegends;
            case CF_SPECIAL -> cfSpecials;
            case ADVENT_DROP -> adventDrops;
            case RARE -> rares;
            case SUPER_RARE -> superRares;
            case UBER -> ubers;
            case LEGEND_RARE -> legendRares;
        };
    }

    private final MediatorLiveData<Tuple2<String, ImmutableList<Unit>>> rareHypermaxPriorityIntermediate;
    private final MediatorLiveData<Tuple2<String, ImmutableList<Unit>>> superRareHypermaxPriorityIntermediate;
    private final MediatorLiveData<Tuple2<String, ImmutableList<Unit>>> specialHypermaxPriorityIntermediate;
    private final LiveData<ImmutableList<Unit>> rareHypermaxPriority;
    private final LiveData<ImmutableList<Unit>> superRareHypermaxPriority;
    private final LiveData<ImmutableList<Unit>> specialHypermaxPriority;

    public void setHypermaxPriorityQuery(Hypermax.UnitType type, String query) {
        final var key = switch (type) {
            case SPECIAL -> SPECIAL_HP_QUERY_KEY;
            case RARE -> RARE_HP_QUERY_KEY;
            case SUPER_RARE -> SUPER_RARE_HP_QUERY_KEY;
        };
        savedStateHandle.set(key, query);
    }

    public void setHypermaxPriorityType(Hypermax.UnitType type, Hypermax.Priority priority) {
        final var key = switch (type) {
            case SPECIAL -> SPECIAL_HP_PRIORITY_KEY;
            case RARE -> RARE_HP_PRIORITY_KEY;
            case SUPER_RARE -> SUPER_RARE_HP_PRIORITY_KEY;
        };
        savedStateHandle.set(key, priority);
    }

    public LiveData<ImmutableList<Unit>> getHypermaxPriorityLiveData(Hypermax.UnitType type) {
        return switch (type) {
            case SPECIAL -> specialHypermaxPriority;
            case RARE -> rareHypermaxPriority;
            case SUPER_RARE -> superRareHypermaxPriority;
        };
    }

    private final MediatorLiveData<Tuple3<String, ImmutableList<Unit>, Talent.Priority>> nonUberTalentPriorityIntermediate;
    private final MediatorLiveData<Tuple3<String, ImmutableList<Unit>, Talent.Priority>> uberTalentPriorityIntermediate;
    private final LiveData<Tuple2<ImmutableList<Unit>, Talent.Priority>> nonUberTalentPriority;
    private final LiveData<Tuple2<ImmutableList<Unit>, Talent.Priority>> uberTalentPriority;

    public void setTalentPriorityQuery(Talent.UnitType type, String query) {
        final var key = switch (type) {
            case NON_UBER -> NON_UBER_TP_QUERY_KEY;
            case UBER -> UBER_TP_QUERY_KEY;
        };
        savedStateHandle.set(key, query);
    }

    public void setTalentPriorityType(Talent.UnitType type, Talent.Priority priority) {
        final var key = switch (type) {
            case NON_UBER -> NON_UBER_TP_PRIORITY_KEY;
            case UBER -> UBER_TP_PRIORITY_KEY;
        };
        savedStateHandle.set(key, priority);
    }

    public LiveData<Tuple2<ImmutableList<Unit>, Talent.Priority>> getTalentPriorityLiveData(Talent.UnitType type) {
        return switch (type) {
            case NON_UBER -> nonUberTalentPriority;
            case UBER -> uberTalentPriority;
        };
    }

    private ImmutableList<Unit> getSearchResults(List<Unit> originalList, String query) {
        return getSearchResults(originalList, Collections.emptySet(), query);
    }

    private ImmutableList<Unit> getSearchResults(List<Unit> originalList, Set<UnitBaseData.Type> types, String query) {
        final var filters = new HashSet<Predicate<Unit>>();
        FilterUnitByName.safeAddTo(filters, query, explanationSupplier);
        FilterUnitByType.safeAddTo(filters, types);
        return dataSet.filterUnits(originalList, filters);
    }

    public static final ViewModelInitializer<UnitDataViewModel> INITIALIZER = new ViewModelInitializer<>(UnitDataViewModel.class, creationExtras -> {
        final var application = creationExtras.get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
        assert application != null;
        return new UnitDataViewModel(SavedStateHandleSupport.createSavedStateHandle(creationExtras), ((MyApplication) application).getUnitData(), ((MyApplication) application).getUnitExplanationData());
    });

    public static UnitDataViewModel get(ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner, ViewModelProvider.Factory.from(INITIALIZER)).get(UnitDataViewModel.class);
    }

}
