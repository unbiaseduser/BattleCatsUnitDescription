// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidJunit5) apply false
    id("io.freefair.lombok") version "6.6.1"
}
true // Needed to make the Suppress annotation work for the plugins block

subprojects {
    apply(plugin = "de.mannodermaus.android-junit5")
}

buildscript {
    dependencies {
        classpath(libs.navigation.safeargs)
    }
}
