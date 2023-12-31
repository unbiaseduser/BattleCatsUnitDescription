package com.sixtyninefourtwenty.bcud.utils.fragments;

import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStatsManager;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.sixtyninefourtwenty.bcud.ui.activities.MainActivity;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.stuff.Snackbars;
import com.sixtyninefourtwenty.stuff.SystemServices;
import com.sixtyninefourtwenty.stuff.Toasts;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@NonNullTypesByDefault
public abstract non-sealed class BaseBottomSheetDialogFragment<V extends View> extends BottomSheetDialogFragment implements NavigationHelper, SystemServiceHelper, OnScreenMsgHelper, MiscHelper {

    @Nullable
    private V view;

    protected abstract V initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    @NonNull
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater, container);
        return view;
    }

    protected abstract void setup(V view, @Nullable Bundle savedInstanceState);

    @Override
    public void onViewCreated(@NonNull View __, @Nullable Bundle savedInstanceState) {
        assert view != null;
        setup(view, savedInstanceState);
    }

    protected void onCleanup(V view) {
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        assert view != null;
        onCleanup(view);
        view = null;
    }

    //region helpers

    @Override
    public final NavController getNavController() {
        return NavHostFragment.findNavController(this);
    }

    @Override
    public final ClipboardManager getClipboardManager() {
        return SystemServices.getClipboardManager(requireContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public final UsageStatsManager getUsageStatsManager() {
        return SystemServices.getUsageStatsManager(requireContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public final StorageStatsManager getStorageStatsManager() {
        return SystemServices.getStorageStatsManager(requireContext());
    }

    @Override
    public final Toast makeToast(int textRes, int duration) {
        return com.sixtyninefourtwenty.stuff.Toasts.makeToast(requireContext(), textRes, duration);
    }

    @Override
    public final Toast makeToast(CharSequence text, int duration) {
        return Toasts.makeToast(requireContext(), text, duration);
    }

    @Override
    public final Snackbar makeSnackbar(int textRes, int duration) {
        if (requireActivity() instanceof MainActivity ma) {
            return com.sixtyninefourtwenty.stuff.Snackbars.makeSnackbar(ma.getRootView(), textRes, duration);
        } else {
            assert view != null;
            return com.sixtyninefourtwenty.stuff.Snackbars.makeSnackbar(view, textRes, duration);
        }
    }

    @Override
    public final Snackbar makeSnackbar(CharSequence text, int duration) {
        if (requireActivity() instanceof MainActivity ma) {
            return com.sixtyninefourtwenty.stuff.Snackbars.makeSnackbar(ma.getRootView(), text, duration);
        } else {
            assert view != null;
            return Snackbars.makeSnackbar(view, text, duration);
        }
    }

    @Override
    public final void setToolbarTitle(CharSequence text) {
        if (requireActivity() instanceof MainActivity ma) {
            ma.getToolbar().setTitle(text);
        }
    }

    @Override
    public final void openWebsite(String url) {
        final var context = requireContext();
        Utils.openWebsite(context, url);
    }

    //endregion
}
