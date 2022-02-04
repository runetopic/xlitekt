package com.runetopic.xlitekt.plugin.script

import io.github.classgraph.ClassGraph
import kotlin.script.templates.standard.ScriptTemplateWithArgs
import org.koin.dsl.module

val gameScriptModule = module {
    single(createdAtStart = true) {
        loadGameScripts()
    }
}

fun loadGameScripts() {
    ClassGraph().enableClassInfo().scan().use { result ->
        result.allClasses.filter { it.extendsSuperclass(ScriptTemplateWithArgs::class.java) }
            .map { it.loadClass() }
            .map {
                it.constructors.first().newInstance(emptyArray<String>())
            }
    }
}
