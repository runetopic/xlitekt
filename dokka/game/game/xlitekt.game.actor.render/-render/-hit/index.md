//[game](../../../../index.md)/[xlitekt.game.actor.render](../../index.md)/[Render](../index.md)/[Hit](index.md)

# Hit

[jvm]\
data class [Hit](index.md)(val actor: [Actor](../../../xlitekt.game.actor/-actor/index.md), val splats: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitSplat](../../-hit-splat/index.md)&gt;, val bars: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitBar](../../-hit-bar/index.md)&gt;, val tint: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [Render](../index.md)

## Constructors

| | |
|---|---|
| [Hit](-hit.md) | [jvm]<br>fun [Hit](-hit.md)(actor: [Actor](../../../xlitekt.game.actor/-actor/index.md), hits: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitSplat](../../-hit-splat/index.md)&gt;, bars: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitBar](../../-hit-bar/index.md)&gt;) |
| [Hit](-hit.md) | [jvm]<br>fun [Hit](-hit.md)(actor: [Actor](../../../xlitekt.game.actor/-actor/index.md), splats: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitSplat](../../-hit-splat/index.md)&gt;, bars: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitBar](../../-hit-bar/index.md)&gt;, tint: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [hasAlternative](../has-alternative.md) | [jvm]<br>fun [hasAlternative](../has-alternative.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if a render has an alternative update. |

## Properties

| Name | Summary |
|---|---|
| [actor](actor.md) | [jvm]<br>val [actor](actor.md): [Actor](../../../xlitekt.game.actor/-actor/index.md) |
| [bars](bars.md) | [jvm]<br>val [bars](bars.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitBar](../../-hit-bar/index.md)&gt; |
| [splats](splats.md) | [jvm]<br>val [splats](splats.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitSplat](../../-hit-splat/index.md)&gt; |
| [tint](tint.md) | [jvm]<br>val [tint](tint.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
