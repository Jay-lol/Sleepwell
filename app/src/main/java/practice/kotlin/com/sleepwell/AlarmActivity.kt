package practice.kotlin.com.sleepwell

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_alarm.*
import practice.kotlin.com.sleepwell.alarm.AlarmReceiver
import practice.kotlin.com.sleepwell.alarm.AlarmService
import practice.kotlin.com.sleepwell.alarm.AlarmSetting
import practice.kotlin.com.sleepwell.alarm.CurrentTimePicker


class AlarmActivity: Activity() {

    private var currenTimePicker : CurrentTimePicker? = null


    private val mWakeLock: WakeLock? = null

    private fun disableWakeLockAndKeyguard() {
        getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        window.addFlags(4718592)    //4718592
        mWakeLock?.acquire()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        setContentView(R.layout.activity_alarm)

        currenTimePicker = CurrentTimePicker(editText)

        AlarmSetting().setupCurrentTime(this, editText, 4.0F, currenTimePicker)
        setVolumeControlStream(AudioManager.STREAM_ALARM)
        // service 와 activity를 종료
        stopButton.setOnClickListener{
            stopService(Intent(this as Context, AlarmService::class.java))
            unregisterReceiver(currenTimePicker)
            finish()
        }

    }
}