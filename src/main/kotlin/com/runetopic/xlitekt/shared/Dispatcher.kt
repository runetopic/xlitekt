package com.runetopic.xlitekt.shared

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

/**
 * @author Jordan Abraham
 */
object Dispatcher {
    val GAME = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    val UPDATE = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
}
