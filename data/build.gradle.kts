plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation (project(":domain"))
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
    implementation("androidx.compose.material3:material3-android:1.2.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    val koinVersion = "3.5.3"

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
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

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    implementation(kotlin("reflect"))
}