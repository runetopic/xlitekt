package com.runetopic.xlitekt.game.actor.player.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.mapSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * @author Jordan Abraham
 */
class VarsSerializer : KSerializer<Map<Int, Int>> {
    override val descriptor: SerialDescriptor
        get() = mapSerialDescriptor(Int.serializer().descriptor, Int.serializer().descriptor)
    override fun deserialize(decoder: Decoder): Map<Int, Int> = decoder.decodeSerializableValue(MapSerializer(Int.serializer(), Int.serializer()))
    override fun serialize(encoder: Encoder, value: Map<Int, Int>) = encoder.encodeSerializableValue(MapSerializer(Int.serializer(), Int.serializer()), value)
}
