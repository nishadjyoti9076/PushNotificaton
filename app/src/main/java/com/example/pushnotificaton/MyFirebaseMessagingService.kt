package com.example.pushnotificaton

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

const val channel_ID="notificationa_channelID"
const val channel_name="notification_name"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remotemessage: RemoteMessage) {
        super.onMessageReceived(remotemessage)

        if (remotemessage.notification !=null){

            generateNotification(remotemessage.notification?.title!!,remotemessage.notification?.body!!)
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title:String, message: String) : RemoteViews{

        val remteView= RemoteViews("com.example.pushnotificaton",R.layout.message_layout)
        remteView.setTextViewText(R.id.text_title,title)
        remteView.setTextViewText(R.id.text_message,message)
        remteView.setImageViewResource(R.id.imageView,R.drawable.baseline_notifications_active_24)

        return remteView
    }

    fun generateNotification(title: String,message : String){

        val intent=Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // why pending Intent becouse the intent which is going to use in future
        val pendingActivity=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)


        var builder=NotificationCompat.Builder(applicationContext, channel_ID)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingActivity)

        builder=builder.setContent(getRemoteView(title,message))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.O_MR1){

            val notificationChannel=NotificationChannel(channel_ID, channel_name,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

        }

    notificationManager.notify(0,builder.build())

    }


}