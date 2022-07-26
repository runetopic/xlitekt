package xlitekt.game.actor.render.block.body

/**
 * @author Jordan Abraham
 */
data class BodyPartBlock(
    val bodyPart: BodyPart?,
    val builder: BodyPartBuilder.() -> Unit
)
