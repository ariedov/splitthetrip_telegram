package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.chatId
import me.ivmg.telegram.entities.Update

class ErrorAction: Action {
    override fun perform(messenger: TelegramMessenger, update: Update) {
        messenger.sendMessage(update.chatId!!, "Сталася помилка.")
    }
}