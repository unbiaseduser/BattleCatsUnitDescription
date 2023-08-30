package com.sixtyninefourtwenty.bcud.objects.filters.unit;

import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
@NonNullTypesByDefault
public class FilterUnitByType implements Predicate<Unit> {

    public static void safeAddTo(Collection<? super Predicate<Unit>> collection, Set<UnitBaseData.Type> types) {
        if (!types.isEmpty() && !types.equals(EnumSet.allOf(UnitBaseData.Type.class))) {
            collection.add(new FilterUnitByType(types));
        }
    }

    @Getter(AccessLevel.NONE)
    Set<UnitBaseData.Type> types;

    @Override
    public boolean test(Unit unit) {
        return types.contains(unit.getType());
    }
}
