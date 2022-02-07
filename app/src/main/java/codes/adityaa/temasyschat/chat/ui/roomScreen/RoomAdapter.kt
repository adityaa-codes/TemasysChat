package codes.adityaa.temasyschat.chat.ui.roomScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.adityaa.temasyschat.chat.models.Roomlist
import codes.adityaa.temasyschat.databinding.ItemRoomBinding


class RoomAdapter(
    private val onChatPressed: (Roomlist) -> Unit,
) : ListAdapter<Roomlist, RoomAdapter.RoomViewHolder>(RoomDiff()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding =
            ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class RoomViewHolder(private val binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onChatPressed(currentList[absoluteAdapterPosition]) }

        }

        fun bind(roomList: Roomlist) = binding.apply {
            this.roomList = roomList
            binding.materialCardView.isVisible =
                currentList[absoluteAdapterPosition].notificationcount != ""


        }
    }

    class RoomDiff : DiffUtil.ItemCallback<Roomlist>() {
        override fun areItemsTheSame(oldItem: Roomlist, newItem: Roomlist) =
            oldItem.roomid == newItem.roomid

        override fun areContentsTheSame(oldItem: Roomlist, newItem: Roomlist) =
            oldItem == newItem
    }


}