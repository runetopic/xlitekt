package xlitekt.game.ui

import xlitekt.cache.provider.config.enum.EnumEntryTypeProvider
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.item.Item
import xlitekt.game.packet.IfMoveSubPacket
import xlitekt.game.packet.IfOpenSubPacket
import xlitekt.game.packet.IfOpenTopPacket
import xlitekt.game.packet.UpdateContainerFullPacket
import xlitekt.game.packet.VarpSmallPacket
import xlitekt.game.ui.InterfaceMap.addInterfaceListener
import xlitekt.shared.inject
import xlitekt.shared.packInterface

/**
 * @author Tyler Telis
 */
class Interfaces(
    private val player: Player,
    private val interfaces: MutableList<UserInterface> = mutableListOf()
) : MutableList<UserInterface> by interfaces {
    val listeners = mutableListOf<UserInterfaceListener>()

    var currentInterfaceLayout = InterfaceLayout.FIXED

    fun login() {
        player.message("Welcome to Xlitekt.")
        openTop(currentInterfaceLayout.interfaceId)
        gameInterfaces.forEach(::openInterface)
        player.write(VarpSmallPacket(1737, -1)) // TODO TEMP until i write a var system
    }

    fun closeModal() {
        val openModal = currentModal() ?: return
        this -= openModal
    }

    fun closeInventory() {
        val openInventory = currentInventory() ?: return
        this -= openInventory
    }

    private fun modalOpen() = currentModal() != null

    private fun currentModal() = findLast { it.interfaceInfo.resizableChildId == MODAL_CHILD_ID || it.interfaceInfo.resizableChildId == MODAL_CHILD_ID_EXTENDED }
    private fun inventoryOpen() = currentInventory() != null

    private fun currentInventory() = findLast { it.interfaceInfo.resizableChildId == INVENTORY_CHILD_ID }

    private fun UserInterface.isModal() = interfaceInfo.resizableChildId == MODAL_CHILD_ID || interfaceInfo.resizableChildId == MODAL_CHILD_ID_EXTENDED
    private fun UserInterface.isInventory() = interfaceInfo.resizableChildId == INVENTORY_CHILD_ID

    operator fun plusAssign(userInterface: UserInterface) {
        if (modalOpen() && userInterface.isModal()) closeModal()
        if (inventoryOpen() && userInterface.isInventory()) closeInventory()
        this.add(userInterface)
        openInterface(userInterface)
    }

    operator fun minusAssign(userInterface: UserInterface) {
        this.remove(userInterface)
        closeInterface(userInterface)
    }

    fun switchLayout(toLayout: InterfaceLayout) {
        if (toLayout == currentInterfaceLayout) return
        openTop(toLayout.interfaceId)
        val switch = {
            closeModal()
            gameInterfaces.forEach { moveSub(it, toLayout) }
            currentInterfaceLayout = toLayout
        }
        when (currentModal()) {
            UserInterface.AdvancedSettings -> {
                switch()
                openInterface(UserInterface.AdvancedSettings)
            }
            else -> switch()
        }
    }

    fun setText(packedInterface: Int, text: String) = player.write(
        xlitekt.game.packet.IfSetTextPacket(
            packedInterface = packedInterface,
            text = text
        )
    )

    fun setEvent(packedInterface: Int, ifEvent: UserInterfaceEvent.IfEvent) = player.write(
        xlitekt.game.packet.IfSetEventsPacket(
            packedInterface = packedInterface,
            fromSlot = ifEvent.slots.first,
            toSlot = ifEvent.slots.last,
            event = ifEvent.event.value
        )
    )

    fun setContainerUpdateFull(containerKey: Int, interfaceId: Int, childId: Int = 65536, items: List<Item?>) {
        player.write(
            UpdateContainerFullPacket(
                packedInterface = interfaceId.packInterface(childId),
                containerKey = containerKey,
                items = items
            )
        )
    }

    private fun openTop(id: Int) = player.write(IfOpenTopPacket(interfaceId = id))

    private fun openInterface(userInterface: UserInterface) = userInterface.apply {
        add(userInterface)

        val derivedChildId = userInterface.interfaceInfo.resizableChildId
        val childId = derivedChildId.enumChildForLayout(
            currentInterfaceLayout
        )

        val listener = addInterfaceListener(this, player)

        listeners += listener

        listener.init(
            UserInterfaceEvent.CreateEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )

        player.write(
            IfOpenSubPacket(
                interfaceId = interfaceInfo.id,
                toPackedInterface = currentInterfaceLayout.interfaceId.packInterface(childId),
                alwaysOpen = true
            )
        )

        listener.open(
            UserInterfaceEvent.OpenEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )
    }

    private fun closeInterface(userInterface: UserInterface) = userInterface.apply {
        remove(userInterface)

        val childId = userInterface.interfaceInfo.resizableChildId

        player.write(
            xlitekt.game.packet.IfCloseSubPacket(
                packedInterface = currentInterfaceLayout.interfaceId.packInterface(childId.enumChildForLayout(currentInterfaceLayout))
            )
        )

        val listener = listeners.find { listener -> listener.userInterface == this } ?: return@apply

        listener.close(
            UserInterfaceEvent.CloseEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )

        listeners.remove(listener)
    }

    private fun moveSub(userInterface: UserInterface, toLayout: InterfaceLayout) = userInterface.apply {
        val derivedChildId = userInterface.interfaceInfo.resizableChildId
        val fromChildId = derivedChildId.enumChildForLayout(currentInterfaceLayout)
        val toChildId = derivedChildId.enumChildForLayout(toLayout)
        val listener = listeners.find { listener -> listener.userInterface == this }

        listener?.init(
            UserInterfaceEvent.CreateEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )

        player.write(
            IfMoveSubPacket(
                fromPackedInterface = currentInterfaceLayout.interfaceId.packInterface(fromChildId),
                toPackedInterface = toLayout.interfaceId.packInterface(toChildId)
            )
        )

        listener?.open(
            UserInterfaceEvent.OpenEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )
    }

    private fun Int.enumChildForLayout(layout: InterfaceLayout): Int =
        enums.entryType(layout.enumId)
            ?.params
            ?.entries
            ?.find { it.key == InterfaceLayout.RESIZABLE.interfaceId.packInterface(this) }
            ?.value as Int and 0xffff

    private companion object {
        val gameInterfaces = setOf(
            UserInterface.AccountManagement,
            UserInterface.Settings,
            UserInterface.Inventory,
            UserInterface.MiniMap,
            UserInterface.ChatBox,
            UserInterface.Logout,
            UserInterface.Emotes,
            UserInterface.Magic,
            UserInterface.MusicPlayer,
            UserInterface.Skills,
            UserInterface.WornEquipment,
            UserInterface.Friends,
            UserInterface.Prayer,
            UserInterface.CombatOptions,
            UserInterface.CharacterSummary,
            UserInterface.UnknownOverlay,
            UserInterface.ChatChannel
        )

        const val MODAL_CHILD_ID = 16
        const val MODAL_CHILD_ID_EXTENDED = 17
        const val INVENTORY_CHILD_ID = 73

        val enums by inject<EnumEntryTypeProvider>()
    }
}
