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
import xlitekt.game.content.skill.Skill
import xlitekt.game.content.skill.Skills

/**
 * @author Jordan Abraham
 */
class SkillsSerializer : KSerializer<Skills> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("skills") {
            element<Map<String, Int>>("levels")
            element<Map<String, Double>>("experience")
        }

    override fun deserialize(decoder: Decoder): Skills = decoder.decodeStructure(descriptor) {
        decodeElementIndex(descriptor)
        val levels = decodeSerializableElement(
            descriptor,
            0,
            MapSerializer(String.serializer(), Int.serializer()),
        ).values.toIntArray()
        decodeElementIndex(descriptor)
        val experience = decodeSerializableElement(
            descriptor,
            1,
            MapSerializer(String.serializer(), Double.serializer()),
        ).values.toDoubleArray()
        return@decodeStructure Skills(levels, experience)
    }

    override fun serialize(encoder: Encoder, value: Skills) = encoder.encodeStructure(descriptor) {
        encodeSerializableElement(
            descriptor,
            0,
            MapSerializer(String.serializer(), Int.serializer()),
            Skill.values().associate {
                it.name to value.levels[it.id]
            }
        )
        encodeSerializableElement(
            descriptor,
            1,
            MapSerializer(String.serializer(), Double.serializer()),
            Skill.values().associate {
                it.name to value.experience[it.id]
            }
        )
    }
}
