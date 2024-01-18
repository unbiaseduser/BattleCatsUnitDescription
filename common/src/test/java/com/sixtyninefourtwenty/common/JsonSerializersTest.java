package com.sixtyninefourtwenty.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.objects.UnitEEPriorityData;
import com.sixtyninefourtwenty.common.objects.UnitHypermaxData;
import com.sixtyninefourtwenty.common.objects.UnitTFMaterialData;
import com.sixtyninefourtwenty.common.objects.UnitTalentData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.control.Try;
import kotlin.random.Random;

class JsonSerializersTest {

    // JSONObject doesn't override equals and hashcode
    private void assertJsonObjectContentEquals(JSONObject obj1, JSONObject obj2) {
        obj1.keys().forEachRemaining(key -> Try.run(() -> {
            final var elem1 = obj1.get(key);
            final var elem2 = obj2.get(key);
            if (elem1 instanceof JSONObject jsonObject1) {
                final var jsonObject2 = (JSONObject) elem2;
                assertJsonObjectContentEquals(jsonObject1, jsonObject2);
            } else if (elem1 instanceof JSONArray jsonArray1) {
                final var jsonArray2 = (JSONArray) elem2;
                for (int i = 0; i < jsonArray1.length(); i++) {
                    final var innerElem1 = jsonArray1.get(i);
                    final var innerElem2 = jsonArray2.get(i);
                    if (innerElem1 instanceof JSONObject jsonObject1) {
                        final var jsonObject2 = (JSONObject) innerElem2;
                        assertJsonObjectContentEquals(jsonObject1, jsonObject2);
                    } else {
                        assertEquals(innerElem1, innerElem2);
                    }
                }
            } else {
                assertEquals(elem1, elem2);
            }
        }).get());
    }

    @MethodSource("talentDataJson")
    @ParameterizedTest
    void deserializeTalentData(TalentData data, String json) throws Exception {
        assertEquals(data, TalentData.SERIALIZER.fromJson(new JSONObject(json)));
    }

    @MethodSource("talentDataJson")
    @ParameterizedTest
    void serializeTalentData(TalentData data, String json) throws Exception {
        final var jsonObjFromData = TalentData.SERIALIZER.toJson(data);
        final var jsonObjFromString = new JSONObject(json);
        assertJsonObjectContentEquals(jsonObjFromData, jsonObjFromString);
    }

    static Stream<Arguments> talentDataJson() {

        BiFunction<Integer, Talent.Priority, String> jsonFormat = (talentIndex, talentPriority) -> String.format(
            """
            {
                "talent_index": %d,
                "priority": "%s"
            }""", talentIndex, talentPriority.name()
        );

        final var priorityValues = Talent.Priority.values();
        return Stream.generate(() -> {
            final var talentIndex = Random.Default.nextInt();
            final var priority = priorityValues[Random.Default.nextInt(priorityValues.length)];
            return Arguments.of(
                    new TalentData(talentIndex, priority),
                    jsonFormat.apply(talentIndex, priority)
            );
        }).limit(5);
    }

    @MethodSource("tfMaterialDataJson")
    @ParameterizedTest
    void deserializeTfMaterialData(TFMaterialData data, String json) throws Exception {
        assertEquals(data, TFMaterialData.SERIALIZER.fromJson(new JSONObject(json)));
    }

    @MethodSource("tfMaterialDataJson")
    @ParameterizedTest
    void serializeTfMaterialData(TFMaterialData data, String json) throws Exception {
        final var jsonObjFromData = TFMaterialData.SERIALIZER.toJson(data);
        final var jsonObjFromString = new JSONObject(json);
        assertJsonObjectContentEquals(jsonObjFromData, jsonObjFromString);
    }

    static Stream<Arguments> tfMaterialDataJson() {

        BiFunction<Integer, Integer, String> jsonFormat = (talentIndex, quantity) -> String.format(
                """
                {
                    "material_index": %d,
                    "quantity": %d
                }""", talentIndex, quantity
        );

        return Stream.generate(() -> {
            final var materialIndex = Random.Default.nextInt();
            final var quantity = Random.Default.nextInt();
            return Arguments.of(
                    new TFMaterialData(materialIndex, quantity),
                    jsonFormat.apply(materialIndex, quantity)
            );
        }).limit(5);
    }

    @MethodSource("unitBaseDataJson")
    @ParameterizedTest
    void deserializeUnitBaseData(UnitBaseData data, String json) throws Exception {
        assertEquals(data, UnitBaseData.SERIALIZER.fromJson(new JSONObject(json)));
    }

    @MethodSource("unitBaseDataJson")
    @ParameterizedTest
    void serializeUnitBaseData(UnitBaseData data, String json) throws Exception {
        final var jsonObjFromData = UnitBaseData.SERIALIZER.toJson(data);
        final var jsonObjFromString = new JSONObject(json);
        assertJsonObjectContentEquals(jsonObjFromData, jsonObjFromString);
    }

    static Stream<Arguments> unitBaseDataJson() {
        return Stream.of(
                Arguments.of(
                        new UnitBaseData(
                                0, UnitBaseData.Type.STORY_LEGEND,
                                null, null, null
                        ),
                        String.format("""
                        {
                            "unit_id": %d,
                            "type": "%s"
                        }""", 0, UnitBaseData.Type.STORY_LEGEND.name())
                ),
                Arguments.of(
                        new UnitBaseData(
                                1, UnitBaseData.Type.CF_SPECIAL,
                                "blah", null, null
                        ),
                        String.format("""
                        {
                            "unit_id": %d,
                            "type": "%s",
                            "misc_info_text": {
                                "%s": "%s"
                            }
                        }""", 1, UnitBaseData.Type.CF_SPECIAL.name(), UnitBaseData.Info.USEFUL_TO_OWN.name(), "blah")
                ),
                Arguments.of(
                        new UnitBaseData(
                                2, UnitBaseData.Type.RARE,
                                null, "foo", null
                        ),
                        String.format("""
                        {
                            "unit_id": %d,
                            "type": "%s",
                            "misc_info_text": {
                                "%s": "%s"
                            }
                        }""", 2, UnitBaseData.Type.RARE.name(), UnitBaseData.Info.USEFUL_TO_TF_OR_TALENT.name(), "foo")
                ),
                Arguments.of(
                        new UnitBaseData(
                                3, UnitBaseData.Type.SUPER_RARE,
                                null, null, "bar"
                        ),
                        String.format("""
                        {
                            "unit_id": %d,
                            "type": "%s",
                            "misc_info_text": {
                                "%s": "%s"
                            }
                        }""", 3, UnitBaseData.Type.SUPER_RARE.name(), UnitBaseData.Info.HYPERMAX_PRIORITY.name(), "bar")
                ),
                Arguments.of(
                        new UnitBaseData(
                                4, UnitBaseData.Type.UBER,
                                "foobar", "meh", null
                        ),
                        String.format("""
                        {
                            "unit_id": %d,
                            "type": "%s",
                            "misc_info_text": {
                                "%s": "%s",
                                "%s": "%s"
                            }
                        }""", 4, UnitBaseData.Type.UBER.name(),
                                UnitBaseData.Info.USEFUL_TO_OWN.name(), "foobar",
                                UnitBaseData.Info.USEFUL_TO_TF_OR_TALENT.name(), "meh")
                ),
                Arguments.of(
                        new UnitBaseData(
                                5, UnitBaseData.Type.LEGEND_RARE,
                                "foobar", null, "baz"
                        ),
                        String.format("""
                        {
                            "unit_id": %d,
                            "type": "%s",
                            "misc_info_text": {
                                "%s": "%s",
                                "%s": "%s"
                            }
                        }""", 5, UnitBaseData.Type.LEGEND_RARE.name(),
                                UnitBaseData.Info.USEFUL_TO_OWN.name(), "foobar",
                                UnitBaseData.Info.HYPERMAX_PRIORITY.name(), "baz")
                ),
                Arguments.of(
                        new UnitBaseData(
                                6, UnitBaseData.Type.ADVENT_DROP,
                                null, "-", "dmkfnkdv"
                        ),
                        String.format("""
                        {
                            "unit_id": %d,
                            "type": "%s",
                            "misc_info_text": {
                                "%s": "%s",
                                "%s": "%s"
                            }
                        }""", 6, UnitBaseData.Type.ADVENT_DROP.name(),
                                UnitBaseData.Info.USEFUL_TO_TF_OR_TALENT.name(), "-",
                                UnitBaseData.Info.HYPERMAX_PRIORITY.name(), "dmkfnkdv")
                ),
                Arguments.of(
                        new UnitBaseData(
                                7, UnitBaseData.Type.CF_SPECIAL,
                                "qwerty", "/////", "@"
                        ),
                        String.format("""
                        {
                            "unit_id": %d,
                            "type": "%s",
                            "misc_info_text": {
                                "%s": "%s",
                                "%s": "%s",
                                "%s": "%s"
                            }
                        }""", 7, UnitBaseData.Type.CF_SPECIAL.name(),
                                UnitBaseData.Info.USEFUL_TO_OWN.name(), "qwerty",
                                UnitBaseData.Info.USEFUL_TO_TF_OR_TALENT.name(), "/////",
                                UnitBaseData.Info.HYPERMAX_PRIORITY.name(), "@")
                )
        );
    }

    @MethodSource("unitEEPriorityDataJson")
    @ParameterizedTest
    void deserializeUnitEEPriorityData(UnitEEPriorityData data, String json) throws Exception {
        assertEquals(data, UnitEEPriorityData.SERIALIZER.fromJson(new JSONObject(json)));
    }

    @MethodSource("unitEEPriorityDataJson")
    @ParameterizedTest
    void serializeUnitEEPriorityData(UnitEEPriorityData data, String json) throws Exception {
        final var jsonObjFromData = UnitEEPriorityData.SERIALIZER.toJson(data);
        final var jsonObjFromString = new JSONObject(json);
        assertJsonObjectContentEquals(jsonObjFromData, jsonObjFromString);
    }

    static Stream<Arguments> unitEEPriorityDataJson() {
        Function<Tuple3<Integer, ElderEpic, String>, String> jsonFormat = tuple -> String.format(
                """
                {
                    "unit_id": %d,
                    "elder_epic": "%s",
                    "text": "%s"
                }""", tuple._1, tuple._2.name(), tuple._3
        );

        final var elderEpicValues = ElderEpic.values();
        return Stream.generate(() -> {
            final var unitId = Random.Default.nextInt();
            final var elderEpic = elderEpicValues[Random.Default.nextInt(elderEpicValues.length)];
            final var text = new String(Random.Default.nextBytes(3));
            return Arguments.of(
                    new UnitEEPriorityData(unitId, elderEpic, text),
                    jsonFormat.apply(Tuple.of(unitId, elderEpic, text))
            );
        }).limit(5);
    }

    @MethodSource("unitHypermaxDataJson")
    @ParameterizedTest
    void deserializeUnitHypermaxData(UnitHypermaxData data, String json) throws Exception {
        assertEquals(data, UnitHypermaxData.SERIALIZER.fromJson(new JSONObject(json)));
    }

    @MethodSource("unitHypermaxDataJson")
    @ParameterizedTest
    void serializeUnitHypermaxData(UnitHypermaxData data, String json) throws Exception {
        final var jsonObjFromData = UnitHypermaxData.SERIALIZER.toJson(data);
        final var jsonObjFromString = new JSONObject(json);
        assertJsonObjectContentEquals(jsonObjFromData, jsonObjFromString);
    }

    static Stream<Arguments> unitHypermaxDataJson() {
        Function<Tuple3<Integer, Hypermax.Priority, Hypermax.UnitType>, String> jsonFormat = tuple -> String.format(
                """
                {
                    "unit_id": %d,
                    "priority": "%s",
                    "type": "%s"
                }""", tuple._1, tuple._2.name(), tuple._3.name()
        );

        final var priorityValues = Hypermax.Priority.values();
        final var typeValues = Hypermax.UnitType.values();
        return Stream.generate(() -> {
            final var unitId = Random.Default.nextInt();
            final var priority = priorityValues[Random.Default.nextInt(priorityValues.length)];
            final var type = typeValues[Random.Default.nextInt(typeValues.length)];
            return Arguments.of(
                    new UnitHypermaxData(unitId, priority, type),
                    jsonFormat.apply(Tuple.of(unitId, priority, type))
            );
        }).limit(5);
    }

    @MethodSource("unitTalentDataJson")
    @ParameterizedTest
    void deserializeUnitTalentData(UnitTalentData data, String json) throws Exception {
        assertEquals(data, UnitTalentData.SERIALIZER.fromJson(new JSONObject(json)));
    }

    @MethodSource("unitTalentDataJson")
    @ParameterizedTest
    void serializeUnitTalentData(UnitTalentData data, String json) throws Exception {
        final var jsonObjFromData = UnitTalentData.SERIALIZER.toJson(data);
        final var jsonObjFromString = new JSONObject(json);
        assertJsonObjectContentEquals(jsonObjFromData, jsonObjFromString);
    }

    static Stream<Arguments> unitTalentDataJson() {
        Function<Tuple3<Integer, Talent.UnitType, List<TalentData>>, String> jsonFormat = tuple -> String.format(
                """
                {
                    "unit_id": %d,
                    "unit_type": "%s",
                    "talents": %s
                }""", tuple._1, tuple._2.name(), TalentData.SERIALIZER.listToJson(tuple._3)
        );

        final var typeValues = Talent.UnitType.values();
        final var priorityValues = Talent.Priority.values();
        return Stream.generate(() -> {
            final var unitId = Random.Default.nextInt();
            final var type = typeValues[Random.Default.nextInt(typeValues.length)];
            final var listSize = Random.Default.nextInt(6);
            final var talentDatas = new ArrayList<TalentData>(listSize);
            for (int i = 0; i < listSize; i++) {
                final var talentIndex = Random.Default.nextInt();
                final var priority = priorityValues[Random.Default.nextInt(priorityValues.length)];
                talentDatas.add(new TalentData(talentIndex, priority));
            }
            return Arguments.of(
                    new UnitTalentData(unitId, type, talentDatas),
                    jsonFormat.apply(Tuple.of(unitId, type, talentDatas))
            );
        }).limit(5);
    }

    @MethodSource("unitTfMaterialDataJson")
    @ParameterizedTest
    void deserializeUnitTfMaterialData(UnitTFMaterialData data, String json) throws Exception {
        assertEquals(data, UnitTFMaterialData.SERIALIZER.fromJson(new JSONObject(json)));
    }

    @MethodSource("unitTfMaterialDataJson")
    @ParameterizedTest
    void serializeUnitTfMaterialData(UnitTFMaterialData data, String json) throws Exception {
        final var jsonObjFromData = UnitTFMaterialData.SERIALIZER.toJson(data);
        final var jsonObjFromString = new JSONObject(json);
        assertJsonObjectContentEquals(jsonObjFromData, jsonObjFromString);
    }

    static Stream<Arguments> unitTfMaterialDataJson() {
        BiFunction<Integer, List<TFMaterialData>, String> jsonFormat = (unitId, datas) -> String.format(
                """
                {
                    "unit_id": %d,
                    "materials": %s
                }""", unitId, TFMaterialData.SERIALIZER.listToJson(datas)
        );

        return Stream.generate(() -> {
            final var unitId = Random.Default.nextInt();
            final var listSize = Random.Default.nextInt(6);
            final var materialDatas = new ArrayList<TFMaterialData>(listSize);
            for (int i = 0; i < listSize; i++) {
                final var materialIndex = Random.Default.nextInt();
                final var quantity = Random.Default.nextInt();
                materialDatas.add(new TFMaterialData(materialIndex, quantity));
            }
            return Arguments.of(
                    new UnitTFMaterialData(unitId, materialDatas),
                    jsonFormat.apply(unitId, materialDatas)
            );
        }).limit(5);
    }

}
