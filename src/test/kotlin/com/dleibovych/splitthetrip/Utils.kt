package com.dleibovych.splitthetrip

import me.ivmg.telegram.entities.CallbackQuery
import me.ivmg.telegram.entities.Message
import me.ivmg.telegram.entities.Update
import me.ivmg.telegram.entities.payment.PreCheckoutQuery
import me.ivmg.telegram.entities.payment.ShippingQuery

fun createTelegramUpdate(
    id: Long,
    message: Message? = null,
    editedMessage: Message? = null,
    channelPost: Message? = null,
    editedChannelPost: Message? = null,
    callbackQuery: CallbackQuery? = null,
    preCheckoutQuery: PreCheckoutQuery? = null,
    shippingQuery: ShippingQuery? = null
): Update {
    return Update(
        id,
        message,
        editedMessage,
        channelPost,
        editedChannelPost,
        callbackQuery,
        preCheckoutQuery,
        shippingQuery
    )
}