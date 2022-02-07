package codes.adityaa.temasyschat.chat.ui.chatScreen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import codes.adityaa.temasyschat.R
import com.google.gson.Gson
import codes.adityaa.temasyschat.chat.core.ChatConfigImpl
import codes.adityaa.temasyschat.chat.core.Config
import codes.adityaa.temasyschat.chat.core.NetworkManager
import codes.adityaa.temasyschat.chat.core.SkylinkService
import codes.adityaa.temasyschat.chat.enums.MessageType
import codes.adityaa.temasyschat.chat.models.ChatMessage
import codes.adityaa.temasyschat.chat.models.SkylinkReceivedMessageItem
import codes.adityaa.temasyschat.chat.models.SkylinkServerMessage
import codes.adityaa.temasyschat.chat.models.Roomlist
import codes.adityaa.temasyschat.databinding.FragmentChatBinding
import codes.adityaa.temasyschat.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import org.json.JSONObject
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {
    private val binding by viewBinding(FragmentChatBinding::bind)
    private val viewModel by viewModels<ChatViewModel>()

    lateinit var skylinkService: SkylinkService
    lateinit var networkAware: NetworkManager
    lateinit var chatConfig: ChatConfigImpl
    lateinit var config: Config
    lateinit var chatAdapter: ChatAdapter
    lateinit var chatMessageArrayList: ArrayList<ChatMessage>
    lateinit var roomListItem: Roomlist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar(true)
        networkAware = NetworkManager(requireContext())
        chatConfig = ChatConfigImpl()
        config = Config(requireActivity())
        skylinkService = SkylinkService(networkAware, chatConfig, config, requireContext())

        skylinkService.initSkylinkConnection()

        if (viewModel.chatType == "One") {
            roomListItem = viewModel.getRoomListObject()
            skylinkService.connectToRoom(roomListItem.roomid, viewModel.userId)
        } else {

            skylinkService.connectToRoom(viewModel.groupRoomId, viewModel.userId)
        }

        chatMessageArrayList = ArrayList()
        chatMessageArrayList.clear()
        // Defining the ArrayAdapter to set items to ListView
        chatAdapter = ChatAdapter(
            // processGetChatCollection()
            chatMessageArrayList,
            onChatItemPressed = {
                requireContext().showAlertDialog(
                    title = "Are you sure?",
                    description = "Are you sure that you  want to report this message?",
                    positiveButtonText = "Yes",
                    negativeButtonText = "No, Cancel",
                    onConfirmClick = { viewModel::onChatItemPressed }
                )
            }
        )
        binding.messagesRV.layoutManager = LinearLayoutManager(requireContext())
        binding.messagesRV.adapter = chatAdapter
        // getStoredMessages()
        binding.sendMessageBtn.setOnClickListener {
            if (viewModel.chatType == "One") {
                viewModel.onMessageSent(roomListItem)
            }
            val message = binding.messagesTIET.text.toString().trim()
            processSendServerMessage(message)
            binding.messagesTIET.text?.clear()
        }
        if (viewModel.chatType == "One") {
            binding.roomName.text = roomListItem.receivername
            binding.userAvatar.setImageUrl(roomListItem.receiverimage)
        } else {
            binding.roomName.text = viewModel.getRoomListObject().roomname
            binding.userAvatar.isVisible = false
        }

        initListeners()
        collectUiEvents()
    }

    private fun initListeners() = binding.apply {
        materialToolBar.setNavigationOnClickListener {
            skylinkService.disconnectFromRoom()
            messagesTIET.hideKeyboard()
            findNavController().popBackStack()
        }
    }

    private fun getStoredMessages() {
        skylinkService.getStoredMessagesLocal()
    }

    private fun showProgressBar(status: Boolean) {
        Timber.d("Show Progress Bar $status")
        binding.progressBar.isVisible = status
        binding.swipeRefresh.isVisible = !status
        binding.messagesRV.isVisible = !status
    }

    private fun collectUiEvents() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            skylinkService.events.collectLatest {
                when (it) {
                    is ChatScreenEvents.ServerMessageReceived -> {
                        showProgressBar(false)
                        processServerMessageReceived(
                            "Name",
                            it.message,
                            false,
                            it.timeStamp ?: 0
                        )

                        Timber.d("${it.message},${it.timeStamp}")
                    }
                    is ChatScreenEvents.RoomConnected -> {
                        getStoredMessages()
                    }
                    is ChatScreenEvents.MessageHistoryReceived -> {
                        processMessages(it.message)
                        showProgressBar(false)
                    }
                    is ChatScreenEvents.Loading -> showProgressBar(true)
                    is ChatScreenEvents.ShowToast -> {
                        showProgressBar(false)
                        requireContext().showToast(it.message)
                    }
                }
            }
        }

    private fun processMessages(message: List<SkylinkReceivedMessageItem>) {
        val sortedMessage = message.sortedBy { it.timeStamp }

        for (messages in sortedMessage) {
            var messageRowType: MessageType? = null

            messageRowType =
                if ((messages.userID ?: messages.userId ?: "1") == viewModel.userId) {
                    MessageType.CHAT_LOCAL_GRP_SIG
                } else {
                    MessageType.CHAT_REMOTE_GRP_SIG
                }
            addLocalMessage(
                messages.userId ?: messages.userID ?: "1",
                messages.emailId ?: messages.emailID ?: "NA",
                messages.message,
                messages.timeStamp,
                messageRowType
            )
        }
    }

    private fun processSendServerMessage(message: String) {

        var messageRowType: MessageType? = null
        var skylinkMessage =
            SkylinkServerMessage(
                message,
                viewModel.firstName ?: "Suuz",
                viewModel.userId
            )
        val jsonInString = Gson().toJson(skylinkMessage)
        val mJSONObject = JSONObject(jsonInString)
        skylinkService.sendServerMessage(null, mJSONObject)
        messageRowType = MessageType.CHAT_LOCAL_GRP_SIG

        addLocalMessage(
            viewModel.userId, viewModel.firstName ?: "Suuz",
            message, Date().time, messageRowType
        )
    }

    private fun addLocalMessage(
        localPeerId: String,
        userName: String,
        message: String,
        timeStamp: Long,
        messageRowType: MessageType,
    ) {
        val messageModel = ChatMessage(
            message,
            localPeerId,
            // date2DayTime(Date(timeStamp)),
            getDefaultShortTimeStamp(Date(timeStamp)),
            userName,
            messageRowType,
            email = viewModel.firstName ?: "NA",
            viewModel.userId

        )

        chatMessageArrayList.add(messageModel)
        chatAdapter.notifyDataSetChanged()
        binding.messagesRV.scrollToPosition(chatMessageArrayList.size - 1)
    }

    fun getDefaultShortTimeStamp(date: Date): String {

        val SHORT_TIME_FORMAT = "dd-MM-yyyy HH:mm a"

        val tz = TimeZone.getDefault()
        val df: DateFormat =
            SimpleDateFormat(SHORT_TIME_FORMAT, Locale.ENGLISH)
        df.timeZone = tz
        return df.format(date)
    }

    fun addRemoteMessage(
        remotePeerId: String,
        userName: String,
        message: String,
        timeStamp: String?,
        messageRowType: MessageType,
        email: String,
        senderId: String,
    ) {
        val messageModel = ChatMessage(
            message,
            remotePeerId,
            timeStamp.toString(),
            userName,
            messageRowType,
            email = email,
            senderId = senderId
        )

        chatMessageArrayList.add(messageModel)
        chatAdapter.notifyDataSetChanged()
        binding.messagesRV.scrollToPosition(chatMessageArrayList.size - 1)
    }

    fun processServerMessageReceived(
        remotePeerId: String,
        message: Any?,
        isPrivate: Boolean,
        timeStamp: Long,
    ) {
        if (message == null) return
        Timber.d(message.toString())
        val gson = Gson()
        val messageModel = gson.fromJson(
            message.toString(),
            SkylinkServerMessage::class.java
        )
        if (messageModel.userId == viewModel.userId) {
            addLocalMessage(
                remotePeerId,
                messageModel.emailId,
                messageModel.message,
                timeStamp,
                MessageType.CHAT_LOCAL_GRP_SIG
            )
            return
        }

        addRemoteMessage(
            remotePeerId,
            messageModel.emailId,
            messageModel.message,
            getDefaultShortTimeStamp(Date(timeStamp)),
            MessageType.CHAT_REMOTE_GRP_SIG,
            messageModel.emailId,
            messageModel.userId

        )
    }

//    private fun showWarningDialog() {
//        requireContext().showAlertDialog(
//            title = "Are you sure?",
//            description = "Are you sure that you  want to report this message?",
//            positiveButtonText = "Yes",
//            negativeButtonText = "No, Cancel",
//            onConfirmClick = viewModel::firstName
//        )
//    }
}
