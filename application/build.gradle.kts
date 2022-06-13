@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(deps.plugins.jvm)
}

group = "com.runetopic.xlite"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(deps.bundles.kts)
    implementation(deps.classgraph)
    implementation(project(":cache"))
    implementation(project(":game"))
    implementation(project(":network"))
    implementation(project(":script"))
    implementation(project(":shared"))
    implementation(project(":synchronizer"))
}
