package com.sixtyninefourtwenty.common.objects.repository;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Talent;

@NonNullTypesByDefault
public interface TalentInfoSupplier {

    Talent.Info getInfo(int talentIndex);

}
