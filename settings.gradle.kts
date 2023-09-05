pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "Battle Cats Unit Description"
include (":app")
include(":data-generator")
include(":common")
include(":bottom-sheet-alert-dialog")
project(":bottom-sheet-alert-dialog").projectDir = File("bottom-sheet-alert-dialog/bottom-sheet-alert-dialog")
