package com.runetopic.xlitekt.plugin.ktor

import com.runetopic.xlitekt.cache.cacheModule
import com.runetopic.xlitekt.network.networkModule
import com.runetopic.xlitekt.util.resource.loadAllMapSquares
import io.ktor.application.Application
import io.ktor.application.log
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import org.koin.ktor.ext.modules
import org.koin.mp.KoinPlatformTools

/**
 * @author Jordan Abraham
 */
fun Application.installKoin() {
    modules(
        module { single { this@installKoin.environment } },
        module { single { this@installKoin.log } },
        module { single { loadAllMapSquares() } },
        cacheModule,
        networkModule
    )
}

inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = KoinPlatformTools.defaultContext().get().inject(qualifier, mode, parameters)
