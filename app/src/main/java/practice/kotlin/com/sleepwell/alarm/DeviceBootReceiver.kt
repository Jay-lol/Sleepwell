package practice.kotlin.com.sleepwell.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

class  DeviceBootReceiver  : BroadcastReceiver(){
    private val TAG = "DeviceBootReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Objects.equals(intent!!.action, "android.intent.action.BOOT_COMPLETED")) {

            val pm = context?.getSystemService(Context.POWER_SERVICE) as PowerManager

            // 객체의 제어레벨 설정

            // 객체의 제어레벨 설정
            var sCpuWakeLock : PowerManager.WakeLock?
            sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                        PowerManager.ACQUIRE_CAUSES_WAKEUP or
                        PowerManager.ON_AFTER_RELEASE, "app:alarm")

            //acquire 함수를 실행하여 앱을 깨운다. cpu 를 획득한다

            //acquire 함수를 실행하여 앱을 깨운다. cpu 를 획득한다
            sCpuWakeLock.acquire(10*60*1000L /*10 minutes*/)

            var alarmDb: AlarmDB? = AlarmDB.getInstance(context!!)
            var alarmList = listOf<AlarmTable>()
            var cnt = 0

            val getThread = Thread{
                Log.d(TAG,"스레드인")
                if (alarmDb != null) {
                    alarmList = alarmDb.alarmDao().getAll()
                }
                Log.d("Jay", alarmList.toString())
            }

            getThread.start()
            getThread.join()

            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            var pendingIntent: PendingIntent
            var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


            Log.d(TAG, alarmList.toString())

            for (i in alarmList.indices) {
                var time = alarmList[i].time
                val onOff = alarmList[i].onOff

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = time!!


                if (onOff) {
                    alarmIntent.putExtra("id", alarmList[i].id)

                    // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                    while (calendar.before(Calendar.getInstance())) {
                        calendar.add(Calendar.DATE, 1)    // date 1을더해 더해준다
                    }    // 추가작업 : 10일이상 (10번이상 이 와일문을돌려도) 알람시간이 그대로면 업데이트를 해주자

                    val sendTime = calendar.timeInMillis


                    val currentDateTime = calendar.time
                    val date_text =
                        SimpleDateFormat("MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault())
                            .format(currentDateTime)
                    Log.d(TAG, date_text)
//            if (current_calendar.after(nextNotifyTime)) {
//                nextNotifyTime.add(Calendar.DATE, 1)
//            }

                    // 사용자가  알람을 허용했다면
                    pendingIntent = PendingIntent.getBroadcast(
                        context,
                        alarmList[i].id!!.toInt(),
                        alarmIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT)

                    alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    Log.d("사용자가 알람을??", "허용")

                    cnt += 1

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Log.d("Jay","마시멜로이하버전")
                        // 각버전에 맞게 호출
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Log.d("Jay","롤리팝이상")
                            //API 21 이상 API 23미만
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP, sendTime, pendingIntent)
                        } else {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, sendTime, pendingIntent)
                        }
                    } else {
                        Log.d("Jay","마시멜로이상버전")
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP, sendTime, pendingIntent)
                    }

//                    Toast.makeText(
//                        context.applicationContext,
//                        "[SleepWell] " + cnt.toString() + "개의 알람이 설정되어있습니다",
//                        Toast.LENGTH_SHORT).show()

                } else {
                    Log.d("사용자가 알람을??", "허용안함")
                    pendingIntent = PendingIntent.getBroadcast(
                        context,
                        alarmList[i].id!!.toInt(),
                        alarmIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT)

                    alarmManager.cancel(pendingIntent)
                }
            }

            if(cnt!=0)
                Toast.makeText(
                context.applicationContext,
                "[SleepWell] " + cnt.toString() + "개의 알람이 설정되어있습니다",
                Toast.LENGTH_SHORT).show()

            // on device boot complete, reset the alarm
//            val alarmIntent = Intent(context, AlarmReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
//            val manager =
//                context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            //
//            val sharedPreferences =
//                context!!.getSharedPreferences("daily alarm", MODE_PRIVATE)
//            val millis =
//                sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis())
//            val current_calendar: Calendar = Calendar.getInstance()
//            val nextNotifyTime: Calendar = GregorianCalendar()
//            nextNotifyTime.setTimeInMillis(sharedPreferences.getLong("nextNotifyTime", millis))
//            if (current_calendar.after(nextNotifyTime)) {
//                nextNotifyTime.add(Calendar.DATE, 1)
//            }
//            val currentDateTime: Date = nextNotifyTime.getTime()
//            val date_text: String =
//                SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime)
//            Toast.makeText(
//                context!!.applicationContext,
//                "[SleepWell]" + date_text + "으로 알람이 설정되었습니다!",
//                Toast.LENGTH_SHORT).show()
//
//            manager?.setRepeating(
//                AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, pendingIntent
//            )

            if (sCpuWakeLock != null) {
                sCpuWakeLock.release()
            }
        }

    }
}
