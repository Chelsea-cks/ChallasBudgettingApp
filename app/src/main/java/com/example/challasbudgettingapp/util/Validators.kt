package com.example.challasbudgettingapp.util

/**
 * Pure validation helpers (no Android dependencies) so they can be
 * unit tested on the JVM and reused by every screen.
 */
object Validators {

    /** Simple email check that works in plain JVM unit tests. */
    fun isValidEmail(email: String): Boolean =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email.trim())

    /** Returns the amount if it is a positive number, otherwise null. */
    fun parsePositiveAmount(input: String): Double? {
        val value = input.trim().replace(',', '.').toDoubleOrNull() ?: return null
        return if (value > 0.0 && value.isFinite()) value else null
    }

    /** Accepts dates in yyyy-MM-dd format. */
    fun isValidDate(input: String): Boolean =
        Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$").matches(input.trim())
}
