package com.example.chatapp.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.example.chatapp.R
import com.example.chatapp.activities.MainActivity
import com.example.chatapp.models.User
import com.example.chatapp.utilities.Constants
import com.example.chatapp.utilities.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
//        val preferenceManager = PreferenceManager(applicationContext)
        super.onMessageReceived(message)
        val user = User(message.data.get(Constants.KEY_USER_ID)!!
            ,message.data.get(Constants.KEY_NAME)!!
            ,message.data.get(Constants.KEY_IMAGE)
            ,null
            ,message.data.get(Constants.KEY_FCM_TOKEN)!!)
        val notificationId = Random().nextInt()
        val channelId = "chat_message"
        val bundle = Bundle()
        bundle.putSerializable("user",user)
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.chat_app_nav)
            .setDestination(R.id.chatFragment)
            .setArguments(bundle)
            .createPendingIntent()
        val builder = NotificationCompat.Builder(this,channelId)
        builder.setSmallIcon(R.drawable.ic_round_notifications_24)
        builder.setContentTitle(user.name)
        builder.setContentText(message.data.get(Constants.KEY_MESSAGE))
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(message.data.get(Constants.KEY_MESSAGE)))
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channelName:CharSequence = "Chat Message"
            val channelDescription = "This notification channel is used for chat message notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId,channelName,importance)
            channel.description = channelDescription
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(notificationId,builder.build())
    }


}