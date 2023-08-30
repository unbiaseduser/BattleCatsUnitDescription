package com.sixtyninefourtwenty.bcud.utils;

import android.view.View;

import org.checkerframework.checker.nullness.qual.NonNull;

import github.com.st235.swipetoactionlayout.SwipeAction;
import github.com.st235.swipetoactionlayout.SwipeMenuListener;

@FunctionalInterface
public interface MySwipeMenuListener extends SwipeMenuListener {

    @Override
    default void onClosed(@NonNull View view) {}

    @Override
    default void onOpened(@NonNull View view) {}

    @Override
    default void onFullyOpened(@NonNull View view, @NonNull SwipeAction swipeAction) {}

}
