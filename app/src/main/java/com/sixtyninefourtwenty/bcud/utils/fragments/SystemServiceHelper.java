package com.sixtyninefourtwenty.bcud.utils.fragments;

import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStatsManager;
import android.content.ClipboardManager;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public sealed interface SystemServiceHelper permits BaseBottomSheetAlertDialogFragment, BaseBottomSheetDialogFragment, BaseDialogFragment, BaseFragment {
    ClipboardManager getClipboardManager();
    UsageStatsManager getUsageStatsManager();
    StorageStatsManager getStorageStatsManager();
}
