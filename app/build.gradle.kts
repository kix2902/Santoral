plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "es.kix2902.santoral"
        minSdk = 23
        targetSdk = 32
        versionCode = 60
        versionName = "5.0.1"

        resConfigs("es")
        resValue("string", "app_version", "5.0.1 [60]")

        kapt.arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
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
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3")

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.browser:browser:1.4.0")
    implementation("androidx.preference:preference:1.2.0")
    implementation("androidx.preference:preference-ktx:1.2.0")

    implementation("com.google.android.material:material:1.6.1")

    implementation("androidx.room:room-runtime:2.4.2")
    implementation("androidx.room:room-ktx:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")

    implementation("com.squareup.okhttp3:okhttp:4.8.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("com.google.firebase:firebase-core:21.0.0")
    implementation("com.google.firebase:firebase-ads:21.0.0")
    implementation("com.google.firebase:firebase-crashlytics:18.2.11")
    implementation("com.google.firebase:firebase-analytics:21.0.0")

    implementation("me.zhanghai.android.materialprogressbar:library:1.6.1")
    implementation("com.wdullaer:materialdatetimepicker:4.2.3")
}
