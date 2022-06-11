package script.skill.woodcutting

import xlitekt.game.content.item.Item

enum class TreeType(
    val requiredLevel: Int,
    val experience: Double,
    val respawnTime: IntRange,
    val reward: Item,
    val stumpId: Int,
    val locIds: IntArray
) {

    Tree(
        requiredLevel = 1,
        experience = 25.0,
        respawnTime = 59..98,
        reward = Item(1511, 1),
        stumpId = 1342,
        locIds = intArrayOf(1278)
    ),
    TirannwnTree(
        requiredLevel = 1,
        experience = 25.0,
        respawnTime = 59..98,
        reward = Item(1511, 1),
        stumpId = 1342,
        locIds = intArrayOf(1278)
    );
}
