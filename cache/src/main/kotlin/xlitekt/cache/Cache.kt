package xlitekt.cache

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.huffman.Huffman
import io.ktor.application.ApplicationEnvironment
import org.koin.dsl.module
import xlitekt.cache.Cache.loadProviders
import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.cache.provider.EntryTypeProvider.Companion.MAP_INDEX
import xlitekt.cache.provider.config.enum.EnumEntryType
import xlitekt.cache.provider.config.enum.EnumEntryTypeProvider
import xlitekt.cache.provider.config.hitbar.HitBarEntryType
import xlitekt.cache.provider.config.hitbar.HitBarEntryTypeProvider
import xlitekt.cache.provider.config.hitsplat.HitSplatEntryType
import xlitekt.cache.provider.config.hitsplat.HitSplatEntryTypeProvider
import xlitekt.cache.provider.config.loc.LocEntryType
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.cache.provider.config.npc.NPCEntryType
import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.cache.provider.config.obj.ObjEntryType
import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.cache.provider.config.param.ParamEntryType
import xlitekt.cache.provider.config.param.ParamEntryTypeProvider
import xlitekt.cache.provider.config.sequence.SequenceEntryType
import xlitekt.cache.provider.config.sequence.SequenceEntryTypeProvider
import xlitekt.cache.provider.config.varbit.VarBitEntryType
import xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider
import xlitekt.cache.provider.map.MapEntryTypeProvider
import xlitekt.cache.provider.map.MapSquareEntryType
import xlitekt.cache.provider.ui.InterfaceEntryType
import xlitekt.cache.provider.ui.InterfaceEntryTypeProvider
import java.nio.file.Path

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
val cacheModule = module(createdAtStart = true) {
    single {
        Js5Store(
            path = Path.of(inject<ApplicationEnvironment>().value.config.property("game.cache.path").getString()),
            parallel = true,
            decompressionIndexExclusions = intArrayOf(MAP_INDEX)
        )
    }
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
        LocEntryType::class to LocEntryTypeProvider(),
        MapSquareEntryType::class to MapEntryTypeProvider(),
        SequenceEntryType::class to SequenceEntryTypeProvider(),
        HitSplatEntryType::class to HitSplatEntryTypeProvider(),
        ParamEntryType::class to ParamEntryTypeProvider(),
        HitBarEntryType::class to HitBarEntryTypeProvider()
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
inline fun <reified T : EntryType> entries(): Collection<T> = Cache.providers[T::class]?.entries() as Collection<T>
