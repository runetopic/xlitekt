//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelPatch](index.md)

# ModelPatch

[jvm]\
open class [ModelPatch](index.md) : [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html)

A extended patch object that has isPercussion function. Which is necessary to identify percussion instruments from melodic instruments.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [ModelPatch](-model-patch.md) | [jvm]<br>open fun [ModelPatch](-model-patch.md)(bank: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), program: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [ModelPatch](-model-patch.md) | [jvm]<br>open fun [ModelPatch](-model-patch.md)(bank: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), program: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), percussion: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [getBank](index.md#-1260148407%2FFunctions%2F-82533025) | [jvm]<br>open fun [getBank](index.md#-1260148407%2FFunctions%2F-82533025)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [getProgram](index.md#-1783886735%2FFunctions%2F-82533025) | [jvm]<br>open fun [getProgram](index.md#-1783886735%2FFunctions%2F-82533025)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isPercussion](is-percussion.md) | [jvm]<br>open fun [isPercussion](is-percussion.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
