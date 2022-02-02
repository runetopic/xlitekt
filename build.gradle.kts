import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(deps.plugins.jvm)
    alias(deps.plugins.serialization)
    alias(deps.plugins.shadowjar)
}

group = "com.runetopic.xlite"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("com.runetopic.xlitekt.ApplicationKt")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(deps.bundles.ktor)
    implementation(deps.bundles.koin)
    implementation(deps.bundles.runetopic)
    implementation(deps.bundles.logger)
    implementation(deps.kotlinx.serialization.json)
    implementation(deps.kotlin.reflect)
}

with(tasks) {
    withType<Test> {
        dependencies {
            testImplementation(kotlin("test"))
        }
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlin.time.ExperimentalTime",
            "-Xopt-in=io.ktor.util.InternalAPI",
            "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
        )
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
