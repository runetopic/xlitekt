package com.runetopic.xlitekt.cache

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.huffman.Huffman
import com.runetopic.xlitekt.cache.Cache.loadProviders
import com.runetopic.xlitekt.cache.provider.EntryType
import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.enum.EnumEntryType
import com.runetopic.xlitekt.cache.provider.config.enum.EnumEntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.loc.LocEntryType
import com.runetopic.xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.npc.NPCEntryType
import com.runetopic.xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.obj.ObjEntryType
import com.runetopic.xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.sequence.SequenceEntryType
import com.runetopic.xlitekt.cache.provider.config.sequence.SequenceEntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider
import com.runetopic.xlitekt.cache.provider.map.MapEntryType
import com.runetopic.xlitekt.cache.provider.map.MapEntryTypeProvider
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

object Cache {
    private val logger = InlineLogger()

    val providers = mapOf(
        VarBitEntryType::class to VarBitEntryTypeProvider(),
        InterfaceEntryType::class to InterfaceEntryTypeProvider(),
        EnumEntryType::class to EnumEntryTypeProvider(),
        ObjEntryType::class to ObjEntryTypeProvider(),
        NPCEntryType::class to NPCEntryTypeProvider(),
        MapEntryType::class to MapEntryTypeProvider(),
        LocEntryType::class to LocEntryTypeProvider(),
        SequenceEntryType::class to SequenceEntryTypeProvider()
    )

    fun loadProviders() {
        logger.debug { "Loading ${providers.size} cache providers with ${providers.values.sumOf(EntryTypeProvider<*>::size)} total entries." }
        // Objs have a post loading process for notes, un-notes and placeholders.
        post<ObjEntryType, ObjEntryTypeProvider>()
    }

    private inline fun <reified T : EntryType, reified R : EntryTypeProvider<T>> post() = (providers[T::class] as R).run {
        entries.values.forEach { it.postLoadEntryType() }
    }
}

inline fun <reified T : EntryType> entryType(id: Int): T? = Cache.providers[T::class]?.entryType(id) as T?
