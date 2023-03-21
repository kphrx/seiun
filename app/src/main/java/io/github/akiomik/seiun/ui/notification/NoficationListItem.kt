package io.github.akiomik.seiun.ui.notification

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.akiomik.seiun.R
import io.github.akiomik.seiun.model.app.bsky.notification.Notification

const val DATETIME_FORMAT = "yyyy/MM/dd HH:mm"

@Composable
private fun Avatar(notification: Notification, onClicked: (String) -> Unit) {
    AsyncImage(
        model = notification.author.avatar,
        contentDescription = null,
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .clip(CircleShape)
            .clickable { onClicked(notification.author.did) }
    )
}

@Composable
private fun VoteItem(notification: Notification, onProfileClick: (String) -> Unit) {
    val createdAt = DateFormat.format(
        DATETIME_FORMAT,
        notification.record.createdAt.toInstant().toEpochMilli()
    )

    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Avatar(notification = notification, onClicked = onProfileClick)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                stringResource(
                    R.string.notification_liked,
                    notification.author.displayName ?: notification.author.handle
                )
            )
            Text(
                text = createdAt.toString(),
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun RepostItem(notification: Notification, onProfileClick: (String) -> Unit) {
    val createdAt = DateFormat.format(
        DATETIME_FORMAT,
        notification.record.createdAt.toInstant().toEpochMilli()
    )

    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Avatar(notification = notification, onClicked = onProfileClick)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                stringResource(
                    R.string.notification_reposted,
                    notification.author.displayName ?: notification.author.handle
                )
            )
            Text(
                text = createdAt.toString(),
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun FollowItem(notification: Notification, onProfileClick: (String) -> Unit) {
    val createdAt = DateFormat.format(
        DATETIME_FORMAT,
        notification.record.createdAt.toInstant().toEpochMilli()
    )

    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Avatar(notification = notification, onClicked = onProfileClick)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                stringResource(
                    R.string.notification_followed,
                    notification.author.displayName ?: notification.author.handle
                )
            )
            Text(
                text = createdAt.toString(),
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun InviteItem(notification: Notification, onProfileClick: (String) -> Unit) {
    val createdAt = DateFormat.format(
        DATETIME_FORMAT,
        notification.record.createdAt.toInstant().toEpochMilli()
    )

    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Avatar(notification = notification, onClicked = onProfileClick)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                stringResource(
                    R.string.notification_invited,
                    notification.author.displayName ?: notification.author.handle
                )
            )
            Text(
                text = createdAt.toString(),
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun MentionItem(notification: Notification, onProfileClick: (String) -> Unit) {
    val createdAt = DateFormat.format(
        DATETIME_FORMAT,
        notification.record.createdAt.toInstant().toEpochMilli()
    )

    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Avatar(notification = notification, onClicked = onProfileClick)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                stringResource(
                    R.string.notification_mentioned,
                    notification.author.displayName ?: notification.author.handle
                )
            )
            Text(
                text = createdAt.toString(),
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ReplyItem(notification: Notification, onProfileClick: (String) -> Unit) {
    val createdAt = DateFormat.format(
        DATETIME_FORMAT,
        notification.record.createdAt.toInstant().toEpochMilli()
    )

    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Avatar(notification = notification, onClicked = onProfileClick)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                stringResource(
                    R.string.notification_replied,
                    notification.author.displayName ?: notification.author.handle
                )
            )
            Text(
                text = createdAt.toString(),
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun NotificationListItem(notification: Notification, onProfileClick: (String) -> Unit) {
    val readColor = MaterialTheme.colorScheme.surface
    val unreadColor = MaterialTheme.colorScheme.primaryContainer
    val color = if (notification.isRead) readColor else unreadColor

    Box(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
    ) {
        when (notification.reason) {
            "vote" -> VoteItem(notification = notification, onProfileClick = onProfileClick)
            "repost" -> RepostItem(notification = notification, onProfileClick = onProfileClick)
            "follow" -> FollowItem(notification = notification, onProfileClick = onProfileClick)
            "invite" -> InviteItem(notification = notification, onProfileClick = onProfileClick)
            "mention" ->
                MentionItem(notification = notification, onProfileClick = onProfileClick)
            "reply" -> ReplyItem(notification = notification, onProfileClick = onProfileClick)
            else -> {}
        }
    }
}
