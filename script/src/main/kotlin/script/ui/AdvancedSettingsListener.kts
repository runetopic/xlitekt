package script.ui

import xlitekt.game.content.ui.InterfaceEvent
import xlitekt.game.content.ui.UserInterface.AdvancedSettings
import xlitekt.game.content.ui.UserInterfaceEvent
import xlitekt.game.content.ui.onInterface
import xlitekt.game.content.vars.VarBit
import xlitekt.game.content.vars.VarPlayer
import xlitekt.game.content.vars.Vars

private val closeSettingsChildId = 4
private val firstLayerChildId = 19
private val secondLayerChildId = 21
private val categoriesChildId = 23

/**
 * @author Jordan Abraham
 */
enum class Categories(val id: Int) {
    ACTIVITIES(0),
    AUDIO(1),
    CHAT(2),
    CONTROLS(3),
    DISPLAY(4),
    GAMEPLAY(5),
    INTERFACES(6),
    WARNINGS(7)
}

onInterface<AdvancedSettings> {
    onOpen {
        setEvent(21, 0..147, InterfaceEvent.CLICK_OPTION_1)
        setEvent(23, 0..7, InterfaceEvent.CLICK_OPTION_1)
        setEvent(19, 0..240, InterfaceEvent.CLICK_OPTION_1)
        setEvent(28, 0..122, InterfaceEvent.CLICK_OPTION_1)
    }

    onClick(childId = closeSettingsChildId) {
        interfaces -= AdvancedSettings
    }

    onClick(childId = categoriesChildId) {
        enumValues<Categories>().find { category -> category.id == it.slotId }?.run {
            vars[VarBit.AdvancedSettingsCategory] = id
        }
    }

    onClick(childId = firstLayerChildId) {
        enumValues<Categories>().find { category -> category.id == vars[VarBit.AdvancedSettingsCategory] }?.run {
            when (this) {
                Categories.ACTIVITIES -> it.onActivitiesClick(vars = this@onClick.vars)
                Categories.AUDIO -> it.onAudioClick(vars = this@onClick.vars)
                Categories.CHAT -> it.onChatClick(vars = this@onClick.vars)
                Categories.CONTROLS -> it.onControlsClick(vars = this@onClick.vars)
                Categories.DISPLAY -> it.onDisplayClick(vars = this@onClick.vars)
                Categories.GAMEPLAY -> it.onGameplayClick(vars = this@onClick.vars)
                Categories.INTERFACES -> it.onInterfacesClick(vars = this@onClick.vars)
                Categories.WARNINGS -> it.onWarningsClick(vars = this@onClick.vars)
            }
        }
    }
}

fun UserInterfaceEvent.ButtonClickEvent.onActivitiesClick(vars: Vars) {
    when (slotId) {
        29 -> vars.flip { VarBit.HitSplatTinting }
    }
}

fun UserInterfaceEvent.ButtonClickEvent.onAudioClick(vars: Vars) {
}

fun UserInterfaceEvent.ButtonClickEvent.onChatClick(vars: Vars) {
    when (slotId) {
        1 -> vars.flip { VarPlayer.ProfanityFilter }
        3 -> vars.flip { VarPlayer.ChatEffects }
        4 -> vars.flip { VarPlayer.SplitFriendsPrivateChat }
        5 -> if (VarPlayer.SplitFriendsPrivateChat in vars) {
            vars.flip { VarBit.HidePrivateChatWhenChatboxHidden }
        }
    }
}

fun UserInterfaceEvent.ButtonClickEvent.onControlsClick(vars: Vars) {
    when (slotId) {
        4 -> vars.flip { VarPlayer.SingleMouseButtonMode }
        5 -> vars.flip { VarBit.MiddleMouseButtonCameraControl }
        6 -> vars.flip { VarBit.ShiftClickDropItems }
        8 -> vars.flip { VarBit.MoveFollowerOptionsLowerDown }
        30 -> vars.flip { VarBit.EscClosesCurrentModal }
    }
}

fun UserInterfaceEvent.ButtonClickEvent.onDisplayClick(vars: Vars) {
    when (childId) {
        firstLayerChildId -> when (slotId) {
            5 -> vars.flip { VarBit.ScrollWheelChangesZoomDistance }
        }
        secondLayerChildId -> when (slotId) {
            0, 5, 10, 15, 20 -> {
                vars[VarPlayer.ScreenBrightness] = slotId / 5
            }
        }
    }
}

fun UserInterfaceEvent.ButtonClickEvent.onGameplayClick(vars: Vars) {
}

fun UserInterfaceEvent.ButtonClickEvent.onInterfacesClick(vars: Vars) {
}

fun UserInterfaceEvent.ButtonClickEvent.onWarningsClick(vars: Vars) {
}
