package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.data.Currency

class TransactionChecker {

    fun parseTransaction(transaction: String): TransactionInfo {
        val valueRegex = "^(/\\w+)\\s(\\d[.\\d]*)\$".toRegex()
        val valueCurrencyRegex = "^(/\\w+)\\s(\\d[.\\d]*)\\s(\\w+)\$".toRegex()
        val fromValueCurrencyRegex = "^(/\\w+)\\s(\\d+)\\s(\\d[.\\d]*)\\s(\\w+)\$".toRegex()
        val fromToValueCurrencyRegex = "^(/\\w+)\\s(\\d+)\\s(\\d+)\\s(\\d[.\\d]*)\\s(\\w+)\$".toRegex()

        if (valueRegex.matches(transaction)) {
            val value = valueRegex.find(transaction)!!.groups[2]!!.value
            return ValueTransactionInfo(value.toDouble())
        }

        if (valueCurrencyRegex.matches(transaction)) {
            val groups = valueCurrencyRegex.find(transaction)!!.groups
            val value = groups[2]!!.value.toDouble()
            val currency = Currency(groups[3]!!.value)
            return ValueCurrencyTransactionInfo(value, currency)
        }

        if (fromValueCurrencyRegex.matches(transaction)) {
            val groups = fromValueCurrencyRegex.find(transaction)!!.groups
            val fromId = groups[2]!!.value.toLong()
            val value = groups[3]!!.value.toDouble()
            val currency = Currency(groups[4]!!.value)
            return FromValueCurrencyTransactionInfo(fromId, value, currency)
        }

        if (fromToValueCurrencyRegex.matches(transaction)) {
            val groups = fromToValueCurrencyRegex.find(transaction)!!.groups
            val fromId = groups[2]!!.value.toLong()
            val toId = groups[3]!!.value.toLong()
            val value = groups[4]!!.value.toDouble()
            val currency = Currency(groups[5]!!.value)
            return FromToValueCurrencyTransactionInfo(fromId, toId,value, currency)
        }

        return ErrorTransactionInfo
    }
}


sealed class TransactionInfo

data class ValueTransactionInfo(
    val value: Double
) : TransactionInfo()

data class ValueCurrencyTransactionInfo(
    val value: Double,
    val currency: Currency
) : TransactionInfo()

data class FromValueCurrencyTransactionInfo(
    val fromId: Long,
    val value: Double,
    val currency: Currency
) : TransactionInfo()

data class FromToValueCurrencyTransactionInfo(
    val fromId: Long,
    val toId: Long,
    val value: Double,
    val currency: Currency
) : TransactionInfo()

object ErrorTransactionInfo : TransactionInfo()