package xlitekt.cache.provider

import com.runetopic.cache.store.Js5Store
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.buffer.readUMedium
import xlitekt.shared.inject
import xlitekt.shared.toBoolean

abstract class EntryTypeProvider<R : EntryType> {
    internal val entries by lazy(::load)
    protected val store by inject<Js5Store>()

    protected abstract fun load(): Map<Int, R>
    protected abstract fun ByteReadPacket.loadEntryType(type: R): R
    internal open fun postLoadEntryType(type: R) {}

    fun size() = entries.size
    fun entryType(id: Int): R? = entries[id]
    fun entries(): Collection<R> = entries.values

    protected fun ByteReadPacket.readStringIntParameters(): Map<Int, Any> = buildMap {
        repeat(readUByte().toInt()) {
            val usingString = readUByte().toInt().toBoolean()
            put(readUMedium(), if (usingString) readStringCp1252NullTerminated() else readInt())
        }
    }

    protected fun ByteReadPacket.assertEmptyAndRelease() {
        check(remaining.toInt() == 0) { "The remaining buffer is not empty." }
        release()
    }

    protected fun String.toNameHash(): Int = fold(0) { hash, next -> next.code + ((hash shl 5) - hash) }

    internal companion object {
        // Indexes.
        const val CONFIG_INDEX = 2
        const val INTERFACE_INDEX = 3
        const val SOUND_EFFECT_INDEX = 4
        const val MAP_INDEX = 5
        const val MUSIC_INDEX = 6
        const val SPRITE_INDEX = 8
        const val TEXTURE_INDEX = 9
        const val BINARY_INDEX = 10
        const val FONT_INDEX = 13
        const val VORBIS_INDEX = 14
        const val INSTRUMENT_INDEX = 15

        // Config groups.
        const val FLOOR_UNDERLAY_CONFIG = 1
        const val KIT_CONFIG = 3
        const val FLOOR_OVERLAY_CONFIG = 4
        const val INV_CONFIG = 5
        const val LOC_CONFIG = 6
        const val ENUM_CONFIG = 8
        const val NPC_CONFIG = 9
        const val OBJ_CONFIG = 10
        const val PARAM_CONFIG = 11
        const val SEQUENCE_CONFIG = 12
        const val SPOT_ANIMATION_CONFIG = 13
        const val VARBIT_CONFIG = 14
        const val VARP_CONFIG = 16
        const val VARC_CONFIG = 19
        const val HITSPLAT_CONFIG = 32
        const val HITBAR_CONFIG = 33
        const val STRUCT_CONFIG = 34
        const val WORLD_MAP_CONFIG = 35
    }
}
