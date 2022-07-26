@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(deps.plugins.jvm)
    alias(deps.plugins.serialization)
    alias(deps.plugins.dokka)
}

dependencies {
    implementation(deps.pathfinder)
    implementation(project(":cache"))
    implementation(project(":shared"))
}
