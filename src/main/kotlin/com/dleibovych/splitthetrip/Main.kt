package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.actions.*
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.HandleUpdate
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.Dispatcher
import me.ivmg.telegram.dispatcher.handlers.Handler
import me.ivmg.telegram.dispatcher.text
import me.ivmg.telegram.entities.Update
import org.litote.kmongo.KMongo

fun main() {

    val client = KMongo.createClient("mongo", 27017)
    val database = client.getDatabase("splitthetrip")

    val storage = Storage(database)
    val actionRouter = ActionRouter(storage)

    val bot = bot {
        token = System.getenv("TELEGRAM_BOT_TOKEN") ?: ""
        dispatch {
            var messenger: TelegramMessenger? = null
            all { bot, update ->
                messenger = messenger ?: TelegramMessenger(bot)

                val action = actionRouter.createAction(update)
                action.perform(messenger!!, update)
            }
        }
    }
    bot.startPolling()
}

fun Dispatcher.all(text: String? = null, body: HandleUpdate) {
    addHandler(AllHandler(text, body))
}

class AllHandler(private val text: String? = null, handler: HandleUpdate): Handler(handler) {

    override val groupIdentifier: String = "All Handler"

    override fun checkUpdate(update: Update): Boolean = true

}