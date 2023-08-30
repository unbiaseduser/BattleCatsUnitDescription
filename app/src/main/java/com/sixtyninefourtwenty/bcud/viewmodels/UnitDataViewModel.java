package com.sixtyninefourtwenty.bcud.viewmodels;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
        final Unit currentUnit = savedStateHandle.get(CURRENT_UNIT_TO_DISPLAY_KEY);
        if (currentUnit != null) {
            currentUnitToDisplay.setValue(currentUnit);
        }
        final var storyLegendQuery = requireNonNullElse(savedStateHandle.get(STORY_LEGEND_QUERY_KEY), "");
        storyLegendsIntermediate.setValue(Tuple.of(storyLegendQuery, data.getMainList(UnitBaseData.Type.STORY_LEGEND)));
        final var cfSpecialsQuery = requireNonNullElse(savedStateHandle.get(CF_SPECIAL_QUERY_KEY), "");
        cfSpecialsIntermediate.setValue(Tuple.of(cfSpecialsQuery, data.getMainList(UnitBaseData.Type.CF_SPECIAL)));
        final var adventDropsQuery = requireNonNullElse(savedStateHandle.get(ADVENT_DROP_QUERY_KEY), "");
        adventDropsIntermediate.setValue(Tuple.of(adventDropsQuery, data.getMainList(UnitBaseData.Type.ADVENT_DROP)));
        final var raresQuery = requireNonNullElse(savedStateHandle.get(RARE_QUERY_KEY), "");
        raresIntermediate.setValue(Tuple.of(raresQuery, data.getMainList(UnitBaseData.Type.RARE)));
        final var superRaresQuery = requireNonNullElse(savedStateHandle.get(SUPER_RARE_QUERY_KEY), "");
        superRaresIntermediate.setValue(Tuple.of(superRaresQuery, data.getMainList(UnitBaseData.Type.SUPER_RARE)));
        final var ubersQuery = requireNonNullElse(savedStateHandle.get(UBER_QUERY_KEY), "");
        ubersIntermediate.setValue(Tuple.of(ubersQuery, data.getMainList(UnitBaseData.Type.UBER)));
        final var legendRaresQuery = requireNonNullElse(savedStateHandle.get(LEGEND_RARE_QUERY_KEY), "");
        legendRaresIntermediate.setValue(Tuple.of(legendRaresQuery, data.getMainList(UnitBaseData.Type.LEGEND_RARE)));
        dataSet = data;
        this.explanationSupplier = explanationSupplier;
        this.savedStateHandle = savedStateHandle;
        final var rareHypermaxPriorityQuery = requireNonNullElse(savedStateHandle.get(RARE_HP_QUERY_KEY), "");
        final var rareHypermaxPriorityType = requireNonNullElse(savedStateHandle.get(RARE_HP_PRIORITY_KEY), Hypermax.Priority.MAX);
        rareHypermaxPriorityIntermediate.setValue(Tuple.of(rareHypermaxPriorityQuery, dataSet.getHypermaxPriorityList(rareHypermaxPriorityType, Hypermax.UnitType.RARE)));
        final var superRareHypermaxPriorityQuery = requireNonNullElse(savedStateHandle.get(SUPER_RARE_HP_QUERY_KEY), "");
        final var superRareHypermaxPriorityType = requireNonNullElse(savedStateHandle.get(SUPER_RARE_HP_PRIORITY_KEY), Hypermax.Priority.MAX);
        superRareHypermaxPriorityIntermediate.setValue(Tuple.of(superRareHypermaxPriorityQuery, dataSet.getHypermaxPriorityList(superRareHypermaxPriorityType, Hypermax.UnitType.SUPER_RARE)));
        final var specialHypermaxPriorityQuery = requireNonNullElse(savedStateHandle.get(SPECIAL_HP_QUERY_KEY), "");
        final var specialHypermaxPriorityType = requireNonNullElse(savedStateHandle.get(SPECIAL_HP_PRIORITY_KEY), Hypermax.Priority.MAX);
        specialHypermaxPriorityIntermediate.setValue(Tuple.of(specialHypermaxPriorityQuery, dataSet.getHypermaxPriorityList(specialHypermaxPriorityType, Hypermax.UnitType.SPECIAL)));
        final var nonUberTalentPriorityQuery = requireNonNullElse(savedStateHandle.get(NON_UBER_TP_QUERY_KEY), "");
        final var nonUberTalentPriority = requireNonNullElse(savedStateHandle.get(NON_UBER_TP_PRIORITY_KEY), Talent.Priority.TOP);
        nonUberTalentPriorityIntermediate.setValue(Tuple.of(nonUberTalentPriorityQuery, dataSet.getTalentPriorityList(nonUberTalentPriority, Talent.UnitType.NON_UBER), nonUberTalentPriority));
        final var uberTalentPriorityQuery = requireNonNullElse(savedStateHandle.get(UBER_TP_QUERY_KEY), "");
        final var uberTalentPriority = requireNonNullElse(savedStateHandle.get(UBER_TP_PRIORITY_KEY), Talent.Priority.TOP);
        uberTalentPriorityIntermediate.setValue(Tuple.of(uberTalentPriorityQuery, dataSet.getTalentPriorityList(uberTalentPriority, Talent.UnitType.UBER), uberTalentPriority));
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

    private final MutableLiveData<Unit> currentUnitToDisplay = new MutableLiveData<>();

    public LiveData<Unit> getCurrentUnitToDisplay() {
        return currentUnitToDisplay;
    }

    public void setCurrentUnitToDisplay(Unit unit) {
        if (!unit.equals(currentUnitToDisplay.getValue())) {
            currentUnitToDisplay.setValue(unit);
            savedStateHandle.set(CURRENT_UNIT_TO_DISPLAY_KEY, unit);
        }
    }

    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> storyLegendsIntermediate = new MutableLiveData<>();
    private final LiveData<ImmutableList<Unit>> storyLegends = Transformations.map(storyLegendsIntermediate, pair -> getSearchResults(pair._2, pair._1));
    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> cfSpecialsIntermediate = new MutableLiveData<>();
    private final LiveData<ImmutableList<Unit>> cfSpecials = Transformations.map(cfSpecialsIntermediate, pair -> getSearchResults(pair._2, pair._1));
    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> adventDropsIntermediate = new MutableLiveData<>();
    private final LiveData<ImmutableList<Unit>> adventDrops = Transformations.map(adventDropsIntermediate, pair -> getSearchResults(pair._2, pair._1));
    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> raresIntermediate = new MutableLiveData<>();
    private final LiveData<ImmutableList<Unit>> rares = Transformations.map(raresIntermediate, pair -> getSearchResults(pair._2, pair._1));
    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> superRaresIntermediate = new MutableLiveData<>();
    private final LiveData<ImmutableList<Unit>> superRares = Transformations.map(superRaresIntermediate, pair -> getSearchResults(pair._2, pair._1));
    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> ubersIntermediate = new MutableLiveData<>();
    private final LiveData<ImmutableList<Unit>> ubers = Transformations.map(ubersIntermediate, pair -> getSearchResults(pair._2, pair._1));
    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> legendRaresIntermediate = new MutableLiveData<>();
    private final LiveData<ImmutableList<Unit>> legendRares = Transformations.map(legendRaresIntermediate, pair -> getSearchResults(pair._2, pair._1));

    public void setUnitDescQuery(UnitBaseData.Type type, String query) {
        final var liveData = switch (type) {
            case STORY_LEGEND -> storyLegendsIntermediate;
            case CF_SPECIAL -> cfSpecialsIntermediate;
            case ADVENT_DROP -> adventDropsIntermediate;
            case RARE -> raresIntermediate;
            case SUPER_RARE -> superRaresIntermediate;
            case UBER -> ubersIntermediate;
            case LEGEND_RARE -> legendRaresIntermediate;
        };
        liveData.setValue(requireNonNull(liveData.getValue()).update1(query));
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

    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> rareHypermaxPriorityIntermediate = new MutableLiveData<>();
    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> superRareHypermaxPriorityIntermediate = new MutableLiveData<>();
    private final MutableLiveData<Tuple2<String, ImmutableList<Unit>>> specialHypermaxPriorityIntermediate = new MutableLiveData<>();
    private final LiveData<ImmutableList<Unit>> rareHypermaxPriority = Transformations.map(rareHypermaxPriorityIntermediate, pair -> getSearchResults(pair._2, pair._1));
    private final LiveData<ImmutableList<Unit>> superRareHypermaxPriority = Transformations.map(superRareHypermaxPriorityIntermediate, pair -> getSearchResults(pair._2, pair._1));
    private final LiveData<ImmutableList<Unit>> specialHypermaxPriority = Transformations.map(specialHypermaxPriorityIntermediate, pair -> getSearchResults(pair._2, pair._1));

    public void setHypermaxPriorityQuery(Hypermax.UnitType type, String query) {
        final var liveData = switch (type) {
            case SPECIAL -> specialHypermaxPriorityIntermediate;
            case RARE -> rareHypermaxPriorityIntermediate;
            case SUPER_RARE -> superRareHypermaxPriorityIntermediate;
        };
        liveData.setValue(requireNonNull(liveData.getValue()).update1(query));
        final var key = switch (type) {
            case SPECIAL -> SPECIAL_HP_QUERY_KEY;
            case RARE -> RARE_HP_QUERY_KEY;
            case SUPER_RARE -> SUPER_RARE_HP_QUERY_KEY;
        };
        savedStateHandle.set(key, query);
    }

    public void setHypermaxPriorityType(Hypermax.UnitType type, Hypermax.Priority priority) {
        final var liveData = switch (type) {
            case SPECIAL -> specialHypermaxPriorityIntermediate;
            case RARE -> rareHypermaxPriorityIntermediate;
            case SUPER_RARE -> superRareHypermaxPriorityIntermediate;
        };
        final var list = dataSet.getHypermaxPriorityList(priority, type);
        liveData.setValue(requireNonNull(liveData.getValue()).update2(list));
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

    private final MutableLiveData<Tuple3<String, ImmutableList<Unit>, Talent.Priority>> nonUberTalentPriorityIntermediate = new MutableLiveData<>();
    private final MutableLiveData<Tuple3<String, ImmutableList<Unit>, Talent.Priority>> uberTalentPriorityIntermediate = new MutableLiveData<>();
    private final LiveData<Tuple2<ImmutableList<Unit>, Talent.Priority>> nonUberTalentPriority = Transformations.map(nonUberTalentPriorityIntermediate, triple -> Tuple.of(getSearchResults(triple._2, triple._1), triple._3));
    private final LiveData<Tuple2<ImmutableList<Unit>, Talent.Priority>> uberTalentPriority = Transformations.map(uberTalentPriorityIntermediate, triple -> Tuple.of(getSearchResults(triple._2, triple._1), triple._3));

    public void setTalentPriorityQuery(Talent.UnitType type, String query) {
        final var liveData = switch (type) {
            case NON_UBER -> nonUberTalentPriorityIntermediate;
            case UBER -> uberTalentPriorityIntermediate;
        };
        liveData.setValue(requireNonNull(liveData.getValue()).update1(query));
        final var key = switch (type) {
            case NON_UBER -> NON_UBER_TP_QUERY_KEY;
            case UBER -> UBER_TP_QUERY_KEY;
        };
        savedStateHandle.set(key, query);
    }

    public void setTalentPriorityType(Talent.UnitType type, Talent.Priority priority) {
        final var liveData = switch (type) {
            case NON_UBER -> nonUberTalentPriorityIntermediate;
            case UBER -> uberTalentPriorityIntermediate;
        };
        final var list = dataSet.getTalentPriorityList(priority, type);
        liveData.setValue(requireNonNull(liveData.getValue()).update2(list).update3(priority));
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
