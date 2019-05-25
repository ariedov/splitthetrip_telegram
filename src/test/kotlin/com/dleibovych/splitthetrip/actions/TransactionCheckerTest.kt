package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.data.Currency
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TransactionCheckerTest {

    private lateinit var transactionChecker: TransactionChecker

    @Before
    fun setUp() {
        transactionChecker = TransactionChecker()
    }

    @Test
    fun testWrongTransaction() {
        val transaction = ""

        val result = transactionChecker.parseTransaction(transaction)

        assertEquals(ErrorTransactionInfo, result)
    }

    @Test
    fun testDecimalValueTransaction() {
        val transaction = "/transaction 15.01"

        val result = transactionChecker.parseTransaction(transaction)

        assertTrue(result is ValueTransactionInfo)
        assertEquals(15.01, (result as ValueTransactionInfo).value, 0.0)
    }

    @Test
    fun testValueCurrencyTransaction() {
        val transaction = "/transaction 13 usd"

        val result = transactionChecker.parseTransaction(transaction)

        assertTrue(result is ValueCurrencyTransactionInfo)
        assertEquals(13.0, (result as ValueCurrencyTransactionInfo).value, 0.0)
        assertEquals(Currency("usd"), result.currency)
    }

    @Test
    fun testFromValueCurrencyTransaction() {
        val transaction = "/transaction 1 12.06 usd"

        val result = transactionChecker.parseTransaction(transaction)

        assertTrue(result is FromValueCurrencyTransactionInfo)
        assertEquals(1, (result as FromValueCurrencyTransactionInfo).fromId)
        assertEquals(12.06, (result).value, 0.0)
        assertEquals(Currency("usd"), result.currency)
    }

    @Test
    fun testFromToValueCurrencyTransaction() {
        val transaction = "/transaction 1 2 12.06 usd"

        val result = transactionChecker.parseTransaction(transaction)

        assertTrue(result is FromToValueCurrencyTransactionInfo)
        assertEquals(1, (result as FromToValueCurrencyTransactionInfo).fromId)
        assertEquals(2, (result).toId)
        assertEquals(12.06, (result).value, 0.0)
        assertEquals(Currency("usd"), result.currency)
    }
}