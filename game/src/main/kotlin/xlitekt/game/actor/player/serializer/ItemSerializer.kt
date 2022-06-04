package xlitekt.game.actor.player.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import xlitekt.game.content.item.Item

/**
 * @author Jordan Abraham
 */
class ItemSerializer : KSerializer<Item?> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("item") {
            element<Int>("id")
            element<Int>("amount")
        }

    override fun deserialize(decoder: Decoder): Item? = decoder.decodeStructure(descriptor) {
        val id = decodeIntElement(descriptor, decodeElementIndex(descriptor))
        val amount = decodeIntElement(descriptor, decodeElementIndex(descriptor))
        if (id == -1 && amount == -1) return@decodeStructure null
        return@decodeStructure Item(id, amount)
    }

    override fun serialize(encoder: Encoder, value: Item?) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value?.id ?: -1)
        encodeIntElement(descriptor, 1, value?.amount ?: -1)
    }
}
