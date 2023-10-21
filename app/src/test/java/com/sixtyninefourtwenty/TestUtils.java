package com.sixtyninefourtwenty;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

public final class TestUtils {

    private TestUtils() {}

    public static Unit createUnit(int unitId, UnitBaseData.Type type) {
        return new Unit(
                unitId,
                type,
                ImmutableList.of(),
                ImmutableList.of()
        );
    }

}
