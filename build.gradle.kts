plugins {
    id("com.android.application") version Version.gradlePlugin apply false
    id("com.android.library") version Version.gradlePlugin apply false
    id("org.jetbrains.kotlin.android") version Version.kotlin apply false
    id("com.google.gms.google-services") version "4.3.13" apply false
    id("com.google.firebase.crashlytics") version "2.9.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
