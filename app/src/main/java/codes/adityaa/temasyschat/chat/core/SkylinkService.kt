package codes.adityaa.temasyschat.chat.core

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import codes.adityaa.temasyschat.chat.models.SkylinkReceivedMessageItem
import codes.adityaa.temasyschat.chat.ui.chatScreen.ChatScreenEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import sg.com.temasys.skylink.sdk.listener.LifeCycleListener
import sg.com.temasys.skylink.sdk.listener.MessagesListener
import sg.com.temasys.skylink.sdk.listener.OsListener
import sg.com.temasys.skylink.sdk.listener.RemotePeerListener
import sg.com.temasys.skylink.sdk.rtc.*
import sg.com.temasys.skylink.sdk.rtc.SkylinkConnection.SkylinkState
import sg.com.temasys.skylink.sdk.rtc.SkylinkEvent.CONTEXT_DESCRIPTION
import timber.log.Timber
import java.util.*


class SkylinkService(
    private val networkAware: NetworkAware,
    private val chatConfig: ChatConfig,
    private val config: Config,
    private val context: Context,
) : LifeCycleListener, MessagesListener, RemotePeerListener, OsListener {

    val TAG: String = SkylinkService::class.java.name
    var remotePeerId = " "
    var localPeerID: String? = null
    private val PEER_USER_ID = "senderId"
    private val PEER_USERNAME = "senderName"
    private val MESSAGE_CONTENT = "data"
    private val TIMESTAMP = "timeStamp"

    private val _events = MutableSharedFlow<ChatScreenEvents>()
    val events = _events.asSharedFlow()


    var skylinkConnection: SkylinkConnection? = null


    override fun onConnectToRoomSucessful() {
        Timber.d(TAG, "onConnectToRoomSuccessful")
        if (skylinkConnection != null) {
            localPeerID = skylinkConnection?.localPeerId
        } else {
            Timber.d(TAG, "onConnectToRoomSucessful:Skylink Null ")
        }
        Timber.d(TAG, "initSkylinkConnection:LocalPeerIDAfterRoomConnect $localPeerID")
        Timber.d(TAG, "initSkylinkConnection:RoomName ${skylinkConnection?.roomId}")
        //setEncryptedMap()

        skylinkConnection?.selectedSecretId = "key1"
        Timber.d(TAG, "onConnectToRoomSuccessful: ${skylinkConnection?.selectedSecretId}")
        Timber.d(TAG, "onConnectToRoomSuccessfulPersist: ${skylinkConnection?.isMessagePersist}")
        if (skylinkConnection != null) {
            val state = skylinkConnection!!.skylinkState
            if (state == SkylinkState.CONNECTED) {
                LifecycleService().lifecycleScope.launch(Dispatchers.Default) {
                    _events.emit(ChatScreenEvents.RoomConnected)

                }
            }
        }

        //getStoredMessagesLocal()
    }

    override fun onConnectToRoomFailed(p0: String?) {
        Timber.d(TAG, "onConnectToRoomFailed($p0)")
    }

    override fun onDisconnectFromRoom(p0: SkylinkEvent?, p1: String?) {
        Timber.d(TAG, "onDisconnectFromRoom($p0, message: $p1)")
    }

    override fun onChangeRoomLockStatus(p0: Boolean, p1: String?) {

    }

    override fun onReceiveInfo(p0: SkylinkInfo?, p1: HashMap<String, Any>) {
        Timber.d(
            TAG,
            "onReceiveInfo(skylinkInfo: " + p0.toString()
                    + ", details: " + p1[CONTEXT_DESCRIPTION]
        )
    }

    override fun onReceiveWarning(p0: SkylinkError?, p1: HashMap<String, Any>) {
        val contextDescriptionString = p1[CONTEXT_DESCRIPTION]
        Timber.d(
            TAG,
            "onReceiveWarning(skylinkInfo: " + p0.toString() + ", details: " + contextDescriptionString
        )
    }

    override fun onReceiveError(p0: SkylinkError?, p1: HashMap<String, Any>) {
        val contextDescriptionString = p1[CONTEXT_DESCRIPTION]
        Timber.d(
            TAG,
            "onReceiveError(skylinkInfo: " + p0.toString() + ", details: " + contextDescriptionString
        )

    }

    override fun onReceiveServerMessage(
        message: Any?,
        isPublic: Boolean,
        timeStamp: Long?,
        remotePeerId: String?,
    ) {
        Timber.d(TAG, "onReceiveServerMessage: $message")
        LifecycleService().lifecycleScope.launch(Dispatchers.Default) {
            _events.emit(ChatScreenEvents.ServerMessageReceived(message, timeStamp))

        }
//        (context as MainActivity).onServerMessageReceived(
//            remotePeerId!!,
//            message,
//            isPublic,
//            timeStamp!!
//        )
    }

    override fun onReceiveP2PMessage(
        message: Any?,
        isPublic: Boolean,
        timeStamp: Long?,
        remotePeerId: String?,
    ) {
        Timber.d(TAG, "onReceiveP2PMessage: $message")


    }

    override fun onReceiveRemotePeerJoinRoom(peerId: String?, p1: UserInfo?) {
        remotePeerId = peerId!!
        Timber.d(TAG, "onReceiveRemotePeerJoinRoom: $peerId")

    }

    override fun onConnectWithRemotePeer(p0: String?, p1: UserInfo?, p2: Boolean) {
        Timber.d(TAG, "onConnectWithRemotePeer: $p0")

    }

    override fun onRefreshRemotePeerConnection(
        p0: String?,
        p1: UserInfo?,
        p2: Boolean,
        p3: Boolean,
    ) {

    }

    override fun onReceiveRemotePeerUserData(p0: Any?, p1: String?) {

    }

    override fun onOpenRemotePeerDataConnection(p0: String?) {

    }

    override fun onDisconnectWithRemotePeer(p0: String?, p1: UserInfo?, p2: Boolean) {

    }

    override fun onReceiveRemotePeerLeaveRoom(p0: String?, p1: SkylinkInfo?, p2: UserInfo?) {

    }

    override fun onErrorForRemotePeerConnection(p0: SkylinkError?, p1: HashMap<String, Any>?) {

    }

    override fun onRequirePermission(p0: Intent?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onRequirePermission(p0: Array<out String>?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onGrantPermission(p0: Intent?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onGrantPermission(p0: Array<out String>?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onDenyPermission(p0: Intent?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onDenyPermission(p0: Array<out String>?, p1: Int, p2: SkylinkInfo?) {

    }


    fun initSkylinkConnection() {
        LifecycleService().lifecycleScope.launch(Dispatchers.Default) {
            _events.emit(ChatScreenEvents.Loading)

        }

        val skylinkConfig: SkylinkConfig = chatConfig.getSkylinkConfig()

        skylinkConnection = SkylinkConnection.getInstance()

        skylinkConnection?.init(skylinkConfig, context, object : SkylinkCallback {
            override fun onError(p0: SkylinkError?, p1: HashMap<String, Any>?) {
                val contextDescription = p1?.get(CONTEXT_DESCRIPTION) as String
                Timber.e(contextDescription)
                onConnectToRoomFailed(contextDescription)
            }
        })
        skylinkConnection?.isMessagePersist = true
        skylinkConnection?.selectedSecretId = "key1"
        Timber.d("initSkylinkConnection:" + skylinkConnection?.selectedSecretId + " ")
        skylinkConnection?.lifeCycleListener = this
        skylinkConnection?.remotePeerListener = this
        skylinkConnection?.osListener = this
        skylinkConnection?.messagesListener = this
        skylinkConnection?.isEnableLogs = true


    }

    fun connectToRoom(ROOM_NAME: String, USER_NAME: String) {
        LifecycleService().lifecycleScope.launch(Dispatchers.Default) {
            _events.emit(ChatScreenEvents.Loading)

        }
        skylinkConnection?.connectToRoom(
            config.appKey, config.appKeySecret, ROOM_NAME, USER_NAME,
            object : SkylinkCallback {
                override fun onError(error: SkylinkError?, details: HashMap<String?, Any?>) {
                    val contextDescription = details[CONTEXT_DESCRIPTION] as String?
                    Timber.e("Connect To Room Error ${contextDescription!!}")

                }
            })

        if (skylinkConnection != null) {
            val state = skylinkConnection!!.skylinkState
            if (state == SkylinkState.CONNECTED) {
                LifecycleService().lifecycleScope.launch(Dispatchers.Default) {
                    _events.emit(ChatScreenEvents.RoomConnected)

                }
            }
        }
        setEncryptedMap()

        //getStoredMessagesLocal()
        Timber.d("connectToRoom:" + skylinkConnection?.selectedSecretId + " ")
        //if(SkylinkConnection.SkylinkState.valueOf() == SkylinkConnection.SkylinkState.CONNECTED)

//        skylinkConnection?.disconnectFromRoom(object : SkylinkCallback{
//
//        })
        //Timber.d(TAG, "connectToRoom: " + skylinkConnection?.localPeerId)


    }


    fun sendServerMessage(remotePeerId: String?, message: Any?) {
        if (skylinkConnection != null) {

            skylinkConnection?.sendServerMessage(message, remotePeerId, object : SkylinkCallback {
                override fun onError(error: SkylinkError, details: HashMap<String, Any>) {
                    // processMessageSendFailed()
                    val contextDescription = details[CONTEXT_DESCRIPTION] as String?
                    Timber.e(contextDescription!!)
                }
            })
        }
    }


    fun getPeerId(): String? {
        Timber.d("getPeerId:$localPeerID ")
        return localPeerID
    }

    //    fun getStoredMessagesLocal() {
//        if (skylinkConnection != null) {
//            skylinkConnection?.getStoredMessages(object : SkylinkCallback.StoredMessages {
//                override fun onObtainStoredMessages(
//                    messages: JSONArray?,
//                    p1: MutableMap<SkylinkError, JSONArray>?
//                ) {
//                    Timber.d(TAG, "onObtainStoredMessages:$messages ")
//                    Timber.d(TAG, "onObtainStoredMessages:$p1 ")
//                }
//            })
//        }
//    }
    fun getStoredMessagesLocal() {
        if (skylinkConnection != null) {
            skylinkConnection?.getStoredMessages(object : SkylinkCallback.StoredMessages {
                override fun onObtainStoredMessages(
                    messages: JSONArray?,
                    p1: MutableMap<SkylinkError?, JSONArray?>?,
                ) {
                    Timber.d("onObtainStoredMessages:$messages")
                    processStoredMessagesResult(messages)
                }
            })
        }
    }

    fun processStoredMessagesResult(messages: JSONArray?) {
        var message: Any?
        Timber.d("processStoredMessagesResult:$messages")
        val messagesString = messages.toString()
        val gson = GsonBuilder().create()
        if (messages == null || messages.length() == 0) {
            LifecycleService().lifecycleScope.launch(Dispatchers.Default) {
                _events.emit(ChatScreenEvents.MessageHistoryReceived(listOf()))
            }
            return
        }

        val messagesList: List<SkylinkReceivedMessageItem> =
            gson.fromJson(messagesString, Array<SkylinkReceivedMessageItem>::class.java).toList()

        Timber.d(messagesList.toString())

        // remove the UI for getting message history in the dataset



        LifecycleService().lifecycleScope.launch(Dispatchers.Default) {
            _events.emit(ChatScreenEvents.MessageHistoryReceived(messagesList))

        }


        // process stored messages
//        for (i in 0 until messages.length()) {
//            var userName: String
//            var messageContent: String
//            var timeStamp: String?
//            var messageJson: JSONObject? = null
//            var messageArray: JSONArray? = null
//            message = try {
//                messages[i]
//            } catch (e: JSONException) {
//                e.printStackTrace()
//                continue
//            }
//            if (message is JSONArray) {
//                messageArray = message
//                try {
//                    messageJson = messageArray.getJSONObject(0)
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            } else if (message is JSONObject) {
//                messageJson = message
//            }
//            try {
//                userName =
//                    messageJson!!.getString(PEER_USERNAME)
//                Timber.d("processStoredMessagesResult:Username $userName")
//            } catch (e: JSONException) {
//                // use peerId as senderName if userName is not present
//                try {
//                    userName =
//                        messageJson!!.getString(PEER_USER_ID)
//                    Timber.d("processStoredMessagesResult:UserId $userName")
//                } catch (e1: JSONException) {
//                    userName = "N/A"
//                }
//            }
//            messageContent = try {
//                messageJson!!.getString(MESSAGE_CONTENT)
//            } catch (e: JSONException) {
//                "N/A"
//            }
//            timeStamp = try {
//                val timeStampLong =
//                    messageJson!!.getLong(TIMESTAMP)
//                (context as ChatActivity).getDefaultShortTimeStamp(Date(timeStampLong))
//            } catch (e: JSONException) {
//                "Not correct format as Long or N/A"
//            }
//            (context as ChatActivity).addRemoteMessage(
//                userName,
//                userName,
//                messageContent,
//                timeStamp,
//                MessageType.CHAT_REMOTE_GRP_SIG
//            )
//        }
    }

    fun disconnectFromRoom() {
        if (skylinkConnection == null) return
        skylinkConnection?.disconnectFromRoom(object : SkylinkCallback {
            override fun onError(p0: SkylinkError?, p1: HashMap<String?, Any>?) {
                val contextDescription = p1?.get(CONTEXT_DESCRIPTION)
                Timber.e("SkylinkCallback", contextDescription)

            }
        })
    }


    private fun setEncryptedMap() {
        val encryptionMap: MutableMap<String?, String?> = HashMap()
        encryptionMap["key1"] = "secret1"
        encryptionMap["key2"] = "secret2"
        encryptionMap["key3"] = "secret3"
        //Timber.d(TAG, "setEncryptedMap: $encryptionKey - $encryptionValue")
        skylinkConnection?.isMessagePersist = true
        skylinkConnection?.setEncryptSecretsMap(encryptionMap)
        //  skylinkConnection?.selectedSecretId = encryptionMap["room_ten"]v

    }


}


