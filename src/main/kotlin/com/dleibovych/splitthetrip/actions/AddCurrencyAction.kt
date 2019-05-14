package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.findFirstNonActionText
import me.ivmg.telegram.entities.Update

class AddCurrencyAction : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val message = update.message!!.text
        val currencyName = message?.findFirstNonActionText()

        if (currencyName == null) {
            messenger.sendMessage(
                chatId = update.message!!.chat.id,
                text = "Вкажіть назву валюти у форматі: $name _назва_")
            return
        }
    }

    companion object {
        const val name = "/addcurrency"
    }
}