package xlitekt.cache

import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.huffman.Huffman
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.koin.core.error.NoBeanDefFoundException
import org.koin.dsl.module
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.cache.provider.config.enum.EnumEntryTypeProvider
import xlitekt.cache.provider.config.hitbar.HitBarEntryTypeProvider
import xlitekt.cache.provider.config.hitsplat.HitSplatEntryTypeProvider
import xlitekt.cache.provider.config.inv.InvEntryTypeProvider
import xlitekt.cache.provider.config.kit.KitEntryTypeProvider
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.cache.provider.config.overlay.FloorOverlayEntryTypeProvider
import xlitekt.cache.provider.config.param.ParamEntryTypeProvider
import xlitekt.cache.provider.config.sequence.SequenceEntryTypeProvider
import xlitekt.cache.provider.config.spotanimation.SpotAnimationEntryTypeProvider
import xlitekt.cache.provider.config.struct.StructEntryTypeProvider
import xlitekt.cache.provider.config.underlay.FloorUnderlayEntryTypeProvider
import xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider
import xlitekt.cache.provider.config.varc.VarcEntryTypeProvider
import xlitekt.cache.provider.config.varp.VarpEntryTypeProvider
import xlitekt.cache.provider.config.worldmap.WorldMapElementEntryTypeProvider
import xlitekt.cache.provider.map.MapEntryTypeProvider
import xlitekt.cache.provider.ui.InterfaceEntryTypeProvider
import xlitekt.shared.lazy
import java.nio.file.Path

/**
 * @author Jordan Abraham
 */
val cacheModule = module(createdAtStart = true) {
    single {
        val path = try {
            lazy<ApplicationEnvironment>().config.property("game.cache.path").getString()
        } catch (e: NoBeanDefFoundException) {
            "./cache/data/"
        }
        Js5Store(
            path = Path.of(path),
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
    single { FloorOverlayEntryTypeProvider() }
    single { FloorUnderlayEntryTypeProvider() }
    single { VarcEntryTypeProvider() }
    single { WorldMapElementEntryTypeProvider() }
}

object AnySerializer : KSerializer<Any> {
    override fun deserialize(decoder: Decoder): Any {
        TODO("Not yet implemented")
    }

    override val descriptor: SerialDescriptor get() = ContextualSerializer(Any::class, null, emptyArray()).descriptor

    override fun serialize(encoder: Encoder, value: Any) = when (value) {
        is Int -> encoder.encodeInt(value)
        is String -> encoder.encodeString(value)
        else -> throw IllegalStateException("This can only be in context of INT or STRING.")
    }
}
