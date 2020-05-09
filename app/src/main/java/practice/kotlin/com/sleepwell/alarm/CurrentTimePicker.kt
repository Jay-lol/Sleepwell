package practice.kotlin.com.sleepwell.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.Time
import android.util.Log
import android.widget.TextView


class CurrentTimePicker(private var textView: TextView) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("msg", "On Receive!!")
        refreshCurrentTime(textView)
    }

    fun refreshCurrentTime(paramTextView: TextView) {
        val time = Time()
        time.setToNow()
        val stringBuilder = StringBuilder()
        stringBuilder.append(String.format("%02d", Integer.valueOf(time.hour)))
        stringBuilder.append(":")
        stringBuilder.append(String.format("%02d", Integer.valueOf(time.minute)))
        paramTextView.text = stringBuilder.toString()
    }
}