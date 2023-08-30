package com.sixtyninefourtwenty.bcud.repository;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.AdventBoss;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public interface AdventData {
    ImmutableList<AdventBoss> getBosses();
}
