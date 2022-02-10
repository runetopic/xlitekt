package com.runetopic.xlitekt.plugin.script

import com.github.michaelbull.logging.InlineLogger
import io.github.classgraph.ClassGraph
import kotlin.script.templates.standard.ScriptTemplateWithArgs

private val logger = InlineLogger()

fun loadGameScripts() {
    ClassGraph().enableClassInfo().scan().use { result ->
        result.allClasses.filter { it.extendsSuperclass(ScriptTemplateWithArgs::class.java) }
            .map { it.loadClass().constructors.first().newInstance(emptyArray<String>()) }
            .count()
    }.let { logger.info { "Loaded $it kotlin scripts." } }
}
