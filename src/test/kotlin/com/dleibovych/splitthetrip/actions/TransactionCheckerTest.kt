package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.actions.ErrorTransactionInfo
import com.dleibovych.splitthetrip.actions.TransactionChecker
import com.dleibovych.splitthetrip.actions.ValueCurrencyTransactionInfo
import com.dleibovych.splitthetrip.actions.ValueTransactionInfo
import com.dleibovych.splitthetrip.data.Currency
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
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
        assertEquals(15.01, (result as ValueTransactionInfo).value)
    }

    @Test
    fun testValueCurrencyTransaction() {
        val transaction = "/transaction 13 usd"

        val result = transactionChecker.parseTransaction(transaction)

        assertTrue(result is ValueCurrencyTransactionInfo)
        assertEquals(13.0, (result as ValueCurrencyTransactionInfo).value)
        assertEquals(Currency("usd"), result.currency)
    }

    @Test
    fun testFromValueCurrencyTransaction() {
        val transaction = "/transaction 1 12.06 usd"

        val result = transactionChecker.parseTransaction(transaction)

        assertTrue(result is FromValueCurrencyTransactionInfo)
        assertEquals(1, (result as FromValueCurrencyTransactionInfo).fromId)
        assertEquals(12.06, (result).value)
        assertEquals(Currency("usd"), result.currency)
    }

    @Test
    fun testFromToValueCurrencyTransaction() {
        val transaction = "/transaction 1 2 12.06 usd"

        val result = transactionChecker.parseTransaction(transaction)

        assertTrue(result is FromToValueCurrencyTransactionInfo)
        assertEquals(1, (result as FromToValueCurrencyTransactionInfo).fromId)
        assertEquals(2, (result).toId)
        assertEquals(12.06, (result).value)
        assertEquals(Currency("usd"), result.currency)
    }
}