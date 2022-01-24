import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    application
    kotlin("plugin.serialization")
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
    implementation(deps.kotlinx.serialization.json)
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
            "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
    }
    withType<ShadowJar> {
        manifest {
            attributes(Pair("Main-Class", "com.runetopic.xlitekt.ApplicationKt"))
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
