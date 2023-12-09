package com.sixtyninefourtwenty.bcud.utils.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@NonNullTypesByDefault
public abstract class BaseViewBindingFragment<VB extends ViewBinding> extends BaseFragment<View> {
    @Nullable
    private VB binding;

    @NonNull
    protected abstract VB initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    @Override
    protected final @NonNull View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = initBinding(inflater, container);
        return binding.getRoot();
    }

    protected abstract void setup(@NonNull VB binding, @Nullable Bundle savedInstanceState);

    @Override
    protected final void setup(@NonNull View view, @Nullable Bundle savedInstanceState) {
        assert binding != null;
        setup(binding, savedInstanceState);
    }

    @SuppressWarnings("unused")
    protected void onCleanup(@NonNull VB binding) {}

    @Override
    protected final void onCleanup(@NonNull View view) {
        super.onCleanup(view);
        assert binding != null;
        onCleanup(binding);
        binding = null;
    }
}
