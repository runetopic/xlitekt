package com.runetopic.xlitekt.game.vars

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.mapSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * @author Jordan Abraham
 */
class VarsSerializer : KSerializer<Map<Var, Int>> {
    override fun deserialize(decoder: Decoder): Map<Var, Int> {
        return buildMap {
            decoder.decodeStructure(descriptor) {

            }
        }
    }

    override fun serialize(encoder: Encoder, value: Map<Var, Int>) {
        encoder.encodeStructure(descriptor) {
            value.forEach {
                encodeStringElement(descriptor, 0, it.key.name)
                encodeIntElement(descriptor, 1, it.value)
            }
        }
    }

    override val descriptor: SerialDescriptor
        get() = mapSerialDescriptor(String.serializer().descriptor, Int.serializer().descriptor)
}
