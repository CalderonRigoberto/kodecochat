package com.rcalderon.kodecochat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rcalderon.kodecochat.ui.theme.KodecoChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold  { innerPadding ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    val context = LocalContext.current
                    var chatInputText by remember { mutableStateOf(context.getString(R.string.chat_entry_default)) }
                    var chatOutputText by remember { mutableStateOf(context.getString(R.string.chat_display_default)) }

                    Text(text = chatOutputText)

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = chatInputText,
                            onValueChange = {
                                chatInputText = it
                            },
                            label = {
                                Text(text = stringResource(R.string.chat_entry_label))
                            }
                        )

                        Button(
                            enabled = chatInputText.isNotBlank(),
                            onClick = {
                                chatOutputText = chatInputText
                                chatInputText = ""
                            }
                        ) {
                            Text(text = stringResource(R.string.send_button))
                        }
                    }
                }
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