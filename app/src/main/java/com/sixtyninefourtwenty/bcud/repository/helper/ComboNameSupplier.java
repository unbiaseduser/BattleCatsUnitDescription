package com.sixtyninefourtwenty.bcud.repository.helper;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public interface ComboNameSupplier {

    String getName(int comboIndex);

}
