plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlinSerialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.galleryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.galleryapp"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.volley)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //MVIKotlin

    //Decompose for navigation
    implementation("com.arkivanov.decompose:decompose:2.2.0-alpha03")
    implementation(libs.kotlinx.serialization.json)
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.1.4-compose-experimental")
    implementation("com.arkivanov.decompose:extensions-compose-jetpack:2.1.0")

    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-navigation:3.2.0-beta-1")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")

    implementation("com.arkivanov.mvikotlin:mvikotlin:3.0.0")
    implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:3.0.0")
    implementation("com.arkivanov.mvikotlin:mvikotlin-main:3.0.0")

    //ktor for android
    implementation("io.ktor:ktor-client-android:2.3.2")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.2")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:2.3.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.2")

    //coil
    implementation("io.coil-kt:coil-compose:2.7.0")
}

