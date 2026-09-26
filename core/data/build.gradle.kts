plugins {
    alias(libs.plugins.contentexplorer.android.library)
    alias(libs.plugins.koin.plugin)
}

android {
    namespace = "com.example.contentexplorer.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
}
