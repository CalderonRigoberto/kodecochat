package com.rcalderon.kodecochat.conversation

import android.net.Uri
import androidx.compose.runtime.toMutableStateList
import com.rcalderon.kodecochat.data.model.MessageUiModel
import com.rcalderon.kodecochat.viewmodel.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ConversationUiState(
    val channelName: String,
    initialMessages: List<MessageUiModel>,
    val viewModel: MainViewModel
) {
    private val _messages: MutableList<MessageUiModel> = initialMessages.toMutableStateList()
    val messages: List<MessageUiModel> = _messages
    val authorId: MutableStateFlow<String> = viewModel.currentUserId
    fun addMessage(msg: String, photoUri: Uri?) {
        viewModel.onCreateNewMessage(msg, photoUri)
    }
}