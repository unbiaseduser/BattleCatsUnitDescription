package com.sixtyninefourtwenty.bcuddatagenerator;

import android.app.Application;
import android.content.Context;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sixtyninefourtwenty.common.objects.repository.TFMaterialInfoParser;
import com.sixtyninefourtwenty.common.objects.repository.TFMaterialInfoSupplier;
import com.sixtyninefourtwenty.common.objects.repository.TalentInfoParser;
import com.sixtyninefourtwenty.common.objects.repository.TalentInfoSupplier;
import com.sixtyninefourtwenty.stuff.Contexts;

import java.util.concurrent.Executors;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import lombok.Getter;

public final class MyApplication extends Application {

    private final Lazy<TFMaterialInfoSupplier> materialInfo = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new TFMaterialInfoParser(getAssets()));

    public TFMaterialInfoSupplier getMaterialInfo() {
        return materialInfo.getValue();
    }

    private final Lazy<TalentInfoSupplier> talentInfo = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new TalentInfoParser(getAssets()));

    public TalentInfoSupplier getTalentInfo() {
        return talentInfo.getValue();
    }

    @Getter
    private final ListeningExecutorService threadPool = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    public static MyApplication get(Context context) {
        return Contexts.asApplication(context, MyApplication.class);
    }

}
