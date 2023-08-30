package com.sixtyninefourtwenty.bcud.repository;

import android.content.res.AssetManager;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.bcud.objects.Guide;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;
import com.sixtyninefourtwenty.javastuff.Assets;

import lombok.Getter;

@NonNullTypesByDefault
@Immutable
public final class GuideDataDataSet implements GuideData {

    public GuideDataDataSet(AssetManager assets) {
        guides = loadGuides(GuideIdentifier.GUIDES, "text/guides/", assets);
        helpPins = loadGuides(GuideIdentifier.HELP_PINS, "", assets);
    }

    @Getter
    private final ImmutableList<Guide> guides;
    @Getter
    private final ImmutableList<Guide> helpPins;

    private enum GuideIdentifier {
        GUIDES, HELP_PINS
    }

    private ImmutableList<Guide> loadGuides(GuideIdentifier identifier, String textFileDir, AssetManager assets) {
        final var list = new ImmutableList.Builder<Guide>();
        final var fileLines = switch (identifier) {
            case GUIDES -> Assets.readTextFileLines(assets, "text/guide_list.txt", stream -> stream.map(line -> FastStringUtils.split(line, CommonConstants.PIPE)).collect(new ImmutableListCollector<>()));
            case HELP_PINS -> Assets.readTextFileLines(assets, "text/help_pin_list.txt", stream -> stream.map(line -> FastStringUtils.split(line, CommonConstants.PIPE)).collect(new ImmutableListCollector<>()));
        };
        for (final var line : fileLines) {
            list.add(new Guide(line[0], textFileDir + line[1], line[2]));
        }
        return list.build();
    }

}
