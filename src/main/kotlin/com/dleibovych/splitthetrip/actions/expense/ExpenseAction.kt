package com.dleibovych.splitthetrip.actions.expense

import com.dleibovych.splitthetrip.actions.Action
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Storage
import com.dleibovych.splitthetrip.findFirstDouble
import com.dleibovych.splitthetrip.findFirstLong
import com.dleibovych.splitthetrip.findLastNonActionText
import com.dleibovych.splitthetrip.findSecondDouble
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.InlineKeyboardMarkup
import me.ivmg.telegram.entities.Update

class AddExpenseAction(private val storage: Storage) : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val user = update.message!!.from!!
        val chatId = update.message!!.chat.id

        val registeredUsers = storage.readUsers()
        val botUser = registeredUsers.find { it.id == user.id }
        if (botUser == null) {
            messenger.sendMessage(
                chatId,
                text = "${user.firstName} не зареєстровано як платника. Можете зареєструвати через /register"
            )
            return
        }

        val currencies = storage.getCurrencies()
        if (currencies.isEmpty()) {
            messenger.sendMessage(chatId, "Спочатку добавте валюту в якій можна вести підрахунки через /addcurrency")
            return
        }

        val messageText = update.message!!.text!!
        val chunksCount = chunksCount(messageText) - 1 // the action itself
        val numbersCount = getNumbersCount(messageText)

        when (chunksCount) {
            1 -> processValueOnly(messenger, update, botUser, currencies, chatId)
            2 -> when (numbersCount) {
                1 -> processValueAndCurrency(messenger, update, botUser, currencies, chatId)
                2 -> processValueAndUserId(messenger, update, botUser, currencies, chatId)
                else -> showPrompt(messenger, chatId)
            }
            3 -> processUserIdValueAndCurrency(messenger, update, botUser, currencies, chatId)
            else -> showPrompt(messenger, chatId)
        }
    }

    private fun chunksCount(text: String): Int {
        return text.split(" ").size
    }

    private fun getNumbersCount(text: String): Int {
        val chunks = text.split(" ")
        return chunks.sumBy { if (it.toDoubleOrNull() == null) 0 else 1 }
    }

    private fun showPrompt(messenger: TelegramMessenger, chatId: Long) {
        messenger.sendMessage(chatId, text = "Щоб записати витрату виконайте /add _сума витрати_")
    }

    private fun processValueOnly(
        messenger: TelegramMessenger,
        update: Update,
        user: BotUser,
        currencies: List<Currency>,
        chatId: Long
    ) {
        val value = update.message!!.text?.findFirstDouble()

        if (currencies.size == 1) {
            sendConfirmationMessage(messenger, chatId, value, currencies[0], user)
            return
        }

        showCurrencyChooser(messenger, chatId, currencies, user, value)
    }

    private fun processValueAndUserId(
        messenger: TelegramMessenger,
        update: Update,
        user: BotUser,
        currencies: List<Currency>,
        chatId: Long
    ) {
        val inlineId = update.message!!.text?.findFirstLong()
        val value = update.message!!.text?.findSecondDouble()

        if (inlineId != user.id) {
            messenger.sendMessage(chatId, text = "Підтвердити операцію має той самий користувач який її починав.")
            return
        }

        showCurrencyChooser(messenger, chatId, currencies, user, value)
    }

    private fun processValueAndCurrency(
        messenger: TelegramMessenger,
        update: Update,
        user: BotUser,
        currencies: List<Currency>,
        chatId: Long
    ) {
        val value = update.message!!.text?.findFirstDouble()
        val currencyName = update.message!!.text?.findLastNonActionText()!!

        val currency = Currency(currencyName)
        if (!currencies.contains(currency)) {
            messenger.sendMessage(chatId, text = "Немає валюти $currencyName. Ви можете її додати через /addcurrency")
            return
        }

        sendConfirmationMessage(messenger, chatId, value, currency, user)
    }

    private fun processUserIdValueAndCurrency(
        messenger: TelegramMessenger,
        update: Update,
        user: BotUser,
        currencies: List<Currency>,
        chatId: Long
    ) {
        val inlineId = update.message!!.text?.findFirstLong()
        val value = update.message!!.text?.findSecondDouble()
        val currencyName = update.message!!.text?.findLastNonActionText()!!

        if (inlineId != user.id) {
            messenger.sendMessage(chatId, text = "Підтвердити операцію має той самий користувач який її починав.")
            return
        }

        val currency = Currency(currencyName)
        if (!currencies.contains(currency)) {
            messenger.sendMessage(chatId, text = "Немає валюти $currency. Ви можете її додати через /addcurrency")
            return
        }

        sendConfirmationMessage(messenger, chatId, value, currency, user)
    }

    private fun sendConfirmationMessage(
        messenger: TelegramMessenger,
        chatId: Long,
        value: Double?,
        currency: Currency,
        user: BotUser
    ) {
        messenger.sendMessage(
            chatId, text = "Підтвердити платіж $value ${currency.name}?", replyMarkup = InlineKeyboardMarkup(
                listOf(
                    listOf(
                        InlineKeyboardButton(
                            text = "Підтвердити!",
                            callbackData = "/confirmadd ${user.id} $value ${currency.name}"
                        )
                    )
                )
            )
        )
    }

    private fun showCurrencyChooser(
        messenger: TelegramMessenger,
        chatId: Long,
        currencies: List<Currency>,
        user: BotUser,
        value: Double?
    ) {
        messenger.sendMessage(
            chatId, text = "Оберіть валюту яку хочете викорстати.", replyMarkup = InlineKeyboardMarkup(
                listOf(currencies.map {
                    InlineKeyboardButton(
                        it.name,
                        callbackData = "/add ${user.id} $value ${it.name}"
                    )
                })
            )
        )
    }
}
