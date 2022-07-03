plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = Config.compileSdk
    defaultConfig {
        applicationId = Config.appId
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = Config.versionCode
        versionName = Config.versionName

        resourceConfigurations.addAll(listOf("es"))
        resValue("string", "app_version", "${Config.versionName} [${Config.versionCode}]")

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
        compose = true
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.composeCompiler
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}")

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.browser:browser:1.4.0")
    implementation("androidx.preference:preference-ktx:1.2.0")

    implementation("androidx.activity:activity-compose:1.5.0")
    implementation("androidx.compose.material:material:${Version.compose}")
    implementation("androidx.compose.ui:ui-tooling:${Version.compose}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0")

    implementation("com.google.android.material:material:1.6.1")

    implementation("androidx.room:room-runtime:${Version.room}")
    implementation("androidx.room:room-ktx:${Version.room}")
    kapt("androidx.room:room-compiler:${Version.room}")

    implementation("com.squareup.okhttp3:okhttp:${Version.okhttp}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Version.okhttp}")
    implementation("com.squareup.retrofit2:retrofit:${Version.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Version.retrofit}")
    implementation("com.squareup.picasso:picasso:${Version.picasso}")

    implementation(platform("com.google.firebase:firebase-bom:${Version.firebase}"))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")

    implementation("com.google.firebase:firebase-ads:21.0.0")

    implementation("me.zhanghai.android.materialprogressbar:library:1.6.1")
    implementation("com.wdullaer:materialdatetimepicker:4.2.3")
}
