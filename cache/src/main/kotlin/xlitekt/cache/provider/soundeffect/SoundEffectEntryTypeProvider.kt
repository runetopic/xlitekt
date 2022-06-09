package xlitekt.cache.provider.soundeffect

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.buffer.tryPeek
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class SoundEffectEntryTypeProvider : EntryTypeProvider<SoundEffectEntryType>() {

    override fun load(): Map<Int, SoundEffectEntryType> = store
        .index(SOUND_EFFECT_INDEX)
        .groups()
        .map { ByteBuffer.wrap(it.data).loadEntryType(SoundEffectEntryType(it.id)) }
        .associateBy(SoundEffectEntryType::id)

    override fun ByteBuffer.loadEntryType(type: SoundEffectEntryType): SoundEffectEntryType {
        type.instruments = Array(10) {
            if (tryPeek() != 0) SoundEffectInstrument(this)
            else {
                discard(1)
                null
            }
        }
        type.start = readUShort()
        type.end = readUShort()
        assertEmptyAndRelease()
        return type
    }
}
