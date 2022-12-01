// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Plugins.kotlinGradlePlugin)
        classpath(Plugins.gradleAndroid)
        classpath(Dagger.hiltGradlePlugin)
        classpath(Plugins.safeArgs)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id ("org.jlleitschuh.gradle.ktlint") version ("10.2.1")
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}


