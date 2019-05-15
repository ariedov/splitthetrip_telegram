package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.actions.*
import com.dleibovych.splitthetrip.actions.expense.AddExpenseAction
import com.dleibovych.splitthetrip.actions.expense.ConfirmExpenseAction
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.command
import me.ivmg.telegram.dispatcher.text
import org.litote.kmongo.KMongo

fun main() {

    val client = KMongo.createClient("mongo", 27017)
    val database = client.getDatabase("splitthetrip")

    val storage = Storage(database)

    val bot = bot {
        token = System.getenv("TELEGRAM_BOT_TOKEN") ?: ""
        dispatch {
            var messenger: TelegramMessenger? = null
            text { bot, _ -> messenger = messenger ?: TelegramMessenger(bot) }

            command("start") { _, update -> StartAction(storage).perform(messenger!!, update) }

            command("register") { _, update -> RegisterAction().perform(messenger!!, update) }

            command("confirmregister") { _, update -> ConfirmRegisterAction(storage).perform(messenger!!, update) }

            command("addcurrency") { _, update -> AddCurrencyAction().perform(messenger!!, update) }

            command("confirmcurrency") { _, update -> ConfirmNewCurrencyAction(storage).perform(messenger!!, update) }

            command("add") { _, update -> AddExpenseAction(storage).perform(messenger!!, update) }

            command("confirmadd") { _, update -> ConfirmExpenseAction(storage).perform(messenger!!, update) }

            command("info") { _, update -> InfoAction(storage).perform(messenger!!, update) }

            command("transfer") { bot, update ->

            }
        }
    }
    bot.startPolling()
}
