plugins {
    alias(libs.plugins.contentexplorer.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.koin.plugin)
}

android {
    namespace = "com.example.contentexplorer.core.network"
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
}
