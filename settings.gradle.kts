rootProject.name = "xlitekt"

dependencyResolutionManagement {
    repositories(RepositoryHandler::mavenCentral)
    repositories { maven { url = uri("https://jitpack.io") } }
    repositories { maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") } }

    versionCatalogs {
        create("deps") {
            version("kotlin", "1.7.0")
            version("ktor", "2.0.2")
            version("koin", "3.2.0")
            version("slf4j", "1.7.36")
            version("cache", "1.4.24-SNAPSHOT")
            version("cryptography", "1.0.10-SNAPSHOT")
            version("kotlinx", "1.3.3")
            version("kotlin-inline-logger", "1.0.4")
            version("classgraph", "4.8.147")
            version("shadowjar", "7.1.2")
            version("versions", "0.42.0")
            version("pathfinder", "2.1.4")
            version("fastutil", "8.5.8")
            version("jctools", "3.3.0")

            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("koin-core", "io.insert-koin", "koin-core").versionRef("koin")
            library("koin-ktor", "io.insert-koin", "koin-ktor").versionRef("koin")
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")
            library("cache", "com.runetopic.cache", "cache").versionRef("cache")
            library("cryptography", "com.runetopic.cryptography", "cryptography").versionRef("cryptography")
            library("pathfinder", "com.github.blurite", "pathfinder").versionRef("pathfinder")
            library("classgraph", "io.github.classgraph", "classgraph").versionRef("classgraph")
            library("kscripting", "org.jetbrains.kotlin", "kotlin-scripting-common").versionRef("kotlin")
            library("kruntime", "org.jetbrains.kotlin", "kotlin-script-runtime").versionRef("kotlin")
            library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef("kotlinx")
            library("kotlin-inline-logger", "com.michael-bull.kotlin-inline-logger", "kotlin-inline-logger").versionRef("kotlin-inline-logger")
            library("fastutil", "it.unimi.dsi", "fastutil").versionRef("fastutil")
            library("jctools", "org.jctools", "jctools-core").versionRef("jctools")

            bundle("kts", listOf("kscripting", "kruntime"))
            bundle("ktor", listOf("ktor-server-netty"))
            bundle("koin", listOf("koin-core", "koin-ktor"))
            bundle("runetopic", listOf("cache", "cryptography"))
            bundle("logger", listOf("kotlin-inline-logger", "slf4j-simple"))

            plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
            plugin("shadowjar", "com.github.johnrengelman.shadow").versionRef("shadowjar")
            plugin("versions", "com.github.ben-manes.versions").versionRef("versions")
        }
    }
}
include(
    listOf(
        "application",
        "cache",
        "game",
        "network",
        "rsa",
        "script",
        "shared",
        "synchronizer"
    )
)
