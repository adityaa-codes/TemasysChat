package codes.adityaa.temasyschat.chat.ui.roomScreen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import codes.adityaa.temasyschat.R
import codes.adityaa.temasyschat.databinding.FragmentRoomBinding
import codes.adityaa.temasyschat.util.showToast
import codes.adityaa.temasyschat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RoomFragment : Fragment(R.layout.fragment_room) {
    private val binding by viewBinding(FragmentRoomBinding::bind)

    private val viewModel by viewModels<RoomViewModel>()

    private lateinit var roomListAdapter: RoomAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        initViews()
        collectUiState()
        collectUiEvents()
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.uiState.collectLatest {
            binding.apply {
                roomListAdapter.submitList(it.mainRvItems.roomlist)
                binding.progressBar.isVisible = it.isLoading
                binding.swipeRefresh.isVisible = !it.isLoading
                binding.swipeRefresh.isRefreshing = it.isRefreshing
            }
        }
    }

    private fun collectUiEvents() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.events.collectLatest {
            when (it) {
                is RoomListScreenEvents.NavigateToMyChatScreen -> {
                    findNavController().navigate(
                        RoomFragmentDirections.actionRoomFragmentToChatFragment()
                    )
                }
                is RoomListScreenEvents.ShowToast -> requireContext().showToast(it.message)
            }
        }
    }

    private fun initViews() = binding.apply {
        roomRV.apply {
            adapter = roomListAdapter
            setHasFixedSize(false)
        }
    }

    private fun initAdapters() {
        roomListAdapter = RoomAdapter(
            onChatPressed = viewModel::onChatPressed
        )
    }
}
