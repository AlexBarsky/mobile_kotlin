package ru.mirea.bogomolovaa.notificationapp

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG.toString(), "Permission granted")
        } else {
            Log.d(TAG.toString(), "Permission denied")
            ActivityCompat.requestPermissions(this, arrayOf(POST_NOTIFICATIONS), PermissionCode)
        }
    }

    fun onClickSendNotification(view: View) {
        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText("Congratulation!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that can`t fit in a single line..."))
            .setContentTitle("Mirea")

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, "Student Bogomolov Aleksey Alexandrovich", importance)

        channel.description = "MIREA Channel"

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(1, builder.build())
    }

    private companion object {
        const val CHANNEL_ID = "com.mirea.asd.notification.ANDROID"
        val TAG = MainActivity::class.java.simpleName
        val PermissionCode = 200
    }
}