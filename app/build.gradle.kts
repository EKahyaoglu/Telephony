plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.reversenumlookup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.reversenumlookup"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "MY_API_KEY", "\"${project.properties["MY_API_KEY"]}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
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
}

dependencies {
    implementation("com.android.volley:volley:1.2.1") // For making API requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // For making API requests
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // For parsing JSON responses
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1") // For coroutines
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}