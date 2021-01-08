package practice.kotlin.com.sleepwell.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import practice.kotlin.com.sleepwell.alarm.room.AlarmDB
import practice.kotlin.com.sleepwell.alarm.room.AlarmTable
import java.text.SimpleDateFormat
import java.util.*

/**
 * 재부팅시 리시브 받아서 실행, android.intent.action.BOOT_COMPLETED
 */
class DeviceBootReceiver : BroadcastReceiver() {
    private val TAG = "DeviceBootReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?:return
        context?:return
        if (Objects.equals(intent.action, "android.intent.action.BOOT_COMPLETED")) return

        // 재부팅일 경우만 실행
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        
        // 객체의 제어레벨 설정
        val sCpuWakeLock: PowerManager.WakeLock
        = pm.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "app:alarm"
        )

        //acquire 함수를 실행하여 앱을 깨운다. cpu 를 획득한다
        sCpuWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)

        val alarmDb: AlarmDB? = AlarmDB.getInstance(context)
        var alarmList = listOf<AlarmTable>()
        var alarmSize = 0

        val getThread = Thread {
            Log.d(TAG, "BootReceiver alarm thread")
            alarmDb?:return@Thread
            alarmList = alarmDb.alarmDao().getAll()
        }

        getThread.start()
        getThread.join()

        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        var pendingIntent: PendingIntent
        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (n in alarmList.indices) {
            val time = alarmList[n].time
            val onOff = alarmList[n].onOff

            val preTime = Calendar.getInstance()
            preTime.timeInMillis = time!!

            if (onOff) {
                alarmIntent.putExtra("id", alarmList[n].id)

                // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                while (preTime.before(Calendar.getInstance())) {
                    preTime.add(Calendar.DATE, 1)    // date 1을더해 더해준다
                }    // 추가작업 : 10일이상 (10번이상 이 와일문을돌려도) 알람시간이 그대로면 업데이트를 해주자

                val sendTime = preTime.timeInMillis

                val dateText =
                    SimpleDateFormat("MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault())
                        .format(preTime.time)
                Log.d(TAG, dateText)

                // 사용자가  알람을 허용했다면
                Log.d(TAG, "사용자가 알람을 허용")
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmList[n].id!!.toInt(),
                    alarmIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )

                alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                alarmSize += 1

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Log.d(TAG, "마시멜로이하버전")
                    // 각버전에 맞게 호출
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Log.d(TAG, "롤리팝이상")
                        //API 21 이상 API 23미만
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP, sendTime, pendingIntent
                        )
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, sendTime, pendingIntent)
                    }
                } else {
                    Log.d(TAG, "마시멜로이상버전")
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, sendTime, pendingIntent
                    )
                }
            } else {
                Log.d(TAG, "사용자가 알람을 허용안함")
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmList[n].id!!.toInt(),
                    alarmIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
                alarmManager.cancel(pendingIntent)
            }
        }

        if (alarmSize != 0)
            Toast.makeText(
                context.applicationContext,
                "[SleepWell] " + alarmSize.toString() + "개의 알람이 설정되어있습니다",
                Toast.LENGTH_SHORT
            ).show()

        sCpuWakeLock.release()
    }

}
