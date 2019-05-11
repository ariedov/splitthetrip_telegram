package com.dleibovych.splitthetrip.calculator

import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import org.junit.Assert.*
import org.junit.Test

class DebtTest {

    @Test
    fun testOneToOne() {
        val currency = Currency("default")
        val user1 = BotUser(1, "first", 1)
        val user2 = BotUser(2, "second", 1)
        val shares = listOf(Share(user1, currency, 500), Share(user2, currency, -500))

        val debt1 = calculateDebt(user1, listOf(currency), shares)
        val debt2 = calculateDebt(user2, listOf(currency), shares)

        assertEquals(0, debt1.size)

        assertEquals(1, debt2.size)
        assertEquals(500, debt2[0].debt)
    }

    @Test
    fun testTwoToOne() {
        val currency = Currency("default")
        val user1 = BotUser(1, "first", 1)
        val user2 = BotUser(2, "second", 1)
        val user3 = BotUser(3, "third", 1)
        val shares = listOf(Share(user1, currency,600), Share(user2, currency, -300), Share(user3, currency,-300))

        val debt1 = calculateDebt(user1, listOf(currency), shares)
        val debt2 = calculateDebt(user2, listOf(currency), shares)
        val debt3 = calculateDebt(user3, listOf(currency), shares)

        assertEquals(0, debt1.size)

        assertEquals(1, debt2.size)
        assertEquals(300, debt2[0].debt)

        assertEquals(1, debt3.size)
        assertEquals(300, debt3[0].debt)
    }

    @Test
    fun testOneToTwo() {
        val currency = Currency("default")
        val user1 = BotUser(1, "first", 1)
        val user2 = BotUser(2, "second", 1)
        val user3 = BotUser(3, "third", 1)

        val shares = listOf(Share(user1, currency,150), Share(user2, currency,150), Share(user3, currency,-300))

        val debt1 = calculateDebt(user1, listOf(currency), shares)
        val debt2 = calculateDebt(user2, listOf(currency), shares)
        val debt3 = calculateDebt(user3, listOf(currency),shares)

        assertEquals(0, debt1.size)
        assertEquals(0, debt2.size)

        assertEquals(2, debt3.size)
        assertEquals(150, debt3[0].debt)
        assertEquals(150, debt3[1].debt)
    }
}