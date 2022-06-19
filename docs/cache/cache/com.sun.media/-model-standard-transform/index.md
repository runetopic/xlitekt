//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelStandardTransform](index.md)

# ModelStandardTransform

[jvm]\
open class [ModelStandardTransform](index.md) : [ModelTransform](../-model-transform/index.md)

A standard transformer used in connection blocks. It expects input values to be between 0 and 1. The result of the transform is between 0 and 1 if polarity = unipolar and between -1 and 1 if polarity = bipolar. These constraints only applies to Concave, Convex and Switch transforms.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [ModelStandardTransform](-model-standard-transform.md) | [jvm]<br>open fun [ModelStandardTransform](-model-standard-transform.md)() |
| [ModelStandardTransform](-model-standard-transform.md) | [jvm]<br>open fun [ModelStandardTransform](-model-standard-transform.md)(direction: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [ModelStandardTransform](-model-standard-transform.md) | [jvm]<br>open fun [ModelStandardTransform](-model-standard-transform.md)(direction: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), polarity: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [ModelStandardTransform](-model-standard-transform.md) | [jvm]<br>open fun [ModelStandardTransform](-model-standard-transform.md)(direction: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), polarity: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), transform: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [transform](transform.md) | [jvm]<br>open fun [transform](transform.md)(value: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Properties

| Name | Summary |
|---|---|
| [direction](direction.md) | [jvm]<br>private open var [direction](direction.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DIRECTION_MAX2MIN](-d-i-r-e-c-t-i-o-n_-m-a-x2-m-i-n.md) | [jvm]<br>val [DIRECTION_MAX2MIN](-d-i-r-e-c-t-i-o-n_-m-a-x2-m-i-n.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DIRECTION_MIN2MAX](-d-i-r-e-c-t-i-o-n_-m-i-n2-m-a-x.md) | [jvm]<br>val [DIRECTION_MIN2MAX](-d-i-r-e-c-t-i-o-n_-m-i-n2-m-a-x.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [polarity](polarity.md) | [jvm]<br>private open var [polarity](polarity.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [POLARITY_BIPOLAR](-p-o-l-a-r-i-t-y_-b-i-p-o-l-a-r.md) | [jvm]<br>val [POLARITY_BIPOLAR](-p-o-l-a-r-i-t-y_-b-i-p-o-l-a-r.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [POLARITY_UNIPOLAR](-p-o-l-a-r-i-t-y_-u-n-i-p-o-l-a-r.md) | [jvm]<br>val [POLARITY_UNIPOLAR](-p-o-l-a-r-i-t-y_-u-n-i-p-o-l-a-r.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [transform](transform.md) | [jvm]<br>private open var [transform](transform.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [TRANSFORM_ABSOLUTE](-t-r-a-n-s-f-o-r-m_-a-b-s-o-l-u-t-e.md) | [jvm]<br>val [TRANSFORM_ABSOLUTE](-t-r-a-n-s-f-o-r-m_-a-b-s-o-l-u-t-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [TRANSFORM_CONCAVE](-t-r-a-n-s-f-o-r-m_-c-o-n-c-a-v-e.md) | [jvm]<br>val [TRANSFORM_CONCAVE](-t-r-a-n-s-f-o-r-m_-c-o-n-c-a-v-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [TRANSFORM_CONVEX](-t-r-a-n-s-f-o-r-m_-c-o-n-v-e-x.md) | [jvm]<br>val [TRANSFORM_CONVEX](-t-r-a-n-s-f-o-r-m_-c-o-n-v-e-x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [TRANSFORM_LINEAR](-t-r-a-n-s-f-o-r-m_-l-i-n-e-a-r.md) | [jvm]<br>val [TRANSFORM_LINEAR](-t-r-a-n-s-f-o-r-m_-l-i-n-e-a-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [TRANSFORM_SWITCH](-t-r-a-n-s-f-o-r-m_-s-w-i-t-c-h.md) | [jvm]<br>val [TRANSFORM_SWITCH](-t-r-a-n-s-f-o-r-m_-s-w-i-t-c-h.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
