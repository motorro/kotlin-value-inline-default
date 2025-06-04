# Bad type on operand stack error

Minimum project to reproduce the error with:

- Kotlin 2.1.21
- inline class
- inline functions
- inline class as a default argument

## Inline class
Take a class like [this one](src/main/kotlin/com/motorro/inline/CountryCode.kt):
```kotlin
@JvmInline
value class CountryCode(val code: String?) {
    companion object {
        /**
         * Value object for unknown country
         */
        val UNKNOWN = CountryCode(null)
    }
}
```

## Test 
Write a [test](src/test/kotlin/com/motorro/inline/CountryCodeTest.kt) that uses this class as a default argument in an inline function:
```kotlin
class CountryCodeTest {
    @Test
    fun someTest() = test {
        assertEquals(CountryCode.UNKNOWN, it)
    }

    private inline fun test(
        saved: CountryCode = CountryCode.UNKNOWN,
        crossinline check: (CountryCode) -> Unit
    ) {
        check(saved)
    }
}
```

## Error
The test compiles but fails on runtime with:
```
Bad type on operand stack
Exception Details:
  Location:
    com/motorro/inline/CountryCodeTest.test-aReVLFE$default(Lcom/motorro/inline/CountryCodeTest;Lcom/motorro/inline/CountryCode;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)V @20: invokestatic
  Reason:
    Type 'com/motorro/inline/CountryCode' (current frame, stack[1]) is not assignable to 'java/lang/String'
```

## Working code
The failure seems to be the combination of these used at once:

- value class
- inline function
- default argument

Changing **any** of the following makes the code work.

### Data class instead of value class
```kotlin
data class CountryCode(val code: String?) {
    companion object {
        /**
         * Value object for unknown country
         */
        val UNKNOWN = CountryCode(null)
    }
}
```

### No default argument in function
```kotlin
class CountryCodeTest {
    @Test
    fun someTest() = test(CountryCode.UNKNOWN) {
        assertEquals(CountryCode.UNKNOWN, it)
    }

    private inline fun test(
        saved: CountryCode, // No default argument
        crossinline check: (CountryCode) -> Unit
    ) {
        check(saved)
    }
}
```

### Regular function instead of inline
```kotlin
class CountryCodeTest {
    @Test
    fun someTest() = test {
        assertEquals(CountryCode.UNKNOWN, it)
    }

    // Regular function
    private fun test(
        saved: CountryCode = CountryCode.UNKNOWN,
        check: (CountryCode) -> Unit
    ) {
        check(saved)
    }
}
```



