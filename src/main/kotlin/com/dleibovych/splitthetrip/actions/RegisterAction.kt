package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.*
import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.InlineKeyboardMarkup
import me.ivmg.telegram.entities.Update
import me.ivmg.telegram.entities.User

class RegisterAction : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val user = update.from
        val responsibleFor = (update.text ?: "").findFirstLong() ?: 1

        messenger.sendMessage(
            chatId = update.chatId!!,
            text = formatRegisterMessage(user, responsibleFor),
            replyMarkup = if (user != null) InlineKeyboardMarkup(
                listOf(
                    listOf(
                        InlineKeyboardButton(
                            "Підтвердити!",
                            callbackData = "/confirmregister ${user.id} $responsibleFor"
                        )
                    )
                )
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

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val userId = update.text!!.findFirstLong()
        val responsibleFor = update.text!!.findSecondLong()

        val chatId = update.chatId!!
        if (userId == null || responsibleFor == null || userId != update.from?.id) {
            messenger.sendMessage(
                chatId = chatId,
                text = "Не вдалося зберегти платника."
            )

            return
        }

        val newUser = BotUser(
            id = userId,
            name = update.from!!.firstName,
            responsibleFor = responsibleFor
        )
        val storedUsers = storage.readUsers()
        if (storedUsers.contains(newUser)) {
            messenger.sendMessage(chatId, text = "Платник вже збережений.")
            return
        }

        storage.saveUser(newUser)

        messenger.sendMessage(
            chatId = chatId,
            text = "Успішно зберегли ${update.from!!.firstName} як платника!"
        )
    }
}