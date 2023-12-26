package com.sixtyninefourtwenty.bcud.repository;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

@NonNullTypesByDefault
public interface VerboseUnitData extends UnitData {

    ImmutableList<Unit> getStoryLegends();
    ImmutableList<Unit> getRares();
    ImmutableList<Unit> getSuperRares();
    ImmutableList<Unit> getLegendRares();
    ImmutableList<Unit> getAdventDrops();
    ImmutableList<Unit> getCfSpecials();
    ImmutableList<Unit> getUbers();
    ImmutableList<Unit> getSpecialMaxPriority();
    ImmutableList<Unit> getSpecialHighPriority();
    ImmutableList<Unit> getSpecialMidPriority();
    ImmutableList<Unit> getSpecialLowPriority();
    ImmutableList<Unit> getSpecialMinPriority();
    ImmutableList<Unit> getRareMaxPriority();
    ImmutableList<Unit> getRareHighPriority();
    ImmutableList<Unit> getRareMidPriority();
    ImmutableList<Unit> getRareLowPriority();
    ImmutableList<Unit> getRareMinPriority();
    ImmutableList<Unit> getSuperRareMaxPriority();
    ImmutableList<Unit> getSuperRareHighPriority();
    ImmutableList<Unit> getSuperRareMidPriority();
    ImmutableList<Unit> getSuperRareLowPriority();
    ImmutableList<Unit> getSuperRareMinPriority();
    ImmutableList<Unit> getNonUberTopPriority();
    ImmutableList<Unit> getNonUberHighPriority();
    ImmutableList<Unit> getNonUberMidPriority();
    ImmutableList<Unit> getNonUberLowPriority();
    ImmutableList<Unit> getNonUberDoNotUnlock();
    ImmutableList<Unit> getUberTopPriority();
    ImmutableList<Unit> getUberHighPriority();
    ImmutableList<Unit> getUberMidPriority();
    ImmutableList<Unit> getUberLowPriority();
    ImmutableList<Unit> getUberDoNotUnlock();
    ImmutableList<Unit> getElderList();
    ImmutableList<Unit> getEpicList();

    @Override
    default ImmutableList<Unit> getMainList(UnitBaseData.Type type) {
        return switch (type) {
            case STORY_LEGEND -> getStoryLegends();
            case CF_SPECIAL -> getCfSpecials();
            case ADVENT_DROP -> getAdventDrops();
            case RARE -> getRares();
            case SUPER_RARE -> getSuperRares();
            case UBER -> getUbers();
            case LEGEND_RARE -> getLegendRares();
        };
    }

    @Override
    default ImmutableList<Unit> getHypermaxPriorityList(Hypermax.Priority priority, Hypermax.UnitType type) {
        return switch (priority) {
            case MAX -> switch (type) {
                case SPECIAL -> getSpecialMaxPriority();
                case RARE -> getRareMaxPriority();
                case SUPER_RARE -> getSuperRareMaxPriority();
            };
            case HIGH -> switch (type) {
                case SPECIAL -> getSpecialHighPriority();
                case RARE -> getRareHighPriority();
                case SUPER_RARE -> getSuperRareHighPriority();
            };
            case MID -> switch (type) {
                case SPECIAL -> getSpecialMidPriority();
                case RARE -> getRareMidPriority();
                case SUPER_RARE -> getSuperRareMidPriority();
            };
            case LOW -> switch (type) {
                case SPECIAL -> getSpecialLowPriority();
                case RARE -> getRareLowPriority();
                case SUPER_RARE -> getSuperRareLowPriority();
            };
            case MIN -> switch (type) {
                case SPECIAL -> getSpecialMinPriority();
                case RARE -> getRareMinPriority();
                case SUPER_RARE -> getSuperRareMinPriority();
            };
        };
    }

    @Override
    default ImmutableList<Unit> getTalentPriorityList(Talent.Priority priority, Talent.UnitType type) {
        return switch (priority) {
            case TOP -> switch (type) {
                case NON_UBER -> getNonUberTopPriority();
                case UBER -> getUberTopPriority();
            };
            case HIGH -> switch (type) {
                case NON_UBER -> getNonUberHighPriority();
                case UBER -> getUberHighPriority();
            };
            case MID -> switch (type) {
                case NON_UBER -> getNonUberMidPriority();
                case UBER -> getUberMidPriority();
            };
            case LOW -> switch (type) {
                case NON_UBER -> getNonUberLowPriority();
                case UBER -> getUberLowPriority();
            };
            case DONT -> switch (type) {
                case NON_UBER -> getNonUberDoNotUnlock();
                case UBER -> getUberDoNotUnlock();
            };
        };
    }

    @Override
    default ImmutableList<Unit> getElderEpicList(ElderEpic elderEpic) {
        return switch (elderEpic) {
            case ELDER -> getElderList();
            case EPIC -> getEpicList();
        };
    }
}
