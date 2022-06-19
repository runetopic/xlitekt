//[cache](../../../index.md)/[xlitekt.cache.provider.instrument](../index.md)/[InstrumentEntryType](index.md)

# InstrumentEntryType

[jvm]\
data class [InstrumentEntryType](index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), var audioBuffers: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[InstrumentSample](../-instrument-sample/index.md)?&gt;? = null, var pitchOffset: [ShortArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short-array/index.html)? = null, var volumeOffset: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null, var panOffset: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null, var field3117: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Instrument](../-instrument/index.md)?&gt;? = null, var loopMode: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null, var offsets: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)? = null, var baseVelocity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) : [EntryType](../../xlitekt.cache.provider/-entry-type/index.md)

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [InstrumentEntryType](-instrument-entry-type.md) | [jvm]<br>fun [InstrumentEntryType](-instrument-entry-type.md)(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), audioBuffers: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[InstrumentSample](../-instrument-sample/index.md)?&gt;? = null, pitchOffset: [ShortArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short-array/index.html)? = null, volumeOffset: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null, panOffset: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null, field3117: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Instrument](../-instrument/index.md)?&gt;? = null, loopMode: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null, offsets: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)? = null, baseVelocity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Properties

| Name | Summary |
|---|---|
| [audioBuffers](audio-buffers.md) | [jvm]<br>var [audioBuffers](audio-buffers.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[InstrumentSample](../-instrument-sample/index.md)?&gt;? = null |
| [baseVelocity](base-velocity.md) | [jvm]<br>var [baseVelocity](base-velocity.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [field3117](field3117.md) | [jvm]<br>var [field3117](field3117.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Instrument](../-instrument/index.md)?&gt;? = null |
| [id](id.md) | [jvm]<br>open override val [id](id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [loopMode](loop-mode.md) | [jvm]<br>var [loopMode](loop-mode.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null |
| [offsets](offsets.md) | [jvm]<br>var [offsets](offsets.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)? = null |
| [panOffset](pan-offset.md) | [jvm]<br>var [panOffset](pan-offset.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null |
| [pitchOffset](pitch-offset.md) | [jvm]<br>var [pitchOffset](pitch-offset.md): [ShortArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short-array/index.html)? = null |
| [volumeOffset](volume-offset.md) | [jvm]<br>var [volumeOffset](volume-offset.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null |
