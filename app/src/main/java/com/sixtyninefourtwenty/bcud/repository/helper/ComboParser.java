package com.sixtyninefourtwenty.bcud.repository.helper;

import static com.sixtyninefourtwenty.bcud.utils.Utils.DATA_DELIMITER_COMMA;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;
import static kotlin.collections.CollectionsKt.first;

import android.content.res.AssetManager;

import com.google.common.collect.ImmutableList;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.bcud.objects.Combo;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import java.util.function.IntFunction;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

@NonNullTypesByDefault
public final class ComboParser {

    private final IntList permittedComboFileLineIndices;
    private final ImmutableList<String[]> allCombosDataFileLines;

    public ComboParser(AssetManager assets) {
        final var permittedComboFileLineIndicesArr = DATA_DELIMITER_COMMA.split(requireNonNull(AssetsJava.readFirstFileLine(assets, "text/combos/supported_combo_line_indices.txt")));
        permittedComboFileLineIndices = new IntArrayList(permittedComboFileLineIndicesArr.length);
        for (final var s : permittedComboFileLineIndicesArr) {
            permittedComboFileLineIndices.add(parseInt(s));
        }
        allCombosDataFileLines = AssetsJava.readTextFileLines(assets, "text/combos/NyancomboData.csv", stream -> stream.map(line -> FastStringUtils.split(line, CommonConstants.COMMA)).collect(new ImmutableListCollector<>()));
    }

    public ImmutableList<Combo> createCombos(Iterable<Unit> unitsToSearchFrom) {

        IntFunction<Unit> getUnitById = id -> first(unitsToSearchFrom, unit -> unit.getId() == id);

        final var list = new ImmutableList.Builder<Combo>();
        for (int i = 0; i < allCombosDataFileLines.size(); i++) {
            if (permittedComboFileLineIndices.contains(i)) {
                final var dataLine = allCombosDataFileLines.get(i);
                Unit u1 = null;
                Unit u2 = null;
                Unit u3 = null;
                Unit u4 = null;
                Unit u5 = null;
                int i1 = -1;
                int i2 = -1;
                int i3 = -1;
                int i4 = -1;
                int i5 = -1;
                final var type = first(Combo.Type.VALUES, t -> t.getIndex() == parseInt(dataLine[12]));
                final var level = first(Combo.Level.VALUES, l -> l.getIndex() == parseInt(dataLine[13]));
                for (int j = 2; j <= 10; j += 2) {
                    if (!"-1".equals(dataLine[j])) {
                        switch (j) {
                            case 2 -> {
                                u1 = getUnitById.apply(parseInt(dataLine[j]));
                                i1 = parseInt(dataLine[j + 1]);
                            }
                            case 4 -> {
                                u2 = getUnitById.apply(parseInt(dataLine[j]));
                                i2 = parseInt(dataLine[j + 1]);
                            }
                            case 6 -> {
                                u3 = getUnitById.apply(parseInt(dataLine[j]));
                                i3 = parseInt(dataLine[j + 1]);
                            }
                            case 8 -> {
                                u4 = getUnitById.apply(parseInt(dataLine[j]));
                                i4 = parseInt(dataLine[j + 1]);
                            }
                            case 10 -> {
                                u5 = getUnitById.apply(parseInt(dataLine[j]));
                                i5 = parseInt(dataLine[j + 1]);
                            }
                        }
                    } else break;
                }
                list.add(new Combo(i, type, level,
                        requireNonNull(u1), u2, u3, u4, u5, i1, i2, i3, i4, i5));
            }
        }
        return list.build();
    }

}
