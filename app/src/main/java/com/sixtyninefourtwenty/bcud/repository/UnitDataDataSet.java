package com.sixtyninefourtwenty.bcud.repository;

import android.content.Context;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.objects.Combo;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.repository.helper.ComboParser;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitEEPriorityReasoningDataParser;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitHPDataParser;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitParser;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitTFMaterialDataParser;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitTalentDataParser;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Function;

import kotlin.io.TextStreamsKt;
import lombok.Getter;
import lombok.SneakyThrows;

@Immutable
@NonNullTypesByDefault
@Getter
public final class UnitDataDataSet implements VerboseUnitData {

    @SneakyThrows
    public UnitDataDataSet(Context context) {
        final var assets = context.getAssets();
        try (final var unitTfMaterialDataReader = new BufferedReader(new InputStreamReader(assets.open("text/unit_tf_material_data.json")));
             final var unitEepDataReader = new BufferedReader(new InputStreamReader(assets.open("text/unit_eep_data.json")));
             final var unitTalentDataReader = new BufferedReader(new InputStreamReader(assets.open("text/unit_talent_data.json")));
             final var unitHypermaxDataReader = new BufferedReader(new InputStreamReader(assets.open("text/unit_hypermax_data.json")));
             final var unitBaseDataReader = new BufferedReader(new InputStreamReader(assets.open("text/unit_base_data.json")))) {
            final var tfMaterialDataParser = new UnitTFMaterialDataParser(TextStreamsKt.readText(unitTfMaterialDataReader));
            final var eePriorityDataParser = new UnitEEPriorityReasoningDataParser(TextStreamsKt.readText(unitEepDataReader));
            final var talentDataParser = new UnitTalentDataParser(TextStreamsKt.readText(unitTalentDataReader));
            final var hpDataParser = new UnitHPDataParser(TextStreamsKt.readText(unitHypermaxDataReader));
            final var unitParser = new UnitParser(TextStreamsKt.readText(unitBaseDataReader));
            final Function<TalentData, Talent> talentDataToTalentFunction = data -> data.getTalent(MyApplication.get(context).getTalentData());
            storyLegends = unitParser.createMainList(UnitBaseData.Type.STORY_LEGEND,
                    tfMaterialDataParser::getMaterialListForUnitWithId,
                    talentDataParser::getTalentListForUnitWithId,
                    talentDataToTalentFunction);
            rares = unitParser.createMainList(UnitBaseData.Type.RARE,
                    tfMaterialDataParser::getMaterialListForUnitWithId,
                    talentDataParser::getTalentListForUnitWithId,
                    talentDataToTalentFunction);
            superRares = unitParser.createMainList(UnitBaseData.Type.SUPER_RARE,
                    tfMaterialDataParser::getMaterialListForUnitWithId,
                    talentDataParser::getTalentListForUnitWithId,
                    talentDataToTalentFunction);
            legendRares = unitParser.createMainList(UnitBaseData.Type.LEGEND_RARE,
                    tfMaterialDataParser::getMaterialListForUnitWithId,
                    talentDataParser::getTalentListForUnitWithId,
                    talentDataToTalentFunction);
            adventDrops = unitParser.createMainList(UnitBaseData.Type.ADVENT_DROP,
                    tfMaterialDataParser::getMaterialListForUnitWithId,
                    talentDataParser::getTalentListForUnitWithId,
                    talentDataToTalentFunction);
            cfSpecials = unitParser.createMainList(UnitBaseData.Type.CF_SPECIAL,
                    tfMaterialDataParser::getMaterialListForUnitWithId,
                    talentDataParser::getTalentListForUnitWithId,
                    talentDataToTalentFunction);
            ubers = unitParser.createMainList(UnitBaseData.Type.UBER,
                    tfMaterialDataParser::getMaterialListForUnitWithId,
                    talentDataParser::getTalentListForUnitWithId,
                    talentDataToTalentFunction);
            allUnits = new ImmutableList.Builder<Unit>()
                    .addAll(storyLegends)
                    .addAll(cfSpecials)
                    .addAll(adventDrops)
                    .addAll(rares)
                    .addAll(superRares)
                    .addAll(ubers)
                    .addAll(legendRares)
                    .build();
            specialMaxPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.MAX, Hypermax.UnitType.SPECIAL, allUnits);
            specialHighPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.HIGH, Hypermax.UnitType.SPECIAL, allUnits);
            specialMidPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.MID, Hypermax.UnitType.SPECIAL, allUnits);
            specialLowPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.LOW, Hypermax.UnitType.SPECIAL, allUnits);
            specialMinPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.MIN, Hypermax.UnitType.SPECIAL, allUnits);
            rareMaxPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.MAX, Hypermax.UnitType.RARE, allUnits);
            rareHighPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.HIGH, Hypermax.UnitType.RARE, allUnits);
            rareMidPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.MID, Hypermax.UnitType.RARE, allUnits);
            rareLowPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.LOW, Hypermax.UnitType.RARE, allUnits);
            rareMinPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.MIN, Hypermax.UnitType.RARE, allUnits);
            superRareMaxPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.MAX, Hypermax.UnitType.SUPER_RARE, allUnits);
            superRareHighPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.HIGH, Hypermax.UnitType.SUPER_RARE, allUnits);
            superRareMidPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.MID, Hypermax.UnitType.SUPER_RARE, allUnits);
            superRareLowPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.LOW, Hypermax.UnitType.SUPER_RARE, allUnits);
            superRareMinPriority = hpDataParser.createHypermaxPriorityList(Hypermax.Priority.MIN, Hypermax.UnitType.SUPER_RARE, allUnits);
            nonUberTopPriority = talentDataParser.createTalentPriorityList(Talent.Priority.TOP, Talent.UnitType.NON_UBER, allUnits);
            nonUberHighPriority = talentDataParser.createTalentPriorityList(Talent.Priority.HIGH, Talent.UnitType.NON_UBER, allUnits);
            nonUberMidPriority = talentDataParser.createTalentPriorityList(Talent.Priority.MID, Talent.UnitType.NON_UBER, allUnits);
            nonUberLowPriority = talentDataParser.createTalentPriorityList(Talent.Priority.LOW, Talent.UnitType.NON_UBER, allUnits);
            nonUberDoNotUnlock = talentDataParser.createTalentPriorityList(Talent.Priority.DONT, Talent.UnitType.NON_UBER, allUnits);
            uberTopPriority = talentDataParser.createTalentPriorityList(Talent.Priority.TOP, Talent.UnitType.UBER, allUnits);
            uberHighPriority = talentDataParser.createTalentPriorityList(Talent.Priority.HIGH, Talent.UnitType.UBER, allUnits);
            uberMidPriority = talentDataParser.createTalentPriorityList(Talent.Priority.MID, Talent.UnitType.UBER, allUnits);
            uberLowPriority = talentDataParser.createTalentPriorityList(Talent.Priority.LOW, Talent.UnitType.UBER, allUnits);
            uberDoNotUnlock = talentDataParser.createTalentPriorityList(Talent.Priority.DONT, Talent.UnitType.UBER, allUnits);
            elderList = eePriorityDataParser.createElderEpicPriorityList(ElderEpic.ELDER, allUnits);
            epicList = eePriorityDataParser.createElderEpicPriorityList(ElderEpic.EPIC, allUnits);
        }
        final var comboParser = new ComboParser(assets);
        allCombos = comboParser.createCombos(allUnits);
    }

    private final ImmutableList<Unit> storyLegends;
    private final ImmutableList<Unit> rares;
    private final ImmutableList<Unit> superRares;
    private final ImmutableList<Unit> legendRares;
    private final ImmutableList<Unit> adventDrops;
    private final ImmutableList<Unit> cfSpecials;
    private final ImmutableList<Unit> ubers;
    private final ImmutableList<Unit> allUnits;
    private final ImmutableList<Unit> specialMaxPriority;
    private final ImmutableList<Unit> specialHighPriority;
    private final ImmutableList<Unit> specialMidPriority;
    private final ImmutableList<Unit> specialLowPriority;
    private final ImmutableList<Unit> specialMinPriority;
    private final ImmutableList<Unit> rareMaxPriority;
    private final ImmutableList<Unit> rareHighPriority;
    private final ImmutableList<Unit> rareMidPriority;
    private final ImmutableList<Unit> rareLowPriority;
    private final ImmutableList<Unit> rareMinPriority;
    private final ImmutableList<Unit> superRareMaxPriority;
    private final ImmutableList<Unit> superRareHighPriority;
    private final ImmutableList<Unit> superRareMidPriority;
    private final ImmutableList<Unit> superRareLowPriority;
    private final ImmutableList<Unit> superRareMinPriority;
    private final ImmutableList<Unit> nonUberTopPriority;
    private final ImmutableList<Unit> nonUberHighPriority;
    private final ImmutableList<Unit> nonUberMidPriority;
    private final ImmutableList<Unit> nonUberLowPriority;
    private final ImmutableList<Unit> nonUberDoNotUnlock;
    private final ImmutableList<Unit> uberTopPriority;
    private final ImmutableList<Unit> uberHighPriority;
    private final ImmutableList<Unit> uberMidPriority;
    private final ImmutableList<Unit> uberLowPriority;
    private final ImmutableList<Unit> uberDoNotUnlock;
    private final ImmutableList<Unit> elderList;
    private final ImmutableList<Unit> epicList;
    private final ImmutableList<Combo> allCombos;

}
