package com.sixtyninefourtwenty.bcud;

import android.content.Context;

import androidx.room.Room;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoritesDatabase;
import com.sixtyninefourtwenty.bcud.repository.AdventData;
import com.sixtyninefourtwenty.bcud.repository.AdventDataDataSet;
import com.sixtyninefourtwenty.bcud.repository.FavoritesDataRepository;
import com.sixtyninefourtwenty.bcud.repository.GuideData;
import com.sixtyninefourtwenty.bcud.repository.GuideDataDataSet;
import com.sixtyninefourtwenty.bcud.repository.PonosQuoteData;
import com.sixtyninefourtwenty.bcud.repository.UnitData;
import com.sixtyninefourtwenty.bcud.repository.UnitDataDataSetCSV;
import com.sixtyninefourtwenty.bcud.repository.helper.ComboEffectDescParser;
import com.sixtyninefourtwenty.bcud.repository.helper.ComboEffectDescSupplier;
import com.sixtyninefourtwenty.bcud.repository.helper.ComboNameParser;
import com.sixtyninefourtwenty.bcud.repository.helper.ComboNameSupplier;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitDescPageTextsParser;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitDescPageTextsParserCSV;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitDescPageTextsSupplier;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitEEPriorityReasoningDataParser;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitEEPriorityReasoningDataParserCSV;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitEEPriorityReasoningSupplier;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationParser;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationSupplier;
import com.sixtyninefourtwenty.bcud.utils.AppPreferences;
import com.sixtyninefourtwenty.common.application.CommonApplication;
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

public final class MyApplication extends CommonApplication {

    private final Lazy<AppPreferences> prefs = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new AppPreferences(this));

    public AppPreferences getPrefs() {
        return prefs.getValue();
    }

    private final Lazy<FavoritesDataRepository> favoritesDataRepository = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new FavoritesDataRepository(
            Room.databaseBuilder(this, FavoritesDatabase.class, "favorites").build()
    ));

    public FavoritesDataRepository getFavoritesDataRepository() {
        return favoritesDataRepository.getValue();
    }

    private final Lazy<UnitEEPriorityReasoningSupplier> eeprData = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new UnitEEPriorityReasoningDataParser(getAssets()));

    private final Lazy<UnitEEPriorityReasoningSupplier> eeprDataCSV = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new UnitEEPriorityReasoningDataParserCSV(getAssets()));

    public UnitEEPriorityReasoningSupplier getEEPReasoningData(boolean isCsv) {
        return isCsv ? eeprDataCSV.getValue() : eeprData.getValue();
    }

    public UnitEEPriorityReasoningSupplier getEEPReasoningData() {
        return getEEPReasoningData(true);
    }

    private final Lazy<UnitDescPageTextsSupplier> descPageTextData = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new UnitDescPageTextsParser(getAssets()));

    private final Lazy<UnitDescPageTextsSupplier> descPageTextDataCSV = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new UnitDescPageTextsParserCSV(getAssets()));

    public UnitDescPageTextsSupplier getDescPageTextData(boolean isCsv) {
        return isCsv ? descPageTextDataCSV.getValue() : descPageTextData.getValue();
    }

    public UnitDescPageTextsSupplier getDescPageTextData() {
        return getDescPageTextData(true);
    }

    private final Lazy<UnitData> unitData = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new UnitDataDataSetCSV(this));

    public UnitData getUnitData() {
        return unitData.getValue();
    }

    private final Lazy<GuideData> guideData = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new GuideDataDataSet(getAssets()));

    public GuideData getGuideData() {
        return guideData.getValue();
    }

    private final Lazy<AdventData> adventData = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new AdventDataDataSet(getAssets()));

    public AdventData getAdventData() {
        return adventData.getValue();
    }

    private final Lazy<PonosQuoteData> ponosQuoteData = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new PonosQuoteData(getAssets()));

    public PonosQuoteData getPonosQuoteData() {
        return ponosQuoteData.getValue();
    }

    private final Lazy<TFMaterialInfoSupplier> materialInfo = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new TFMaterialInfoParser(getAssets()));

    public TFMaterialInfoSupplier getMaterialInfo() {
        return materialInfo.getValue();
    }

    private final Lazy<TalentInfoSupplier> talentInfo = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new TalentInfoParser(getAssets()));

    public TalentInfoSupplier getTalentInfo() {
        return talentInfo.getValue();
    }

    private final Lazy<UnitExplanationSupplier> unitExplanationData = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new UnitExplanationParser(getUnitData().getAllUnits(), getAssets()));

    public UnitExplanationSupplier getUnitExplanationData() {
        return unitExplanationData.getValue();
    }

    private final Lazy<ComboNameSupplier> comboNameData = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new ComboNameParser(getAssets()));

    public ComboNameSupplier getComboNameData() {
        return comboNameData.getValue();
    }

    private final Lazy<ComboEffectDescSupplier> comboEffectDescData = LazyKt.lazy(LazyThreadSafetyMode.NONE, () -> new ComboEffectDescParser(getAssets()));

    public ComboEffectDescSupplier getComboEffectDescData() {
        return comboEffectDescData.getValue();
    }

    @Getter
    private final ListeningExecutorService threadPool = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    public void eagerInit() {
        getUnitData();
        getGuideData();
        getAdventData();
        getMaterialData();
        getTalentData();
    }

    public static MyApplication get(Context context) {
        return Contexts.asApplication(context, MyApplication.class);
    }
}
