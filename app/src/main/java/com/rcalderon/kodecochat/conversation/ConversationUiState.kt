package com.rcalderon.kodecochat.conversation

import android.net.Uri
import androidx.compose.runtime.toMutableStateList
import com.rcalderon.kodecochat.data.model.MessageUiModel

class ConversationUiState(
    val channelName: String,
    initialMessages: List<MessageUiModel>
) {
    private val _messages: MutableList<MessageUiModel> = initialMessages.toMutableStateList()
    val messages: List<MessageUiModel> = _messages

    fun addMessage(msg: String, photoUri: Uri?) {

    }
}