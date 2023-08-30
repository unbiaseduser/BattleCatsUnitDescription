package com.sixtyninefourtwenty.bcud.markwonplugins;

import android.text.Layout;
import android.text.style.AlignmentSpan;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

import io.noties.markwon.MarkwonConfiguration;
import io.noties.markwon.RenderProps;
import io.noties.markwon.html.HtmlTag;
import io.noties.markwon.html.tag.SimpleTagHandler;

public final class CenterTagHandler extends SimpleTagHandler {

    @NonNull
    @Override
    public Object getSpans(@NonNull MarkwonConfiguration configuration, @NonNull RenderProps renderProps, @NonNull HtmlTag tag) {
        return new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
    }

    @NonNull
    @Override
    public Collection<String> supportedTags() {
        return Set.of("center");
    }

}
