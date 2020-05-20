package practice.kotlin.com.sleepwell.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.Time
import android.util.Log
import android.widget.TextView


class CurrentTimePicker(private var textView: TextView, private var apOrpm : TextView) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("msg", "On Receive!!")
        refreshCurrentTime(textView, apOrpm)
    }

    fun refreshCurrentTime(paramTextView: TextView, apOrpm : TextView) {
        val time = Time()
        time.setToNow()
        val stringBuilder = StringBuilder()
        var hour :Int
        val min : Int
        Log.d("dd", "${Integer.valueOf(time.hour)}")
        if(Integer.valueOf(time.hour)>12){
            apOrpm.text = "오후"
            stringBuilder.append(String.format("%2d ", Integer.valueOf(time.hour)-12))
            stringBuilder.append(" ")
            stringBuilder.append(String.format(" %02d", Integer.valueOf(time.minute)))
        }
        else {
            apOrpm.text = "오전"
            hour = Integer.valueOf(time.hour)
            if(hour < 1)
                hour = 12
            stringBuilder.append(String.format("%2d ", hour))
            stringBuilder.append(" ")
            stringBuilder.append(String.format(" %02d", Integer.valueOf(time.minute)))
        }

        paramTextView.text = stringBuilder.toString()
    }
}