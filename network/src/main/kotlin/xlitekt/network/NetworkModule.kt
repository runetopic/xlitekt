package xlitekt.network

import org.koin.dsl.module
import xlitekt.http.HttpServer

/**
 * @author Jordan Abraham
 */
val networkModule = module(createdAtStart = true) {
    single { HttpServer() }
    single { Network() }
}
