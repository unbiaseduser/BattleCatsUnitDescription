package com.sixtyninefourtwenty.common.utils;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EnumValuesCache {

    private final Map<Class<? extends Enum<?>>, ImmutableList<Enum<?>>> map = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <E extends Enum<E>> ImmutableList<E> getEnumValues(Class<E> clazz) {
        return (ImmutableList<E>) map.computeIfAbsent(clazz, enumClass -> ImmutableList.copyOf(requireNonNull(enumClass.getEnumConstants())));
    }

}
