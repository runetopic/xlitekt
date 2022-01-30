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

            alias("ktor-server-netty").to("io.ktor", "ktor-server-netty").versionRef("ktor")
            alias("koin-core").to("io.insert-koin", "koin-core").versionRef("koin")
            alias("koin-ktor").to("io.insert-koin", "koin-ktor").versionRef("koin")
            alias("slf4j-simple").to("org.slf4j", "slf4j-simple").versionRef("slf4j")
            alias("cache").to("com.runetopic.cache", "cache").versionRef("cache")
            alias("cryptography").to("com.runetopic.cryptography", "cryptography").versionRef("cryptography")
            alias("kotlinx-serialization-json").to("org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef("kotlinx")
            alias("kotlin-inline-logger").to("com.michael-bull.kotlin-inline-logger", "kotlin-inline-logger").versionRef("kotlin-inline-logger")

            bundle("ktor", listOf("ktor-server-netty"))
            bundle("koin", listOf("koin-core", "koin-ktor"))
            bundle("runetopic", listOf("cache", "cryptography"))
            bundle("logger", listOf("kotlin-inline-logger", "slf4j-simple"))

            alias("jvm").toPluginId("org.jetbrains.kotlin.jvm").version("1.6.10")
            alias("serialization").toPluginId("org.jetbrains.kotlin.plugin.serialization").version("1.6.10")
            alias("shadowjar").toPluginId("com.github.johnrengelman.shadow").version("7.0.0")
        }
    }
}
