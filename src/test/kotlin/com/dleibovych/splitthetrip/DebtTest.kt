package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.calculator.Share
import com.dleibovych.splitthetrip.calculator.User
import com.dleibovych.splitthetrip.calculator.calculateDebt
import org.junit.Assert.*
import org.junit.Test

class DebtTest {

    @Test
    fun testOneToOne() {
        val user1 = User(1, "first", 1, 1000)
        val user2 = User(2, "second", 1, 0)
        val shares = listOf(
            Share(user1, 500),
            Share(user2, -500)
        )

        val debt1 = calculateDebt(user1, shares)
        val debt2 = calculateDebt(user2, shares)

        assertEquals(0, debt1.size)

        assertEquals(1, debt2.size)
        assertEquals(user1, debt2[0].user)
        assertEquals(500, debt2[0].share)
    }
}