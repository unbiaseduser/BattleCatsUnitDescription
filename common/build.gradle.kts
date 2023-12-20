plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.androidJunit5)
}

android {
    namespace = "com.sixtyninefourtwenty.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {

    implementation(project(":stuff"))
    implementation(project(":stuff-java"))
    implementation(libs.appcompat)
    implementation(libs.material)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation(libs.guava)
    implementation(libs.vavr)
    implementation(libs.faststringutils)

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
    androidTestImplementation(libs.junit.jupiter.api)
    androidTestRuntimeOnly(libs.junit.jupiter.engine)
    androidTestImplementation(libs.android.junit5.test.core)
    androidTestRuntimeOnly(libs.android.junit5.test.runner)
}