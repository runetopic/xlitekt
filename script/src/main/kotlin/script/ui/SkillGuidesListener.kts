import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface

onInterface<UserInterface.SkillGuide> {
    onOpen {
        setEvent(8, 0..99)
    }
}
