package com.sixtyninefourtwenty;

import static com.sixtyninefourtwenty.TestUtils.createUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitEEPriorityReasoningDataParserCSV;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitHPDataParserCSV;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitParserCSV;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitTFMaterialDataParserCSV;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitTalentDataParserCSV;
import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.utils.Validations;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import kotlin.random.Random;

class CSVParsersTest {

    private static Path eePriorityTextFile;
    private static Path hpDataTextFile;
    private static Path unitDataTextFile;
    private static Path tpDataTextFile;
    private static Path tfMaterialDataTextFile;

    @BeforeAll
    static void init() {
        eePriorityTextFile = Paths.get("src/main/assets/text/eep_data.txt");
        hpDataTextFile = Paths.get("src/main/assets/text/hp_data.txt");
        unitDataTextFile = Paths.get("src/main/assets/text/unit_data.txt");
        tpDataTextFile = Paths.get("src/main/assets/text/tp_data.txt");
        tfMaterialDataTextFile = Paths.get("src/main/assets/text/unit_material_data.txt");
        List.of(eePriorityTextFile, hpDataTextFile, unitDataTextFile, tpDataTextFile, tfMaterialDataTextFile).forEach(path -> assertTrue(Files.exists(path)));
    }

    @Test
    void testEEPriorityParser() throws Exception {
        final var parser = new UnitEEPriorityReasoningDataParserCSV(Files.newInputStream(eePriorityTextFile));
        final var slapstickCatsId = 152;
        //Slapstick Cats has elder TF
        final var slapstickElderTFReasoning = parser.getPriorityReasoningForUnitWithId(slapstickCatsId, ElderEpic.ELDER);
        assertNotEquals(Validations.NO_INFO, slapstickElderTFReasoning);
        //Slapstick Cats doesn't have epic TF
        final var slapstickEpicTFReasoning = parser.getPriorityReasoningForUnitWithId(slapstickCatsId, ElderEpic.EPIC);
        assertEquals(Validations.NO_INFO, slapstickEpicTFReasoning);
        //Some random cat that definitely doesn't have elder/epic TF
        final var someCatWithNoElderEpicTFId = 10;
        final var definitelyEmptyElderTFReasoning = parser.getPriorityReasoningForUnitWithId(someCatWithNoElderEpicTFId, ElderEpic.ELDER);
        final var definitelyEmptyEpicTFReasoning = parser.getPriorityReasoningForUnitWithId(someCatWithNoElderEpicTFId, ElderEpic.EPIC);
        List.of(definitelyEmptyElderTFReasoning, definitelyEmptyEpicTFReasoning).forEach(s -> assertEquals(Validations.NO_INFO, s));

        //Should return empty list when passed an empty unit list
        final var emptyElderCats = parser.createElderEpicPriorityList(ElderEpic.ELDER, ImmutableList.of());
        assertTrue(emptyElderCats.isEmpty());
        final var emptyEpicCats = parser.createElderEpicPriorityList(ElderEpic.EPIC, ImmutableList.of());
        assertTrue(emptyEpicCats.isEmpty());

        final var fakeSlapstickCats = createUnit(152, UnitBaseData.Type.SUPER_RARE);
        final var fakeDancerCat = createUnit(10, UnitBaseData.Type.CF_SPECIAL);
        final var unitListOfSlapstickAndDancerCats = ImmutableList.of(fakeSlapstickCats, fakeDancerCat);
        final var elderListThatShouldContainSlapstickCats = parser.createElderEpicPriorityList(ElderEpic.ELDER, unitListOfSlapstickAndDancerCats);
        //Slapstick has elder TF, so returned unit list should contain it
        assertTrue(elderListThatShouldContainSlapstickCats.contains(fakeSlapstickCats));
        //Dancer doesn't, so list shouldn't contain it
        assertFalse(elderListThatShouldContainSlapstickCats.contains(fakeDancerCat));
    }

    @Test
    void testHpParser() throws Exception {
        final var parser = new UnitHPDataParserCSV(Files.newInputStream(hpDataTextFile));

        //Should return empty list when passed an empty unit list
        final var emptySpecialMaxPriorityList = parser.createHypermaxPriorityList(Hypermax.Priority.MAX, Hypermax.UnitType.SPECIAL, ImmutableList.of());
        assertTrue(emptySpecialMaxPriorityList.isEmpty());

        final var fakeBahamutCat = createUnit(25, UnitBaseData.Type.STORY_LEGEND);
        final var fakeValkyrieCat = createUnit(24, UnitBaseData.Type.STORY_LEGEND);

        final var specialMaxPriorityListThatShouldContainOnlyBahamutCat = parser.createHypermaxPriorityList(Hypermax.Priority.MAX, Hypermax.UnitType.SPECIAL, ImmutableList.of(fakeBahamutCat, fakeValkyrieCat));
        assertTrue(specialMaxPriorityListThatShouldContainOnlyBahamutCat.contains(fakeBahamutCat));
        assertFalse(specialMaxPriorityListThatShouldContainOnlyBahamutCat.contains(fakeValkyrieCat));
    }

    @Test
    void testUnitDataParser() throws Exception {
        final var parser = new UnitParserCSV(Files.newInputStream(unitDataTextFile));
        final var storyLegends = parser.createMainList(UnitBaseData.Type.STORY_LEGEND,
                unitId -> ImmutableList.of(),
                unitId -> ImmutableList.of(),
                data -> new Talent(Random.Default.nextInt(), ""));
        //Can't use createFakeUnit() here bc units created by unit parser has miscellaneous info filled in and those by the method doesn't
        //Should contain Valkyrie Cat
        assertTrue(storyLegends.stream().anyMatch(unit -> unit.getId() == 24));
        //Shouldn't contain Dancer Cat
        assertTrue(storyLegends.stream().noneMatch(unit -> unit.getId() == 10));
    }

    @Test
    void testTalentDataParser() throws Exception {
        final var parser = new UnitTalentDataParserCSV(Files.newInputStream(tpDataTextFile));
        //Valkyrie Cat doesn't have talents
        assertTrue(parser.getTalentListForUnitWithId(24).isEmpty());
        //Sanzo Cat has talents
        assertFalse(parser.getTalentListForUnitWithId(50).isEmpty());

        final var emptyNonUberTopPriorityTalentList = parser.createTalentPriorityList(Talent.Priority.TOP, Talent.UnitType.NON_UBER, ImmutableList.of());
        assertTrue(emptyNonUberTopPriorityTalentList.isEmpty());
        final var fakeSanzoCat = createUnit(50, UnitBaseData.Type.RARE);
        final var fakeBahamutCat = createUnit(25, UnitBaseData.Type.STORY_LEGEND);
        final var nonUberTopPriorityUnitListThatShouldContainSanzoCat = parser.createTalentPriorityList(Talent.Priority.TOP, Talent.UnitType.NON_UBER, ImmutableList.of(fakeSanzoCat, fakeBahamutCat));
        assertTrue(nonUberTopPriorityUnitListThatShouldContainSanzoCat.contains(fakeSanzoCat));
        assertFalse(nonUberTopPriorityUnitListThatShouldContainSanzoCat.contains(fakeBahamutCat));
    }

    @Test
    void testTfMaterialDataParser() throws Exception {
        final var parser = new UnitTFMaterialDataParserCSV(Files.newInputStream(tfMaterialDataTextFile));
        //Valkyrie Cat doesn't require tf materials
        assertTrue(parser.getMaterialListForUnitWithId(24).isEmpty());
        //Cat God requires 1000000 xp and 1 gold catfruit
        final var catGodTfMaterialList = parser.getMaterialListForUnitWithId(437);
        assertEquals(2, catGodTfMaterialList.size());
        final var xpData = catGodTfMaterialList.get(0);
        assertEquals(1000000, xpData.getQuantity());
        assertEquals(6, xpData.getMaterialIndex()); //XP
        final var goldCatfruitData = catGodTfMaterialList.get(1);
        assertEquals(1, goldCatfruitData.getQuantity());
        assertEquals(44, goldCatfruitData.getMaterialIndex()); //gold catfruit
    }

}
