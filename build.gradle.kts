// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10" apply false

}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.webrtc.org") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.2")
        classpath("com.google.gms:google-services:4.4.4")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.52")
    }
}