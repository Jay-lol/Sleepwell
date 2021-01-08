package practice.kotlin.com.sleepwell.adapter

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import practice.kotlin.com.sleepwell.alarm.room.AlarmTable
import practice.kotlin.com.sleepwell.alarmListener
import practice.kotlin.com.sleepwell.databinding.AlarmItemBinding

class ViewHolderAlarm(private val binding: AlarmItemBinding, private val callback: alarmListener) :
    RecyclerView.ViewHolder(binding.root) {
    private val TAG: String = "로그"

    @SuppressLint("SetTextI18n")
    fun bind(alarm: AlarmTable, adapterPosition: Int) {
        binding.item = this

        var hour: String = alarm.hour.toString()
        val min: String = alarm.min.toString()
        if (alarm.ampm) binding.amOrpm.text = "오후"
        else binding.amOrpm.text = "오전"
        if (alarm.hour > 12) hour = (alarm.hour - 12).toString()
        if (alarm.hour < 1) hour = "12"
        // 시간을 보기좋게 처리
        if (alarm.min >= 10) binding.timeBox.text = "$hour : $min"
        else binding.timeBox.text = "$hour : 0${min}"

        binding.onOffSwitch.setCheckedImmediately(alarm.onOff)

        setClickListener(alarm, adapterPosition)
    }

    private fun setClickListener(alarm: AlarmTable, adapterPosition: Int) {
        val idx = alarm.id
        idx ?: return
        binding.alarmListCardView.setOnLongClickListener {
            Log.d(TAG, "ViewHolderAlarm ~ setClickListener() called")
            callback.deleteAlarm(idx, adapterPosition)
            true
        }
        binding.alarmListCardView.setOnClickListener {
            callback.changeAlarmTime(idx, adapterPosition)
        }
        binding.onOffSwitch.setOnClickListener {
            Log.d(TAG, "알람 ${binding.onOffSwitch.isChecked} $idx")
            callback.onOffalarm(idx, binding.onOffSwitch.isChecked)
        }
    }
}