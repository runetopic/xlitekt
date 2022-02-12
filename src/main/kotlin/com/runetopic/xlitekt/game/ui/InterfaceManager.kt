package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.event.impl.IfEvent
import com.runetopic.xlitekt.network.packet.IfCloseSubPacket
import com.runetopic.xlitekt.network.packet.IfMoveSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.IfSetEventsPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.ext.packInterface

/**
 * @author Tyler Telis
 */
class InterfaceManager(
    private val player: Player
) {
    private val open = mutableMapOf<Int, Int>()
    private val eventBus by inject<EventBus>()

    var currentLayout = Layout.FIXED

    fun login() {
        player.client.writePacket(VarpSmallPacket(1737, -1)) // TODO TEMP until i write a var system
        sendLayout()
        message("Welcome to Xlitekt.")
    }

    private fun sendLayout() {
        openTop(currentLayout.interfaceId)
        InterfaceInfo.values().forEach {
            val interfaceId = it.interfaceId
            if (interfaceId == -1) return@forEach
            val componentId = it.childIdForLayout(currentLayout)
            if (componentId == -1) return@forEach
            openSub(it.interfaceId, componentId, true)
        }
    }

    fun switchLayout(mode: Layout) {
        if (mode == currentLayout) return

        closeTop(currentLayout.interfaceId)

        val previousLayout = this.currentLayout
        this.currentLayout = mode

        openTop(currentLayout.interfaceId)

        InterfaceInfo.values().forEach {
            val fromInterfaceId = previousLayout.interfaceId
            val fromChildId = it.childIdForLayout(previousLayout)
            if (fromChildId == -1) return@forEach
            val toInterfaceId = mode.interfaceId
            val toChildId = it.childIdForLayout(mode)
            if (toChildId == -1) return@forEach
            if (moveSub(fromInterfaceId, fromChildId, toInterfaceId, toChildId)) open.remove(fromInterfaceId)
        }
    }

    private fun moveSub(fromInterfaceId: Int, fromChildId: Int, toInterfaceId: Int, toChildId: Int): Boolean {
        val packedFromInterface = fromInterfaceId.packInterface(fromChildId)
        val packedToInterface = toInterfaceId.packInterface(toChildId)
        if (open.containsKey(packedFromInterface)) {
            open(packedToInterface, open[packedFromInterface]!!)
            player.client.writePacket(IfMoveSubPacket(packedFromInterface, packedToInterface))
            return true
        }
        return false
    }

    private fun open(packed: Int, interfaceId: Int): Boolean {
        if (open.containsKey(packed)) return false
        open[packed] = interfaceId
        return true
    }

    private fun openTop(interfaceId: Int) {
        val packed = interfaceId.packInterface()
        if (open(packed, interfaceId)) {
            eventBus.notify(IfEvent.IfOpenEvent(player, interfaceId, 0, true))
            player.client.writePacket(IfOpenTopPacket(interfaceId))
        }
    }

    fun closeTop(interfaceId: Int) {
        val packed = interfaceId.packInterface()
        if (open.containsKey(packed)) {
            closeSub(packed)
        }
    }

    private fun openSub(interfaceId: Int, childId: Int, alwaysOpen: Boolean) {
        val packed = currentLayout.interfaceId.packInterface(childId)
        if (open(packed, interfaceId)) {
            eventBus.notify(
                IfEvent.IfOpenEvent(
                    player,
                    interfaceId,
                    childId,
                    alwaysOpen
                )
            )
            player.client.writePacket(
                IfOpenSubPacket(
                    interfaceId,
                    packed,
                    alwaysOpen
                )
            )
        }
    }

    private fun closeSub(packedInterface: Int) {
        open.remove(packedInterface)
        player.client.writePacket(IfCloseSubPacket(packedInterface))
    }

    fun message(message: String) {
        player.client.writePacket(MessageGamePacket(0, message, false))
    }

    fun interfaceEvents(interfaceId: Int, childId: Int, fromSlot: Int, toSlot: Int, events: InterfaceEvent) {
        player.client.writePacket(
            IfSetEventsPacket(
                interfaceId,
                childId,
                fromSlot,
                toSlot,
                events.value
            )
        )
    }

    fun clientScript(scriptId: Int, parameters: List<Any>) = player.client.writePacket(RunClientScriptPacket(scriptId, parameters))
}
