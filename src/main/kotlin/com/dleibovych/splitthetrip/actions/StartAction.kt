package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.chatId
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.Update

class StartAction(private val storage: Storage) : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        storage.clear()

        messenger.sendMessage(
            chatId = update.chatId!!,
            text = formatInitialMessage())
    }

    private fun formatInitialMessage() : String {
        val builder = StringBuilder()
        builder.appendln("Вітаю! Я бот який намагається допомогти поділити кошти в подорожі.")
        builder.appendln()
        builder.appendln("Ти вже почав подорож, ось деякі корисні команди які можуть тобі знадобитись:")
        builder.appendln("/start - почати подорож")
        builder.appendln("""
            /register - додати людину яка оплачує поїздку.
            Ти можеш додати себе просто викликавши команду,
            додати людину вказавши її в повідомленні,
            та додати кількість людей за яку сплачуєш просто написавши число в кінці повідомлення""".trimIndent())
        builder.appendln("/addcurrency додати валюту якою ви оплачуєте")
        builder.appendln("/removecurrency видалити валюту якою ви оплачуєте")
        builder.appendln("/add додати витрати.")
        builder.appendln("/info показати виписку поїздки")
        builder.appendln("/transfer вказати що вже повернув частину грошей. Вкажи людину якій повернув кошти, або вкажи двох щоб я зрозумів хто кому повернув.")
        builder.appendln("/end завершити поїздку.")

        return builder.toString()
    }
}