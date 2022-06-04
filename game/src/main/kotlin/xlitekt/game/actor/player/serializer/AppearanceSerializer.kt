package xlitekt.game.actor.player.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.Render.Appearance.Gender
import xlitekt.game.actor.render.block.body.BodyPart
import xlitekt.game.actor.render.block.body.BodyPartColor

/**
 * @author Jordan Abraham
 */
class AppearanceSerializer : KSerializer<Render.Appearance> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("appearance") {
            element<String>("gender")
            element<Map<String, Int>>("bodyParts")
            element<Map<String, Int>>("bodyPartColors")
            element<String>("displayName")
        }

    override fun deserialize(decoder: Decoder): Render.Appearance = decoder.decodeStructure(descriptor) {
        val gender = decodeIntElement(descriptor, decodeElementIndex(descriptor))
        decodeElementIndex(descriptor)
        val bodyParts = decoder.decodeSerializableValue(MapSerializer(Int.serializer(), Int.serializer())).map { BodyPart(it.key) to it.value }.toMap()
        decodeElementIndex(descriptor)
        val bodyPartColors = decoder.decodeSerializableValue(MapSerializer(Int.serializer(), Int.serializer())).map { BodyPartColor.findByKey(it.key) to it.value }.toMap()
        val displayName = decodeStringElement(descriptor, decodeElementIndex(descriptor))

        val appearance = Render.Appearance()
        appearance.gender = Gender(gender)
        appearance.bodyParts.putAll(bodyParts)
        appearance.bodyPartColors.putAll(bodyPartColors)
        appearance.displayName = displayName
        return@decodeStructure appearance
    }

    override fun serialize(encoder: Encoder, value: Render.Appearance) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value.gender.id)
        encodeSerializableElement(descriptor, 1, MapSerializer(Int.serializer(), Int.serializer()), value.bodyParts.map { it.key.id to it.value }.toMap())
        encodeSerializableElement(descriptor, 2, MapSerializer(Int.serializer(), Int.serializer()), value.bodyPartColors.map { it.key.id to it.value }.toMap())
        encodeStringElement(descriptor, 3, value.displayName)
    }
}
