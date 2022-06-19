//[game](../../../../index.md)/[xlitekt.game.actor.render](../../index.md)/[Render](../index.md)/[Appearance](index.md)

# Appearance

[jvm]\
class [Appearance](index.md) : [Render](../index.md)

## Constructors

| | |
|---|---|
| [Appearance](-appearance.md) | [jvm]<br>fun [Appearance](-appearance.md)() |

## Types

| Name | Summary |
|---|---|
| [Gender](-gender/index.md) | [jvm]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)<br>value class [Gender](-gender/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [hasAlternative](../has-alternative.md) | [jvm]<br>fun [hasAlternative](../has-alternative.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if a render has an alternative update. |
| [isFemale](is-female.md) | [jvm]<br>fun [isFemale](is-female.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isMale](is-male.md) | [jvm]<br>fun [isMale](is-male.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

## Properties

| Name | Summary |
|---|---|
| [bodyPartColors](body-part-colors.md) | [jvm]<br>val [bodyPartColors](body-part-colors.md): [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[BodyPartColor](../../../xlitekt.game.actor.render.block.body/-body-part-color/index.md), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; |
| [bodyParts](body-parts.md) | [jvm]<br>val [bodyParts](body-parts.md): [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[BodyPart](../../../xlitekt.game.actor.render.block.body/-body-part/index.md), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; |
| [displayName](display-name.md) | [jvm]<br>var [displayName](display-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [equipment](equipment.md) | [jvm]<br>lateinit var [equipment](equipment.md): [Equipment](../../../xlitekt.game.content.container.equipment/-equipment/index.md) |
| [gender](gender.md) | [jvm]<br>var [gender](gender.md): [Render.Appearance.Gender](-gender/index.md) |
| [headIcon](head-icon.md) | [jvm]<br>var [headIcon](head-icon.md): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; |
| [hidden](hidden.md) | [jvm]<br>var [hidden](hidden.md): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [skullIcon](skull-icon.md) | [jvm]<br>var [skullIcon](skull-icon.md): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; |
| [transform](transform.md) | [jvm]<br>var [transform](transform.md): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; |
