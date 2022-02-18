rootProject.name = "xlitekt"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories(RepositoryHandler::mavenCentral)
    repositories { maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") } }

    versionCatalogs {
        create("deps") {
            version("ktor", "1.6.7")
            version("koin", "3.1.5")
            version("slf4j", "1.7.32")
            version("cache", "1.4.19-SNAPSHOT")
            version("cryptography", "1.0.10-SNAPSHOT")
            version("kotlinx", "1.3.2")
            version("kotlin-inline-logger", "1.0.4")
            version("kotlin-reflect", "1.6.10")
            version("classgraph", "4.8.139")
            version("kts", "1.6.10")

            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("koin-core", "io.insert-koin", "koin-core").versionRef("koin")
            library("koin-ktor", "io.insert-koin", "koin-ktor").versionRef("koin")
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")
            library("cache", "com.runetopic.cache", "cache").versionRef("cache")
            library("cryptography", "com.runetopic.cryptography", "cryptography").versionRef("cryptography")
            library("classgraph", "io.github.classgraph", "classgraph").versionRef("classgraph")
            library("kscripting", "org.jetbrains.kotlin", "kotlin-scripting-common").versionRef("kts")
            library("kruntime", "org.jetbrains.kotlin", "kotlin-script-runtime").versionRef("kts")
            library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin-reflect")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef("kotlinx")
            library("kotlin-inline-logger", "com.michael-bull.kotlin-inline-logger", "kotlin-inline-logger").versionRef("kotlin-inline-logger")

            bundle("kts", listOf("kscripting", "kruntime"))
            bundle("ktor", listOf("ktor-server-netty"))
            bundle("koin", listOf("koin-core", "koin-ktor"))
            bundle("runetopic", listOf("cache", "cryptography"))
            bundle("logger", listOf("kotlin-inline-logger", "slf4j-simple"))

            plugin("jvm", "org.jetbrains.kotlin.jvm").version("1.6.10")
            plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").version("1.6.10")
            plugin("shadowjar", "com.github.johnrengelman.shadow").version("7.1.2")
            plugin("versions", "com.github.ben-manes.versions").version("0.42.0")
        }
    }
}
