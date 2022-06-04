package xlitekt.game.actor.render.block.body

@JvmInline
value class BodyPartColor(private val packedBodyPartColor: Int) {
    val id get() = packedBodyPartColor shr 8
    val colorLength get() = packedBodyPartColor and 0xff

    companion object {
        val Hair = BodyPartColor(0 shl 8 or 23)
        val Torso = BodyPartColor(1 shl 8 or 27)
        val Legs = BodyPartColor(2 shl 8 or 27)
        val Feet = BodyPartColor(3 shl 8 or 5)
        val Skin = BodyPartColor(4 shl 8 or 7)

        private val map = mapOf(
            0 to Hair,
            1 to Torso,
            2 to Legs,
            3 to Feet,
            4 to Skin
        )

        internal fun findByKey(key: Int): BodyPartColor = map[key]!!
    }
}
