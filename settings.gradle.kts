rootProject.name = "xlitekt"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    defaultLibrariesExtensionName.set("deps")
    repositories(RepositoryHandler::mavenCentral)
    repositories { maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") } }
}

pluginManagement {
    plugins {
        kotlin("jvm") version "1.6.10"
    }
}
