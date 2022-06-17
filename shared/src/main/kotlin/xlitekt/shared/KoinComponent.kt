package xlitekt.shared

import org.koin.mp.KoinPlatformTools

/**
 * @author Jordan Abraham
 */
inline fun <reified T : Any> inject(): Lazy<T> = KoinPlatformTools.defaultContext().get().inject(null, KoinPlatformTools.defaultLazyMode(), null)
inline fun <reified T : Any> insert(): T = KoinPlatformTools.defaultContext().get().inject<T>(null, KoinPlatformTools.defaultLazyMode(), null).value
