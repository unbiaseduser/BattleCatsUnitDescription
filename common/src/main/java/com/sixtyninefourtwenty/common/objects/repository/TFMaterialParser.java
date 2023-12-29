package com.sixtyninefourtwenty.common.objects.repository;

import static java.util.stream.Collectors.toList;

import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.common.objects.TFMaterial;
import com.sixtyninefourtwenty.common.utils.CommonConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class TFMaterialParser implements TFMaterialSupplier {

    private final List<TFMaterial> materials;

    @SneakyThrows
    public TFMaterialParser(InputStream inputStream) {
        try (final var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            materials = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.PIPE))
                    .map(parts -> new TFMaterial(Integer.parseInt(parts[0]), parts[1]))
                    .collect(toList());
        }
    }

}
