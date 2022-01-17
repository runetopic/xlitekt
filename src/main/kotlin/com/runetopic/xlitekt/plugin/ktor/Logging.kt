package com.runetopic.xlitekt.plugin.ktor

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging

/**
 * @author Jordan Abraham
 */
fun Application.installLogging() = install(CallLogging)
