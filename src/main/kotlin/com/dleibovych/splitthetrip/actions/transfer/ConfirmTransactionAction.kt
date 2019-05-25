package com.dleibovych.splitthetrip.actions.transfer

import com.dleibovych.splitthetrip.actions.Action
import com.dleibovych.splitthetrip.actions.FromToValueCurrencyTransactionInfo
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.actions.TransactionChecker
import com.dleibovych.splitthetrip.chatId
import com.dleibovych.splitthetrip.convertToMoneyLong
import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Storage
import com.dleibovych.splitthetrip.data.Transfer
import com.dleibovych.splitthetrip.from
import com.dleibovych.splitthetrip.text
import me.ivmg.telegram.entities.Update

class ConfirmTransactionAction(
    private val storage: Storage,
    private val checker: TransactionChecker
) : Action {

    private val currencies: List<Currency> by lazy { storage.getCurrencies() }
    private val users: List<BotUser> by lazy { storage.readUsers() }

    override fun perform(messenger: TelegramMessenger, update: Update) {
        val transaction = checker.parseTransaction(update.text!!)
        if (transaction !is FromToValueCurrencyTransactionInfo) {
            messenger.sendMessage(update.chatId!!, "Сталася помилка, недостатньо інформації.")
            return
        }

        if (update.from!!.id != transaction.fromId) {
            messenger.sendMessage(update.chatId!!, "Підтвердити операцію має той хто її почав.")
        }

        val fromUser = users.find { it.id == transaction.fromId }
        val toUser = users.find { it.id == transaction.toId }

        if (fromUser == null) {
            messenger.sendMessage(
                update.chatId!!,
                "Відправника не зареєстровано як платника. Ти можеш зареєструвати себе через /register"
            )
            return
        }

        if (toUser == null) {
            messenger.sendMessage(
                update.chatId!!,
                "Отримувача не зареєстровано як платника. Ти можеш зареєструвати себе через /register"
            )
            return
        }

        val currency = currencies.find { it == transaction.currency }
        if (currency == null) {
            messenger.sendMessage(
                update.chatId!!,
                "Валюта ${transaction.currency.name} не зареєстровано. Ти можеш додати валюту через /addcurrency _валюта_"
            )
            return
        }

        messenger.sendMessage(update.chatId!!, "Переказ успішно збережено.")
        storage.addTransfer(Transfer(fromUser, toUser, transaction.value.convertToMoneyLong(), currency))
    }
}