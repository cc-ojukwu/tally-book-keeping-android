import org.gradle.api.JavaVersion

/**
 * Created by Christopher Ojukwu on 06/10/2022.
 */

object Config {
    const val minSdkVersion = 26
    const val compileSdkVersion = 33
    const val targetSdkVersion = 33
    const val versionName = "1.0"
    const val versionCode = 1
    val javaVersion = JavaVersion.VERSION_1_8
    const val applicationId = "com.chrisojukwu.tallybookkeeping"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object Plugins {
    object Version {
        const val gradleAndroidVersion = "7.3.1"
        const val googleServices = "4.3.5"
    }

    const val gradleAndroid = "com.android.tools.build:gradle:${Version.gradleAndroidVersion}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.Versions.kotlin}"
    const val safeArgs =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Navigation.Versions.navigation}"
}

object Kotlin {

    object Versions {
        const val kotlin = "1.7.10"
        const val coroutines = "1.4.2"
    }

    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutineTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
}

/**Helps handle group of libraries**/
interface Libraries {
    val components: List<String>
}

object AndroidX : Libraries {
    private object Versions {
        const val androidx_core = "1.7.0"
        const val appCompat = "1.5.0"
        const val lifeCycle = "2.5.1"
        const val preferences = "1.1.1"
        const val work = "2.7.1"
        const val paging = "3.1.1"
        const val fragment = "1.3.0-alpha06"

        const val archCoreTesting = "2.1.0"
        const val coreKtxTest = "1.3.0"
        const val testExt = "1.1.2"
        const val testRules = "1.3.0"
    }

    const val coreKtx = "androidx.core:core-ktx:${Versions.androidx_core}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifeCycle}"
    const val viewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifeCycle}"
    const val lifeCycleCommon =
        "androidx.lifecycle:lifecycle-common-java8:${Versions.lifeCycle}"
    const val preferences = "androidx.preference:preference-ktx:${Versions.preferences}"

    const val workManager = "androidx.work:work-runtime-ktx:${Versions.work}"
    const val paging = "androidx.paging:paging-runtime-ktx:${Versions.paging}"

    const val archCoreTesting = "androidx.arch.core:core-testing:${Versions.archCoreTesting}"
    const val coreKtxTest = "androidx.test:core-ktx:${Versions.coreKtxTest}"
    const val testExt = "androidx.test.ext:junit-ktx:${Versions.testExt}"
    const val testRules = "androidx.test:rules:${Versions.testRules}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragment}"

    override val components: List<String>
        get() = listOf(coreKtx, viewModel, appCompat, lifeCycleCommon, liveData, preferences)

}

object Dagger : Libraries {

    private object Versions {
        const val hilt = "2.44"
    }

    const val daggerHilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"

    override val components: List<String> = listOf(daggerHilt, hiltCompiler)
}

object Network : Libraries {

    private object Versions {
        const val retrofit = "2.9.0"
        const val okhttp = "4.9.3"
        const val moshi = "1.14.0"
    }

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    //const val okhttpInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"


    override val components: List<String>
        get() = listOf(retrofit)
}

object Database : Libraries {

    object Versions {
        const val room = "2.4.3"
    }

    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

    override val components: List<String>
        get() = listOf(roomRuntime, roomKtx)
}

object Navigation : Libraries {

    object Versions {
        const val navigation = "2.5.2"
    }

    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    override val components: List<String>
        get() = listOf(navigationFragment, navigationUi)
}

object View : Libraries {

    private object Versions {
        const val material = "1.6.1"
        const val recyclerView = "1.2.0-beta01"
        const val constraintLayout = "2.1.4"
        const val swipeRefresh = "1.1.0"
        const val viewPager = "1.0.0"
        const val splashScreen = "1.0.0"

    }

    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val viewPager = "androidx.viewpager2:viewpager2:${Versions.viewPager}"
    const val swipeRefresh =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreen}"

    override val components = listOf(
        material, constraintLayout, viewPager, swipeRefresh,
        recyclerView
    )
}

object Utils : Libraries {

    private object Versions {
        const val timber = "4.7.1"
        const val datastore = "1.0.0"
    }

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val datastore = "androidx.datastore:datastore-preferences:${Versions.datastore}"

    override val components: List<String>
        get() = listOf(timber, datastore)
}

object UnitTest : Libraries {
    private object Versions {
        const val junit = "4.13.2"
        const val mockito = "4.0.0"
        const val hamcrest = "1.3"
        const val roboelectric = "4.6"
    }

    const val junit = "junit:junit:${Versions.junit}"
    const val roomTest = "androidx.room:room-testing:${Database.Versions.room}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val hamcrest = "org.hamcrest:hamcrest-all:${Versions.hamcrest}"
    const val roboelectric = "org.robolectric:robolectric:${Versions.roboelectric}"

    override val components: List<String>
        get() = listOf(junit)
}

object AndroidTest : Libraries {
    private object Versions {
        const val espresso = "3.4.0"
        const val junitExt = "1.1.3"
    }

    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espressoIntent = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    const val idlingResource = "androidx.test.espresso:espresso-idling-resource:${Versions.espresso}"

    override val components: List<String>
        get() = listOf(espresso, junitExt)
}

object Firebase {

    object Versions {
        const val firebase = "29.0.0"
        const val crashlytics_plugin = "2.8.0"
    }

    // When using the BoM, you don't specify versions in Firebase library dependencies
    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebase}"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
}

object Google {

    object Versions {
        const val play = "18.0.0"
    }

    const val googlePlayGms = "com.google.android.gms:play-services-location:${Versions.play}"
}