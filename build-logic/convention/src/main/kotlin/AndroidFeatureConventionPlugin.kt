import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("contentexplorer.android.library.compose")
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                add("implementation", project(":core:domain"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:designsystem"))
            }
        }
    }
}
