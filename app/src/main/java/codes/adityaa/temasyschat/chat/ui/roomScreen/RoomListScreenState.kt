package codes.adityaa.temasyschat.chat.ui.roomScreen

import codes.adityaa.temasyschat.chat.models.RoomListDTO

data class RoomListScreenState(
    val mainRvItems: RoomListDTO = RoomListDTO(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
)