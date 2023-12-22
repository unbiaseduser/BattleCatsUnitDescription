package com.sixtyninefourtwenty.bcud.enums;

import androidx.annotation.StringRes;

import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@NonNullTypesByDefault
public enum License {
    APACHE2(R.string.apache_2, "https://www.apache.org/licenses/LICENSE-2.0"),
    GPL2_CE(R.string.gpl2_ce, "https://openjdk.org/legal/gplv2+ce.html"),
    GPL3(R.string.gpl_3, "https://www.gnu.org/licenses/gpl-3.0.html"),
    MIT(R.string.mit, "https://spdx.org/licenses/MIT.html"),
    UNLICENSE(R.string.unlicense, "https://unlicense.org");
    @StringRes
    private final int licenseName;
    private final String licenseUrl;
}
