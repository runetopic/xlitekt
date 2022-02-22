package com.runetopic.xlitekt.game.actor.player.serializer

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.world.map.location.Location
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

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
        }

    override fun deserialize(decoder: Decoder): Player = decoder.decodeStructure(descriptor) {
        val username = decodeStringElement(descriptor, decodeElementIndex(descriptor))
        val password = decodeStringElement(descriptor, decodeElementIndex(descriptor))
        val rights = decodeIntElement(descriptor, decodeElementIndex(descriptor))
        val location = decodeSerializableElement(descriptor, decodeElementIndex(descriptor), LocationSerializer())
        val vars = decodeSerializableElement(descriptor, decodeElementIndex(descriptor), VarsSerializer())

        val player = Player(
            username = username,
            password = password,
            rights = rights,
            location = location
        )
        player.vars.putAll(vars)
        return player
    }

    override fun serialize(encoder: Encoder, value: Player) = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.username)
        encodeStringElement(descriptor, 1, value.password)
        encodeIntElement(descriptor, 2, value.rights)
        encodeSerializableElement(descriptor, 3, LocationSerializer(), value.location)
        encodeSerializableElement(descriptor, 4, VarsSerializer(), value.vars as Map<Int, Int>)
    }
}
