package com.sixtyninefourtwenty.common.objects.repository;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Talent;

import java.util.List;

@NonNullTypesByDefault
public interface TalentSupplier {

    List<Talent> getTalents();

    default Talent getTalentByIndex(int talentIndex) {
        return getTalents().stream()
                .filter(talent -> talent.getIndex() == talentIndex)
                .findFirst()
                .orElseThrow();
    }

}
