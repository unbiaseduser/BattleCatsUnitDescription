package com.sixtyninefourtwenty.bcud.utils;

import android.view.MotionEvent;

import org.checkerframework.checker.nullness.qual.Nullable;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.ViewType;

@FunctionalInterface
public interface MyOnPhotoEditorListener extends OnPhotoEditorListener {
    @Override
    default void onAddViewListener(@Nullable ViewType viewType, int i) {}

    @Override
    default void onRemoveViewListener(@Nullable ViewType viewType, int i) {}

    @Override
    default void onStartViewChangeListener(@Nullable ViewType viewType) {}

    @Override
    default void onStopViewChangeListener(@Nullable ViewType viewType) {}

    @Override
    default void onTouchSourceImage(@Nullable MotionEvent motionEvent) {}
}
