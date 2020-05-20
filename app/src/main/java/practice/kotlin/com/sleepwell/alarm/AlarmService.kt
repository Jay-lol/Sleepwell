package practice.kotlin.com.sleepwell.alarm

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import practice.kotlin.com.sleepwell.AlarmActivity
import java.io.IOException


class AlarmService : Service() {

    private val vibration = 1
    private lateinit var mPlayer : MediaPlayer
    private var soundVolume = 10


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        var bool : Boolean? = null
        var stringBuilder : StringBuilder? = null
        val id = intent.getLongExtra("id", 0)

        if (intent == null) {
            bool = true
        } else {
            bool = false
        }
        stringBuilder?.append(bool)


        Log.d("Jay", stringBuilder.toString());

        super.onStartCommand(intent, flags, startId)

        if(intent == null){
//            AlarmSetting().cancelAlarmingNotification(this as Context)
            stopSelf()
            return START_NOT_STICKY
        }

        startForeground(1, AlarmSetting().alarmNoti(this as Context) )

        val tntent = Intent(this as Context, AlarmActivity::class.java)
        tntent.putExtra("id",id)
        Log.d("서비스 아이디", id.toString())
        tntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 268435456

        startActivity(tntent)
        initializePlayer(1)


        return START_STICKY     // 종료시 null

    }

    override fun onDestroy() {
        super.onDestroy()
        AlarmSetting().cancelAlarmingNotification(this as Context)
        finalizePlayer()
    }

    private fun initializePlayer(p0: Int) {

        var paramInt = (getSystemService(Context.AUDIO_SERVICE) as AudioManager).ringerMode

        if (this.vibration == 1 || (paramInt != 0 && paramInt != 1)) {
            var uri: Uri? = null
            uri = Uri.parse("android.resource://practice.kotlin.com.sleepwell/raw/alarm")
            this.mPlayer = MediaPlayer()
            try {
                if (uri != null) {
                    this.mPlayer.setDataSource(this as Context, uri)
                }
                this.mPlayer.setAudioStreamType(4)
                this.mPlayer.isLooping = true
                this.mPlayer.prepare()

                val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
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

    private fun finalizePlayer() {
        if (mPlayer != null) {
            mPlayer.pause()
            mPlayer.stop()
        }
    }
}