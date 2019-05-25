package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.*
import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.InlineKeyboardMarkup
import me.ivmg.telegram.entities.Update

class TransactionAction(
    private val storage: Storage,
    private val transactionChecker: TransactionChecker
) : Action {

    private val currencies: List<Currency> by lazy { storage.getCurrencies() }
    private val users: List<BotUser> by lazy { storage.readUsers() }

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val users = storage.readUsers()
        val user = users.find { it.id == update.from!!.id }
        if (user == null) {
            messenger.sendMessage(
                update.chatId!!,
                "Користувача не зареєстровано як платника. Ви можете зареєструватись як платник через /register"
            )
            return
        }

        when (val transaction = transactionChecker.parseTransaction(update.text!!)) {
            is ValueTransactionInfo -> processValue(update, messenger, transaction)
            is ValueCurrencyTransactionInfo -> processValueCurrency(update, messenger, transaction)
            is FromValueCurrencyTransactionInfo -> processFromValueCurrency(update, messenger, transaction)
            is FromToValueCurrencyTransactionInfo -> processFromToValueCurrency(update, messenger, transaction)
            else -> messenger.sendMessage(
                update.chatId!!,
                "Щоб добавити витрату введіть суму після команди. /transfer _сума_"
            )
        }
    }

    private fun processValue(update: Update, messenger: TelegramMessenger, info: ValueTransactionInfo) {
        val userId = update.from!!.id
        if (currencies.size == 1) {
            sendToChooserMessage(messenger, update, userId, info.value)
        } else {
            messenger.sendMessage(
                update.chatId!!,
                "Оберіть валюту переказу.",
                replyMarkup = InlineKeyboardMarkup(
                    currencies
                        .map {
                            listOf(
                                InlineKeyboardButton(
                                    it.name,
                                    callbackData = "/transfer $userId ${info.value} ${it.name}"
                                )
                            )
                        })
            )
        }
    }

    private fun processValueCurrency(update: Update, messenger: TelegramMessenger, info: ValueCurrencyTransactionInfo) {
        val currency = currencies.find { it.name == info.currency.name }
        if (currency == null) {
            messenger.sendMessage(update.chatId!!, "Валюти ${info.currency.name} не знайдено")
            return
        }

        sendToChooserMessage(messenger, update, update.from!!.id, info.value)
    }

    private fun processFromValueCurrency(
        update: Update,
        messenger: TelegramMessenger,
        info: FromValueCurrencyTransactionInfo
    ) {
        if (update.from!!.id != info.fromId) {
            messenger.sendMessage(update.chatId!!, "Підтвердити операцію має той хто її почав.")
            return
        }

        val currency = currencies.find { it.name == info.currency.name }
        if (currency == null) {
            messenger.sendMessage(update.chatId!!, "Валюти ${info.currency.name} не знайдено")
            return
        }

        sendToChooserMessage(messenger, update, update.from!!.id, info.value)
    }

    private fun processFromToValueCurrency(
        update: Update,
        messenger: TelegramMessenger,
        info: FromToValueCurrencyTransactionInfo
    ) {
        if (update.from!!.id != info.fromId) {
            messenger.sendMessage(update.chatId!!, "Підтвердити операцію має той хто її почав.")
            return
        }

        val currency = currencies.find { it.name == info.currency.name }
        if (currency == null) {
            messenger.sendMessage(update.chatId!!, "Валюти ${info.currency.name} не знайдено")
            return
        }

        val fromUser = users.find { it.id == info.fromId }
        val toUser = users.find { it.id == info.toId }
        messenger.sendMessage(
            update.chatId!!,
            "Підтвердити переказ: від ${fromUser!!.name} до ${toUser!!.name} - ${info.value} ${info.currency.name}",
            replyMarkup = InlineKeyboardMarkup(
                listOf(
                    listOf(
                        InlineKeyboardButton(
                            "Підтвердити!",
                            callbackData = "/confirmtransfer ${info.fromId} ${info.toId} ${info.value} ${info.currency.name}"
                        )
                    )
                )
            )
        )
    }

    private fun sendToChooserMessage(
        messenger: TelegramMessenger,
        update: Update,
        userId: Long,
        value: Double
    ) {
        messenger.sendMessage(
            update.chatId!!, "Оберіть людину якій ви переводите кошти.",
            replyMarkup = InlineKeyboardMarkup(
                users
                    .filter { it.id != userId }
                    .map {
                        listOf(
                            InlineKeyboardButton(
                                it.name,
                                callbackData = "/transfer $userId ${it.id} $value ${currencies[0].name}"
                            )
                        )
                    })
        )
    }
}

class ConfirmTransactionAction : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}