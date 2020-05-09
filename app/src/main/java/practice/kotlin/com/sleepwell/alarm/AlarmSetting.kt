package practice.kotlin.com.sleepwell.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.text.format.Time
import android.widget.TextView
import practice.kotlin.com.sleepwell.AlarmActivity
import practice.kotlin.com.sleepwell.R


class AlarmSetting {

    fun refreshCurrentTime(paramTextView: TextView) {
        val time = Time()
        time.setToNow()
        val stringBuilder = StringBuilder()
        stringBuilder.append(String.format("%02d", *arrayOf<Any>(Integer.valueOf(time.hour))))
        stringBuilder.append(":")
        stringBuilder.append(String.format("%02d", *arrayOf<Any>(Integer.valueOf(time.minute))))
        paramTextView.text = stringBuilder.toString()
    }

    fun setupCurrentTime(paramContext: Context, paramTextView: TextView, paramFloat: Float,
                         paramCurrentTimePicker: CurrentTimePicker?) {
        val intentFilter = IntentFilter("android.intent.action.TIME_TICK")
        paramTextView.setTextSize(0, paramTextView.textSize * paramFloat)
        paramContext.registerReceiver(paramCurrentTimePicker, intentFilter)
        refreshCurrentTime(paramTextView)
    }

    fun alarmNoti(paramContext : Context) : Notification? {
        var notification: Notification?  = null
        val intent = Intent(paramContext, AlarmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK //268435456
        val pendingIntent = PendingIntent.getActivity(paramContext, 0, intent, 0)
        val notificationManager =
            paramContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= 26) {

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channelName = "매일 알람 채널"

            val notificationChannel =
                NotificationChannel("alarming_channel", channelName, importance)
            notificationChannel.canShowBadge()
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = -16776961
            notificationChannel.lockscreenVisibility = 0
            notificationChannel.setShowBadge(true)
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel)
                notification = Notification.Builder(
                    paramContext,
                    "alarming_channel"
                ).setContentTitle("상태바 드래그시 보이는 타이틀").setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentText("상태바 드래그시 보이는 서브타이틀").setAutoCancel(false)
                    .setChannelId("alarming_channel").setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis()).build()
            } else {
                paramContext
            }
        } else {
            notification =
                Notification.Builder(paramContext).setContentTitle("상태바 드래그시 보이는 타이틀")
                    .setSmallIcon(17301550).setContentText("상태바 드래그시 보이는 서브타이틀").setAutoCancel(false)
                    .setContentIntent(pendingIntent).setWhen(System.currentTimeMillis()).build()
        }
        return notification

    }
    fun cancelAlarmingNotification(paramContext: Context) {
        (paramContext.getSystemService("notification") as NotificationManager).cancel("alarming", 2147483647)
    }
}