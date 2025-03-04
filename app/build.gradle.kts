plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.musicapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.musicapp"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation("androidx.media3:media3-session:1.3.1")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.5.6")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.6.2")

    // Navigation
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-datasource-okhttp:1.3.1")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-analytics")

    val koinVersion = "3.5.3"

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    testImplementation("io.insert-koin:koin-test:$koinVersion")

    // JUnit dependency for unit tests
    testImplementation("junit:junit:4.13.2")

// Mockito dependency for unit tests
    testImplementation("org.mockito:mockito-core:4.0.0")

// Optional: Mockito Kotlin extension if you use Kotlin
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

// AndroidX Test - Core library
    testImplementation("androidx.test:core:1.4.0")

// AndroidX Test - JUnit support
    androidTestImplementation("androidx.test.ext:junit:1.1.3")

// Optional: AndroidX Test - Mockito Android support (for instrumented tests)
    androidTestImplementation("org.mockito:mockito-android:4.0.0")

    implementation("androidx.media3:media3-ui:1.4.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    implementation(kotlin("reflect"))

    implementation("androidx.activity:activity-ktx:1.10.1")

}