package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.talentpriority;

import com.sixtyninefourtwenty.common.objects.Talent;

public final class TalentPriorityTabNonUberFragment extends TalentPriorityTabFragment {
    @Override
    protected Talent.UnitType getTalentPriorityType() {
        return Talent.UnitType.NON_UBER;
    }

}