package xlitekt.cache.provider.soundeffect

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class SoundEffectEntryTypeProvider : EntryTypeProvider<SoundEffectEntryType>() {

    override fun load(): Map<Int, SoundEffectEntryType> = store
        .index(SOUND_EFFECT_INDEX)
        .groups()
        .map { ByteReadPacket(it.data).loadEntryType(SoundEffectEntryType(it.id)) }
        .associateBy(SoundEffectEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: SoundEffectEntryType): SoundEffectEntryType {
        type.instruments = Array(10) {
            if (tryPeek() != 0) SoundEffectInstrument(this)
            else {
                discard(1)
                null
            }
        }
        type.start = readUShort().toInt()
        type.end = readUShort().toInt()
        assertEmptyAndRelease()
        return type
    }
}
