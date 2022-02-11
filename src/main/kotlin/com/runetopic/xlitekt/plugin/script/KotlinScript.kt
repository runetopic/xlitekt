package com.runetopic.xlitekt.plugin.script

import io.github.classgraph.ClassGraph
import io.ktor.application.Application
import io.ktor.application.log
import kotlin.script.templates.standard.ScriptTemplateWithArgs

fun Application.installKotlinScript() {
    ClassGraph().enableClassInfo().scan().use { result ->
        result.allClasses.filter { it.extendsSuperclass(ScriptTemplateWithArgs::class.java) }
            .map { it.loadClass().constructors.first().newInstance(emptyArray<String>()) }
            .count()
    }.let { log.debug("Installed $it kotlin scripts.") }
}
