package com.example.todoit

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat

class TaskReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val taskTitle = intent?.getStringExtra("taskTitle")
        val taskDescription = intent?.getStringExtra("taskDescription")

        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val mainIntent = Intent(context, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        mainIntent.putExtra("openFragment", "HomeFragment")  // Optional: send info to open a specific fragment

        // Use TaskStackBuilder to ensure back navigation works correctly
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(mainIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }


        // Create a notification builder
        val builder = NotificationCompat.Builder(context, "channel_id")
        builder.setSmallIcon(R.drawable.log)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.log))
        builder.setContentTitle(taskTitle)
        builder.setContentText(taskDescription)
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setContentIntent(pendingIntent)  // Attach the pending intent
        builder.setAutoCancel(true)

        // Create a notification manager
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder.build())
    }
}