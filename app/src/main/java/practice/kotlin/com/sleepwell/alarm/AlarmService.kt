package practice.kotlin.com.sleepwell.alarm

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import practice.kotlin.com.sleepwell.view.AlarmActivity
import java.io.IOException

/**
 * 1. 알람음 재생
 * 2. 액티비티를 깨운다
 */
class AlarmService : Service() {

    private val TAG: String = "로그"

    private val vibration = 1
    private lateinit var mPlayer : MediaPlayer
    private var soundVolume = 10
    private val alarmSetting : AlarmSetting = AlarmSetting()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "AlarmService ~ onStartCommand() called")
        super.onStartCommand(intent, flags, startId)

        val id = intent.getLongExtra("id", 0)

        // 노티로 알람을 준다음 백그라운드 서비스 실행
        startForeground(1, alarmSetting.createAlarmingNotification(this))

        val alarmIntent = Intent(this, AlarmActivity::class.java)
        alarmIntent.putExtra("id", id)
        Log.d(TAG, "서비스ID : $id")

        // 액티비티 실행하기 위한 플래그
        // 새로운 Task를 생성하여 그 Task 안에 Activity를 추가하여 준다
        alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        startActivity(alarmIntent)
        initializePlayer()

        return START_STICKY
    }

    private fun initializePlayer() {

        val paramInt = (getSystemService(Context.AUDIO_SERVICE) as AudioManager).ringerMode

        if (this.vibration == 1 || (paramInt != 0 && paramInt != 1)) {
            var uri: Uri? = null
            uri = Uri.parse("android.resource://practice.kotlin.com.sleepwell/raw/alarm")
            this.mPlayer = MediaPlayer()
            try {
                if (uri != null) {
                    this.mPlayer.setDataSource(this, uri)
                }
                this.mPlayer.setAudioStreamType(4)
                this.mPlayer.isLooping = true
                this.mPlayer.prepare()

                val audioManager : AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.setStreamVolume(
                    AudioManager.STREAM_ALARM,
                    ((this.soundVolume / 100.0f * audioManager.getStreamMaxVolume(4)).toInt()),
                    0
                )
                this.mPlayer.start()

            } catch (illegalStateException: IllegalStateException) {
                illegalStateException.printStackTrace()
            } catch (iOException: IOException) {
                iOException.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmSetting.cancelAlarmingNotification(this)
        mPlayer.pause()
        mPlayer.stop()
    }
}