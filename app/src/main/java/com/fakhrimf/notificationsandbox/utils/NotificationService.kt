package com.fakhrimf.notificationsandbox.utils

import android.app.*
import androidx.core.app.RemoteInput.*
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.fakhrimf.notificationsandbox.R

class NotificationService : IntentService("NotificationService") {
    override fun onHandleIntent(intent: Intent?) {
        if(intent != null) showNotification()
    }

    private fun showNotification() {
        val replyLabel = getString(R.string.reply)
        val remoteInput = Builder(REPLY_KEY).setLabel(replyLabel).build()
        val replyAction =
            NotificationCompat.Action.Builder(R.drawable.ic_send_white_24, replyLabel, getPendingIntent())
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build()
        val builder =
            NotificationCompat.Builder(this, REPLY_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(this.getString(R.string.message_sent))
                .setContentTitle(this.getString(R.string.new_notification))
                .setShowWhen(true)
                .addAction(replyAction)
        val channel = NotificationChannel(
            REPLY_NOTIFICATION_CHANNEL_ID,
            REPLY_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        builder.setChannelId(REPLY_NOTIFICATION_CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)
        val notification = builder.build()
        notificationManager.notify(NOTIFY_ID, notification)
    }

    private fun getPendingIntent(): PendingIntent {
        val intent: Intent = NotificationBroadcastReceiver.getReply(this, NOTIFY_ID, MESSAGE_ID)
        return PendingIntent.getBroadcast(applicationContext, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        fun getReplyMessage(intent: Intent): CharSequence? {
            val remoteInput = getResultsFromIntent(intent)
            return remoteInput?.getCharSequence(REPLY_KEY)
        }
    }
}