plugins {
    alias(libs.plugins.contentexplorer.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.koin.plugin)
}

android {
    namespace = "com.example.contentexplorer.core.database"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
}
