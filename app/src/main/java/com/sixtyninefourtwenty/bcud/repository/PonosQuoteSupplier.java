package com.sixtyninefourtwenty.bcud.repository;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public interface PonosQuoteSupplier {
    ImmutableList<String> getQuotes();
}
