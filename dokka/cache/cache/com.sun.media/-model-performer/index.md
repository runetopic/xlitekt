//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelPerformer](index.md)

# ModelPerformer

[jvm]\
open class [ModelPerformer](index.md)

This class is used to define how to synthesize audio in universal maner for both SF2 and DLS instruments.

#### Author

Karl Helgason

## Functions

| Name | Summary |
|---|---|
| [isDefaultConnectionsEnabled](is-default-connections-enabled.md) | [jvm]<br>open fun [isDefaultConnectionsEnabled](is-default-connections-enabled.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isReleaseTriggered](is-release-triggered.md) | [jvm]<br>open fun [isReleaseTriggered](is-release-triggered.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isSelfNonExclusive](is-self-non-exclusive.md) | [jvm]<br>open fun [isSelfNonExclusive](is-self-non-exclusive.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setDefaultConnectionsEnabled](set-default-connections-enabled.md) | [jvm]<br>open fun [setDefaultConnectionsEnabled](set-default-connections-enabled.md)(addDefaultConnections: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [setReleaseTriggered](set-release-triggered.md) | [jvm]<br>open fun [setReleaseTriggered](set-release-triggered.md)(value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [connectionBlocks](connection-blocks.md) | [jvm]<br>private open var [connectionBlocks](connection-blocks.md): [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[ModelConnectionBlock](../-model-connection-block/index.md)&gt; |
| [exclusiveClass](exclusive-class.md) | [jvm]<br>private open var [exclusiveClass](exclusive-class.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [keyFrom](key-from.md) | [jvm]<br>private open var [keyFrom](key-from.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [keyTo](key-to.md) | [jvm]<br>private open var [keyTo](key-to.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [name](name.md) | [jvm]<br>private open var [name](name.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [oscillators](oscillators.md) | [jvm]<br>private open val [oscillators](oscillators.md): [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[ModelOscillator](../-model-oscillator/index.md)&gt; |
| [selfNonExclusive](self-non-exclusive.md) | [jvm]<br>private open var [selfNonExclusive](self-non-exclusive.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [userObject](user-object.md) | [jvm]<br>private open var [userObject](user-object.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [velFrom](vel-from.md) | [jvm]<br>private open var [velFrom](vel-from.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [velTo](vel-to.md) | [jvm]<br>private open var [velTo](vel-to.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
