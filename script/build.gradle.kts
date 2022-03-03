@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.bundles.kts)
    implementation(deps.classgraph)
    implementation(deps.pathfinder)
    implementation(project(":game"))
    implementation(project(":cache"))
    implementation(project(":network"))
    implementation(project(":shared"))
}
