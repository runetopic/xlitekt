package xlitekt.cache.tool

import com.github.michaelbull.logging.InlineLogger
import com.sun.media.SF2Soundbank
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
import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.binary.title.TitleEntryType
import xlitekt.cache.provider.binary.title.TitleEntryTypeProvider
import xlitekt.cache.provider.config.enum.EnumEntryType
import xlitekt.cache.provider.config.enum.EnumEntryTypeProvider
import xlitekt.cache.provider.config.hitbar.HitBarEntryType
import xlitekt.cache.provider.config.hitbar.HitBarEntryTypeProvider
import xlitekt.cache.provider.config.hitsplat.HitSplatEntryType
import xlitekt.cache.provider.config.hitsplat.HitSplatEntryTypeProvider
import xlitekt.cache.provider.config.inv.InvEntryType
import xlitekt.cache.provider.config.inv.InvEntryTypeProvider
import xlitekt.cache.provider.config.kit.KitEntryType
import xlitekt.cache.provider.config.kit.KitEntryTypeProvider
import xlitekt.cache.provider.config.loc.LocEntryType
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.cache.provider.config.npc.NPCEntryType
import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.cache.provider.config.obj.ObjEntryType
import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.cache.provider.config.overlay.FloorOverlayEntryType
import xlitekt.cache.provider.config.overlay.FloorOverlayEntryTypeProvider
import xlitekt.cache.provider.config.param.ParamEntryType
import xlitekt.cache.provider.config.param.ParamEntryTypeProvider
import xlitekt.cache.provider.config.sequence.SequenceEntryType
import xlitekt.cache.provider.config.sequence.SequenceEntryTypeProvider
import xlitekt.cache.provider.config.spotanimation.SpotAnimationEntryType
import xlitekt.cache.provider.config.spotanimation.SpotAnimationEntryTypeProvider
import xlitekt.cache.provider.config.struct.StructEntryType
import xlitekt.cache.provider.config.struct.StructEntryTypeProvider
import xlitekt.cache.provider.config.underlay.FloorUnderlayEntryType
import xlitekt.cache.provider.config.underlay.FloorUnderlayEntryTypeProvider
import xlitekt.cache.provider.config.varbit.VarBitEntryType
import xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider
import xlitekt.cache.provider.config.varc.VarcEntryType
import xlitekt.cache.provider.config.varc.VarcEntryTypeProvider
import xlitekt.cache.provider.config.varp.VarpEntryType
import xlitekt.cache.provider.config.varp.VarpEntryTypeProvider
import xlitekt.cache.provider.config.worldmap.WorldMapElementEntryType
import xlitekt.cache.provider.config.worldmap.WorldMapElementEntryTypeProvider
import xlitekt.cache.provider.font.FontEntryType
import xlitekt.cache.provider.font.FontEntryTypeProvider
import xlitekt.cache.provider.instrument.InstrumentEntryType
import xlitekt.cache.provider.instrument.InstrumentEntryTypeProvider
import xlitekt.cache.provider.music.MusicEntryType
import xlitekt.cache.provider.music.MusicEntryTypeProvider
import xlitekt.cache.provider.sprite.Sprite
import xlitekt.cache.provider.sprite.SpriteEntryType
import xlitekt.cache.provider.sprite.SpriteEntryTypeProvider
import xlitekt.cache.provider.sprite.titlescreen.TitleScreenEntryType
import xlitekt.cache.provider.sprite.titlescreen.TitleScreenEntryTypeProvider
import xlitekt.cache.provider.texture.TextureEntryType
import xlitekt.cache.provider.texture.TextureEntryTypeProvider
import xlitekt.cache.provider.ui.InterfaceEntryType
import xlitekt.cache.provider.ui.InterfaceEntryTypeProvider
import xlitekt.cache.tool.util.SoundFont
import xlitekt.shared.inject
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import javax.imageio.ImageIO
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.io.path.outputStream
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
fun main() {
    val logger = InlineLogger()
    startKoin {
        loadKoinModules(cacheModule)
    }
    logger.info { "Dumping..." }
    val time = measureTime(CacheDumper::dump)
    logger.info { "Took $time to complete." }
}

internal object CacheDumper {
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

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        serializersModule = SerializersModule {
            contextual(Any::class, AnySerializer)
        }
    }

    private val soundbank = SF2Soundbank().apply {
        name = "Old School RuneScape SoundFont"
        romName = "osrs"
        creationDate = LocalDateTime.now().run {
            "${month.name}, $dayOfMonth, $year"
        }
        vendor = "Old School RuneScape"
        copyright = "1999 - 2022 Jagex Ltd. 220 Science Park, Cambridge, CB4 0WA, United Kingdom"
    }
    private val addedInstruments = mutableListOf<Int>()

    fun dump() {
        mapOf(
            "varbits" to varbits,
            "interfaces" to interfaces,
            "enums" to enums,
            "objs" to objs,
            "npcs" to npcs,
            "locs" to locs,
            "sequences" to sequences,
            "hitSplats" to hitSplats,
            "hitBars" to hitBars,
            "params" to params,
            "structs" to structs,
            "kits" to kits,
            "invs" to invs,
            "spotAnimations" to spotAnimations,
            "varps" to varps,
            "floorOverlays" to floorOverlays,
            "floorUnderlays" to floorUnderlays,
            "varcs" to varcs,
            "worldmap" to worldmap,
            "sprites" to sprites,
            "textures" to textures,
            "title" to title,
            "titlescreen" to titlescreen,
            "fonts" to fonts,
            "musics" to musics,
            "instruments" to instruments
        ).entries.parallelStream().forEach {
            val path = Path.of("./cache/data/dump/${it.key}/")
            if (path.notExists()) path.createDirectories()
            it.value.entries().forEach { entry ->
                entry.writeToPath(path)
            }
        }

        // The soundbank.
        // TODO The combined soundbank file generating is not 100% proper. Everything else is correct.
        val soundbankPath = Path.of("./cache/data/dump/soundbank/")
        if (soundbankPath.notExists()) soundbankPath.createDirectories()
        soundbank.save(File(soundbankPath.toString(), "soundbank.sf2"))

        // The chatbox icons.
        val entry = sprites.entryType(423) ?: return
        for (sprite in entry.sprites.filter(Sprite::renderable)) {
            sprite.write(Path.of("./cache/data/dump/chatboxicons/"), "${entry.id}_${sprite.id}")
        }
    }

    private fun EntryType.writeToPath(path: Path) {
        when (this) {
            is VarBitEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is EnumEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is InterfaceEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is ObjEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is NPCEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is LocEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is SequenceEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is HitSplatEntryType -> {
                json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
                if (sprites.entryType(backgroundSprite)?.sprites == null) return
                for (sprite in sprites.entryType(backgroundSprite)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(Path.of("$path/sprites/"), "${id}_${sprite.id}")
                }

                if (sprites.entryType(leftSpriteId)?.sprites == null) return
                for (sprite in sprites.entryType(leftSpriteId)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(Path.of("$path/sprites/"), "${id}_${sprite.id}")
                }

                if (sprites.entryType(rightSpriteId)?.sprites == null) return
                for (sprite in sprites.entryType(rightSpriteId)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(Path.of("$path/sprites/"), "${id}_${sprite.id}")
                }

                if (sprites.entryType(spriteId2)?.sprites == null) return
                for (sprite in sprites.entryType(spriteId2)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(Path.of("$path/sprites/"), "${id}_${sprite.id}")
                }
            }
            is HitBarEntryType -> {
                json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
                if (sprites.entryType(frontSpriteId)?.sprites == null) return
                for (sprite in sprites.entryType(frontSpriteId)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(Path.of("$path/sprites/"), "${id}_${sprite.id}")
                }

                if (sprites.entryType(backgroundSpriteId)?.sprites == null) return
                for (sprite in sprites.entryType(backgroundSpriteId)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(Path.of("$path/sprites/"), "${id}_${sprite.id}")
                }
            }
            is ParamEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is StructEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is KitEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is InvEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is SpotAnimationEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is VarpEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is FloorOverlayEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is FloorUnderlayEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is VarcEntryType -> json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
            is WorldMapElementEntryType -> {
                json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
                if (sprites.entryType(sprite1)?.sprites == null) return
                for (sprite in sprites.entryType(sprite1)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(Path.of("$path/sprites/"), "${id}_${sprite.id}")
                }

                if (sprites.entryType(sprite2)?.sprites == null) return
                for (sprite in sprites.entryType(sprite2)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(Path.of("$path/sprites/"), "${id}_${sprite.id}")
                }
            }
            is SpriteEntryType -> {
                for (sprite in sprites.filter(Sprite::renderable)) {
                    sprite.write(path, "${id}_${sprite.id}")
                }
            }
            is TextureEntryType -> {
                json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
                for (textureId in textureIds ?: intArrayOf()) {
                    val spriteEntry = sprites.entryType(textureId) ?: continue
                    for (sprite in spriteEntry.sprites.filter(Sprite::renderable)) {
                        sprite.write(Path.of("$path/sprites/"), "${id}_${spriteEntry.id}_${sprite.id}")
                    }
                }
            }
            is TitleEntryType -> {
                ImageIO.write(ImageIO.read(ByteArrayInputStream(pixels!!)), "jpg", File(path.toString(), "title.jpg"))
            }
            is TitleScreenEntryType -> {
                if (sprites.entryType(id)?.sprites == null) return
                for (sprite in sprites.entryType(id)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(path, "${name}_${id}_${sprite.id}")
                }
            }
            is FontEntryType -> {
                json.encodeToStream(this, Path.of("$path/$id.json").outputStream())
                if (sprites.entryType(id)?.sprites == null) return
                for (sprite in sprites.entryType(id)?.sprites!!.filter(Sprite::renderable)) {
                    sprite.write(Path.of("$path/sprites/"), "${name}_${id}_${sprite.id}")
                }
            }
            is MusicEntryType -> {
                val name = if (name != null) "$name!!_$id" else "$id"
                bytes?.let { Files.write(Path.of(path.toString(), "$name.midi"), it) }
            }
            is InstrumentEntryType -> {
                val soundfont = SoundFont()
                soundfont.addSamples(this, addedInstruments)
                soundfont.sf2Soundbank.instruments.forEach(soundbank::addInstrument)
                soundfont.sf2Soundbank.resources.forEach(soundbank::addResource)
                soundfont.sf2Soundbank.save(File(path.toString(), "$id.sf2"))
            }
        }
    }

    private fun Sprite.write(path: Path, name: String) {
        if (path.notExists()) path.createDirectories()
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        image.setRGB(0, 0, width, height, pixels, 0, width)
        ImageIO.write(image, "png", File(path.toString(), "$name.png"))
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
