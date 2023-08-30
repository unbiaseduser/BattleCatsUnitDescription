package com.sixtyninefourtwenty.bcud.repository.helper;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public interface ComboEffectDescSupplier {

    String getDesc(int typeIndex, int levelIndex);

}
