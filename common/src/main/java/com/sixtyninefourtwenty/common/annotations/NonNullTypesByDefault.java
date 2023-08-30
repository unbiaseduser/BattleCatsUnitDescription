package com.sixtyninefourtwenty.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR})
@TypeQualifierDefault({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Nonnull
@Documented
public @interface NonNullTypesByDefault { }
