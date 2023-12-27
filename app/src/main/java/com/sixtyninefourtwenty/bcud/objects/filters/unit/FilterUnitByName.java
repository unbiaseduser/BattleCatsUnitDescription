package com.sixtyninefourtwenty.bcud.objects.filters.unit;

import static java.util.Objects.requireNonNull;

import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationSupplier;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import java.util.Collection;
import java.util.function.Predicate;

import kotlin.text.StringsKt;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
@NonNullTypesByDefault
public class FilterUnitByName implements Predicate<Unit> {

    public static void safeAddTo(Collection<? super Predicate<Unit>> collection, String nameQuery, UnitExplanationSupplier explanationSupplier) {
        if (!nameQuery.isEmpty()) {
            collection.add(new FilterUnitByName(nameQuery, explanationSupplier));
        }
    }

    @Getter(AccessLevel.NONE)
    String nameQuery;

    UnitExplanationSupplier explanationSupplier;

    @Override
    public boolean test(Unit unit) {
        final var explanation = unit.getExplanation(explanationSupplier);
        final var containsFirstTwoForms = StringsKt.contains(explanation.getFirstFormName(), nameQuery, true) ||
                StringsKt.contains(explanation.getSecondFormName(), nameQuery, true);
        if (!unit.hasTF(explanationSupplier)) return containsFirstTwoForms;
        return containsFirstTwoForms || StringsKt.contains(requireNonNull(explanation.getTrueFormName()), nameQuery, true);
    }
}
