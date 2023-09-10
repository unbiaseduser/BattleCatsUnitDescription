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
include(":conflict-resolver")
project(":conflict-resolver").projectDir = File("conflict-resolver/conflict-resolver")
include(":custom-action-mode")
project(":custom-action-mode").projectDir = File("custom-action-mode/custom-action-mode")
include(":material-spinner")
project(":material-spinner").projectDir = File("material-spinner/material-spinner")