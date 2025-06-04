package com.motorro.inline


/**
 * Alpha-3 country code
 */
@JvmInline
value class CountryCode(val code: String?) {
    companion object {
        /**
         * Value object for unknown country
         */
        val UNKNOWN = CountryCode(null)
    }
}