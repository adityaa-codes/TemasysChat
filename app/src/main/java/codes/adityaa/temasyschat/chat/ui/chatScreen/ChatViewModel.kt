package codes.adityaa.temasyschat.chat.ui.chatScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codes.adityaa.temasyschat.chat.models.ChatMessage
import codes.adityaa.temasyschat.chat.models.Roomlist
import codes.adityaa.temasyschat.chat.ui.chatScreen.ChatScreenEvents
import codes.adityaa.temasyschat.chat.ui.chatScreen.ChatScreenState
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
class ChatViewModel @Inject constructor(
    private val preferencesManager: SharedPrefManager,


    ) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChatScreenEvents>()
    val events = _events.asSharedFlow()

    val userId = preferencesManager.userId
    val firstName = preferencesManager.firstName
    val email = preferencesManager.userEmail
    val chatType = preferencesManager.chatType
    val groupRoomId = preferencesManager.roomId?:"11"

    fun getRoomListObject(): Roomlist {
        return preferencesManager.getRoomListObject()
    }

    fun onMessageSent(roomlist: Roomlist) {
        Timber.d("Message Sent")
    }


    fun onChatItemPressed(chatMessage: ChatMessage) = viewModelScope.launch {
        Timber.d("Chat Pressed")

    }


}