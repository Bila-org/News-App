import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("kotlinx-serialization")


    //   id("org.jetbrains.kotlin.jvm")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    //id("Kotlin-kapt")
    //id("org.jetbrains.kotlin.kapt")

    //   id("androidx.room")
//    id ("kotlin-kapt")
    //   id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.newsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.newsapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        project.rootProject.file("local.properties")
            .inputStream().use{
                properties.load(it)
            }
        val apiKey = properties.getProperty("API_KEY")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        buildConfig = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // For work manager dagger hilt
    implementation("androidx.hilt:hilt-work:1.2.0")
    implementation("androidx.work:work-runtime-ktx:2.10.2")

    // Lazily load Paging
    implementation("androidx.paging:paging-runtime:3.3.6")
    implementation("androidx.paging:paging-compose:3.3.6")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.1.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")


    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")


    // Custom Browser
    implementation("androidx.browser:browser:1.8.0")

    // Webview dependency
    implementation("androidx.webkit:webkit:1.14.0")


    // Room db
    val room_version = "2.7.2"
    implementation("androidx.room:room-paging:$room_version")
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:${room_version}")
    ksp("androidx.room:room-compiler:$room_version")
    //kapt("androidx.room:room-compiler:$room_version")
    implementation(kotlin("stdlib-jdk8"))

    // Dagger-Hilt dependency
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.56.2")
    kspAndroidTest("com.google.dagger:hilt-android-compiler:2.56.2")

    // For local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:2.56.2")
    kspTest("com.google.dagger:hilt-android-compiler:2.56.2")

    // Extended Icons
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.9.1")

    // Coil library
    implementation("io.coil-kt:coil-compose:2.6.0")


    //implementation("com.google.dagger:hilt-android:2.56.2")
    //kapt ("com.google.dagger:hilt-compiler:2.56.2")

    // androidTestImplementation ("com.google.dagger:hilt-android-testting:2.56.2")
    // kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.56.2")

    //testImplementation("com.google.dagger:hilt-android-testing:2.56.2")
    //   kaptTest("com.google.dagger:hilt-android-compiler:2.56.2")


}

/*
ksp {
    arg("ksp.incremental", "true")
    arg("ksp.incremental.intermodule", "true")
}*/