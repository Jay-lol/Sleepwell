package practice.kotlin.com.sleepwell.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import android.widget.Toast


class AlarmBroadcastReceiver : BroadcastReceiver() {

    private val TAG: String = "로그"
    private val mWakeLock: WakeLock? = null

    private fun reenableWakeLockAndKeyguard() {
        val wakeLock = mWakeLock
        if (wakeLock != null && wakeLock.isHeld) mWakeLock!!.release()
    }

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra("id", 0)

        Log.d(TAG,"AlarmingReceiver 알람 아이디 : $alarmId")

        Toast.makeText(
            context.applicationContext,
            "[SleepWell] 알람울림",
            Toast.LENGTH_SHORT).show()

        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra("id", alarmId)

        val pm : PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        // 객체의 제어레벨 설정
        val sCpuWakeLock : WakeLock?
        sCpuWakeLock = pm.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "app:alarm")

        //acquire 함수를 실행하여 앱을 깨운다. cpu 를 획득한다
        sCpuWakeLock.acquire(10*60*1000L /*10 minutes*/)


        // 서비스,백그라운드로 소리와 액티비티를 같이 깨워준다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
        reenableWakeLockAndKeyguard()

        sCpuWakeLock?.release()
    }

}
