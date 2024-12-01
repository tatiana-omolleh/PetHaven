import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.example.pethaven"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pethaven"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"

        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        buildConfigField(type = "String", name = "SUPABASE_KEY", value = "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdkbHpicG9ieGZvZmh1anh4aWl0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzI3MDAxMzgsImV4cCI6MjA0ODI3NjEzOH0.T5bIbywHPH9tINbus0tVBfW3gF-Df91OeQEoQRzvCBk\"")
        buildConfigField(type = "String", name = "SUPABASE_URL", value = "\"https://gdlzbpobxfofhujxxiit.supabase.co\"")
        buildConfigField(type = "String", name = "SERVER_CLIENT_ID", value = "\"697589588255-1blgjkrr6lhrj1b6n4k1or2af2mvsbip.apps.googleusercontent.com\"")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
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
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Credentials manager and sign in with google
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    // Supabase libraries
    implementation(platform(libs.bom))
    implementation(libs.postgrest.kt)
    implementation(libs.compose.auth.ui)
    implementation(libs.compose.auth)
    implementation(libs.gotrue.kt)
    implementation(libs.ktor.client.android)
}
