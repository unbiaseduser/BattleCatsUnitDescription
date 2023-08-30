package com.sixtyninefourtwenty.bcud.repository.helper;

import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public interface UnitExplanationSupplier {

    Unit.Explanation getExplanation(int unitId);

}
