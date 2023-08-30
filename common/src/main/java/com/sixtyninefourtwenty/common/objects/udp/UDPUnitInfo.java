package com.sixtyninefourtwenty.common.objects.udp;

import androidx.annotation.Nullable;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import io.vavr.Tuple2;
import lombok.Value;

@Value
@NonNullTypesByDefault
public class UDPUnitInfo {

    String name;
    Tuple2<String, String> description;
    String rarity;
    @Nullable
    Comments comments;

    @NonNullTypesByDefault
    public sealed interface Comments {
        String getOwn();
    }

    @Value
    @NonNullTypesByDefault
    public static final class OwnAndTFComments implements Comments {
        String own;
        String tf;
    }

    @Value
    @NonNullTypesByDefault
    public static final class OwnAndTalentComments implements Comments {
        String own;
        String talent;
    }

}
