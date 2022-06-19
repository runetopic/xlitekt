//[game](../../../index.md)/[xlitekt.game.content.command](../index.md)/[Command](index.md)

# Command

[jvm]\
class [Command](index.md)(val command: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), var filter: [Player](../../xlitekt.game.actor.player/-player/index.md).() -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = { true }, var use: [Player](../../xlitekt.game.actor.player/-player/index.md).([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {})

Tyler Telis

## Constructors

| | |
|---|---|
| [Command](-command.md) | [jvm]<br>fun [Command](-command.md)(command: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), filter: [Player](../../xlitekt.game.actor.player/-player/index.md).() -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = { true }, use: [Player](../../xlitekt.game.actor.player/-player/index.md).([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}) |

## Functions

| Name | Summary |
|---|---|
| [filter](filter.md) | [jvm]<br>fun [filter](filter.md)(filter: [Player](../../xlitekt.game.actor.player/-player/index.md).() -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [Command](index.md) |
| [use](use.md) | [jvm]<br>fun [use](use.md)(function: [Player](../../xlitekt.game.actor.player/-player/index.md).([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Command](index.md) |

## Properties

| Name | Summary |
|---|---|
| [command](command.md) | [jvm]<br>val [command](command.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [filter](filter.md) | [jvm]<br>var [filter](filter.md): [Player](../../xlitekt.game.actor.player/-player/index.md).() -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [use](use.md) | [jvm]<br>var [use](use.md): [Player](../../xlitekt.game.actor.player/-player/index.md).([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
