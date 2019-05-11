package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Storage
import com.dleibovych.splitthetrip.findFirstLong
import com.dleibovych.splitthetrip.findSecondLong
import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.Update
import me.ivmg.telegram.entities.User

class RegisterAction : Action {

    override fun perform(bot: Bot, update: Update) {
        val mentionedUser = update.message?.entities?.find { it.user != null }?.user
        val resultUser = mentionedUser ?: update.message?.from

        val responsibleFor = (update.message?.text ?: "").findFirstLong() ?: 1

        bot.sendMessage(
            chatId = update.message?.chat?.id ?: 0,
            text = formatRegisterMessage(resultUser, responsibleFor),
            replyMarkup = if (resultUser != null) InlineKeyboardButton(
                "Підтвердити!",
                callbackData = "/confirmregister ${resultUser.id} $responsibleFor"
            ) else null
        )
    }

    private fun formatRegisterMessage(user: User?, responsibleFor: Long): String {
        val builder = StringBuilder()
        if (user == null) {
            builder.append("Сталася помилка, не зрозуміло кого зберігати.")
            return builder.toString()
        }

        builder.append("${user.firstName} готовий платити за $responsibleFor.")
        return builder.toString()
    }
}


class ConfirmRegisterAction(private val storage: Storage) : Action {

    override fun perform(bot: Bot, update: Update) {
        val userId = update.message?.text?.findFirstLong()
        val responsibleFor = update.message?.text?.findSecondLong()

        if (userId == null || responsibleFor == null || userId != update.message?.from?.id) {
            bot.sendMessage(
                chatId = update.message!!.chat.id,
                text = "Не вдалося зберегти платника."
            )

            return
        }

        storage.saveUser(
            BotUser(
                id = userId,
                name = update.message?.from?.firstName ?: "",
                responsibleFor = responsibleFor
            )
        )

        bot.sendMessage(
            chatId = update.message!!.chat.id,
            text = "Успішно зберегли ${update.message?.from?.firstName} як платника!"
        )
    }
}