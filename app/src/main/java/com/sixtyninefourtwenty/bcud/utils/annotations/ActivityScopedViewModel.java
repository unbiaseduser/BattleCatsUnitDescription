package com.sixtyninefourtwenty.bcud.utils.annotations;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that this {@link ViewModel} should be scoped to an {@link Activity} (rather than a {@link Fragment}, or anything else i haven't heard of).
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ActivityScopedViewModel {
    String reason();
}
