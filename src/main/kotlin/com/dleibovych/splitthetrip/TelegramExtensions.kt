package com.dleibovych.splitthetrip

import me.ivmg.telegram.entities.Update
import me.ivmg.telegram.entities.User

val Update.chatId: Long?
    get() = callbackQuery?.message?.chat?.id ?: message?.chat?.id

val Update.text: String?
    get() = callbackQuery?.data ?: message?.text

val Update.from: User?
    get() = callbackQuery?.from ?: message?.from