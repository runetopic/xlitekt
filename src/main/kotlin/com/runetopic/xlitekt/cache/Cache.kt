package com.runetopic.xlitekt.cache

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.huffman.Huffman
import com.runetopic.xlitekt.cache.Cache.loadProviders
import com.runetopic.xlitekt.cache.provider.EntryType
import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.loc.LocEntryType
import com.runetopic.xlitekt.cache.provider.config.loc.LocEntryTypeProvider
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
        NPCEntryType::class to NPCEntryTypeProvider(),
        LocEntryType::class to LocEntryTypeProvider()
    )

    fun loadProviders() {
        logger.debug { "Loading ${providers.size} cache providers with ${providers.values.sumOf(EntryTypeProvider<*>::size)} total entries." }
        // Objs have a post loading process for notes, un-notes and placeholders.
        post<ObjEntryType, ObjEntryTypeProvider>()
    }

    inline fun <reified T : EntryType> entryType(id: Int): T? = providers[T::class]?.entryType(id) as T?

    private inline fun <reified T : EntryType, reified R : EntryTypeProvider<T>> post() = (providers[T::class] as R).run {
        entries.values.forEach { it.postLoadEntryType() }
    }
}
