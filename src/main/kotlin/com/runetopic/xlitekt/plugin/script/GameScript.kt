package com.runetopic.xlitekt.plugin.script

import com.github.michaelbull.logging.InlineLogger
import io.github.classgraph.ClassGraph
import org.koin.dsl.module
import kotlin.script.templates.standard.ScriptTemplateWithArgs

private val logger = InlineLogger()

val gameScriptModule = module(createdAtStart = true) {
    single { loadGameScripts() }
}

fun loadGameScripts() {
    ClassGraph().enableClassInfo().scan().use { result ->
        result.allClasses.filter { it.extendsSuperclass(ScriptTemplateWithArgs::class.java) }
            .map { it.loadClass().constructors.first().newInstance(emptyArray<String>()) }
            .count()
    }.let { logger.debug { "Loaded $it kotlin scripts." } }
}
