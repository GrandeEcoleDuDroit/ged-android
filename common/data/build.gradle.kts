plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

val SERVER_URL: String =
    project.findProperty("SERVER_URL") as String?
        ?: System.getenv("SERVER_URL")
        ?: error("SERVER_URL is not set in gradle.properties or as environment variable")

val ORACLE_BUCKET_URL: String =
    project.findProperty("ORACLE_BUCKET_URL") as String?
        ?: System.getenv("ORACLE_BUCKET_URL")
        ?: error("ORACLE_BUCKET_URL is not set in gradle.properties or as environment variable")

android {
    namespace = "com.upsaclay.common.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 29

        consumerProguardFiles("consumer-rules.pro")
        buildConfigField(
            "String",
            "ORACLE_BUCKET_URL",
            "\"$ORACLE_BUCKET_URL\"",
        )
        buildConfigField(
            "String",
            "SERVER_URL",
            "\"$SERVER_URL\"",
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.okhttp)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.jakewharton.timber)

    testImplementation(libs.junit)

    implementation(project(":common:domain"))
}
