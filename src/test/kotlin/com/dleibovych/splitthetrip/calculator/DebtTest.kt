package com.dleibovych.splitthetrip.calculator

import com.dleibovych.splitthetrip.data.BotUser
import org.junit.Assert.*
import org.junit.Test

class DebtTest {

    @Test
    fun testOneToOne() {
        val user1 = BotUser(1, "first", 1, 1000)
        val user2 = BotUser(2, "second", 1, 0)
        val shares = listOf(Share(user1, 500), Share(user2, -500))

        val debt1 = calculateDebt(user1, shares)
        val debt2 = calculateDebt(user2, shares)

        assertEquals(0, debt1.size)

        assertEquals(1, debt2.size)
        assertEquals(user1, debt2[0].user)
        assertEquals(500, debt2[0].share)
    }

    @Test
    fun testTwoToOne() {
        val user1 = BotUser(1, "first", 1, 900)
        val user2 = BotUser(2, "second", 1, 0)
        val user3 = BotUser(3, "third", 1, 0)
        val shares = listOf(Share(user1, 600), Share(user2, -300), Share(user3, -300))

        val debt1 = calculateDebt(user1, shares)
        val debt2 = calculateDebt(user2, shares)
        val debt3 = calculateDebt(user3, shares)

        assertEquals(0, debt1.size)

        assertEquals(1, debt2.size)
        assertEquals(user1, debt2[0].user)
        assertEquals(300, debt2[0].share)

        assertEquals(1, debt3.size)
        assertEquals(user1, debt3[0].user)
        assertEquals(300, debt3[0].share)
    }

    @Test
    fun testOneToTwo() {
        val user1 = BotUser(1, "first", 1, 450)
        val user2 = BotUser(2, "second", 1, 450)
        val user3 = BotUser(3, "third", 1, 0)

        val shares = listOf(Share(user1, 150), Share(user2, 150), Share(user3, -300))

        val debt1 = calculateDebt(user1, shares)
        val debt2 = calculateDebt(user2, shares)
        val debt3 = calculateDebt(user3, shares)

        assertEquals(0, debt1.size)
        assertEquals(0, debt2.size)

        assertEquals(2, debt3.size)
        assertEquals(user1, debt3[0].user)
        assertEquals(user2, debt3[1].user)
        assertEquals(150, debt3[0].share)
        assertEquals(150, debt3[1].share)
    }
}