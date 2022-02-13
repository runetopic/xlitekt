package com.runetopic.xlitekt.cache

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.huffman.Huffman
import com.runetopic.xlitekt.cache.Cache.loadProviders
import com.runetopic.xlitekt.cache.provider.EntryType
import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.npc.NPCEntryType
import com.runetopic.xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.obj.ObjEntryType
import com.runetopic.xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider
import com.runetopic.xlitekt.cache.provider.ui.InterfaceEntryType
import com.runetopic.xlitekt.cache.provider.ui.InterfaceEntryTypeProvider
import io.ktor.application.ApplicationEnvironment
import org.koin.dsl.module
import java.nio.file.Path

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
val cacheModule = module(createdAtStart = true) {
    single { Js5Store(path = Path.of(inject<ApplicationEnvironment>().value.config.property("game.cache.path").getString()), parallel = true) }
    single { Huffman(get<Js5Store>().index(indexId = 10).group(groupName = "huffman").file(0).data) }
    single { loadProviders() }
}
private val logger = InlineLogger()

object Cache {
    val providers = mapOf(
        VarBitEntryType::class to VarBitEntryTypeProvider(),
        InterfaceEntryType::class to InterfaceEntryTypeProvider(),
        ObjEntryType::class to ObjEntryTypeProvider(),
        NPCEntryType::class to NPCEntryTypeProvider()
    )

    fun loadProviders() {
        // Use parallelStream() because some of these loaders can be pretty big. This is just to reduce server startup time.
        providers.values.parallelStream().forEach(EntryTypeProvider<*>::load)
        logger.debug { "Finished loading ${providers.size} cache providers with ${providers.values.sumOf(EntryTypeProvider<*>::size)} total entries." }
    }

    inline fun <reified T : EntryType> entryType(id: Int): T? = providers[T::class]?.entryType(id) as T?
}
