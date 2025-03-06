package com.rcalderon.kodecochat.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rcalderon.kodecochat.R
import com.rcalderon.kodecochat.components.JumpToBottom
import com.rcalderon.kodecochat.components.KodecochatAppBar
import com.rcalderon.kodecochat.data.model.Message
import com.rcalderon.kodecochat.data.model.MessageUiModel
import com.rcalderon.kodecochat.utilities.isoToTimeAgo
import kotlinx.coroutines.launch

@Composable
fun ConversationContent(
    uiState: ConversationUiState
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val authorId = uiState.authorId.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = uiState.messages) {
        scope.launch {
            scrollState.scrollToItem(0)
        }
    }

    Surface() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Messages(
                    messages = uiState.messages,
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState,
                    authorId = authorId.value
                )
                UserInput(
                    onMessageSent = {
                        uiState.addMessage(it, null)
                    },
                    resetScroll = {
                         scope.launch {
                             scrollState.scrollToItem(0)
                         }
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding()
                )
            }

            ChannelNameBar(channelName = "Android Apprentice")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelNameBar(channelName: String) {
    KodecochatAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = channelName, style = MaterialTheme.typography.titleMedium)
            }
        },
        actions = {
            Icon(
                imageVector = Icons.Outlined.Info,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clickable { }
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .height(24.dp),
                contentDescription = stringResource(R.string.info)
            )
        }
    )
}

@Composable
fun Messages(
    modifier: Modifier,
    messages: List<MessageUiModel>,
    scrollState: LazyListState,
    authorId: String
) {
    val scope = rememberCoroutineScope()

    Box(modifier = modifier) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            contentPadding = WindowInsets.statusBars.add(WindowInsets(top = 90.dp))
                .asPaddingValues(),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                items = messages,
                key = { _, message -> message.id }
            ) { index, content ->

                val prevAuthor = messages.getOrNull(index - 1)?.message?.userId
                val nextAuthor = messages.getOrNull(index + 1)?.message?.userId
                val userId = messages.getOrNull(index)?.message?.userId
                val isFirstMessageByAuthor = prevAuthor != content.message.userId
                val isLastMessageByAuthor = nextAuthor != content.message.userId


                MessageUi(
                    onAuthorClick = {

                    },
                    msg = content,
                    authorId = authorId,
                    userId = userId ?: "",
                    isFirstMessageByAuthor = isFirstMessageByAuthor,
                    isLastMessageByAuthor = isLastMessageByAuthor
                )

            }
        }
    }

    val jumpThreshold = with(LocalDensity.current) {
        56.dp.toPx();
    }
    val jumpToBottomButtonEnabled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex != 0 ||
                    scrollState.firstVisibleItemScrollOffset > jumpThreshold
        }
    }

    JumpToBottom(
        // Only show if the scroller is not at the bottom
        enabled = jumpToBottomButtonEnabled,
        onClicked = {
            scope.launch {
                scrollState.animateScrollToItem(0)
            }
        },
    )

}

@Composable
fun MessageUi(
    onAuthorClick: (String) -> Unit,
    msg: MessageUiModel,
    authorId: String,
    userId: String,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean
) {
    val isUserMe = authorId == userId
    val borderColor =
        if (isUserMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    val authorImageId: Int =
        if (isUserMe) R.drawable.profile_photo_android_developer else R.drawable.someone_else
    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier

    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            Image(
                modifier = Modifier
                    .clickable {
                        onAuthorClick(msg.message.userId)
                    }
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painterResource(id = authorImageId),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        } else {
            Spacer(modifier = Modifier.width(74.dp))
        }

        AuthorAndTextMessage(
            msg = msg,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            authorClicked = onAuthorClick,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    msg: MessageUiModel,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameTimestamp(msg, isUserMe)
        }

        ChatItemBubble(
            msg.message,
            isUserMe,
            authorClicked = authorClicked
        )

        if (isFirstMessageByAuthor) {
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun AuthorNameTimestamp(msg: MessageUiModel, userMe: Boolean) {
    var userFullName: String = msg.user.fullName
    if (userMe) {
        userFullName = "me"
    }

    Row(
        modifier = Modifier.semantics(mergeDescendants = true) {}
    ) {
        Text(
            text = userFullName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = msg.message.createdOn.toString().isoToTimeAgo(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ChatItemBubble(message: Message, isUserMe: Boolean, authorClicked: (String) -> Unit) {
    val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

    val pressedState = remember { mutableStateOf(false) }
    val backgroundCOlor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column {
        Surface(
            color = backgroundCOlor,
            shape = ChatBubbleShape
        ) {
            if (message.text.isNotEmpty()) {
                ClickeableMessage(
                    message = message,
                    isUserMe = isUserMe,
                    authorClicked = authorClicked
                )
            }
        }
    }
}

@Composable
fun ClickeableMessage(
    message: Message,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val styledMessage = messageFormatter(
        message.text,
        primary = isUserMe
    )

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

