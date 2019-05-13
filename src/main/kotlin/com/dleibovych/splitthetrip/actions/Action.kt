package com.dleibovych.splitthetrip.actions

import me.ivmg.telegram.entities.Update

interface Action {

    fun perform(messenger: TelegramMessenger, update: Update)
}