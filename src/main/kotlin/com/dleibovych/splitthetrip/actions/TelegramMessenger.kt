package com.dleibovych.splitthetrip.actions

import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.ReplyMarkup

class TelegramMessenger(private val bot: Bot) {

    fun sendMessage(chatId: Long, text: String, replyMarkup: ReplyMarkup? = null) {
        bot.sendMessage(chatId, text = text, replyMarkup = replyMarkup)
    }

    fun editMessageReplyMarkup(messageId: Long, replyMarkup: ReplyMarkup?) {
        bot.editMessageReplyMarkup(messageId, replyMarkup = replyMarkup)
    }
}