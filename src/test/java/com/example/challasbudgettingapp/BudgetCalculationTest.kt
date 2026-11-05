package com.example.challasbudgettingapp

import com.example.challasbudgettingapp.util.BudgetMath
import org.junit.Assert.assertEquals
import org.junit.Test

/** Tests the real budget calculations used by the dashboard and savings goals. */
class BudgetCalculationTest {

    @Test fun balance_isIncomeMinusExpenses() =
        assertEquals(3000.0, BudgetMath.balance(5000.0, 2000.0), 0.01)

    @Test fun balance_canGoNegative() =
        assertEquals(-500.0, BudgetMath.balance(1500.0, 2000.0), 0.01)

    @Test fun goalProgress_halfway() =
        assertEquals(50.0, BudgetMath.goalProgress(500.0, 1000.0), 0.01)

    @Test fun goalProgress_isCappedAt100() =
        assertEquals(100.0, BudgetMath.goalProgress(2000.0, 1000.0), 0.01)

    @Test fun goalProgress_zeroTargetIsSafe() =
        assertEquals(0.0, BudgetMath.goalProgress(100.0, 0.0), 0.01)
}
