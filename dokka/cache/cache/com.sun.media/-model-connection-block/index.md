//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelConnectionBlock](index.md)

# ModelConnectionBlock

[jvm]\
open class [ModelConnectionBlock](index.md)

Connection blocks are used to connect source variable to a destination variable. For example Note On velocity can be connected to output gain. In DLS this is called articulator and in SoundFonts (SF2) a modulator.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [ModelConnectionBlock](-model-connection-block.md) | [jvm]<br>open fun [ModelConnectionBlock](-model-connection-block.md)() |
| [ModelConnectionBlock](-model-connection-block.md) | [jvm]<br>open fun [ModelConnectionBlock](-model-connection-block.md)(scale: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), destination: [ModelDestination](../-model-destination/index.md)) |
| [ModelConnectionBlock](-model-connection-block.md) | [jvm]<br>open fun [ModelConnectionBlock](-model-connection-block.md)(source: [ModelSource](../-model-source/index.md), destination: [ModelDestination](../-model-destination/index.md)) |
| [ModelConnectionBlock](-model-connection-block.md) | [jvm]<br>open fun [ModelConnectionBlock](-model-connection-block.md)(source: [ModelSource](../-model-source/index.md), scale: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), destination: [ModelDestination](../-model-destination/index.md)) |
| [ModelConnectionBlock](-model-connection-block.md) | [jvm]<br>open fun [ModelConnectionBlock](-model-connection-block.md)(source: [ModelSource](../-model-source/index.md), control: [ModelSource](../-model-source/index.md), destination: [ModelDestination](../-model-destination/index.md)) |
| [ModelConnectionBlock](-model-connection-block.md) | [jvm]<br>open fun [ModelConnectionBlock](-model-connection-block.md)(source: [ModelSource](../-model-source/index.md), control: [ModelSource](../-model-source/index.md), scale: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), destination: [ModelDestination](../-model-destination/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [addSource](add-source.md) | [jvm]<br>open fun [addSource](add-source.md)(source: [ModelSource](../-model-source/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [destination](destination.md) | [jvm]<br>private open var [destination](destination.md): [ModelDestination](../-model-destination/index.md) |
| [scale](scale.md) | [jvm]<br>private open var [scale](scale.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [sources](sources.md) | [jvm]<br>private open var [sources](sources.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ModelSource](../-model-source/index.md)&gt; |
