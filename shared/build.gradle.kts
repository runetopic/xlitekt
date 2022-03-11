@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(deps.plugins.jvm)
}

apply(plugin = deps.plugins.serialization.get().pluginId)

dependencies {}
