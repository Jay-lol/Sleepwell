package practice.kotlin.com.sleepwell.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import practice.kotlin.com.sleepwell.alarm.room.AlarmTable
import practice.kotlin.com.sleepwell.alarmListener
import practice.kotlin.com.sleepwell.databinding.AlarmItemBinding

class AlarmRecyclerAdapter(val context: Context, var alarms: List<AlarmTable>,
                           private val callback : alarmListener)
    : RecyclerView.Adapter<ViewHolderAlarm>() {

    override fun getItemCount(): Int = alarms.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAlarm {
        return ViewHolderAlarm(
            AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            callback
        )
    }

    override fun onBindViewHolder(holder: ViewHolderAlarm, position: Int) {
        holder.bind(alarms[position], holder.adapterPosition)
    }

    fun change(newList : List<AlarmTable>){
        alarms = newList
    }
}