package com.dleibovych.splitthetrip.calculator

import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Expense
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.exp

class ShareTest {

    @Test
    fun testCalculationOneToOneSingleCurrency() {
        val users = listOf(
            BotUser(1, "first", 1),
            BotUser(2, "second", 1)
        )
        val currencies = listOf(Currency("default"))
        val expenses = listOf(Expense(users[0], 1000, currencies[0]))

        val share = calculateShare(users, currencies, expenses, emptyList())

        assertEquals(2, share.size)

        assertEquals(1, share[0].user.id)
        assertEquals(500, share[0].share)

        assertEquals(2, share[1].user.id)
        assertEquals(-500, share[1].share)
    }

    @Test
    fun testCalculationOneToOneWithThree() {
        val users = listOf(
            BotUser(1, "first", 1),
            BotUser(2, "second", 1),
            BotUser(3, "third", 1)
        )
        val currencies = listOf(Currency("default"))
        val expenses = listOf(Expense(users[0], 900, currencies[0]))

        val share = calculateShare(users, currencies, expenses, emptyList())

        assertEquals(3, share.size)

        assertEquals(1, share[0].user.id)
        assertEquals(600, share[0].share)

        assertEquals(2, share[1].user.id)
        assertEquals(-300, share[1].share)

        assertEquals(3, share[2].user.id)
        assertEquals(-300, share[2].share)
    }

    @Test
    fun testCalculateShareWithTwoPayers() {
        val users = listOf(
            BotUser(1, "first", 3),
            BotUser(2, "second", 4)
        )
        val currencies = listOf(Currency("default"))
        val expenses = listOf(Expense(users[0], 10000, currencies[0]), Expense(users[1], 4000, currencies[0]))

        val share = calculateShare(users, currencies, expenses, emptyList())

        assertEquals(2, share.size)

        assertEquals(1, share[0].user.id)
        assertEquals(4000, share[0].share)

        assertEquals(2, share[1].user.id)
        assertEquals(-4000, share[1].share)
    }

    @Test
    fun testCalculateShareWithThreePayers() {
        val users = listOf(
            BotUser(1, "first", 3),
            BotUser(2, "second", 7),
            BotUser(3, "third", 3)
        )
        val currencies = listOf(Currency("default"))
        val expenses = listOf(
            Expense(users[0], 1000, currencies[0]),
            Expense(users[1], 5000, currencies[0]),
            Expense(users[2], 7000, currencies[0])
        )

        val share = calculateShare(users, currencies, expenses, emptyList())

        assertEquals(3, share.size)

        assertEquals(1, share[0].user.id)
        assertEquals(-2000, share[0].share)

        assertEquals(2, share[1].user.id)
        assertEquals(-2000, share[1].share)

        assertEquals(3, share[2].user.id)
        assertEquals(4000, share[2].share)
    }
}