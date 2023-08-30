package com.sixtyninefourtwenty.bcud.objects;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import com.sixtyninefourtwenty.bcud.enums.License;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import lombok.Value;

@Value
@NonNullTypesByDefault
@Immutable
public class ThirdPartyLibInfo {
    String name;
    Dev dev;
    License license;
    ImmutableList<String> websites;

    @Value
    @NonNullTypesByDefault
    @Immutable
    public static class Dev {
        String name;
        String website;
    }

}
