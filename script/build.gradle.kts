@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
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
