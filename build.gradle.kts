import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    application
}

group = "com.runetopic.xlite"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(deps.bundles.ktor)
    implementation(deps.bundles.koin)
    implementation(deps.cache)
}

with(tasks) {
    withType<Test> {
        dependencies {
            testImplementation(kotlin("test"))
        }
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi", "-Xopt-in=kotlin.time.ExperimentalTime")
    }
    withType<ShadowJar> {
        manifest {
            attributes(Pair("Main-Class", "io.ktor.server.netty.EngineMain"))
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
