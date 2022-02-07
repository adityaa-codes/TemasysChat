package codes.adityaa.temasyschat.chat.models

import codes.adityaa.temasyschat.chat.models.Roomlist

data class RoomListDTO(
    val roomlist: List<Roomlist> = emptyList(),
    val status_code: Int = 0
)