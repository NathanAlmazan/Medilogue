package com.automata.medilogue

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.automata.medilogue.adapters.ChatRecycleViewAdapter
import com.automata.medilogue.data.ChatTypes
import com.automata.medilogue.data.Message
import com.automata.medilogue.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    // view
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    // view model
    private val chatViewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(view.context)
        layoutManager.stackFromEnd = true
        binding.rvChatConversation.layoutManager = layoutManager

        chatViewModel.messageList.observe(viewLifecycleOwner) { messages ->
            binding.rvChatConversation.adapter = ChatRecycleViewAdapter(messages) { m -> handleOptionClicked(m) }
            binding.rvChatConversation.scrollToPosition(messages.size - 1)
        }

        binding.ibSend.setOnClickListener {
            chatViewModel.handleMessageSent(binding.etInputMessage.text.toString())
            binding.etInputMessage.text?.clear()
        }
    }

    private fun handleOptionClicked(message: String) {
        chatViewModel.handleMessageSent(message)
    }
}