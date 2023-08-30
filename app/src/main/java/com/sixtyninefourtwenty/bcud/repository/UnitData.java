package com.sixtyninefourtwenty.bcud.repository;

import androidx.annotation.IntRange;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Combo;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;

import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import kotlin.collections.CollectionsKt;

@NonNullTypesByDefault
public interface UnitData {
    ImmutableList<Unit> getAllUnits();
    ImmutableList<Unit> getMainList(UnitBaseData.Type type);
    ImmutableList<Unit> getHypermaxPriorityList(Hypermax.Priority priority, Hypermax.UnitType type);
    ImmutableList<Unit> getTalentPriorityList(Talent.Priority priority, Talent.UnitType type);
    ImmutableList<Unit> getElderEpicList(ElderEpic elderEpic);
    default Unit getUnitById(@IntRange(from = 1) int id) {
        return CollectionsKt.first(getAllUnits(), unit -> unit.getId() == id);
    }
    default ImmutableList<Unit> filterUnits(Iterable<Unit> originalList, Iterable<Predicate<Unit>> filters) {
        final var iterator = filters.iterator();
        if (!iterator.hasNext()) return ImmutableList.copyOf(originalList);
        var finalPredicate = iterator.next();
        while (iterator.hasNext()) {
            finalPredicate = finalPredicate.and(iterator.next());
        }
        return StreamSupport.stream(originalList.spliterator(), false)
                .filter(finalPredicate)
                .collect(new ImmutableListCollector<>());
    }
    ImmutableList<Combo> getAllCombos();
    default ImmutableList<Combo> filterCombos(Iterable<Combo> originalList, Iterable<Predicate<Combo>> filters) {
        final var iterator = filters.iterator();
        if (!iterator.hasNext()) return ImmutableList.copyOf(originalList);
        var finalPredicate = iterator.next();
        while (iterator.hasNext()) {
            finalPredicate = finalPredicate.and(iterator.next());
        }
        return StreamSupport.stream(originalList.spliterator(), false)
                .filter(finalPredicate)
                .distinct()
                .collect(new ImmutableListCollector<>());
    }
    default ImmutableList<Combo> filterCombos(Iterable<Predicate<Combo>> filters) {
        return filterCombos(getAllCombos(), filters);
    }
    default ImmutableList<Combo> findCombosContainingUnit(Unit unit) {
        return ImmutableList.copyOf(CollectionsKt.filter(getAllCombos(), combo -> combo.getUnits().contains(unit)));
    }
}
