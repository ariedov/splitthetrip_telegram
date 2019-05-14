package com.dleibovych.splitthetrip.actions.expense

import com.dleibovych.splitthetrip.actions.Action
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.convertToMoneyLong
import com.dleibovych.splitthetrip.data.Expense
import com.dleibovych.splitthetrip.data.Storage
import com.dleibovych.splitthetrip.findFirstLong
import com.dleibovych.splitthetrip.findLastNonActionText
import com.dleibovych.splitthetrip.findSecondDouble
import me.ivmg.telegram.entities.Update

class ConfirmExpenseAction(private val storage: Storage) : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val messageText = update.message!!.text!!
        val chatId = update.message!!.chat.id

        val inlineId =  messageText.findFirstLong()

        if (inlineId != update.message!!.from!!.id) {
            messenger.sendMessage(chatId, "Підтвердити операцію має той хто її почав.")
            return
        }

        val savedUsers = storage.readUsers()
        val currentUser = savedUsers.find { it.id == inlineId }
        if (currentUser == null) {
            messenger.sendMessage(chatId, text = "${update.message!!.from!!.firstName} не зареєстровано як платника. Ви можете зареєструватись як платник виконавши /register")
            return
        }

        val currencyName = messageText.findLastNonActionText()
        val currency = storage.getCurrencies().find { it.name == currencyName }
        if (currency == null) {
            messenger.sendMessage(chatId, text = "Валюта $currencyName не добавлена. Ви можете добавити її через /addcurrency")
            return
        }

        val value = messageText.findSecondDouble()!!

        storage.addExpense(Expense(currentUser, value.convertToMoneyLong(), currency))
        messenger.sendMessage(chatId, text = "Операцію збережено")
    }
}