package xlitekt.shared

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools

/**
 * @author Jordan Abraham
 */
inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = KoinPlatformTools.defaultContext().get().inject(qualifier, mode, parameters)

inline fun <reified T : Any> lazy(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null
): T = KoinPlatformTools.defaultContext().get().inject<T>(qualifier, mode, parameters).value
