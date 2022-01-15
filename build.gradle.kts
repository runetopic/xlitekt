import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.runetopic.xlitekt"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(deps.bundles.ktor)
}

with(tasks) {
    withType<Test> {
        dependencies {
            testImplementation(kotlin("test"))
        }
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
