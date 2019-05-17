package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.calculator.calculateDebt
import com.dleibovych.splitthetrip.calculator.calculateShare
import com.dleibovych.splitthetrip.chatId
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Expense
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.Update
import java.lang.StringBuilder

class InfoAction(private val storage: Storage) : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val users = storage.readUsers()
        val currencies = storage.getCurrencies()
        val expenses = storage.getExpenses()
        val transfers = storage.getTransfers()

        if (users.isEmpty()) {
            messenger.sendMessage(
                update.chatId!!,
                text = "Немає зареєстрованих людей. Ви можете добавити себе через /register"
            )
            return
        }

        if (currencies.isEmpty()) {
            messenger.sendMessage(
                update.chatId!!,
                text = "Немає жодної зареєстрованої валюти. Ви можете добавити одну через /addcurrency _назва_"
            )
            return
        }

        if (expenses.isEmpty()) {
            messenger.sendMessage(1, text = "Немає зареєстрованих витрат. Ви можете добавити одну через /add _сума_")
            return
        }

        if (users.size == 1) {
            sendSingleUserMessage(update, currencies, expenses, messenger)
            return
        }

        val shares = calculateShare(users, currencies, expenses, transfers)
        val result = users.fold(StringBuilder()) { rootBuilder, user ->
            val debts = calculateDebt(user, currencies, shares)
            debts.fold(rootBuilder, { builder, debt ->
                builder.append(user.name)
                builder.append(" має повернути ")
                builder.append(debt.toUser.name)
                builder.append(" - ")
                builder.append("*")
                builder.append(debt.debt.toMoneyString())
                builder.append(" ")
                builder.append(debt.currency.name)
                builder.append("*")
            })
            rootBuilder.append("\n")
        }.toString().trim()

        messenger.sendMessage(update.chatId!!, text = result)
    }

    private fun sendSingleUserMessage(
        update: Update,
        currencies: List<Currency>,
        expenses: List<Expense>,
        messenger: TelegramMessenger
    ) {
        val message = currencies.fold(StringBuilder()) { builder, currency ->
            expenses
                .filter { it.currency == currency }
                .fold(builder) { _, expense ->
                    builder.append(expense.user.name)
                    builder.append(" витратив ")
                    builder.append("*${expense.amount.toMoneyString()} ${expense.currency.name}*")
                }
            builder.append("\n")
        }.toString().trim()
        messenger.sendMessage(update.chatId!!, text = message)
    }
}

private fun Long.toMoneyString(): String {
    return String.format("%.2f", this / 100.00)
}