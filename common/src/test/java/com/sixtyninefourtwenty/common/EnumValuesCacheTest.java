package com.sixtyninefourtwenty.common;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.utils.EnumValuesCache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

class EnumValuesCacheTest {

    private EnumValuesCache cache;

    @BeforeEach
    void setup() {
        cache = new EnumValuesCache();
    }
    
    @Test
    void test() {
        assertIterableEquals(EnumSet.allOf(Talent.Priority.class), cache.getEnumValues(Talent.Priority.class));
    }

}
