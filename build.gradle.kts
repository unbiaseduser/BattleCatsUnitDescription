// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    id("io.freefair.lombok") version "6.6.1"
    alias(libs.plugins.room) apply false
}

buildscript {
    dependencies {
        classpath(libs.navigation.safeargs)
    }
}
