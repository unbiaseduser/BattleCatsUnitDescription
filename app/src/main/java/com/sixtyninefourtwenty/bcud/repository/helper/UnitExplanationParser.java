package com.sixtyninefourtwenty.bcud.repository.helper;

import static java.util.Objects.requireNonNull;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

import android.content.res.AssetManager;

import com.google.common.collect.ImmutableList;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

import io.vavr.collection.Stream;
import io.vavr.control.Try;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class UnitExplanationParser implements UnitExplanationSupplier {

    @SneakyThrows
    public UnitExplanationParser(Iterable<Unit> units, Function<String, InputStream> openInputStreamFromFileName) {
        for (final var unit : units) {
            final var unitId = unit.getId();
            final var fileName = "Unit_Explanation" + (unitId + 1) + "_en.csv";
            final ImmutableList<String[]> lines;
            try (final var fileReader = new BufferedReader(new InputStreamReader(openInputStreamFromFileName.apply(fileName)))) {
                lines = fileReader.lines()
                        .filter(not(String::isEmpty))
                        .map(line -> FastStringUtils.split(line, CommonConstants.PIPE))
                        .collect(new ImmutableListCollector<>());
            }
            final Function<String[], String> parseDesc = parts -> Stream.of(parts)
                    .drop(1)
                    .takeUntil(part -> part.isEmpty() || part.contains("\u000E"))
                    .collect(joining(" "));
            final var unitHasTF = lines.get(2).length > 0 && !lines.get(2)[0].equals(lines.get(1)[0]);
            data.put(unitId, new Unit.Explanation(
                    lines.get(0)[0],
                    lines.get(1)[0],
                    unitHasTF ? lines.get(2)[0] : null,
                    parseDesc.apply(lines.get(0)),
                    parseDesc.apply(lines.get(1)),
                    unitHasTF ? parseDesc.apply(lines.get(2)) : null
            ));
        }
    }

    public UnitExplanationParser(Iterable<Unit> units, AssetManager assets) {
        this(units, fileName -> Try.of(() -> assets.open("text/unit_desc/" + fileName)).get());
    }

    private final Int2ObjectMap<Unit.Explanation> data = new Int2ObjectOpenHashMap<>();

    @Override
    public Unit.Explanation getExplanation(int unitId) {
        return requireNonNull(data.get(unitId), "Can't get explanation for unit with id " + unitId);
    }
}
