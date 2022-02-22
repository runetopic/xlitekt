package com.runetopic.xlitekt.game.actor.player

import com.runetopic.xlitekt.game.vars.VarsSerializer
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
    override fun deserialize(decoder: Decoder): Player {
        decoder.decodeStructure(descriptor) {
            val username = decodeStringElement(descriptor, 0)
            val rights = decodeIntElement(descriptor, 1)
            val x = decodeIntElement(descriptor, 2)
            val z = decodeIntElement(descriptor, 3)
            val level = decodeIntElement(descriptor, 4)
            return Player(username)
        }
    }

    override fun serialize(encoder: Encoder, value: Player) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.username)
            encodeIntElement(descriptor, 1, value.rights)
            encodeIntElement(descriptor, 2, value.location.x)
            encodeIntElement(descriptor, 3, value.location.z)
            encodeIntElement(descriptor, 4, value.location.level)
            encodeSerializableElement(descriptor, 5, VarsSerializer(), value.vars)
        }
    }

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("player") {
            element<String>("username")
            element<Int>("rights")
            element<Int>("x")
            element<Int>("z")
            element<Int>("level")
            element<Map<String, Int>>("vars")
        }
}
