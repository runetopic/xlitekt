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
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.skill.Skills
import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
class PlayerSerializer : KSerializer<Player> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("player") {
            element<String>("username")
            element<String>("password")
            element<Int>("rights")
            element<Location>("location")
            element<Map<Int, Int>>("vars")
            element<Render.Appearance>("appearance")
            element<Float>("runEnergy")
            element<Skills>("skills")
        }

    override fun deserialize(decoder: Decoder): Player = decoder.decodeStructure(descriptor) {
        val username = decodeStringElement(descriptor, decodeElementIndex(descriptor))
        val password = decodeStringElement(descriptor, decodeElementIndex(descriptor))
        val rights = decodeIntElement(descriptor, decodeElementIndex(descriptor))
        val location = decodeSerializableElement(descriptor, decodeElementIndex(descriptor), LocationSerializer())
        val vars = decodeSerializableElement(descriptor, decodeElementIndex(descriptor), MapSerializer(Int.serializer(), Int.serializer()))
        val appearance = decodeSerializableElement(descriptor, decodeElementIndex(descriptor), AppearanceSerializer())
        val runEnergy = decodeFloatElement(descriptor, decodeElementIndex(descriptor))
        val skills = decodeSerializableElement(descriptor, decodeElementIndex(descriptor), SkillsSerializer())

        val player = Player(
            location = location,
            username = username,
            password = password,
            runEnergy = runEnergy,
            rights = rights,
            appearance = appearance,
            skills = skills
        )
        player.vars.putAll(vars)
        return player
    }

    override fun serialize(encoder: Encoder, value: Player) = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.username)
        encodeStringElement(descriptor, 1, value.password)
        encodeIntElement(descriptor, 2, value.rights)
        encodeSerializableElement(descriptor, 3, LocationSerializer(), value.location)
        encodeSerializableElement(descriptor, 4, MapSerializer(Int.serializer(), Int.serializer()), value.vars)
        encodeSerializableElement(descriptor, 5, AppearanceSerializer(), value.appearance)
        encodeFloatElement(descriptor, 6, value.runEnergy)
        encodeSerializableElement(descriptor, 7, SkillsSerializer(), value.skills)
    }
}
