import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(deps.plugins.jvm)
    alias(deps.plugins.shadowjar)
    alias(deps.plugins.versions)
    alias(deps.plugins.serialization)
}

group = "com.runetopic.xlite"
version = "1.0.0-SNAPSHOT"

allprojects {
    plugins.withType<KotlinPluginWrapper> {
        dependencies {
            implementation(kotlin("stdlib"))
            implementation(deps.bundles.runetopic)
            implementation(deps.bundles.logger)
            implementation(deps.bundles.ktor)
            implementation(deps.bundles.koin)
            implementation(deps.kotlinx.serialization.json)
        }
    }

    with(tasks) {
        withType<Test> {
            dependencies {
                testImplementation(kotlin("test"))
            }
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "11"
            kotlinOptions.freeCompilerArgs = listOf(
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlin.time.ExperimentalTime",
                "-Xopt-in=io.ktor.util.InternalAPI",
                "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
                "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
            )
        }
    }
}

application {
    mainClass.set("xlitekt.application.ApplicationKt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
