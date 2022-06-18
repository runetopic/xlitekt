package xlitekt.game.actor.render.block.body

/**
 * @author Jordan Abraham
 */
class BodyPartBlockListener : MutableMap<Int, BodyPartBlock> by mutableMapOf() {
    fun bodyPartBlock(index: Int, bodyPart: BodyPart? = null, builder: BodyPartBuilder.() -> Unit) {
        this[index] = BodyPartBlock(bodyPart, builder)
    }
}
