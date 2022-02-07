package codes.adityaa.temasyschat.util.sampleDTOS

import codes.adityaa.temasyschat.chat.models.RoomListDTO
import codes.adityaa.temasyschat.util.sampleJsons.roomListJson
import com.google.gson.Gson

object SampleDTOModels {

    fun getRoomList(): RoomListDTO =
        Gson().fromJson(roomListJson, RoomListDTO::class.java)



}
