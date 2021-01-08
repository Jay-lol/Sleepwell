package practice.kotlin.com.sleepwell.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.TextView
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.view.AlarmActivity


class AlarmSetting {

    private val TAG: String = "로그"

    /**
     * 알람 시작시 세팅해주는 함수
     */
    fun setupCurrentTime(
        paramContext: Context, paramTextView: TextView, apOrpm: TextView, paramFloat: Float,
        paramCurrentTimePicker: CurrentTimePicker
    ) {

        // AlarmActivity의 시간텍스트를 바꿔주는 리시버 등록.
        // CurrentTimePicker리시버에게 매분 마다 체크할 수 있게 해줌
        val intentFilter = IntentFilter("android.intent.action.TIME_TICK")
        paramTextView.setTextSize(0, paramTextView.textSize * paramFloat)
        paramContext.registerReceiver(paramCurrentTimePicker, intentFilter)
        paramCurrentTimePicker.refreshCurrentTime(paramTextView, apOrpm)
    }

    /**
     * 알람 노티 설정
     */
    fun createAlarmingNotification(paramContext: Context): Notification {
        val notification: Notification
        val moveAlarmActivityIntent = Intent(paramContext, AlarmActivity::class.java)
        moveAlarmActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(paramContext, 0, moveAlarmActivityIntent, 0)

        val notificationManager =
            paramContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= 26) {  // 채널ID추가

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
                .setChannelId("alarming_channel")
                .setContentIntent(pendingIntent)   // 클릭시 알람액티비티로 이동
                .setWhen(System.currentTimeMillis()).build()

        } else {
            notification =
                Notification.Builder(paramContext)
                    .setContentTitle("SleepWell")
                    .setContentText("알람이 울립니다")
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent)    // 클릭시 알람액티비티로 이동
                    .setWhen(System.currentTimeMillis()).build()
        }
        return notification
    }

    /**
     * 알람 노티 취소
     */
    fun cancelAlarmingNotification(context: Context) =
        (context.getSystemService("notification") as NotificationManager)
            .cancel("alarming", 2147483647)

}