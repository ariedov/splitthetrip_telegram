package com.dleibovych.splitthetrip

import me.ivmg.telegram.entities.Update

val Update.chatId: Long?
    get() = callbackQuery?.message?.chat?.id ?: message?.chat?.id

val Update.text: String?
    get() = callbackQuery?.data ?: message?.text