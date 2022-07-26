package script.skill.woodcutting

enum class TreeType(
    val requiredLevel: Int,
    val experience: Double,
    val respawnTime: IntRange,
    val reward: Int,
    val stumpId: Int,
    val locIds: IntArray
) {
    Tree(
        requiredLevel = 1,
        experience = 25.0,
        respawnTime = 59..98,
        reward = 1511,
        stumpId = 1342,
        locIds = intArrayOf(1276, 1278)
    ),
    Oak(
        requiredLevel = 15,
        experience = 37.5,
        respawnTime = 5..5,
        reward = 1521,
        stumpId = 1344,
        locIds = intArrayOf(10820, 42395)
    ),
    Willow(
        requiredLevel = 30,
        experience = 67.5,
        respawnTime = 5..5,
        reward = 1519,
        stumpId = 9711,
        locIds = intArrayOf(10819, 10829, 10831, 10833)
    );
}

fun TreeType.getBronzeChance(): Pair<Int, Int> = when (this) {
    TreeType.Tree -> 64 to 200
    TreeType.Oak -> 32 to 100
    else -> 0 to 0
}

fun TreeType.getIronChance(): Pair<Int, Int> = when (this) {
    TreeType.Tree -> 96 to 300
    TreeType.Oak -> 48 to 150
    else -> 0 to 0
}

fun TreeType.getSteelChance(): Pair<Int, Int> = when (this) {
    TreeType.Tree -> 128 to 400
    TreeType.Oak -> 64 to 200
    else -> 0 to 0
}

fun TreeType.getBlackChance(): Pair<Int, Int> = when (this) {
    TreeType.Tree -> 144 to 450
    TreeType.Oak -> 72 to 225
    else -> 0 to 0
}

fun TreeType.getMithrilChance(): Pair<Int, Int> = when (this) {
    TreeType.Tree -> 160 to 500
    TreeType.Oak -> 80 to 250
    else -> 0 to 0
}

fun TreeType.getAdamantChance(): Pair<Int, Int> = when (this) {
    TreeType.Tree -> 192 to 600
    TreeType.Oak -> 96 to 300
    else -> 0 to 0
}

fun TreeType.getRuneChance(): Pair<Int, Int> = when (this) {
    TreeType.Tree -> 224 to 700
    TreeType.Oak -> 112 to 350
    else -> 0 to 0
}

fun TreeType.getDragonChance(): Pair<Int, Int> = when (this) {
    TreeType.Tree -> 240 to 750
    TreeType.Oak -> 120 to 375
    else -> 0 to 0
}

fun TreeType.getCrystalChance(): Pair<Int, Int> = when (this) {
    TreeType.Tree -> 250 to 800
    else -> 0 to 0
}

fun TreeType.alwaysFelled() = this == TreeType.Tree
