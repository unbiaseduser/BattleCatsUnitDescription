package com.sixtyninefourtwenty;

import static com.sixtyninefourtwenty.TestUtils.createUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationParser;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationSupplier;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import io.vavr.control.Try;

class UnitExplanationTest {

    private static UnitExplanationSupplier explanationSupplier;

    private static InputStream openInputStreamFromPath(Path path) {
        return Try.of(() -> Files.newInputStream(path)).get();
    }

    @BeforeAll
    static void setupAll() {
        final var units = List.of(createUnit(24, UnitBaseData.Type.STORY_LEGEND),
                createUnit(554, UnitBaseData.Type.STORY_LEGEND));
        explanationSupplier = new UnitExplanationParser(units, fileName -> openInputStreamFromPath(Path.of("src/main/assets/text/unit_desc/" + fileName)));
    }

    @Test
    void testExplanationSupplierWithUnitWithTf() {
        final var ex = explanationSupplier.getExplanation(24);
        assertTrue(ex.hasTf());
        assertEquals("Valkyrie Cat", ex.getFirstFormName());
        assertEquals("Descended from the fields of Valhalla to provide salvation for the Battle Cats! Uses the Heavenly Spear Meownir (Area Attack)", ex.getFirstFormDescription());
        assertEquals("True Valkyrie Cat", ex.getSecondFormName());
        assertEquals("Changed to her final form. Troubled because God keeps hitting on her. Uses the Heavenly Spear Meownir (Area Attack)", ex.getSecondFormDescription());
        assertEquals("Holy Valkyrie Cat", ex.getTrueFormName());
        assertEquals("The curse is broken, and the savior of Catkind has returned! The restored Meownir pierces foes, freezing them in their tracks. (Area Attack)", ex.getTrueFormDescription());
    }

    @Test
    void testExplanationSupplierWithUnitWithoutTf() {
        final var ex = explanationSupplier.getExplanation(554);
        assertFalse(ex.hasTf());
        assertEquals("Master Uril", ex.getFirstFormName());
        assertEquals("The will of one in flowing robes is the law! And so, he forced his way into the Cat Army. 100% Slow vs Relics, Area Surge attacks.", ex.getFirstFormDescription());
        assertEquals("Mystic Uril", ex.getSecondFormName());
        assertEquals("Fabled martial artist who sealed away the demon king. ...says the parrot. This mask is a gift from his grandchild. 100% Slow vs Relics, Area Surge attacks.", ex.getSecondFormDescription());
        assertNull(ex.getTrueFormName());
        assertNull(ex.getTrueFormDescription());
    }

    @Test
    void testExplanationSupplierFailOnNonExistentUnit() {
        assertThrows(Exception.class, () -> explanationSupplier.getExplanation(Integer.MAX_VALUE));
    }

}
