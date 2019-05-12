package com.dleibovych.splitthetrip.calculator

import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Expense
import com.dleibovych.splitthetrip.data.Transfer
import org.junit.Assert.*
import org.junit.Test

class BalanceTest {

    @Test
    fun userBalanceWithOneTransaction() {
        val user = BotUser(0, "default", 1)
        val currency = Currency("default")
        val expenses = listOf(Expense(user, 100, currency))

        val balance = calculateUserBalance(user, currency, expenses, emptyList())

        assertEquals(100, balance)
    }

    @Test
    fun userBalanceWithWrongUser() {
        val user1 = BotUser(0, "default", 1)
        val user2 = BotUser(1, "second", 1)
        val currency = Currency("default")
        val expenses = listOf(Expense(user1, 100, currency))

        val balance = calculateUserBalance(user2, currency, expenses, emptyList())

        assertEquals(0, balance)
    }

    @Test
    fun userBalanceWithTransfer() {
        val user1 = BotUser(0, "default", 1)
        val user2 = BotUser(1, "second", 1)
        val currency = Currency("default")
        val expenses = listOf(Expense(user1, 100, currency))
        val transfers = listOf(Transfer(user2, user1, 50, currency))

        val balance = calculateUserBalance(user1, currency, expenses, transfers)

        assertEquals(50, balance)
    }
}