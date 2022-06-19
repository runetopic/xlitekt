//[cache](../../../index.md)/[xlitekt.cache.provider.soundeffect](../index.md)/[SoundEffectInstrument](index.md)

# SoundEffectInstrument

[jvm]\
data class [SoundEffectInstrument](index.md)(val buffer: [ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html), var oscillatorVolume: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = intArrayOf(0, 0, 0, 0, 0), var oscillatorPitch: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = intArrayOf(0, 0, 0, 0, 0), var oscillatorDelays: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = intArrayOf(0, 0, 0, 0, 0), var delayTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, var delayDecay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 100, var duration: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 500, var offset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, var pitch: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, var volume: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, var pitchModifier: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, var pitchModifierAmplitude: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, var volumeMultiplier: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, var volumeMultiplierAmplitude: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, var release: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, var attack: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, var filter: [SoundEffectAudioFilter](../-sound-effect-audio-filter/index.md)? = null, var filterEnvelope: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null)

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [SoundEffectInstrument](-sound-effect-instrument.md) | [jvm]<br>fun [SoundEffectInstrument](-sound-effect-instrument.md)(buffer: [ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html), oscillatorVolume: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = intArrayOf(0, 0, 0, 0, 0), oscillatorPitch: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = intArrayOf(0, 0, 0, 0, 0), oscillatorDelays: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) = intArrayOf(0, 0, 0, 0, 0), delayTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, delayDecay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 100, duration: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 500, offset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, pitch: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, volume: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, pitchModifier: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, pitchModifierAmplitude: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, volumeMultiplier: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, volumeMultiplierAmplitude: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, release: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, attack: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null, filter: [SoundEffectAudioFilter](../-sound-effect-audio-filter/index.md)? = null, filterEnvelope: [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [synthesize](synthesize.md) | [jvm]<br>fun [synthesize](synthesize.md)(var1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), var2: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |

## Properties

| Name | Summary |
|---|---|
| [attack](attack.md) | [jvm]<br>var [attack](attack.md): [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null |
| [buffer](buffer.md) | [jvm]<br>val [buffer](buffer.md): [ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html) |
| [delayDecay](delay-decay.md) | [jvm]<br>var [delayDecay](delay-decay.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 100 |
| [delayTime](delay-time.md) | [jvm]<br>var [delayTime](delay-time.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [duration](duration.md) | [jvm]<br>var [duration](duration.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 500 |
| [filter](filter.md) | [jvm]<br>var [filter](filter.md): [SoundEffectAudioFilter](../-sound-effect-audio-filter/index.md)? = null |
| [filterEnvelope](filter-envelope.md) | [jvm]<br>var [filterEnvelope](filter-envelope.md): [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null |
| [offset](offset.md) | [jvm]<br>var [offset](offset.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [oscillatorDelays](oscillator-delays.md) | [jvm]<br>var [oscillatorDelays](oscillator-delays.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [oscillatorPitch](oscillator-pitch.md) | [jvm]<br>var [oscillatorPitch](oscillator-pitch.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [oscillatorVolume](oscillator-volume.md) | [jvm]<br>var [oscillatorVolume](oscillator-volume.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [pitch](pitch.md) | [jvm]<br>var [pitch](pitch.md): [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null |
| [pitchModifier](pitch-modifier.md) | [jvm]<br>var [pitchModifier](pitch-modifier.md): [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null |
| [pitchModifierAmplitude](pitch-modifier-amplitude.md) | [jvm]<br>var [pitchModifierAmplitude](pitch-modifier-amplitude.md): [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null |
| [release](release.md) | [jvm]<br>var [release](release.md): [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null |
| [volume](volume.md) | [jvm]<br>var [volume](volume.md): [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null |
| [volumeMultiplier](volume-multiplier.md) | [jvm]<br>var [volumeMultiplier](volume-multiplier.md): [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null |
| [volumeMultiplierAmplitude](volume-multiplier-amplitude.md) | [jvm]<br>var [volumeMultiplierAmplitude](volume-multiplier-amplitude.md): [SoundEffectEnvelope](../-sound-effect-envelope/index.md)? = null |
