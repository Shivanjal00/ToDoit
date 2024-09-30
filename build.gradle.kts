buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath (libs.jetbrains.kotlin.gradle.plugin) // Update to latest stable version
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}