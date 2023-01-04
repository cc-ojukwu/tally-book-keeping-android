plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        applicationId = Config.applicationId
        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = Config.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        dataBinding = true
        compose = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(1.8)
        targetCompatibility(1.8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    tasks.withType().all {
        kotlinOptions {
            jvmTarget = Config.javaVersion.toString()
        }
    }
}

dependencies {

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(Kotlin.stdlib)
    implementation(View.constraintLayout)

    // Material Design
    implementation(View.material)
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("com.google.android.gms:play-services-auth:20.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //implementation ("com.google.code.gson:gson:2.10")
    //implementation ("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.3.1")

    // AndroidX Test - JVM testing
    testImplementation(AndroidX.testExt)
    testImplementation(AndroidX.coreKtxTest)
    testImplementation(AndroidX.archCoreTesting)
    testImplementation(UnitTest.junit)
    testImplementation(UnitTest.roboelectric)
    testImplementation(UnitTest.hamcrest)
    testImplementation(Kotlin.coroutineTest)
    testImplementation(UnitTest.mockitoCore)

    // AndroidX Test - Instrumented testing
    androidTestImplementation(UnitTest.mockitoCore)
    androidTestImplementation(AndroidX.testExt)
    androidTestImplementation(AndroidTest.espresso)
    androidTestImplementation(AndroidTest.espressoContrib)
    androidTestImplementation(AndroidTest.espressoIntent)
    androidTestImplementation(AndroidX.archCoreTesting)
    androidTestImplementation(AndroidX.coreKtxTest)
    androidTestImplementation(AndroidX.testRules)
    androidTestImplementation(Kotlin.coroutineTest)

    // Dagger-Hilt
    implementation(Dagger.daggerHilt)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")
    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${rootProject.extra["compose_version"]}")
    kapt(Dagger.hiltCompiler)

    // Retrofit
    implementation(Network.retrofit)
    implementation(Network.moshiConverter)
    implementation(Network.okhttp)
    implementation(Network.moshiKotlin)
    implementation(Network.moshi)
    kapt ("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    // Lifecycle KTX
    implementation(AndroidX.viewModel)
    implementation(AndroidX.lifeCycleCommon)
    implementation(AndroidX.liveData)

    // Navigation Components
    implementation(Navigation.navigationFragment)
    implementation(Navigation.navigationUi)

    // Kotlin Coroutines
    implementation(Kotlin.coroutinesAndroid)

    //Datastore
    implementation(Utils.datastore)

    //Splashscreen
    implementation(View.splashScreen)

    // Room
    implementation(Database.roomRuntime)
    kapt(Database.roomCompiler)
    implementation(Database.roomKtx)

    // Preferences
    implementation(AndroidX.preferences)

    // Timber
    implementation(Utils.timber)

    // Paging Library
    implementation(AndroidX.paging)

    // WorkManager
    implementation(AndroidX.workManager)


}