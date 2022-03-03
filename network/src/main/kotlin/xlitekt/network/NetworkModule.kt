package xlitekt.network

import org.koin.dsl.module

/**
 * @author Jordan Abraham
 */
val networkModule = module(createdAtStart = true) {
    single { Network() }
}
