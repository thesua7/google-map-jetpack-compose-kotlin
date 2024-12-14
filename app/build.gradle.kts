plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp") version "2.0.0-Beta4-1.0.17" // ksp plugin for kotlin dsl
    id("com.google.dagger.hilt.android") // for hilt di
}

android {
    namespace = "com.thesua7.map7"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.thesua7.map7"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    sourceSets {
        getByName("main") {
            java.srcDir("build/generated/ksp/main/kotlin") //ksp
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // compose
    implementation("androidx.compose.ui:ui:1.7.6")  // Jetpack Compose UI
    implementation("androidx.compose.material3:material3:1.3.1") // Material 3 for Compose
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")  // For preview support
    implementation("androidx.activity:activity-compose:1.9.3") // For Compose integration with Activities
    implementation("androidx.navigation:navigation-compose:2.8.5") // For Compose navigation
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.6")  // Debugging support for Compose
    debugImplementation("androidx.compose.ui:ui-tooling-preview:1.7.6")
    implementation("androidx.compose.foundation:foundation:1.7.6")  // Foundation for SwipeRefresh
    implementation("androidx.compose.foundation:foundation-layout:1.7.6")  // Foundation layout helpers

    // hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")   // If using Hilt compose

    implementation("com.google.maps.android:maps-compose:6.2.1")// map for compose
    implementation("com.google.android.gms:play-services-maps:19.0.0")// Maps SDK for Android
    implementation ("com.google.android.gms:play-services-location:21.3.0") // location
    implementation("com.google.maps.android:android-maps-utils:3.8.2") // map utils


    implementation ("com.google.accompanist:accompanist-permissions:0.36.0") // for permissions
}