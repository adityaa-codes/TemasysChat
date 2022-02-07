package codes.adityaa.temasyschat.chat.models

import codes.adityaa.temasyschat.chat.enums.MessageType

data class ChatMessage(
    val message: String,
    val peerID: String,
    val timeStamp: String,
    val userName: String,
    val messageRowType: MessageType,
    val email: String,
    val senderId: String,
)