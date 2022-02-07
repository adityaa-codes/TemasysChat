package codes.adityaa.temasyschat.chat.ui.chatScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import codes.adityaa.temasyschat.R
import codes.adityaa.temasyschat.chat.enums.MessageType
import codes.adityaa.temasyschat.chat.models.ChatMessage
import com.google.android.material.card.MaterialCardView

class ChatAdapter(
    private val messageArrayList: ArrayList<ChatMessage>,
    private val onChatItemPressed: (ChatMessage) -> Unit,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == MessageType.CHAT_LOCAL_GRP_SIG.ordinal) {
            LocalMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_local, parent, false)
            )
        } else if (viewType == MessageType.CHAT_REMOTE_GRP_SIG.ordinal) {
            RemoteMessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_remote, parent, false)
            )

        } else {
            DateMessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_remote, parent, false)
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = messageArrayList[position]
        if (currentItem.messageRowType.ordinal == MessageType.CHAT_LOCAL_GRP_SIG.ordinal) {
            (holder as LocalMessageViewHolder).bind(position)
        } else if (currentItem.messageRowType.ordinal == MessageType.CHAT_REMOTE_GRP_SIG.ordinal) {
            (holder as RemoteMessageViewHolder).bind(position)
        } else {
            (holder as DateMessageViewHolder).bind(position)

        }

    }

    override fun getItemCount(): Int {
        return messageArrayList.size

    }

    inner class LocalMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var messageContent: TextView = itemView.findViewById(R.id.localMessageTV)
        var peerID: TextView = itemView.findViewById(R.id.localPeerIDTV)
        var timeStamp: TextView = itemView.findViewById(R.id.localTimestampTV)

        fun bind(position: Int) {
            val currentItem = messageArrayList[position]
            messageContent.text = currentItem.message
            peerID.text = currentItem.peerID
            timeStamp.text = currentItem.timeStamp
        }


    }

    inner class RemoteMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageContent: TextView = itemView.findViewById(R.id.remoteMessageTV)
        var peerID: TextView = itemView.findViewById(R.id.remotePeerIDTV)
        var timeStamp: TextView = itemView.findViewById(R.id.remoteTimestampTV)
        var chatItem: MaterialCardView = itemView.findViewById(R.id.remoteMessageCV)

        init {
            chatItem.setOnLongClickListener {
                onChatItemPressed(messageArrayList[absoluteAdapterPosition])
                return@setOnLongClickListener true
            }
        }

        fun bind(position: Int) {

            val currentItem = messageArrayList[position]
            messageContent.text = currentItem.message
            peerID.text = currentItem.peerID
            timeStamp.text = currentItem.timeStamp
        }
    }

    inner class DateMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeStamp: TextView = itemView.findViewById(R.id.remoteTimestampTV)

        fun bind(position: Int) {

            val currentItem = messageArrayList[position]
            timeStamp.text = currentItem.timeStamp
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = messageArrayList[position]
        return currentItem.messageRowType.ordinal
    }
}