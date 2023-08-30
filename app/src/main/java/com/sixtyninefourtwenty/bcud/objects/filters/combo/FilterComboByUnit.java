package com.sixtyninefourtwenty.bcud.objects.filters.combo;

import com.sixtyninefourtwenty.bcud.objects.Combo;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
@NonNullTypesByDefault
public class FilterComboByUnit implements Predicate<Combo> {

    @Getter(AccessLevel.NONE)
    Iterable<Unit> filteredUnits;

    @Override
    public boolean test(Combo combo) {
        for (final var unit : filteredUnits)
            if (combo.getUnits().contains(unit)) return true;
        return false;
    }
}
