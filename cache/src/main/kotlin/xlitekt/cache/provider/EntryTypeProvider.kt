package xlitekt.cache.provider

import com.runetopic.cache.store.Js5Store
import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUMedium
import xlitekt.shared.inject
import xlitekt.shared.toBoolean
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
abstract class EntryTypeProvider<R : EntryType> {
    internal val entries by lazy(::load)
    protected val store by inject<Js5Store>()

    protected abstract fun load(): Map<Int, R>
    protected abstract fun ByteBuffer.loadEntryType(type: R): R
    internal open fun postLoadEntryType(type: R) {}

    protected fun ByteBuffer.readStringIntParameters(): Map<Int, Any> = buildMap {
        repeat(readUByte()) {
            val usingString = readUByte().toBoolean()
            put(readUMedium(), if (usingString) readStringCp1252NullTerminated() else readInt())
        }
    }

    protected fun ByteBuffer.assertEmptyAndRelease() {
        check(remaining() == 0) { "The remaining buffer is not empty. Remaining=${remaining()}" }
    }

    /**
     * Folds a String to a RuneScape cache name hash.
     */
    protected fun String.toNameHash() = fold(0) { hash, next -> next.code + ((hash shl 5) - hash) }

    /**
     * Get the total number of [EntryType]s contained within this provider.
     *
     * @return [Int] The size.
     *
     * <b>Example</b>
     *
     * ```
     * val provider by inject<ObjEntryTypeProvider>()
     * val size = provider.size()
     * ```
     */
    fun size() = entries.size

    /**
     * Gets a [EntryType] with the given id.
     *
     * @return [EntryType]
     *
     * <b>Example</b>
     *
     * ```
     * val provider by inject<ObjEntryTypeProvider>()
     * val entry = provider.entryType(abyssal_whip_4151)
     * ```
     */
    fun entryType(id: Int) = entries[id]

    /**
     * Gets if the given id has an associated [EntryType].
     * This is widely used as a verifier for server logic as it is safe to compare against the cache information.
     *
     * @return [Boolean] True if this provider contains an [EntryType] with the given id.
     *
     * <b>Example</b>
     *
     * ```
     * val provider by inject<ObjEntryTypeProvider>()
     * val exists = provider.exists(abyssal_whip_4151)
     * ```
     */
    fun exists(id: Int) = entries.containsKey(id)

    /**
     * Gets a [Collection] of [EntryType]s contained within this provider.
     *
     * @return [Collection] of [EntryType]s.
     *
     * <b>Example</b>
     *
     * ```
     * val provider by inject<ObjEntryTypeProvider>()
     * val entries = provider.entries()
     * ```
     */
    fun entries() = entries.values

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
        const val DB_INDEXES_INDEX = 21

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
        const val DB_ROW_CONFIG = 38
        const val DB_TABLE_CONFIG = 39
    }
}
