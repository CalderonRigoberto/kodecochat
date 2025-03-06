package com.rcalderon.kodecochat.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rcalderon.kodecochat.data.model.ChatRoom
import com.rcalderon.kodecochat.data.model.Message
import com.rcalderon.kodecochat.data.model.MessageUiModel
import com.rcalderon.kodecochat.data.model.User
import com.rcalderon.kodecochat.data.model.initialMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.UUID

class MainViewModel : ViewModel() {

    private val userId = UUID.randomUUID().toString()
    private val _messages: MutableList<MessageUiModel> = initialMessages.toMutableStateList()
    private val _messagesFlow: MutableStateFlow<List<MessageUiModel>> by lazy {
        MutableStateFlow(mutableListOf())
    }
    val messages = _messagesFlow.asStateFlow()

    private val emptyChatRoom = ChatRoom(
        id = "public",
        name = "Android Apprentice",
        createdOn = Clock.System.now(),
        messagesCollectionId = "1440174b9330e430b46da939f0b04a34a40e10ac8073671156da174fef1ffaef",
        isPrivate = false,
        collectionID = "public",
        createdBy = "Kodeco User"
    )
    private val _currentChatRoom = MutableStateFlow(emptyChatRoom)
    val currentRoom = _currentChatRoom.asStateFlow()
    val currentUserId = MutableStateFlow(userId)

    init {
        viewModelScope.launch(Dispatchers.Default) {
            _messagesFlow.emit(_messages)
        }
    }

    fun onCreateNewMessage(messageText: String, photoUri: Uri?) {
        val currentMoment = Clock.System.now()
        val message = Message(
            UUID.randomUUID().toString(),
            currentMoment,
            currentRoom.value.id,
            messageText,
            userId,
            photoUri
        )

        if(message.photoUri == null) {
            viewModelScope.launch(Dispatchers.Default) {
                createMessageForRoom(message, currentRoom.value)
            }
        }
    }


    private suspend fun createMessageForRoom(message: Message, chatRoom: ChatRoom) {
        val user = User(userId)
        val messageUiModel = MessageUiModel(message, user)
        _messages.add(0, messageUiModel)
        _messagesFlow.emit(_messages)
    }
}