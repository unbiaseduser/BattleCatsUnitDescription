package com.sixtyninefourtwenty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sixtyninefourtwenty.bcud.repository.PonosQuoteData;
import com.sixtyninefourtwenty.bcud.repository.PonosQuoteSupplier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class PonosQuotesDataTest {

    private static Path ponosQuotesFile;

    @BeforeAll
    static void setup() {
        ponosQuotesFile = Paths.get("src/main/assets/text/MainMenu_en.csv");
    }

    @Test
    void test() throws Exception {
        final PonosQuoteSupplier data = new PonosQuoteData(Files.newInputStream(ponosQuotesFile));
        final var quotes = data.getQuotes();
        assertEquals("Welcome to the Cat Base! Prepare yourself for battle here! When you're ready, attack!", quotes.get(0));
    }

}
