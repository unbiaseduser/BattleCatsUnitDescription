@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    id("androidx.navigation.safeargs")
    alias(libs.plugins.androidJunit5)
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sixtyninefourtwenty.bcud"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }

        release {
            isMinifyEnabled = false
            proguardFiles += getDefaultProguardFile("proguard-android-optimize.txt")
            proguardFiles += file("proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    namespace = "com.sixtyninefourtwenty.bcud"

}

dependencies {

    implementation(project(":common"))
    implementation(project(":custom-action-mode"))
    implementation(project(":material-spinner"))
    implementation(project(":stuff"))
    implementation(project(":stuff-java"))
    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview.selection)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.browser)
    implementation(libs.fastscroll)
    implementation(libs.commonmark)
    implementation(libs.commonmark.ext.gfm.strikethrough)
    implementation(libs.commonmark.ext.gfm.tables)
    configurations.all {
        exclude(group = "com.atlassian.commonmark") //exclude the old commonmark version pulled in by markwon to manually use the latest version
    }
    implementation (libs.markwon.core)
    implementation (libs.markwon.html)
    implementation (libs.markwon.image)
    implementation (libs.markwon.image.coil)
    implementation (libs.markwon.recycler)
    implementation (libs.markwon.recycler.table)

    implementation(libs.preference)
    implementation(libs.work.runtime)
    implementation(libs.fulldraggabledrawer)
    implementation(libs.photoeditor)
    implementation(libs.colorpicker)
    implementation(libs.betterlinkmovementmethod)

    implementation(libs.room.runtime)
    implementation(libs.room.guava)
    annotationProcessor(libs.room.compiler)

    implementation(libs.guava)
    implementation(libs.fastutil)
    implementation(libs.coil)
    implementation(libs.balloon)
    implementation(libs.durationhumanizer)
    implementation(libs.swipetoactionlayout)
    implementation(libs.expandablelayout)
    implementation(libs.multistateview)
    implementation(libs.switchicon)
    implementation(libs.circularimageview)
    implementation(libs.flowlayout)
    implementation(libs.materialpopupmenuplus)
    implementation(libs.materialbanner)
    implementation(libs.materialaboutlibraryplus)
    implementation(libs.vavr)
    implementation(libs.faststringutils)
    implementation(libs.custompreferencesthemingintegration)
    implementation(libs.themingpreferenceintegration)
    implementation(libs.bottomsheetalertdialog)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.json)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.vintage.engine)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.awaitility)
    androidTestImplementation(libs.junit.jupiter.api)
    androidTestRuntimeOnly(libs.junit.jupiter.engine)
    androidTestImplementation(libs.android.junit5.test.core)
    androidTestRuntimeOnly(libs.android.junit5.test.runner)
    debugImplementation(libs.fragment.testing)

}