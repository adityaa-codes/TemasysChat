package codes.adityaa.temasyschat.chat.ui.roomScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codes.adityaa.temasyschat.chat.models.Roomlist
import codes.adityaa.temasyschat.util.sampleDTOS.SampleDTOModels
import codes.adityaa.temasyschat.util.sharedPreference.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomListScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RoomListScreenEvents>()
    val events = _events.asSharedFlow()
    private var userId = sharedPrefManager.userId.toString() // "2"

    init {
        remakeApiCall()
        Timber.d(userId)
    }


    private fun remakeApiCall() = viewModelScope.launch {
        getProfile()
    }

    fun onChatPressed(roomlist: Roomlist) = viewModelScope.launch {
        sharedPrefManager.saveRoomListObject(roomList = roomlist)
        sharedPrefManager.saveChatType("One")
        _events.emit(RoomListScreenEvents.NavigateToMyChatScreen(roomlist))
    }



    private suspend fun getProfile(overrideProgressBar: Boolean = false) {
        _uiState.emit(uiState.value.copy(mainRvItems = SampleDTOModels.getRoomList()))
    }


}
