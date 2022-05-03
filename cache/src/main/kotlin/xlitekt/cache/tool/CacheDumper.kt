package xlitekt.cache.tool

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import xlitekt.cache.cacheModule
import xlitekt.cache.provider.binary.title.TitleEntryTypeProvider
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
import xlitekt.cache.provider.font.FontEntryTypeProvider
import xlitekt.cache.provider.instrument.InstrumentEntryTypeProvider
import xlitekt.cache.provider.music.MusicEntryTypeProvider
import xlitekt.cache.provider.sprite.Sprite
import xlitekt.cache.provider.sprite.SpriteEntryTypeProvider
import xlitekt.cache.provider.sprite.titlescreen.TitleScreenEntryTypeProvider
import xlitekt.cache.provider.texture.TextureEntryTypeProvider
import xlitekt.cache.provider.ui.InterfaceEntryTypeProvider
import xlitekt.shared.inject
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO
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
    CacheDumper.dumpJson()
    CacheDumper.dumpSprites()
    CacheDumper.dumpTextures()
    CacheDumper.dumpTitleScreen()
    CacheDumper.dumpFonts()
    CacheDumper.dumpHitBars()
    CacheDumper.dumpHitSplats()
    CacheDumper.dumpWorldMapElements()
    CacheDumper.dumpChatBoxIcons()
    CacheDumper.dumpMusicTracks()
}

object CacheDumper {
    private val varbits by inject<VarBitEntryTypeProvider>()
    private val interfaces by inject<InterfaceEntryTypeProvider>()
    private val enums by inject<EnumEntryTypeProvider>()
    private val objs by inject<ObjEntryTypeProvider>()
    private val npcs by inject<NPCEntryTypeProvider>()
    private val locs by inject<LocEntryTypeProvider>()
    // private val maps by inject<MapEntryTypeProvider>()
    private val sequences by inject<SequenceEntryTypeProvider>()
    private val hitSplats by inject<HitSplatEntryTypeProvider>()
    private val params by inject<ParamEntryTypeProvider>()
    private val hitBars by inject<HitBarEntryTypeProvider>()
    private val structs by inject<StructEntryTypeProvider>()
    private val kits by inject<KitEntryTypeProvider>()
    private val invs by inject<InvEntryTypeProvider>()
    private val spotAnimations by inject<SpotAnimationEntryTypeProvider>()
    private val varps by inject<VarpEntryTypeProvider>()
    private val floorOverlays by inject<FloorOverlayEntryTypeProvider>()
    private val floorUnderlays by inject<FloorUnderlayEntryTypeProvider>()
    private val varcs by inject<VarcEntryTypeProvider>()
    private val worldmap by inject<WorldMapElementEntryTypeProvider>()
    private val sprites by inject<SpriteEntryTypeProvider>()
    private val textures by inject<TextureEntryTypeProvider>()
    private val title by inject<TitleEntryTypeProvider>()
    private val titlescreen by inject<TitleScreenEntryTypeProvider>()
    private val fonts by inject<FontEntryTypeProvider>()
    private val musics by inject<MusicEntryTypeProvider>()
    private val instruments by inject<InstrumentEntryTypeProvider>()

    fun dumpJson() {
        val json = Json {
            prettyPrint = true
            encodeDefaults = true
            serializersModule = SerializersModule {
                contextual(Any::class, AnySerializer)
            }
        }

        Path.of("./cache/data/dump/varbits/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            varbits.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/interfaces/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            interfaces.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/enums/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            enums.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/objs/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            objs.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/npcs/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            npcs.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/locs/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            locs.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/sequences/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            sequences.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/hitSplats/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            hitSplats.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/params/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            params.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/hitBars/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            hitBars.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/structs/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            structs.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/kits/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            kits.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/invs/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            invs.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/spotAnimations/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            spotAnimations.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/varps/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            varps.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/floorOverlays/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            floorOverlays.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/floorUnderlays/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            floorUnderlays.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/varcs/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            varcs.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/worldmap/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            worldmap.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/textures/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            textures.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.id}.json").outputStream())
            }
        }

        Path.of("./cache/data/dump/fonts/").apply {
            if (notExists()) createDirectories()
        }.also { path ->
            fonts.entries().parallelStream().forEach {
                json.encodeToStream(it, Path.of("$path/${it.name ?: it.id}.json").outputStream())
            }
        }
    }

    fun dumpSprites() {
        Path.of("./cache/data/dump/sprites/").apply {
            if (notExists()) createDirectories()
        }.also {
            for (entry in sprites.entries()) {
                for (sprite in entry.sprites.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.id}_${sprite.id}")
                }
            }
        }
    }

    fun dumpTextures() {
        Path.of("./cache/data/dump/textures/sprites/").apply {
            if (notExists()) createDirectories()
        }.also {
            for (texture in textures.entries().filter { ids -> ids.textureIds != null }) {
                for (id in texture.textureIds!!) {
                    val entry = sprites.entryType(id) ?: continue
                    for (sprite in entry.sprites.filter(Sprite::renderable)) {
                        sprite.write(it, "png", "${texture.id}_${entry.id}_${sprite.id}")
                    }
                }
            }
        }
    }

    fun dumpTitleScreen() {
        Path.of("./cache/data/dump/titlescreen/").apply {
            if (notExists()) createDirectories()
        }.also {
            // Background.
            ImageIO.write(ImageIO.read(ByteArrayInputStream(title.entries().first().pixels!!)), "jpg", File(it.toString(), "title.jpg"))

            // Title screen.
            for (entry in titlescreen.entries()) {
                if (sprites.entryType(entry.id)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.id)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.name}_${entry.id}_${sprite.id}")
                }
            }
        }
    }

    fun dumpFonts() {
        Path.of("./cache/data/dump/fonts/sprites/").apply {
            if (notExists()) createDirectories()
        }.also {
            for (entry in fonts.entries()) {
                if (sprites.entryType(entry.id)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.id)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.name}_${entry.id}_${sprite.id}")
                }
            }
        }
    }

    fun dumpHitBars() {
        Path.of("./cache/data/dump/hitBars/sprites/").apply {
            if (notExists()) createDirectories()
        }.also {
            for (entry in hitBars.entries()) {
                if (sprites.entryType(entry.frontSpriteId)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.frontSpriteId)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.id}_${entry.frontSpriteId}")
                }

                if (sprites.entryType(entry.backgroundSpriteId)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.backgroundSpriteId)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.id}_${entry.backgroundSpriteId}")
                }
            }
        }
    }

    fun dumpHitSplats() {
        Path.of("./cache/data/dump/hitSplats/sprites/").apply {
            if (notExists()) createDirectories()
        }.also {
            for (entry in hitSplats.entries()) {
                if (sprites.entryType(entry.backgroundSprite)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.backgroundSprite)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.id}_${entry.backgroundSprite}")
                }

                if (sprites.entryType(entry.leftSpriteId)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.leftSpriteId)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.id}_${entry.leftSpriteId}")
                }

                if (sprites.entryType(entry.rightSpriteId)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.rightSpriteId)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.id}_${entry.rightSpriteId}")
                }

                if (sprites.entryType(entry.spriteId2)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.spriteId2)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.id}_${entry.spriteId2}")
                }
            }
        }
    }

    fun dumpWorldMapElements() {
        Path.of("./cache/data/dump/worldmap/sprites/").apply {
            if (notExists()) createDirectories()
        }.also {
            for (entry in worldmap.entries()) {
                if (sprites.entryType(entry.sprite1)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.sprite1)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.id}_${entry.sprite1}")
                }

                if (sprites.entryType(entry.sprite2)?.sprites == null) continue
                for (sprite in sprites.entryType(entry.sprite2)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(it, "png", "${entry.id}_${entry.sprite2}")
                }
            }
        }
    }

    fun dumpChatBoxIcons() {
        Path.of("./cache/data/dump/chatboxicons/").apply {
            if (notExists()) createDirectories()
        }.also {
            val entry = sprites.entryType(423) ?: return
            for (sprite in entry.sprites.filter(Sprite::renderable)) {
                sprite.write(it, "png", "${entry.id}_${sprite.id}")
            }
        }
    }

    fun dumpMusicTracks() {
        Path.of("./cache/data/dump/music/").apply {
            if (notExists()) createDirectories()
        }.also {
            for (entry in musics.entries()) {
                val name = if (entry.name != null) "${entry.name!!}_${entry.id}" else "${entry.id}"
                entry.bytes?.let { bytes -> Files.write(Path.of(it.toString(), "$name.midi"), bytes) }
            }
        }
    }

    private fun Sprite.write(path: Path, format: String, name: String) {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        image.setRGB(0, 0, width, height, pixels, 0, width)
        ImageIO.write(image, format, File(path.toString(), "$name.$format"))
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
}
