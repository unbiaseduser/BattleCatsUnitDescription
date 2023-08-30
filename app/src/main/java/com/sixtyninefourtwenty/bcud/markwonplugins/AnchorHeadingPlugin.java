package com.sixtyninefourtwenty.bcud.markwonplugins;

import android.text.Spannable;
import android.view.View;
import android.widget.TextView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.ObjIntConsumer;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.LinkResolverDef;
import io.noties.markwon.MarkwonConfiguration;
import io.noties.markwon.core.spans.HeadingSpan;
import lombok.AllArgsConstructor;

/**
 * Taken from Markwon sample app.
 */
@AllArgsConstructor
public final class AnchorHeadingPlugin extends AbstractMarkwonPlugin {

    private final ObjIntConsumer<TextView> scrollTo;

    @Override
    public void configureConfiguration(MarkwonConfiguration.@NonNull Builder builder) {
        builder.linkResolver(new AnchorHeadingPlugin.AnchorLinkResolver(scrollTo));
    }

    @Override
    public void afterSetText(@NonNull TextView textView) {
        final var spannable = (Spannable) textView.getText();
        // obtain heading spans
        final var spans = spannable.getSpans(0, spannable.length(), HeadingSpan.class);
        for (final var span : spans) {
            final int start = spannable.getSpanStart(span);
            final int end = spannable.getSpanEnd(span);
            final int flags = spannable.getSpanFlags(span);
            spannable.setSpan(
                    new AnchorHeadingPlugin.AnchorSpan(createAnchor(spannable.subSequence(start, end))),
                    start,
                    end,
                    flags
            );
        }
    }

    @AllArgsConstructor
    private static final class AnchorLinkResolver extends LinkResolverDef {

        private final ObjIntConsumer<TextView> scrollTo;

        @Override
        public void resolve(@NonNull View view, @NonNull String link) {
            if (link.startsWith("#")) {
                final var textView = (TextView) view;
                final var spanned = (Spannable) textView.getText();
                final var spans = spanned.getSpans(0, spanned.length(), AnchorHeadingPlugin.AnchorSpan.class);
                final var anchor = link.substring(1);
                for (var span : spans) {
                    if (anchor.equals(span.anchor)) {
                        final int start = spanned.getSpanStart(span);
                        final int line = textView.getLayout().getLineForOffset(start);
                        final int top = textView.getLayout().getLineTop(line);
                        scrollTo.accept(textView, top);
                        return;
                    }
                }
            }
            super.resolve(view, link);
        }
    }

    @AllArgsConstructor
    private static final class AnchorSpan {
        final String anchor;
    }

    @NonNull
    public static String createAnchor(@NonNull CharSequence content) {
        return content.toString()
                .replaceAll("[^\\w]", "")
                .toLowerCase();
    }
}
