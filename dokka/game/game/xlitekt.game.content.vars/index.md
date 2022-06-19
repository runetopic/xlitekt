//[game](../../index.md)/[xlitekt.game.content.vars](index.md)

# Package xlitekt.game.content.vars

## Types

| Name | Summary |
|---|---|
| [Var](-var/index.md) | [jvm]<br>abstract class [Var](-var/index.md)(val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [VarBit](-var-bit/index.md) | [jvm]<br>sealed class [VarBit](-var-bit/index.md) : [Var](-var/index.md) |
| [VarPlayer](-var-player/index.md) | [jvm]<br>sealed class [VarPlayer](-var-player/index.md) : [Var](-var/index.md) |
| [Vars](-vars/index.md) | [jvm]<br>class [Vars](-vars/index.md)(player: [Player](../xlitekt.game.actor.player/-player/index.md), vars: [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; = mutableMapOf()) : [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; |
| [VarsMap](-vars-map/index.md) | [jvm]<br>object [VarsMap](-vars-map/index.md) |
| [VarType](-var-type/index.md) | [jvm]<br>enum [VarType](-var-type/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[VarType](-var-type/index.md)&gt; |
