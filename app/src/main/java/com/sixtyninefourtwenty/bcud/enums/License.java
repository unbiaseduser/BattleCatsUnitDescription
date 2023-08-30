package com.sixtyninefourtwenty.bcud.enums;

import android.content.Context;
import android.widget.ScrollView;

import androidx.annotation.StringRes;

import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.markwonplugins.CenterTagHandler;
import com.sixtyninefourtwenty.bcud.utils.HasAssetMarkdownFile;
import com.sixtyninefourtwenty.bcud.utils.builders.MarkwonBuilderWrapper;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import io.noties.markwon.Markwon;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@NonNullTypesByDefault
public enum License implements HasAssetMarkdownFile {
    APACHE2(R.string.apache_2, "https://www.apache.org/licenses/LICENSE-2.0",
            "text/licenses/apache2.html", true),
    GPL2_CE(R.string.gpl2_ce, "https://openjdk.org/legal/gplv2+ce.html",
            "text/licenses/gpl2_ce.html", true),
    GPL3(R.string.gpl_3, "https://www.gnu.org/licenses/gpl-3.0.html",
            "text/licenses/gpl3.html", true),
    MIT(R.string.mit, "https://spdx.org/licenses/MIT.html",
            "text/licenses/mit.txt", false),
    UNLICENSE(R.string.unlicense, "https://unlicense.org",
            "text/licenses/unlicense.txt", false);
    @StringRes
    private final int licenseName;
    private final String licenseUrl;
    @Getter(AccessLevel.NONE)
    private final String licenseFilePath;
    private final boolean isLicenseLong;

    @Override
    public String getFilePath() {
        return licenseFilePath;
    }

    @Override
    public Markwon createMarkwon(Context context, ScrollView sv) {
        return new MarkwonBuilderWrapper(context).html(new CenterTagHandler()).build();
    }
}
