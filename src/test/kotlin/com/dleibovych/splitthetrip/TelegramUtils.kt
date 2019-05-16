package com.dleibovych.splitthetrip

import me.ivmg.telegram.entities.*
import me.ivmg.telegram.entities.payment.PreCheckoutQuery
import me.ivmg.telegram.entities.payment.ShippingQuery
import me.ivmg.telegram.entities.payment.SuccessfulPayment
import java.time.Instant
import java.util.*

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

fun createTelegramMessage(
    id: Long,
    chat: Chat,
    from: User? = null,
    date: Int = (Date.from(Instant.now()).time / 1000).toInt(),
    forwardedFrom: User? = null,
    forwardedFromChat: Chat? = null,
    forwardDate: Int? = null,
    replyToMessage: Message? = null,
    editDate: Int? = null,
    text: String? = null,
    entities: List<MessageEntity>? = null,
    captionEntities: List<MessageEntity>? = null,
    audio: Audio? = null,
    document: Document? = null,
    game: Game? = null,
    photo: List<PhotoSize>? = null,
    sticker: Sticker? = null,
    video: Video? = null,
    voice: Voice? = null,
    videoNote: VideoNote? = null,
    caption: String? = null,
    contact: Contact? = null,
    location: Location? = null,
    venue: Venue? = null,
    newChatMember: User? = null,
    leftChatMember: User? = null,
    newChatTitle: String? = null,
    newChatPhoto: List<PhotoSize>? = null,
    deleteChatPhoto: Boolean? = null,
    groupChatCreated: Boolean? = null,
    supergroupChatCreated: Boolean? = null,
    channelChatCreated: Boolean? = null,
    migrateToChatId: Long? = null,
    migrateFromChatId: Long? = null,
    invoice: Invoice? = null,
    successfulPayment: SuccessfulPayment? = null
): Message {
    return Message(
        id,
        from,
        date,
        chat,
        forwardedFrom,
        forwardedFromChat,
        forwardDate,
        replyToMessage,
        editDate,
        text,
        entities,
        captionEntities,
        audio,
        document,
        game,
        photo,
        sticker,
        video,
        voice,
        videoNote,
        caption,
        contact,
        location,
        venue,
        newChatMember,
        leftChatMember,
        newChatTitle,
        newChatPhoto,
        deleteChatPhoto,
        groupChatCreated,
        supergroupChatCreated,
        channelChatCreated,
        migrateToChatId,
        migrateFromChatId,
        invoice,
        successfulPayment
    )
}

fun createTelegramChat(
    id: Long,
    type: String = "",
    title: String? = null,
    username: String? = null,
    firstName: String? = null,
    lastName: String? = null,
    allMembersAreAdministrators: Boolean? = null,
    photo: ChatPhoto? = null,
    description: String? = null,
    inviteLink: String? = null,
    pinnedMessage: String? = null,
    stickerSetName: String? = null,
    canSetStickerSet: Boolean? = null

): Chat {
    return Chat(
        id,
        type,
        title,
        username,
        firstName,
        lastName,
        allMembersAreAdministrators,
        photo,
        description,
        inviteLink,
        pinnedMessage,
        stickerSetName,
        canSetStickerSet
    )
}

fun createTelegramUser(
    id: Long,
    isBot: Boolean,
    firstName: String,
    lastName: String? = null,
    username: String? = null,
    languageCode: String? = null
): User {
    return User(id, isBot, firstName, lastName, username, languageCode)
}

fun createCallbackQuery(
    id: String,
    user: User,
    data: String,
    message: Message? = null,
    inlineMessageId: String? = null
): CallbackQuery {
    return CallbackQuery(id, user, message, inlineMessageId, data)
}