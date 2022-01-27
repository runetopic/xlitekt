package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.HintArrowType

/**
 * @author Jordan Abraham
 */
data class HintArrowPacket(
    val type: HintArrowType,
    val targetIndex: Int,
    val targetX: Int,
    val targetZ: Int,
    val targetHeight: Int
) : Packet {
    constructor(type: HintArrowType, pid: Int) : this(type, pid, -1, -1, -1)
    constructor(type: HintArrowType, targetX: Int, targetZ: Int, targetHeight: Int) : this(type, -1, targetX, targetZ, targetHeight)
}
