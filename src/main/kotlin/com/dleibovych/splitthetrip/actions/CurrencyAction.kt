package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.chatId
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Storage
import com.dleibovych.splitthetrip.findLastNonActionText
import com.dleibovych.splitthetrip.text
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.InlineKeyboardMarkup
import me.ivmg.telegram.entities.Update

class AddCurrencyAction : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val message = update.text
        val currencyName = message?.findLastNonActionText()

        val chatId = update.chatId!!
        if (currencyName == null) {
            sendPromptCurrencyMessage(messenger, chatId)
            return
        }

        messenger.sendMessage(
            chatId = chatId,
            text = "Додати нову валюту *$currencyName*?",
            replyMarkup = InlineKeyboardMarkup(
                listOf(
                    listOf(
                        InlineKeyboardButton(
                            text = "Так",
                            callbackData = "/confirmcurrency $currencyName"
                        )
                    )
                )
            )
        )
    }

    companion object {
        const val name = "/addcurrency"
    }
}

class ConfirmNewCurrencyAction(private val storage: Storage) : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val message = update.text
        val currencyName = message?.findLastNonActionText()

        val chatId = update.chatId!!
        if (currencyName == null) {
            sendPromptCurrencyMessage(messenger, chatId)
            return
        }

        val newCurrency = Currency(currencyName)
        val storedCurrencies = storage.getCurrencies()
        if (storedCurrencies.contains(newCurrency)) {
            messenger.sendMessage(chatId, "Валюта $currencyName вже збережена.")
            return
        }

        storage.addCurrency(newCurrency)
        messenger.sendMessage(
            chatId = chatId,
            text = "Успішно збережено валюту $currencyName"
        )
    }

    companion object {
        const val name = "/confirmcurrency"
    }
}

private fun sendPromptCurrencyMessage(messenger: TelegramMessenger, chatId: Long) {
    messenger.sendMessage(
        chatId = chatId,
        text = "Вкажіть назву валюти у форматі: /addcurrency _назва_"
    )
}