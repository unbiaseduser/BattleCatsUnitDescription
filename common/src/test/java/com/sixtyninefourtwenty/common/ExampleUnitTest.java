package com.sixtyninefourtwenty.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.common.io.CharStreams;
import com.sixtyninefourtwenty.common.objects.udp.UDPJsonParsing;
import com.sixtyninefourtwenty.common.objects.udp.UDPUnitInfo;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ExampleUnitTest {

    @Test
    void parseJson() throws IOException {
        try (final var jsonReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("UDP_Release_2.7.2.json")))) {
            final var map = UDPJsonParsing.parseUDPJson(CharStreams.toString(jsonReader));
            final var unitInfo = map.get(10);
            assertNotNull(unitInfo);
            assertEquals("Kung Fu Cat", unitInfo.getName());
            assertInstanceOf(UDPUnitInfo.OwnAndTalentComments.class, unitInfo.getComments());
        }
    }
}