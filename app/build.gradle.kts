plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.todoit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.todoit"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.room.runtime.v230)
    kapt(libs.room.compiler)
    implementation(libs.androidx.room.ktx.v230)
    androidTestImplementation(libs.androidx.room.testing)

//     implementation(libs.androidx.lifecycle.extensions.z)
//     implementation(libs.androidx.lifecycle.common.java8)
       implementation(libs.androidx.lifecycle.viewmodel.ktx)
//     implementation(libs.androidx.activity.ktx)

/*     implementation(libs.kotlinx.coroutines.android)
     implementation(libs.kotlinx.coroutines.core)*/
}