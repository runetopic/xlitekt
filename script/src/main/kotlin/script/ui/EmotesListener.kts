package script.ui

import xlitekt.game.ui.InterfaceEvent.CLICK_OPTION_1
import xlitekt.game.ui.UserInterface
import xlitekt.game.ui.onInterface
import xlitekt.shared.inject
import xlitekt.shared.resource.Sequences
import xlitekt.shared.resource.SpotAnimations

private enum class Emotes(
    val slotId: Int,
    val emoteName: String
) {
    YES(0, "yes"),
    NO(1, "no"),
    BOW(2, "bow"),
    ANGRY(3, "angry"),
    THINK(4, "think"),
    WAVE(5, "wave"),
    SHRUG(6, "shrug"),
    CHEER(7, "cheer"),
    BECKON(8, "beckon"),
    LAUGH(9, "laugh"),
    JUMP_FOR_JOY(10, "jump_for_joy"),
    YAWN(11, "yawn"),
    DANCE(12, "dance"),
    JIG(13, "jig"),
    SPIN(14, "spin"),
    HEAD_BANG(15, "head_bang"),
    CRY(16, "cry"),
    BLOW_KISS(17, "blow_kiss"),
    PANIC(18, "panic"),
    RASPBERRY(19, "raspberry"),
    CLAP(20, "clap"),
    SALUTE(21, "salute"),
    GOBLIN_BOW(22, "goblin_bow"),
    GOBLIN_SALUTE(23, "goblin_salute"),
    GLASS_BOX(24, "glass_box"),
    CLIMB_ROPE(25, "climb_rope"),
    LEAN(26, "lean"),
    GLASS_WALL(27, "glass_wall"),
    IDEA(28, "idea"),
    STAMP(29, "stamp"),
    FLAP(30, "flap"),
    SLAP_HEAD(31, "slap_head"),
    ZOMBIE_WALK(32, "zombie_walk"),
    ZOMBIE_DANCE(33, "zombie_dance"),
    SCARED(34, "scared"),
    RABBIT_HOP(35, "rabbit_hop"),
    SIT_UP(36, "sit_up"),
    PUSH_UP(37, "push_up"),
    STAR_JUMP(38, "star_jump"),
    JOG(39, "jog"),
    // TODO Flex.
    ZOMBIE_HAND(41, "zombie_hand"),
    HYPERMOBILE_DRINKER(42, "hypermobile_drinker"),
    // TODO Skillcape.
    AIR_GUITAR(44, "air_guitar"),
    // TODO Uri transform.
    SMOOTH_DANCE(46, "smooth_dance"),
    CRAZY_DANCE(47, "crazy_dance"),
    PREMIER_SHIELD(48, "premier_shield")
    // TODO Explore.
    // TODO Relic unlock.
}

private val sequences by inject<Sequences>()
private val spotAnimations by inject<SpotAnimations>()

onInterface<UserInterface.Emotes> {
    onOpen {
        setEvent(childId = 1, slots = 0..50, event = CLICK_OPTION_1)
    }

    onClick {
        val emote = enumValues<Emotes>().find { emote -> emote.slotId == it.slotId } ?: return@onClick
        val sequence = sequences[emote.emoteName] ?: return@onClick
        // Not every emote has a spot animation associated with it.
        val spotAnimation = spotAnimations[emote.emoteName]

        animate(sequence.id)
        if (spotAnimation != null) {
            // If the emote has an associated spot animation, use it here.
            spotAnimate(spotAnimation.id)
        }
    }
}
