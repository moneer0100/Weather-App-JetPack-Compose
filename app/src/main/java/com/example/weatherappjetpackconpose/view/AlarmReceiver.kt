package com.example.weatherappjetpackconpose.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.weatherappjetpackconpose.R

import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm triggered!")
        val latitude = intent?.getDoubleExtra("LATITUDE", 0.0)
        val longitude = intent?.getDoubleExtra("LONGITUDE", 0.0)

        val message = "Alarm triggered for location: $latitude, $longitude"

        context?.let {

            val channelId = "weatherAppChannel"
            val channelName = "Weather Alerts"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Channel for weather alerts"
                }
                notificationManager.createNotificationChannel(channel)
            }

            val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notification = NotificationCompat.Builder(it, channelId)
                .setContentTitle("Weather Alert")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(notificationSound)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE) // إضافة الاهتزاز
                .build()


            val notificationManager =
                it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
        }
    }
}
