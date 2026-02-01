plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

val KEYSTORE_PATH: String =
    project.findProperty("KEYSTORE_PATH") as String?
        ?: System.getenv("KEYSTORE_PATH")
        ?: error("KEYSTORE_PATH is not set in gradle.properties or as environment variable")

val KEYSTORE_PASSWORD: String =
    project.findProperty("KEYSTORE_PASSWORD") as String?
        ?: System.getenv("KEYSTORE_PASSWORD")
        ?: error("KEYSTORE_PASSWORD is not set in gradle.properties or as environment variable")

val KEY_ALIAS: String =
    project.findProperty("KEY_ALIAS") as String?
        ?: System.getenv("KEY_ALIAS")
        ?: error("KEY_ALIAS is not set in gradle.properties or as environment variable")

val KEY_PASSWORD: String =
    project.findProperty("KEY_PASSWORD") as String?
        ?: System.getenv("KEY_PASSWORD")
        ?: error("KEY_PASSWORD is not set in gradle.properties or as environment variable")

android {
    namespace = "com.upsaclay.gedoise"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.upsaclay.gedoise"
        minSdk = 29
        targetSdk = 35
        versionCode = 19
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    signingConfigs {
        create("release") {
            storeFile = file(KEYSTORE_PATH)
            storePassword = KEYSTORE_PASSWORD
            keyAlias = KEY_ALIAS
            keyPassword = KEY_PASSWORD
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources.excludes.add("META-INF/*")
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
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.gson)
    implementation(libs.jakewharton.timber)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.test)
    testImplementation(kotlin("test"))
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)

    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.navigation.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":app:domain"))
    implementation(project(":app:data"))

    implementation(project(":authentication"))
    implementation(project(":authentication:domain"))
    implementation(project(":authentication:data"))

    implementation(project(":common"))
    implementation(project(":common:domain"))
    implementation(project(":common:data"))

    implementation(project(":message"))
    implementation(project(":message:domain"))
    implementation(project(":message:data"))

    implementation(project(":news"))
    implementation(project(":news:domain"))
    implementation(project(":news:data"))

    implementation(project(":mission"))
    implementation(project(":mission:domain"))
    implementation(project(":mission:data"))
}
