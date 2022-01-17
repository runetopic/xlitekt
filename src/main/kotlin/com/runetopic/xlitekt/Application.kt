package com.runetopic.xlitekt

import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.network.startListeningOnPort
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import java.nio.file.Path

const val PORT = 43594 // TODO move this into a properties file or some shit
const val IO_TIMEOUT = 10_000L // TODO move this into a properties file or some shit

fun Application.module() {
    install(Koin) {
        modules(
            module {
                single { Js5Store(path = Path.of("${System.getProperty("user.home")}/202/"), parallel = true) }
            }
        )
    }
    startListeningOnPort(PORT)
}
