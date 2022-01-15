rootProject.name = "xlitekt"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    defaultLibrariesExtensionName.set("deps")
    repositories(RepositoryHandler::mavenCentral)
}

pluginManagement {
    plugins {
        kotlin("jvm") version "1.6.10"
    }
}
