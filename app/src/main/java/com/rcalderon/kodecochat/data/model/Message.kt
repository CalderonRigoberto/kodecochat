package com.rcalderon.kodecochat.data.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID
import com.rcalderon.kodecochat.R


@Immutable
data class Message(
    val _id: String = UUID.randomUUID().toString(),
    val createdOn: Instant? = Clock.System.now(),
    val roomId: String = "public",
    val text: String = "test",
    val userId: String = UUID.randomUUID().toString(),
    val photoUri: Uri? = null,
    val authorImage: Int = if(userId == "me") R.drawable.profile_photo_android_developer else R.drawable.someone_else
)