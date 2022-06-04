@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(deps.plugins.jvm)
    alias(deps.plugins.serialization)
}

dependencies {
    implementation(deps.kotlinx.serialization.json)
    implementation(project(":shared"))
}
