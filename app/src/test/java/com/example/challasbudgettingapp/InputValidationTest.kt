package com.example.challasbudgettingapp

import com.example.challasbudgettingapp.util.Validators
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Tests the real validation code used by the login, income and expense screens. */
class InputValidationTest {

    @Test fun validEmail_isAccepted() = assertTrue(Validators.isValidEmail("test@gmail.com"))
    @Test fun emailWithoutAt_isRejected() = assertFalse(Validators.isValidEmail("testgmail.com"))
    @Test fun emptyEmail_isRejected() = assertFalse(Validators.isValidEmail(""))

    @Test fun positiveAmount_isParsed() = assertEquals(150.5, Validators.parsePositiveAmount("150.5")!!, 0.001)
    @Test fun commaDecimal_isParsed() = assertEquals(150.5, Validators.parsePositiveAmount("150,5")!!, 0.001)
    @Test fun negativeAmount_isRejected() = assertNull(Validators.parsePositiveAmount("-100"))
    @Test fun zeroAmount_isRejected() = assertNull(Validators.parsePositiveAmount("0"))
    @Test fun textAmount_isRejected() = assertNull(Validators.parsePositiveAmount("abc"))

    @Test fun validDate_isAccepted() = assertTrue(Validators.isValidDate("2026-11-06"))
    @Test fun badDate_isRejected() = assertFalse(Validators.isValidDate("06/11/2026"))
    @Test fun impossibleMonth_isRejected() = assertFalse(Validators.isValidDate("2026-13-01"))
}
