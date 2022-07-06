package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

const val NOTIF_ID = 10
const val FILE_KEY = "file_id"
const val DOWNLOAD_KEY = "download_id"
const val NOTIF_ID_KEY = "notification_id"
const val STATUS_KEY = "status_id"
const val CHANNEL_ID = "channelID"

fun NotificationManager.sendNotification(context: Context, id:Long, statusString:String,
                                         filename:String) {
    val intent = Intent(context, DetailActivity::class.java)
    //pass important info with intent
    intent.putExtra(NOTIF_ID_KEY, NOTIF_ID)
    intent.putExtra(DOWNLOAD_KEY, id)
    intent.putExtra(FILE_KEY, filename)
    intent.putExtra(STATUS_KEY, statusString)

    val pendIntent = PendingIntent.getActivity(context, NOTIF_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_description))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .addAction(R.drawable.ic_assistant_black_24dp, context.getString(R.string.notification_button),
            pendIntent)
        .setAutoCancel(true)

    notify(NOTIF_ID, builder.build())
}

fun NotificationManager.createNotificationChannel(channelId: String, channelName: String) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val channel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_LOW)
        createNotificationChannel(channel)
    }
}