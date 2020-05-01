/*
 * Created by m.coder on 2020/5/1.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import androidx.core.app.NotificationCompat
import com.nanaten.bustime.BuildConfig
import com.nanaten.bustime.R
import com.nanaten.bustime.ui.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val time = intent?.getStringExtra("Time")
        val notification =
            NotificationCompat.Builder(context, BuildConfig.APPLICATION_ID + ".notification")
                .apply {
                    val i = Intent(context, MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    val pendingIntent =
                        PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT)
                    setSmallIcon(R.drawable.ic_launcher_foreground)
                    setContentTitle("リマインダー")
                    setContentText("もうすぐバスの発車時刻です: $time")
                    setAutoCancel(true)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        priority = NotificationManager.IMPORTANCE_HIGH

                        // 2秒後にHeads-up Notificationを消す
                        Thread(Runnable {
                            Handler().postDelayed({
                                priority = NotificationManager.IMPORTANCE_DEFAULT
                            }, 2000)
                        })
                    }
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    setContentIntent(pendingIntent)
                }
                .build()
        notificationManager.notify(0, notification)
    }
}