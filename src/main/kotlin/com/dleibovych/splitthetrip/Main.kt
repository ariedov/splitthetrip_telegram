package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.actions.ConfirmRegisterAction
import com.dleibovych.splitthetrip.actions.RegisterAction
import com.dleibovych.splitthetrip.actions.StartAction
import com.dleibovych.splitthetrip.data.MongoStorage
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.command
import org.litote.kmongo.KMongo

fun main() {

    val client = KMongo.createClient()
    val database = client.getDatabase("splitthetrip")

    val storage = MongoStorage(database)

    val bot = bot {
        token = System.getenv("TELEGRAM_BOT_TOKEN") ?: ""
        dispatch {
            command("start") { bot, update ->
                StartAction(storage).perform(bot, update)
            }

            command("register") { bot, update ->
                RegisterAction().perform(bot, update)
            }

            command("confirmregister") { bot, update ->
                ConfirmRegisterAction(storage).perform(bot, update)
            }

            command("addcurrency") { bot, update ->

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
