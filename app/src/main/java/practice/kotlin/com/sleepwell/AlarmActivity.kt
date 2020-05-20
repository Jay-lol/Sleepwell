package practice.kotlin.com.sleepwell

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_alarm.*
import practice.kotlin.com.sleepwell.alarm.*


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

        currenTimePicker = CurrentTimePicker(editText, apOrpm)
        val alarmDb = AlarmDB.getInstance(this)
        val id = intent.getLongExtra("id",0)
        Log.d("액티비티 아이디", id.toString())

        Thread{
            alarmDb?.alarmDao()?.updateonoff(id, false)
        }.start()


        val  mAnimation = AlphaAnimation(1.0F, 0.0F)
		mAnimation.setDuration(1000)
		mAnimation.setInterpolator(LinearInterpolator())
		mAnimation.setRepeatCount(Animation.INFINITE)
		mAnimation.setRepeatMode(Animation.REVERSE)
        divide.startAnimation(mAnimation)

        AlarmSetting().setupCurrentTime(this, editText, apOrpm, 3.0F, currenTimePicker)
//        setVolumeControlStream(AudioManager.STREAM_ALARM)

        // service 와 activity를 종료
        stopButton.setOnClickListener{
            stopService(Intent(this as Context, AlarmService::class.java))
            unregisterReceiver(currenTimePicker)
            finish()
        }

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KeyEvent.KEYCODE_HOME -> {
                Log.d("키코드", "홈키")
                stopService(Intent(this as Context, AlarmService::class.java))
                unregisterReceiver(currenTimePicker)
                finish()
                return true
            }

        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopService(Intent(this as Context, AlarmService::class.java))
        unregisterReceiver(currenTimePicker)
        finish()
    }

}