package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.actions.StartAction
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.command

fun main() {

    val bot = bot {
        token = System.getenv("TELEGRAM_BOT_TOKEN") ?: ""
        dispatch {
            command("start") { bot, update ->
                StartAction().perform(bot, update)
            }

            command("register") { bot, update ->

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
