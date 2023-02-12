package com.automata.medilogue.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.automata.medilogue.data.ChatTypes
import com.automata.medilogue.data.Message
import com.automata.medilogue.databinding.ItemChatResponseBinding


class ChatRecycleViewAdapter(
    private val messageList: List<Message>,
    private val onOptionClick: (String) -> Unit
): RecyclerView.Adapter<ChatRecycleViewAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemChatResponseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            when(data.type) {
                ChatTypes.CHAT_BOT -> {
                    binding.cvChatBotResponseWrapper.visibility = View.VISIBLE
                    binding.tvChatBotMessage.text = data.content
                    binding.cvUserResponseWrapper.visibility = View.GONE
                }
                ChatTypes.USER_RESPONSE -> {
                    binding.cvUserResponseWrapper.visibility = View.VISIBLE
                    binding.tvUserMessage.text = data.content
                    binding.cvChatBotResponseWrapper.visibility = View.GONE
                }
                ChatTypes.OPTIONS -> {
                    binding.cvChatBotResponseWrapper.visibility = View.VISIBLE
                    binding.tvChatBotMessage.text = data.content
                    binding.cvUserResponseWrapper.visibility = View.GONE
                    binding.cvChatBotResponseWrapper.setOnClickListener {
                        onOptionClick(data.content)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatResponseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}