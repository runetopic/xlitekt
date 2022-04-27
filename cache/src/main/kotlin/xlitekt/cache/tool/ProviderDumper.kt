package xlitekt.cache.tool

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import xlitekt.cache.AnySerializer
import xlitekt.cache.cacheModule
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
import xlitekt.cache.provider.ui.InterfaceEntryTypeProvider
import xlitekt.shared.inject
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.io.path.outputStream

/**
 * @author Jordan Abraham
 */
fun main() {
    startKoin {
        loadKoinModules(cacheModule)
    }

    val json = Json {
        prettyPrint = true
        encodeDefaults = true
        serializersModule = SerializersModule {
            contextual(Any::class, AnySerializer)
        }
    }

    val varbits by inject<VarBitEntryTypeProvider>()
    val interfaces by inject<InterfaceEntryTypeProvider>()
    val enums by inject<EnumEntryTypeProvider>()
    val objs by inject<ObjEntryTypeProvider>()
    val npcs by inject<NPCEntryTypeProvider>()
    val locs by inject<LocEntryTypeProvider>()
    // val maps by inject<MapEntryTypeProvider>()
    val sequences by inject<SequenceEntryTypeProvider>()
    val hitSplats by inject<HitSplatEntryTypeProvider>()
    val params by inject<ParamEntryTypeProvider>()
    val hitBars by inject<HitBarEntryTypeProvider>()
    val structs by inject<StructEntryTypeProvider>()
    val kits by inject<KitEntryTypeProvider>()
    val invs by inject<InvEntryTypeProvider>()
    val spotAnimations by inject<SpotAnimationEntryTypeProvider>()
    val varps by inject<VarpEntryTypeProvider>()
    val floorOverlays by inject<FloorOverlayEntryTypeProvider>()
    val floorUnderlays by inject<FloorUnderlayEntryTypeProvider>()
    val varcs by inject<VarcEntryTypeProvider>()

    Path.of("./cache/data/dump/").apply {
        if (notExists()) createDirectories()
    }.also {
        json.encodeToStream(varbits.entries().toList(), Path.of("$it/varbits.json").outputStream())
        json.encodeToStream(interfaces.entries().toList(), Path.of("$it/interfaces.json").outputStream())
        json.encodeToStream(enums.entries().toList(), Path.of("$it/enums.json").outputStream())
        json.encodeToStream(objs.entries().toList(), Path.of("$it/objs.json").outputStream())
        json.encodeToStream(npcs.entries().toList(), Path.of("$it/npcs.json").outputStream())
        json.encodeToStream(locs.entries().toList(), Path.of("$it/locs.json").outputStream())
        // json.encodeToStream(maps.entries().toList(), Path.of("$it/maps.json").outputStream())
        json.encodeToStream(sequences.entries().toList(), Path.of("$it/sequences.json").outputStream())
        json.encodeToStream(hitSplats.entries().toList(), Path.of("$it/hitSplats.json").outputStream())
        json.encodeToStream(params.entries().toList(), Path.of("$it/params.json").outputStream())
        json.encodeToStream(hitBars.entries().toList(), Path.of("$it/hitBars.json").outputStream())
        json.encodeToStream(structs.entries().toList(), Path.of("$it/structs.json").outputStream())
        json.encodeToStream(kits.entries().toList(), Path.of("$it/kits.json").outputStream())
        json.encodeToStream(invs.entries().toList(), Path.of("$it/invs.json").outputStream())
        json.encodeToStream(spotAnimations.entries().toList(), Path.of("$it/spotAnimations.json").outputStream())
        json.encodeToStream(varps.entries().toList(), Path.of("$it/varps.json").outputStream())
        json.encodeToStream(floorOverlays.entries().toList(), Path.of("$it/floorOverlays.json").outputStream())
        json.encodeToStream(floorUnderlays.entries().toList(), Path.of("$it/floorUnderlays.json").outputStream())
        json.encodeToStream(varcs.entries().toList(), Path.of("$it/varcs.json").outputStream())
    }
}
