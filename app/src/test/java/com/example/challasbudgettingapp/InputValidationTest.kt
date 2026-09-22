package com.example.challasbudgettingapp

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InputValidationTest {

    @Test
    fun validEmail_returnsTrue() {

        val email = "test@gmail.com"

        val isValid = email.contains("@") &&
                email.contains(".")

        assertTrue(isValid)
    }

    @Test
    fun invalidEmail_returnsFalse() {

        val email = "testgmail.com"

        val isValid = email.contains("@") &&
                email.contains(".")

        assertFalse(isValid)
    }

    @Test
    fun emptyName_isInvalid() {

        val name = ""

        assertTrue(name.isBlank())
    }

    @Test
    fun negativeExpense_isInvalid() {

        val amount = -100.0

        assertTrue(amount < 0)
    }
}