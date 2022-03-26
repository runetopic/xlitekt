@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(project(":game"))
    implementation(project(":shared"))
}
