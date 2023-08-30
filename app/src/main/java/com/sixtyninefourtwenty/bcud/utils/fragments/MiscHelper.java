package com.sixtyninefourtwenty.bcud.utils.fragments;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public sealed interface MiscHelper permits BaseBottomSheetAlertDialogFragment, BaseBottomSheetDialogFragment, BaseDialogFragment, BaseFragment {
    void setToolbarTitle(CharSequence text);
    void openWebsite(String url);
}
