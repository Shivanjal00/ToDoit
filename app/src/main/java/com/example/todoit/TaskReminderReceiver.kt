package com.example.todoit

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat

class TaskReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        // Create a notification builder
        val builder = NotificationCompat.Builder(context, "channel_id")
        builder.setSmallIcon(R.drawable.log)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.log))
        builder.setContentTitle("Task Reminder")
        builder.setContentText("You have a task reminder")
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)

        // Create a notification manager
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder.build())
    }
}