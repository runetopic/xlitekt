//[cache](../../../index.md)/[xlitekt.cache.provider.soundeffect](../index.md)/[SoundEffectEntryType](index.md)

# SoundEffectEntryType

[jvm]\
data class [SoundEffectEntryType](index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), var instruments: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[SoundEffectInstrument](../-sound-effect-instrument/index.md)?&gt;? = null, var start: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, var end: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) : [EntryType](../../xlitekt.cache.provider/-entry-type/index.md)

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [SoundEffectEntryType](-sound-effect-entry-type.md) | [jvm]<br>fun [SoundEffectEntryType](-sound-effect-entry-type.md)(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), instruments: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[SoundEffectInstrument](../-sound-effect-instrument/index.md)?&gt;? = null, start: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, end: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [toInstrumentSample](to-instrument-sample.md) | [jvm]<br>fun [toInstrumentSample](to-instrument-sample.md)(samples: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)?): [InstrumentSample](../../xlitekt.cache.provider.instrument/-instrument-sample/index.md) |

## Properties

| Name | Summary |
|---|---|
| [end](end.md) | [jvm]<br>var [end](end.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [id](id.md) | [jvm]<br>open override val [id](id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [instruments](instruments.md) | [jvm]<br>var [instruments](instruments.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[SoundEffectInstrument](../-sound-effect-instrument/index.md)?&gt;? = null |
| [start](start.md) | [jvm]<br>var [start](start.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
