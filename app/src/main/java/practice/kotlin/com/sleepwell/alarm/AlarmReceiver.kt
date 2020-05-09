package practice.kotlin.com.sleepwell.alarm

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import androidx.annotation.RequiresApi


class AlarmReceiver : BroadcastReceiver() {

    private var mContext: Context? = null

    private val mWakeLock: WakeLock? = null

    private fun reenableWakeLockAndKeyguard() {
        val wakeLock = mWakeLock
        if (wakeLock != null && wakeLock.isHeld) mWakeLock!!.release()
    }

//    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent){
//        val notificationManager =
//            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("kairylab", "AlarmingReceiver is called.")

        val intent = Intent(context, AlarmService::class.java)

        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
//        reenableWakeLockAndKeyguard();


//        context.startActivity(alarmactivity)
//        Log.d("eoor", "receiver on")
//        val id = Intent(context, AlarmActivity::class.java)
//        id.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(id)


//        val alarmact = Intent(context, AlarmActivity::class.java)
//            alarmact.addFlags(
//            Intent.FLAG_ACTIVITY_NEW_TASK
//                    or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        context.startActivity(alarmact)

//        notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
//                or Intent.FLAG_ACTIVITY_SINGLE_TOP)

//        val notification = Uri.parse("R.raw.alarm")
//        Ringtone r = RingtoneManager.getRingtone(context, notification);
//        r.play()
//
//        val pendingI = PendingIntent.getActivity(
//            context, 0,
//            notificationIntent, 0
//        )
//
//        val builder = NotificationCompat.Builder(context, "default")
//
//
//        //OREO API 26 이상에서는 채널 필요
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder.setSmallIcon(R.drawable.ic_stars_black_24dp) //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
//            val channelName = "매일 알람 채널"
//            val description = "매일 정해진 시간에 알람합니다."
//            val importance = NotificationManager.IMPORTANCE_HIGH //소리와 알림메시지를 같이 보여줌
//            val channel = NotificationChannel("default", channelName, importance)
//            channel.description = description
//            notificationManager?.createNotificationChannel(channel)
//        } else builder.setSmallIcon(R.mipmap.ic_launcher_round)
//        // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
//
//        builder.setAutoCancel(true)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setWhen(System.currentTimeMillis())
//            .setTicker("{Time to watch some cool stuff!}")
//            .setContentTitle("상태바 드래그시 보이는 타이틀")
//            .setContentText("상태바 드래그시 보이는 서브타이틀")
//            .setContentInfo("INFO")
//            .setContentIntent(pendingI)

//        if (notificationManager != null) {
//
//            // 노티피케이션 동작시킴
//            notificationManager.notify(1234, builder.build())
//
//            val nextNotifyTime: Calendar = Calendar.getInstance()
//            // 내일 같은 시간으로 알람시간 결정
//            nextNotifyTime.add(Calendar.DATE, 1)
//
//            //  Preference에 설정한 값 저장
//            val editor =
//                context!!.getSharedPreferences("daily alarm", MODE_PRIVATE).edit()
//            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis())
//            editor.apply()
//            val currentDateTime: Date = nextNotifyTime.getTime()
//            val date_text: String =
//                SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime)
//            Toast.makeText(
//                context!!.applicationContext,
//                "다음 알람은 " + date_text + "으로 알람이 설정되었습니다!",
//                Toast.LENGTH_SHORT).show()
//        }
    }

}
