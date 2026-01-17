plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp") version "2.1.10-1.0.29"
    id("kotlin-parcelize")
}

android {
    namespace = "com.tshikasi.tshikasi_auto_school"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tshikasi.tshikasi_auto_school"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }

    sourceSets {
        getByName("main") {
            assets.srcDirs("src/main/assets")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "SYMBOL_TABLE"
            }
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
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
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }



    packaging {
        resources {
            excludes += "/mozilla/public-suffix-list.txt"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
        }
        jniLibs {
            pickFirsts += "**/libc++_shared.so"
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = true
        }
    }
}

configurations.all {
    exclude(group = "org.apache.httpcomponents", module = "httpclient")
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
    implementation(libs.androidx.constraintlayout)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ExoPlayer para vídeo e áudio
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")
    implementation("androidx.media3:media3-common:1.2.0")

    // Media3 ExoPlayer - Versão completa com todos os formatos
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.2.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.2.1")
    implementation("androidx.media3:media3-ui:1.2.1")

    // IMPORTANTE: Adicione também os datasources
    implementation("androidx.media3:media3-datasource:1.2.1")
    implementation("androidx.media3:media3-datasource-okhttp:1.2.1")

    // Firebase
// Firebase - BOM manages versions
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.messaging)
    implementation("com.google.firebase:firebase-firestore-ktx") // No version specified
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    implementation("io.arrow-kt:arrow-core:1.2.4")

    val camerax_version = "1.4.0"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")
    implementation("androidx.camera:camera-video:$camerax_version")
    implementation("androidx.camera:camera-extensions:$camerax_version")

    implementation("androidx.work:work-runtime-ktx:2.9.1")

    val ktorVersion = "2.3.12"
    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-websockets:$ktorVersion")
    implementation("org.slf4j:slf4j-android:1.7.36")
    implementation("io.ktor:ktor-client-okhttp:2.3.7")

    implementation("com.github.perthcpe23:android-mjpeg-view:1.0.9")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("com.github.DantSu:ESCPOS-ThermalPrinter-Android:2.0.6")
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.dagger:hilt-android:2.52")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.24.1")
    implementation("androidx.compose.material:material-icons-extended:1.7.0")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("com.squareup:kotlinpoet:1.18.1")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("co.yml:ycharts:2.1.0")
    implementation("androidx.exifinterface:exifinterface:1.3.7")
    implementation("org.osmdroid:osmdroid-android:6.1.14")
    implementation("com.github.MKergall:osmbonuspack:6.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    val supabase_version = "2.6.0"
    implementation("io.github.jan-tennert.supabase:supabase-kt:$supabase_version")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:$supabase_version")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:$supabase_version")
    implementation("io.github.jan-tennert.supabase:storage-kt:$supabase_version")
    implementation("io.github.jan-tennert.supabase:realtime-kt:$supabase_version")
    implementation("io.github.jan-tennert.supabase:functions-kt:$supabase_version")

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.maps.android:maps-compose:4.4.1")
    implementation("com.google.maps.android:maps-ktx:5.1.1")
    implementation("com.google.android.libraries.places:places:3.5.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Koin - CORRETO
    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
    testImplementation("io.insert-koin:koin-test:3.5.0")

    // ExoPlayer core (você já deve ter)
    implementation("androidx.media3:media3-exoplayer:1.2.0")

    // ADICIONE ESTA LINHA - HLS support
    implementation("androidx.media3:media3-exoplayer-hls:1.2.0")



    // Email
    implementation("com.sun.mail:android-mail:1.6.0") {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }
    implementation("com.sun.mail:android-activation:1.6.0")

}