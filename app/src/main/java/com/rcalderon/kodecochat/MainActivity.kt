package com.rcalderon.kodecochat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rcalderon.kodecochat.conversation.ConversationContent
import com.rcalderon.kodecochat.conversation.ConversationUiState
import com.rcalderon.kodecochat.ui.theme.KodecoChatTheme
import com.rcalderon.kodecochat.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val messagesWithUsers by viewModel.messages.collectAsStateWithLifecycle()
            val currentUiState = ConversationUiState(channelName = "Android Apprentice", messagesWithUsers, viewModel)

            KodecoChatTheme {
                ConversationContent(
                    uiState = currentUiState
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KodecoChatTheme {
        Greeting("Android")
    }
}