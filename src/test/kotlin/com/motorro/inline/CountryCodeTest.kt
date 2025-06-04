package com.motorro.inline

import org.junit.Test
import kotlin.test.assertEquals

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
