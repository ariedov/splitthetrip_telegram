package com.dleibovych.splitthetrip.calculator

import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Expense
import com.dleibovych.splitthetrip.data.Transfer
import kotlin.math.abs
import kotlin.math.min

fun calculateShare(
    users: List<BotUser>,
    currencies: List<Currency>,
    expenses: List<Expense>,
    transfers: List<Transfer>
): List<Share> {
    return currencies.map { currency ->
        val totalBalance = expenses
            .filter { it.currency == currency }
            .fold(0L) { result, expense -> result + expense.amount }
        val totalPeople = users.fold(0L) { result, user -> result + user.responsibleFor }
        return users.map { user ->
            val owns = user.responsibleFor / totalPeople.toDouble() * totalBalance
            Share(user, currency, calculateUserBalance(user, currency, expenses, transfers) - owns.toLong())
        }
    }
}

fun calculateDebt(user: BotUser, currencies: List<Currency>, shares: List<Share>): List<Debt> {
    val userShare = shares.find { it.user.id == user.id }

    // share is > 0, no debts
    if (userShare == null || userShare.share > 0) {
        return listOf()
    }

    return currencies.flatMap { currency ->
        shares
            .filter { it != userShare }
            .filter { it.share > 0 }
            .map { Debt(it.user, currency, min(abs(userShare.share), it.share)) }
    }
}

fun calculateUserBalance(
    user: BotUser,
    currency: Currency,
    expenses: List<Expense>,
    transfers: List<Transfer>
): Long {
    var result: Long
    result =
        expenses
            .filter { it.user.id == user.id }
            .filter { it.currency == currency }
            .fold(0L) { expenseBalance, expense -> expenseBalance + expense.amount }

    result +=
        transfers
            .filter { it.to.id == user.id }
            .filter { it.currency == currency }
            .fold(0L) { transfersBalance, transfer -> transfersBalance - transfer.amount }
    return result
}

data class Share(val user: BotUser, val currency: Currency, val share: Long)

data class Debt(val toUser: BotUser, val currency: Currency, val debt: Long)