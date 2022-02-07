package codes.adityaa.temasyschat.chat.ui.chatScreen

import codes.adityaa.temasyschat.chat.models.RoomListDTO

data class ChatScreenState(
    val mainRvItems: RoomListDTO = RoomListDTO(), //TODO:Have to change to message Model
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
)