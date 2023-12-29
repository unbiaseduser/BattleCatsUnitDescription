package com.sixtyninefourtwenty.common.objects.repository;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.TFMaterial;

import java.util.List;

@NonNullTypesByDefault
public interface TFMaterialSupplier {

    List<TFMaterial> getMaterials();

    default TFMaterial getMaterialByIndex(int materialIndex) {
        return getMaterials().stream()
                .filter(material -> material.getIndex() == materialIndex)
                .findFirst()
                .orElseThrow();
    }

}
