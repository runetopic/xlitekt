//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelOscillator](index.md)

# ModelOscillator

[jvm]\
interface [ModelOscillator](index.md)

This interface is used for oscillators. See example in ModelDefaultOscillator which is a wavetable oscillator.

#### Author

Karl Helgason

## Functions

| Name | Summary |
|---|---|
| [getAttenuation](get-attenuation.md) | [jvm]<br>abstract fun [getAttenuation](get-attenuation.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>Attenuation is in cB. |
| [getChannels](get-channels.md) | [jvm]<br>abstract fun [getChannels](get-channels.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [open](open.md) | [jvm]<br>abstract fun [open](open.md)(samplerate: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)): [ModelOscillatorStream](../-model-oscillator-stream/index.md) |

## Inheritors

| Name |
|---|
| [ModelWavetable](../-model-wavetable/index.md) |
