//[cache](../../../index.md)/[xlitekt.cache.provider.soundeffect](../index.md)/[SoundEffectEnvelope](index.md)

# SoundEffectEnvelope

[jvm]\
data class [SoundEffectEnvelope](index.md)(val buffer: [ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html)? = null, var form: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, var start: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, var end: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, var segments: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 2, var durations: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = IntArray(2), var phases: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = IntArray(2))

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [SoundEffectEnvelope](-sound-effect-envelope.md) | [jvm]<br>fun [SoundEffectEnvelope](-sound-effect-envelope.md)(buffer: [ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html)? = null, form: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, start: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, end: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, segments: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 2, durations: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = IntArray(2), phases: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = IntArray(2)) |

## Functions

| Name | Summary |
|---|---|
| [decodeSegments](decode-segments.md) | [jvm]<br>fun [decodeSegments](decode-segments.md)(buffer: [ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html)) |
| [doStep](do-step.md) | [jvm]<br>fun [doStep](do-step.md)(var1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [reset](reset.md) | [jvm]<br>fun [reset](reset.md)() |

## Properties

| Name | Summary |
|---|---|
| [amplitude](amplitude.md) | [jvm]<br>var [amplitude](amplitude.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [buffer](buffer.md) | [jvm]<br>val [buffer](buffer.md): [ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html)? = null |
| [durations](durations.md) | [jvm]<br>var [durations](durations.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [end](end.md) | [jvm]<br>var [end](end.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [form](form.md) | [jvm]<br>var [form](form.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [max](max.md) | [jvm]<br>var [max](max.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [phaseIndex](phase-index.md) | [jvm]<br>var [phaseIndex](phase-index.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [phases](phases.md) | [jvm]<br>var [phases](phases.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [segments](segments.md) | [jvm]<br>var [segments](segments.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 2 |
| [start](start.md) | [jvm]<br>var [start](start.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [step](step.md) | [jvm]<br>var [step](step.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [ticks](ticks.md) | [jvm]<br>var [ticks](ticks.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
