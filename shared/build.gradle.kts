@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(deps.plugins.jvm)
}

apply(plugin = deps.plugins.serialization.get().pluginId)

dependencies {}
