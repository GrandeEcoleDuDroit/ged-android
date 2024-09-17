import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if(localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { stream ->
        localProperties.load(stream)
    }
}

val gedoiseVm1BaseUrl = project.findProperty("GEDOISE_VM_1_BASE_URL") as String
val gedoiseVm2BaseUrl = project.findProperty("GEDOISE_VM_2_BASE_URL") as String

android {
    namespace = "com.upsaclay.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "SERVICE_1_BASE_URL",
                "\"${localProperties.getProperty("LOCAL_SERVER_BASE_URL")}\""
            )

            buildConfigField(
                "String",
                "SERVICE_2_BASE_URL",
                "\"${localProperties.getProperty("LOCAL_SERVER_BASE_URL")}\""
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "SERVICE_1_BASE_URL",
                "\"$gedoiseVm1BaseUrl\""
            )

            buildConfigField(
                "String",
                "SERVICE_2_BASE_URL",
                "\"$gedoiseVm2BaseUrl\""
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    kotlinOptions {
        jvmTarget = "19"
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
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.okhttp)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.koin)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.coil.compose)
    implementation(libs.jakewharton.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}