package practice.kotlin.com.sleepwell.view

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager.WakeLock
import android.util.Log
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_alarm.*
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.alarm.AlarmService
import practice.kotlin.com.sleepwell.alarm.AlarmSetting
import practice.kotlin.com.sleepwell.alarm.CurrentTimePicker
import practice.kotlin.com.sleepwell.alarm.room.AlarmDB
import practice.kotlin.com.sleepwell.databinding.ActivityAlarmBinding


class AlarmActivity: AppCompatActivity() {

    private val TAG: String = "로그"
    private lateinit var currenTimePicker : CurrentTimePicker
    companion object{
        var alarmActivity : Activity? = null
    }
    private val mWakeLock: WakeLock? = null
    private lateinit var binding : ActivityAlarmBinding

    private fun disableWakeLockAndKeyguard() {
        getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        window.addFlags(4718592)    //4718592
        mWakeLock?.acquire(10*60*1000L /*10 minutes*/)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmActivity = this
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm)
        binding.alarmActivity = this
        binding.lifecycleOwner = this
        binding.stopButton.setBackgroundResource(R.drawable.alert_dialog_border)

        currenTimePicker = CurrentTimePicker(editText, apOrpm)

        val id = intent.getLongExtra("id", 0)
        Log.d(TAG, "알람ID : $id")
        Thread {
            AlarmDB.getInstance(this)?.alarmDao()?.updateOnOff(id, false)
        }.start()

        val mAnimation = AlphaAnimation(1.0F, 0.0F)
        mAnimation.duration = 1000
        mAnimation.interpolator = LinearInterpolator()
        mAnimation.repeatCount = Animation.INFINITE
        mAnimation.repeatMode = Animation.REVERSE
        binding.divide.startAnimation(mAnimation)

        AlarmSetting().setupCurrentTime(this, binding.editText,
            apOrpm, 3.0F, currenTimePicker)
//        setVolumeControlStream(AudioManager.STREAM_ALARM)

    }

    fun closeActivity() = onPause()
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "AlarmActivity ~ onPause() called")
        stopService(Intent(this, AlarmService::class.java))
        try{
            unregisterReceiver(currenTimePicker)
        }catch (e : Exception){
            Log.e(TAG, "onPause: $e")
        }
        ActivityCompat.finishAffinity(this)
    }

}