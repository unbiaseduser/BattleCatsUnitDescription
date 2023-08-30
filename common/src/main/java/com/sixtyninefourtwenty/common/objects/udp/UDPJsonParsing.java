package com.sixtyninefourtwenty.common.objects.udp;

import com.google.common.collect.ImmutableMap;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.json.JSONObject;

import io.vavr.Tuple;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class UDPJsonParsing {

    private UDPJsonParsing() {}

    @SneakyThrows
    public static ImmutableMap<Integer, UDPUnitInfo> parseUDPJson(String json) {
        final var result = new ImmutableMap.Builder<Integer, UDPUnitInfo>();
        final var obj = new JSONObject(json);
        final var keys = obj.keys();
        while (keys.hasNext()) {
            final var key = keys.next();
            final var innerObj = obj.getJSONObject(key);
            final var unitId = Integer.parseInt(key);
            UDPUnitInfo.Comments comments = null;
            final var commentsJson = innerObj.optJSONObject("Comments");
            if (commentsJson != null) {
                if (commentsJson.has("tf")) {
                    comments = new UDPUnitInfo.OwnAndTFComments(commentsJson.getString("own"), commentsJson.getString("tf"));
                } else if (commentsJson.has("talent")) {
                    comments = new UDPUnitInfo.OwnAndTalentComments(commentsJson.getString("own"), commentsJson.getString("talent"));
                }
            }
            result.put(unitId, new UDPUnitInfo(
                    innerObj.getString("Name"),
                    Tuple.of(innerObj.getJSONArray("Description").getString(0), innerObj.getJSONArray("Description").getString(1)),
                    innerObj.getString("Rarity"),
                    comments
            ));
        }
        return result.build();
    }
}
