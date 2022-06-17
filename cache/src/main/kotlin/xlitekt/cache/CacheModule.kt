package xlitekt.cache

import com.runetopic.cache.store.Js5Store
import io.ktor.server.application.ApplicationEnvironment
import org.koin.core.error.NoBeanDefFoundException
import org.koin.dsl.module
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.cache.provider.binary.huffman.HuffmanEntryTypeProvider
import xlitekt.cache.provider.binary.title.TitleEntryTypeProvider
import xlitekt.cache.provider.config.dbrow.DBRowEntryTypeProvider
import xlitekt.cache.provider.config.dbtable.DBTableEntryTypeProvider
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
import xlitekt.cache.provider.dbindex.DBIndexEntryTypeProvider
import xlitekt.cache.provider.font.FontEntryTypeProvider
import xlitekt.cache.provider.instrument.InstrumentEntryTypeProvider
import xlitekt.cache.provider.map.MapSquareEntryTypeProvider
import xlitekt.cache.provider.music.MusicEntryTypeProvider
import xlitekt.cache.provider.soundeffect.SoundEffectEntryTypeProvider
import xlitekt.cache.provider.sprite.SpriteEntryTypeProvider
import xlitekt.cache.provider.sprite.titlescreen.TitleScreenEntryTypeProvider
import xlitekt.cache.provider.texture.TextureEntryTypeProvider
import xlitekt.cache.provider.ui.InterfaceEntryTypeProvider
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider
import xlitekt.shared.lazyInject
import java.nio.file.Path

/**
 * @author Jordan Abraham
 */
val cacheModule = module(createdAtStart = true) {
    single {
        val path = try {
            lazyInject<ApplicationEnvironment>().config.property("game.cache.path").getString()
        } catch (e: NoBeanDefFoundException) {
            "./cache/data/"
        }
        Js5Store(
            path = Path.of(path),
            parallel = true,
            decompressionIndexExclusions = intArrayOf(EntryTypeProvider.MAP_INDEX)
        )
    }
    single { HuffmanEntryTypeProvider() }
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
    single { MapSquareEntryTypeProvider() }
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
    single { SpriteEntryTypeProvider() }
    single { TextureEntryTypeProvider() }
    single { TitleEntryTypeProvider() }
    single { TitleScreenEntryTypeProvider() }
    single { FontEntryTypeProvider() }
    single { InstrumentEntryTypeProvider() }
    single { VorbisEntryTypeProvider() }
    single { SoundEffectEntryTypeProvider() }
    single { MusicEntryTypeProvider() }
    single { DBIndexEntryTypeProvider() }
    single { DBTableEntryTypeProvider() }
    single { DBRowEntryTypeProvider() }
}
