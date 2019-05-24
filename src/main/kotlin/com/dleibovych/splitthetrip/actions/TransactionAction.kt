package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.*
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.InlineKeyboardMarkup
import me.ivmg.telegram.entities.Update

class TransactionAction(private val storage: Storage) : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val users = storage.readUsers()
        val user = users.find { it.id == update.from!!.id }
        if (user == null) {
            messenger.sendMessage(
                update.chatId!!,
                "Користувача не зареєстровано як платника. Ви можете зареєструватись як платник через /register"
            )
            return
        }

        val numbersCount = update.text!!.getNumbersCount()

        val value = update.text!!.findFirstDouble()
        val currencies = storage.getCurrencies()
        if (numbersCount == 1) {
            if (currencies.size == 1) {
                messenger.sendMessage(
                    update.chatId!!,
                    "Оберіть людину якій ви переводите кошти.",
                    replyMarkup = InlineKeyboardMarkup(
                        users
                            .filter { it.id != user.id }
                            .map {
                                listOf(
                                    InlineKeyboardButton(
                                        it.name,
                                        callbackData = "/confirmtransfer ${user.id} ${it.id} $value ${currencies[0].name}"
                                    )
                                )
                            })
                )
            } else {
                messenger.sendMessage(
                    update.chatId!!,
                    "Оберіть валюту переказу.",
                    replyMarkup = InlineKeyboardMarkup(
                        currencies
                            .map {
                                listOf(
                                    InlineKeyboardButton(
                                        it.name,
                                        callbackData = "/confirmtransfer ${user.id} 0 $value ${it.name}"
                                    )
                                )
                            })
                )
            }

            return
        }

        messenger.sendMessage(update.chatId!!, "Щоб добавити витрату введіть суму після команди. /transfer _сума_")
    }
}

class ConfirmTransactionAction : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}