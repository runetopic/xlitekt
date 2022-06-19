//[cache](../../../index.md)/[com.sun.media](../index.md)/[SF2Modulator](index.md)

# SF2Modulator

[jvm]\
open class [SF2Modulator](index.md)

Soundfont modulator container.

#### Author

Karl Helgason

## Properties

| Name | Summary |
|---|---|
| [amount](amount.md) | [jvm]<br>protected open var [amount](amount.md): [Short](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short/index.html) |
| [amountSourceOperator](amount-source-operator.md) | [jvm]<br>protected open var [amountSourceOperator](amount-source-operator.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [destinationOperator](destination-operator.md) | [jvm]<br>protected open var [destinationOperator](destination-operator.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_CHANNEL_PRESSURE](-s-o-u-r-c-e_-c-h-a-n-n-e-l_-p-r-e-s-s-u-r-e.md) | [jvm]<br>val [SOURCE_CHANNEL_PRESSURE](-s-o-u-r-c-e_-c-h-a-n-n-e-l_-p-r-e-s-s-u-r-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_DIRECTION_MAX_MIN](-s-o-u-r-c-e_-d-i-r-e-c-t-i-o-n_-m-a-x_-m-i-n.md) | [jvm]<br>val [SOURCE_DIRECTION_MAX_MIN](-s-o-u-r-c-e_-d-i-r-e-c-t-i-o-n_-m-a-x_-m-i-n.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_DIRECTION_MIN_MAX](-s-o-u-r-c-e_-d-i-r-e-c-t-i-o-n_-m-i-n_-m-a-x.md) | [jvm]<br>val [SOURCE_DIRECTION_MIN_MAX](-s-o-u-r-c-e_-d-i-r-e-c-t-i-o-n_-m-i-n_-m-a-x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_MIDI_CONTROL](-s-o-u-r-c-e_-m-i-d-i_-c-o-n-t-r-o-l.md) | [jvm]<br>val [SOURCE_MIDI_CONTROL](-s-o-u-r-c-e_-m-i-d-i_-c-o-n-t-r-o-l.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_NONE](-s-o-u-r-c-e_-n-o-n-e.md) | [jvm]<br>val [SOURCE_NONE](-s-o-u-r-c-e_-n-o-n-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_NOTE_ON_KEYNUMBER](-s-o-u-r-c-e_-n-o-t-e_-o-n_-k-e-y-n-u-m-b-e-r.md) | [jvm]<br>val [SOURCE_NOTE_ON_KEYNUMBER](-s-o-u-r-c-e_-n-o-t-e_-o-n_-k-e-y-n-u-m-b-e-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_NOTE_ON_VELOCITY](-s-o-u-r-c-e_-n-o-t-e_-o-n_-v-e-l-o-c-i-t-y.md) | [jvm]<br>val [SOURCE_NOTE_ON_VELOCITY](-s-o-u-r-c-e_-n-o-t-e_-o-n_-v-e-l-o-c-i-t-y.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_PITCH_SENSITIVITY](-s-o-u-r-c-e_-p-i-t-c-h_-s-e-n-s-i-t-i-v-i-t-y.md) | [jvm]<br>val [SOURCE_PITCH_SENSITIVITY](-s-o-u-r-c-e_-p-i-t-c-h_-s-e-n-s-i-t-i-v-i-t-y.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_PITCH_WHEEL](-s-o-u-r-c-e_-p-i-t-c-h_-w-h-e-e-l.md) | [jvm]<br>val [SOURCE_PITCH_WHEEL](-s-o-u-r-c-e_-p-i-t-c-h_-w-h-e-e-l.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_POLARITY_BIPOLAR](-s-o-u-r-c-e_-p-o-l-a-r-i-t-y_-b-i-p-o-l-a-r.md) | [jvm]<br>val [SOURCE_POLARITY_BIPOLAR](-s-o-u-r-c-e_-p-o-l-a-r-i-t-y_-b-i-p-o-l-a-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_POLARITY_UNIPOLAR](-s-o-u-r-c-e_-p-o-l-a-r-i-t-y_-u-n-i-p-o-l-a-r.md) | [jvm]<br>val [SOURCE_POLARITY_UNIPOLAR](-s-o-u-r-c-e_-p-o-l-a-r-i-t-y_-u-n-i-p-o-l-a-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_POLY_PRESSURE](-s-o-u-r-c-e_-p-o-l-y_-p-r-e-s-s-u-r-e.md) | [jvm]<br>val [SOURCE_POLY_PRESSURE](-s-o-u-r-c-e_-p-o-l-y_-p-r-e-s-s-u-r-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_TYPE_CONCAVE](-s-o-u-r-c-e_-t-y-p-e_-c-o-n-c-a-v-e.md) | [jvm]<br>val [SOURCE_TYPE_CONCAVE](-s-o-u-r-c-e_-t-y-p-e_-c-o-n-c-a-v-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_TYPE_CONVEX](-s-o-u-r-c-e_-t-y-p-e_-c-o-n-v-e-x.md) | [jvm]<br>val [SOURCE_TYPE_CONVEX](-s-o-u-r-c-e_-t-y-p-e_-c-o-n-v-e-x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_TYPE_LINEAR](-s-o-u-r-c-e_-t-y-p-e_-l-i-n-e-a-r.md) | [jvm]<br>val [SOURCE_TYPE_LINEAR](-s-o-u-r-c-e_-t-y-p-e_-l-i-n-e-a-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SOURCE_TYPE_SWITCH](-s-o-u-r-c-e_-t-y-p-e_-s-w-i-t-c-h.md) | [jvm]<br>val [SOURCE_TYPE_SWITCH](-s-o-u-r-c-e_-t-y-p-e_-s-w-i-t-c-h.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [sourceOperator](source-operator.md) | [jvm]<br>protected open var [sourceOperator](source-operator.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [TRANSFORM_ABSOLUTE](-t-r-a-n-s-f-o-r-m_-a-b-s-o-l-u-t-e.md) | [jvm]<br>val [TRANSFORM_ABSOLUTE](-t-r-a-n-s-f-o-r-m_-a-b-s-o-l-u-t-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [TRANSFORM_LINEAR](-t-r-a-n-s-f-o-r-m_-l-i-n-e-a-r.md) | [jvm]<br>val [TRANSFORM_LINEAR](-t-r-a-n-s-f-o-r-m_-l-i-n-e-a-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [transportOperator](transport-operator.md) | [jvm]<br>protected open var [transportOperator](transport-operator.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
