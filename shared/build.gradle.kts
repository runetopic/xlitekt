@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(deps.plugins.jvm)
    alias(deps.plugins.serialization)
}

dependencies {
    implementation(deps.kotlinx.serialization.json)
}
