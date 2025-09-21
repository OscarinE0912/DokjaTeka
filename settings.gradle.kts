pluginManagement {
    plugins {
        id("com.android.application") version "8.1.2"
        id("org.jetbrains.kotlin.android") version "1.9.10"
        id("com.google.gms.google-services") version "4.3.15"
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GestorDeArchivos2"
include(":app")
