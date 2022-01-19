package com.runetopic.xlitekt.cache

import com.runetopic.cache.store.Js5Store
import io.ktor.application.ApplicationEnvironment
import org.koin.dsl.module
import java.nio.file.Path

/**
 * @author Jordan Abraham
 */
val cacheModule = module {
    single { Js5Store(path = Path.of(inject<ApplicationEnvironment>().value.config.property("game.cache.path").getString()), parallel = true) }
}
