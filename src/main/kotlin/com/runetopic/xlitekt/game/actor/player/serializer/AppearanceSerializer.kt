package com.runetopic.xlitekt.game.actor.player.serializer

import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.actor.render.Render.Appearance.Gender
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.BodyPart
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.BodyPartColor
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
import java.util.EnumMap

/**
 * @author Jordan Abraham
 */
class AppearanceSerializer : KSerializer<Render.Appearance> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("appearance") {
            element<String>("gender")
            element<Map<String, Int>>("bodyParts")
            element<Map<String, Int>>("bodyPartColors")
        }

    override fun deserialize(decoder: Decoder): Render.Appearance = decoder.decodeStructure(descriptor) {
        val gender = decodeStringElement(descriptor, decodeElementIndex(descriptor))
        decodeElementIndex(descriptor)
        val bodyParts = EnumMap(decoder.decodeSerializableValue(MapSerializer(String.serializer(), Int.serializer())).map { BodyPart.valueOf(it.key) to it.value }.toMap())
        decodeElementIndex(descriptor)
        val bodyPartColors = EnumMap(decoder.decodeSerializableValue(MapSerializer(String.serializer(), Int.serializer())).map { BodyPartColor.valueOf(it.key) to it.value }.toMap())

        val appearance = Render.Appearance()
        appearance.gender = Gender.valueOf(gender)
        appearance.bodyParts.putAll(bodyParts)
        appearance.bodyPartColors.putAll(bodyPartColors)
        return appearance
    }

    override fun serialize(encoder: Encoder, value: Render.Appearance) = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.gender.name)
        encodeSerializableElement(descriptor, 1, MapSerializer(String.serializer(), Int.serializer()), value.bodyParts.map { it.key.name to it.value }.toMap())
        encodeSerializableElement(descriptor, 2, MapSerializer(String.serializer(), Int.serializer()), value.bodyPartColors.map { it.key.name to it.value }.toMap())
    }
}
