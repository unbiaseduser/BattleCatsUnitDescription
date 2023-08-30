package com.sixtyninefourtwenty.common.objects.repository;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.TFMaterial;

@NonNullTypesByDefault
public interface TFMaterialInfoSupplier {

    TFMaterial.Info getInfo(int materialIndex);

}
