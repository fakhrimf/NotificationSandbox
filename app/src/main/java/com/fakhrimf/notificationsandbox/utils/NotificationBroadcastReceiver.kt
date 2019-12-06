package com.fakhrimf.notificationsandbox.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.fakhrimf.notificationsandbox.R

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("INFO","Broadcast Receiver onreceive, Intent action ${intent.action} ? $REPLY_ACTION")
        if(REPLY_ACTION == intent.action) {
            val message = NotificationService.getReplyMessage(intent)
            val messageID = intent.getIntExtra(MESSAGE_KEY, 0)
            val info = context.getString(R.string.message_info, messageID, message)
            Toast.makeText(context, info, Toast.LENGTH_LONG).show()
            val notifyID = intent.getIntExtra(NOTIFY_KEY, 0)
            updateNotification(context, notifyID)
        }
    }

    private fun updateNotification(context: Context, notifyID: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder =
            NotificationCompat.Builder(context, REPLY_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(context.getString(R.string.message_sent))
                .setContentTitle(context.getString(R.string.new_notification))
        val channel = NotificationChannel(
            REPLY_NOTIFICATION_CHANNEL_ID,
            REPLY_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        builder.setChannelId(REPLY_NOTIFICATION_CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)
        val notification = builder.build()
        notificationManager.notify(notifyID, notification)
    }

    companion object {
        fun getReply(context: Context, notifyID: Int, messageID: Int): Intent {
            val intent = Intent(context, BroadcastReceiver::class.java)
            intent.action = REPLY_ACTION
            intent.putExtra(MESSAGE_KEY, messageID)
            intent.putExtra(NOTIFY_KEY, notifyID)
            return intent
        }
    }
}