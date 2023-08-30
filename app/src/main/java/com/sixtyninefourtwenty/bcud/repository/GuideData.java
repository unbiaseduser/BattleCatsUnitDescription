package com.sixtyninefourtwenty.bcud.repository;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Guide;

public interface GuideData {
    ImmutableList<Guide> getGuides();
    ImmutableList<Guide> getHelpPins();
}
