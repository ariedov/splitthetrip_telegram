package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.actions.*
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.command
import org.litote.kmongo.KMongo

fun main() {

    val client = KMongo.createClient()
    val database = client.getDatabase("splitthetrip")

    val storage = Storage(database)

    val bot = bot {
        token = System.getenv("TELEGRAM_BOT_TOKEN") ?: ""
        dispatch {
            val messenger = TelegramMessenger(this.bot)
            command("start") { _, update ->
                StartAction(storage).perform(messenger, update)
            }

            command("register") { _, update ->
                RegisterAction().perform(messenger, update)
            }

            command("confirmregister") { _, update ->
                ConfirmRegisterAction(storage).perform(messenger, update)
            }

            command("addcurrency") { bot, update ->
                AddCurrencyAction().perform(messenger, update)
            }

            command("confirmcurrency") { bot, update ->
                ConfirmNewCurrencyAction(storage).perform(messenger, update)
            }

            command("add") { bot, update ->

            }

            command("info") { bot, update ->

            }

            command("transfer") { bot, update ->

            }

            command("end") { bot, update ->

            }
        }
    }
    bot.startPolling()
}
