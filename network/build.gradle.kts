@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(project(":game"))
    implementation(project(":http"))
    implementation(project(":shared"))
}
