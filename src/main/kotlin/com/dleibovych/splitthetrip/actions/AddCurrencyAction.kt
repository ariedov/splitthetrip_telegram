package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.data.Storage
import com.dleibovych.splitthetrip.findFirstNonActionText
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.Update

class AddCurrencyAction : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val message = update.message!!.text
        val currencyName = message?.findFirstNonActionText()

        val chatId = update.message!!.chat.id
        if (currencyName == null) {
            messenger.sendMessage(
                chatId = chatId,
                text = "Вкажіть назву валюти у форматі: $name _назва_")
            return
        }

        messenger.sendMessage(
            chatId = chatId,
            text = "Додати нову валюту *$currencyName*?",
            replyMarkup = InlineKeyboardButton(
                text = "Так",
                callbackData = "/confirmcurrency $currencyName"
            )
        )
    }

    companion object {
        const val name = "/addcurrency"
    }
}

class ConfirmNewCurrencyAction(private val storage: Storage) : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {

    }

    companion object {
        const val name = "/confirmcurrency"
    }
}