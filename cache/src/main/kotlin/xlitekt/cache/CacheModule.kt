package xlitekt.cache

import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.huffman.Huffman
import io.ktor.application.ApplicationEnvironment
import org.koin.dsl.module
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.cache.provider.config.enum.EnumEntryTypeProvider
import xlitekt.cache.provider.config.hitbar.HitBarEntryTypeProvider
import xlitekt.cache.provider.config.hitsplat.HitSplatEntryTypeProvider
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.cache.provider.config.param.ParamEntryTypeProvider
import xlitekt.cache.provider.config.sequence.SequenceEntryTypeProvider
import xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider
import xlitekt.cache.provider.map.MapEntryTypeProvider
import xlitekt.cache.provider.ui.InterfaceEntryTypeProvider
import java.nio.file.Path
import xlitekt.cache.provider.config.inv.InvEntryTypeProvider
import xlitekt.cache.provider.config.kit.KitEntryTypeProvider
import xlitekt.cache.provider.config.spotanimation.SpotAnimationEntryTypeProvider
import xlitekt.cache.provider.config.struct.StructEntryTypeProvider
import xlitekt.cache.provider.config.varp.VarpEntryTypeProvider

/**
 * @author Jordan Abraham
 */
val cacheModule = module(createdAtStart = true) {
    single {
        Js5Store(
            path = Path.of(inject<ApplicationEnvironment>().value.config.property("game.cache.path").getString()),
            parallel = true,
            decompressionIndexExclusions = intArrayOf(EntryTypeProvider.MAP_INDEX)
        )
    }
    single { Huffman(get<Js5Store>().index(indexId = 10).group(groupName = "huffman").file(0).data) }
    single { VarBitEntryTypeProvider() }
    single { InterfaceEntryTypeProvider() }
    single { EnumEntryTypeProvider() }
    single {
        ObjEntryTypeProvider().apply {
            entries().forEach(::postLoadEntryType)
        }
    }
    single { NPCEntryTypeProvider() }
    single { LocEntryTypeProvider() }
    single { MapEntryTypeProvider() }
    single { SequenceEntryTypeProvider() }
    single { HitSplatEntryTypeProvider() }
    single { ParamEntryTypeProvider() }
    single { HitBarEntryTypeProvider() }
    single { StructEntryTypeProvider() }
    single { KitEntryTypeProvider() }
    single { InvEntryTypeProvider() }
    single { SpotAnimationEntryTypeProvider() }
    single { VarpEntryTypeProvider() }
}
