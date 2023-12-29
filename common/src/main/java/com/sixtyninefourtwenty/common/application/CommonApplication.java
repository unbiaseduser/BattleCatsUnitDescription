package com.sixtyninefourtwenty.common.application;

import android.app.Application;

import com.sixtyninefourtwenty.common.objects.repository.TFMaterialParser;
import com.sixtyninefourtwenty.common.objects.repository.TFMaterialSupplier;
import com.sixtyninefourtwenty.common.objects.repository.TalentParser;
import com.sixtyninefourtwenty.common.objects.repository.TalentSupplier;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;

public class CommonApplication extends Application {

    private final Lazy<TalentSupplier> talentSupplierLazy = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new TalentParser(AssetsJava.openQuietly(getAssets(), "text/talents.txt")));

    public final TalentSupplier getTalentData() {
        return talentSupplierLazy.getValue();
    }

    private final Lazy<TFMaterialSupplier> tfMaterialSupplierLazy = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new TFMaterialParser(AssetsJava.openQuietly(getAssets(), "text/tf_materials.txt")));

    public final TFMaterialSupplier getMaterialData() {
        return tfMaterialSupplierLazy.getValue();
    }

}
