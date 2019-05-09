package com.dleibovych.splitthetrip.actions

import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.Update

interface Action {

    fun perform(bot: Bot, update: Update)
}