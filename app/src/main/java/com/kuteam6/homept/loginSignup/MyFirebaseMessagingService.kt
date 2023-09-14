package com.kuteam6.homept.loginSignup

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kuteam6.homept.R


const val channelId = "notification_channel"
const val channelName = "com.kuteam6.homept.loginSignup"
class MyFirebaseMessagingService: FirebaseMessagingService() {

    // generate the notification
    //attach the notification created with the custom layout
    // show the notification

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if(remoteMessage.getNotification() != null){
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }

    }

    fun getRemoteView(title: String, message: String): RemoteViews{
        val remoteView = RemoteViews("com.kuteam6.homept.loginSignup", R.layout.notification)

        remoteView.setTextViewText(R.id.alarm_title,title)
        remoteView.setTextViewText(R.id.alarm_message,message)
        remoteView.setImageViewResource(R.id.alarm_logo, R.drawable.pt_alarm)

        return remoteView
    }

    fun generateNotification(title: String, message: String){

        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )

        // channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.pt_alarm)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        notificationManager.notify(0, builder.build())
    }
}