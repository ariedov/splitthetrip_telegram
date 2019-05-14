package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.calculator.calculateDebt
import com.dleibovych.splitthetrip.calculator.calculateShare
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.Update
import java.lang.StringBuilder

class InfoAction(private val storage: Storage) : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val users = storage.readUsers()
        val currencies = storage.getCurrencies()
        val expenses = storage.getExpenses()
        val transfers = storage.getTransfers()

        val shares = calculateShare(users, currencies, expenses, transfers)

        val result = users.fold(StringBuilder()) { builder, user ->
            val debts = calculateDebt(user, currencies, shares)
            debts.fold(builder, { builder, debt ->
                builder.append(user.name)
                builder.append(" має повернути ")
                builder.append(debt.toUser.name)
                builder.append(" - ")
                builder.append("*")
                builder.append(debt.debt)
                builder.append(" ")
                builder.append(debt.currency.name)
                builder.append("*")
            })
            builder.appendln()
        }.toString().trim()

        messenger.sendMessage(update.message!!.chat.id, text = result)
    }
}