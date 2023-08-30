package com.sixtyninefourtwenty.bcud.objects.filters.combo;

import com.sixtyninefourtwenty.bcud.objects.Combo;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
@NonNullTypesByDefault
public class FilterComboByType implements Predicate<Combo> {

    public static void safeAddTo(Collection<? super Predicate<Combo>> collection, Set<Combo.Type> types) {
        if (!types.isEmpty() && types.size() != Combo.Type.VALUES.size()) {
            collection.add(new FilterComboByType(types));
        }
    }

    @Getter(AccessLevel.NONE)
    Set<Combo.Type> types;

    @Override
    public boolean test(Combo combo) {
        for (final var type : types)
            if (combo.getType() == type) return true;
        return false;
    }

}
