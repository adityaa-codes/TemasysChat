package codes.adityaa.temasyschat.chat.ui.roomScreen

import codes.adityaa.temasyschat.chat.models.Roomlist

sealed class RoomListScreenEvents {

    data class ShowToast(val message: String) : RoomListScreenEvents()

    class NavigateToMyChatScreen(val roomList: Roomlist) : RoomListScreenEvents()

}
