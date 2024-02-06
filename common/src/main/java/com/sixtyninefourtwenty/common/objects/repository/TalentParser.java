package com.sixtyninefourtwenty.common.objects.repository;

import static java.util.stream.Collectors.toList;

import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.utils.CommonConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public final class TalentParser implements TalentSupplier {

    private final List<Talent> talents;

    @SneakyThrows
    public TalentParser(InputStream inputStream) {
        try (final var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            talents = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.CSV_DELIMITER_PIPE))
                    .map(parts -> new Talent(Integer.parseInt(parts[0]), parts[1]))
                    .collect(toList());
        }
    }

}
