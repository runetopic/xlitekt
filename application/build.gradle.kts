@Suppress("DSL_SCOPE_VIOLATION")
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
    implementation(project(":http"))
    implementation(project(":network"))
    implementation(project(":script"))
    implementation(project(":shared"))
}
