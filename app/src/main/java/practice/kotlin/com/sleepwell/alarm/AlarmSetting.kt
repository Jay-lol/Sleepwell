package practice.kotlin.com.sleepwell.alarm

import android.app.*
import android.app.AlarmManager.AlarmClockInfo
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.text.format.Time
import android.util.Log
import android.widget.TextView
import practice.kotlin.com.sleepwell.AlarmActivity
import practice.kotlin.com.sleepwell.R


class AlarmSetting {

    fun refreshCurrentTime(paramTextView: TextView, apOrpm : TextView) { // 알람시작시 세팅되는
        val time = Time()
        time.setToNow()
        val stringBuilder = StringBuilder()
        var hour :Int
        val min : Int
        Log.d("Time" , "${Integer.valueOf(time.hour)}")
        if(Integer.valueOf(time.hour)>12){
            apOrpm.text = "오후"
            stringBuilder.append(String.format("%2d ", Integer.valueOf(time.hour) -12))
            stringBuilder.append(" ")
            stringBuilder.append(String.format(" %02d", Integer.valueOf(time.minute)))
        }
        else {
            apOrpm.text = "오전"
            hour = Integer.valueOf(time.hour)
            if(hour < 1)
                hour = 12
            stringBuilder.append(String.format("%2d ", hour))
            stringBuilder.append(" ")
            stringBuilder.append(String.format(" %02d", Integer.valueOf(time.minute)))
        }
//        val time = Time()
//        time.setToNow()
//        val stringBuilder = StringBuilder()
//        stringBuilder.append(String.format("%02d ", Integer.valueOf(time.hour)))
//        stringBuilder.append(" ")
//        stringBuilder.append(String.format(" %02d", Integer.valueOf(time.minute)))
        paramTextView.text = stringBuilder.toString()
    }

    fun setupCurrentTime(paramContext: Context, paramTextView: TextView, apOrpm : TextView, paramFloat: Float,
                         paramCurrentTimePicker: CurrentTimePicker?) {
        val intentFilter = IntentFilter("android.intent.action.TIME_TICK")
        paramTextView.setTextSize(0, paramTextView.textSize * paramFloat)
        paramContext.registerReceiver(paramCurrentTimePicker, intentFilter)
        refreshCurrentTime(paramTextView, apOrpm)
    }

    // 노티랑 관련
    fun alarmNoti(paramContext : Context) : Notification? {
        var notification: Notification?  = null
        val intent = Intent(paramContext, AlarmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK //268435456
        val pendingIntent = PendingIntent.getActivity(paramContext, 0, intent, 0)
        val notificationManager =
            paramContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= 26) {

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channelName = "알람"

            val notificationChannel =
                NotificationChannel("alarming_channel", channelName, importance)
            notificationChannel.canShowBadge()
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = -16776961
            notificationChannel.lockscreenVisibility = 0
            notificationChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(notificationChannel)

            notification = Notification.Builder(
                paramContext,
                "alarming_channel")
                .setContentTitle("SleepWell")
                .setContentText("알람이 울립니다")
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setAutoCancel(false)
                .setChannelId("alarming_channel").setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis()).build()

        } else {
            notification =
                Notification.Builder(paramContext)
                    .setContentTitle("SleepWell")
                    .setContentText("알람이 울립니다")
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent).setWhen(System.currentTimeMillis()).build()
        }
        return notification
    }

    fun cancelAlarmingNotification(paramContext: Context) {
        (paramContext.getSystemService("notification") as NotificationManager).cancel("alarming", 2147483647)
    }

    fun setAlarm(paramContext: Context, paramInt1: Int, paramInt2: Int) {

    }
}