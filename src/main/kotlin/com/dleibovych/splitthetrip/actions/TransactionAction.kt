package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.*
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.Update

class TransactionAction(private val storage: Storage): Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val user = storage.readUsers().find { it.id == update.from!!.id }
        if (user == null) {
            messenger.sendMessage(update.chatId!!, "Користувача не зареєстровано як платника. Ви можете зареєструватись як платник через /register")
            return
        }

        val chunksCount = update.text!!.chunksCount() - 1 // the action itself
        val numbersCount = update.text!!.getNumbersCount()

        if (chunksCount == 0 || numbersCount == 0) {
            messenger.sendMessage(update.chatId!!, "Щоб добавити витрату введіть суму після команди. /transfer _сума_")
        }
    }
}

class ConfirmTransactionAction: Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}