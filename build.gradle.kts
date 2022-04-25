import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
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
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlin.time.ExperimentalTime",
                "-opt-in=io.ktor.util.InternalAPI",
                "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
                "-opt-in=kotlin.ExperimentalUnsignedTypes"
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
