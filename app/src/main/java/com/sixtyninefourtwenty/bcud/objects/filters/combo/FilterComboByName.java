package com.sixtyninefourtwenty.bcud.objects.filters.combo;

import com.sixtyninefourtwenty.bcud.objects.Combo;
import com.sixtyninefourtwenty.bcud.repository.helper.ComboNameSupplier;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import java.util.Collection;
import java.util.function.Predicate;

import kotlin.text.StringsKt;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
@NonNullTypesByDefault
public class FilterComboByName implements Predicate<Combo> {

    public static void safeAddTo(Collection<? super Predicate<Combo>> collection, String nameQuery, ComboNameSupplier supplier) {
        if (!nameQuery.isBlank()) {
            collection.add(new FilterComboByName(nameQuery, supplier));
        }
    }

    @Getter(AccessLevel.NONE)
    String nameQuery;
    ComboNameSupplier supplier;

    @Override
    public boolean test(Combo combo) {
        return StringsKt.contains(combo.getName(supplier), nameQuery, true);
    }
}
