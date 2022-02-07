package codes.adityaa.temasyschat.chat.ui.chatScreen

import codes.adityaa.temasyschat.chat.models.SkylinkReceivedMessageItem

sealed class ChatScreenEvents {

    data class ShowToast(val message: String) : ChatScreenEvents()

    object NavigateToMyRoomScreen : ChatScreenEvents()
    object RoomConnected : ChatScreenEvents()
    object Loading : ChatScreenEvents()

    data class ServerMessageReceived(val message: Any?, val timeStamp: Long?) : ChatScreenEvents()
    data class MessageHistoryReceived(val message: List<SkylinkReceivedMessageItem>) : ChatScreenEvents()

}
