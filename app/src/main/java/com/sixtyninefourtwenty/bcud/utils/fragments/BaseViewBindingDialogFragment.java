package com.sixtyninefourtwenty.bcud.utils.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@NonNullTypesByDefault
public abstract class BaseViewBindingDialogFragment<VB extends ViewBinding> extends BaseDialogFragment<View> {
    @Nullable
    private VB binding;

    @NonNull
    protected abstract VB initBinding(@NonNull LayoutInflater inflater);

    @Override
    protected final @NonNull View initView(@NonNull LayoutInflater inflater) {
        binding = initBinding(inflater);
        return binding.getRoot();
    }

    @NonNull
    protected abstract Dialog initDialog(@NonNull VB binding, @Nullable Bundle savedInstanceState);

    @NonNull
    @Override
    protected final Dialog initDialog(@NonNull View view, @Nullable Bundle savedInstanceState) {
        assert binding != null;
        return initDialog(binding, savedInstanceState);
    }

    protected void onCleanup(@NonNull VB binding) {}

    @Override
    protected final void onCleanup(@NonNull View view) {
        super.onCleanup(view);
        assert binding != null;
        onCleanup(binding);
        binding = null;
    }
}
