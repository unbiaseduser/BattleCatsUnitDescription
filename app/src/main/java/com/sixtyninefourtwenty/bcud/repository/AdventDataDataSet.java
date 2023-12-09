package com.sixtyninefourtwenty.bcud.repository;

import android.content.res.AssetManager;

import com.google.common.collect.ImmutableList;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.bcud.objects.AdventBoss;
import com.sixtyninefourtwenty.bcud.objects.AdventStage;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import java.util.List;

import kotlin.collections.CollectionsKt;
import lombok.Getter;

@Getter
@NonNullTypesByDefault
public final class AdventDataDataSet implements AdventData {

    private final ImmutableList<AdventBoss> bosses;

    public AdventDataDataSet(AssetManager assets) {
        bosses = initBosses(assets);
    }

    private List<AdventStage> initStages(AssetManager assets) {
        var lines = AssetsJava.readTextFileLines(assets, "text/advent_stages.txt", stream -> stream.map(line -> FastStringUtils.split(line, CommonConstants.PIPE)).collect(new ImmutableListCollector<>()));
        var detailsDir = "text/advents/";
        var imgDir = "img/advent_stages/";
        return CollectionsKt.map(lines, line -> new AdventStage(Integer.parseInt(line[0]), imgDir + line[1] + ".png", line[2], detailsDir + line[3] + ".md", line[4]));
    }

    private ImmutableList<AdventBoss> initBosses(AssetManager assets) {
        var stages = initStages(assets);
        var bossesFileLines = AssetsJava.readTextFileLines(assets, "text/advent_bosses.txt", stream -> stream.map(line -> FastStringUtils.split(line, CommonConstants.PIPE)).collect(new ImmutableListCollector<>()));
        var bossInfosFileLines = AssetsJava.readTextFileLines(assets, "text/advent_boss_infos.txt", stream -> stream.map(line -> FastStringUtils.split(line, CommonConstants.PIPE)).collect(new ImmutableListCollector<>()));
        var list = new ImmutableList.Builder<AdventBoss>();
        for (var bossParts : bossesFileLines) {
            int currentBossId = Integer.parseInt(bossParts[0]);
            var stagesOfThisBoss = CollectionsKt.filter(stages, stage -> stage.getBossId() == currentBossId);
            var bossInfo = CollectionsKt.first(bossInfosFileLines, info -> Integer.parseInt(info[0]) == currentBossId)[1];
            list.add(new AdventBoss(currentBossId, bossParts[1], bossInfo, bossParts[2], ImmutableList.copyOf(stagesOfThisBoss)));
        }
        return list.build();
    }
}
