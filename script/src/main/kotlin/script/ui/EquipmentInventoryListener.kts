import xlitekt.game.actor.player.script
import xlitekt.game.content.container.inventory.INVENTORY_CONTAINER_KEY
import xlitekt.game.content.ui.InterfaceEvent
import xlitekt.game.content.ui.InterfaceListener
import xlitekt.game.content.ui.UserInterface
import xlitekt.shared.insert

insert<InterfaceListener>().userInterface<UserInterface.EquipmentInventory> {
    onOpen {
        setEvent(
            childId = 0,
            slots = 0..27,
            InterfaceEvent.ClickOption1,
            InterfaceEvent.ClickOption10,
            InterfaceEvent.DragDepth1,
            InterfaceEvent.DragTargetable
        )
        script(149, "", "", "", "Equip", -1, -1, 7, 4, INVENTORY_CONTAINER_KEY, UserInterface.EquipmentInventory.interfaceInfo.id shl 16)
    }
}
