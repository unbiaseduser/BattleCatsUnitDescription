package com.sixtyninefourtwenty.bcud.utils.builders;

import android.content.Context;
import android.widget.ScrollView;

import com.sixtyninefourtwenty.bcud.markwonplugins.AnchorHeadingPlugin;
import com.sixtyninefourtwenty.bcud.markwonplugins.TableOfContentsPlugin;
import com.sixtyninefourtwenty.bcud.utils.AppPreferences;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.html.TagHandler;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.image.coil.CoilImagesPlugin;
import io.noties.markwon.image.file.FileSchemeHandler;

/**
 * Wrapper for {@link Markwon.Builder} that's less annoying to work with.
 */
@NonNullTypesByDefault
public final class MarkwonBuilderWrapper {
    private final Context context;
    private final Markwon.Builder builder;

    public MarkwonBuilderWrapper(Context context) {
        this.context = context;
        builder = Markwon.builder(context);
    }

    public MarkwonBuilderWrapper images() {
        builder.usePlugin(ImagesPlugin.create(plugin -> plugin.addSchemeHandler(FileSchemeHandler.createWithAssets(context))))
                .usePlugin(CoilImagesPlugin.create(context));
        return this;
    }

    public MarkwonBuilderWrapper html() {
        builder.usePlugin(HtmlPlugin.create());
        return this;
    }

    public MarkwonBuilderWrapper html(TagHandler tagHandler) {
        builder.usePlugin(HtmlPlugin.create(plugin -> plugin.addHandler(tagHandler)));
        return this;
    }

    public MarkwonBuilderWrapper tables() {
        builder.usePlugin(TablePlugin.create(context));
        return this;
    }

    public MarkwonBuilderWrapper anchor(ScrollView scrollView) {
        builder.usePlugin(new AnchorHeadingPlugin((view, top) -> scrollView.smoothScrollTo(0, top)));
        if (AppPreferences.get(context).getUseToc()) {
            builder.usePlugin(new TableOfContentsPlugin(context));
        }
        return this;
    }

    public Markwon build() {
        return builder.build();
    }
}
