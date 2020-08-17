package practice.kotlin.com.sleepwell.alarm

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.alarm_item.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.clickBoard
import practice.kotlin.com.sleepwell.statics.commuList

class AlarmRecyclerAdapter(val context: Context, var alarms: List<AlarmTable>, listener : clickBoard)
    : RecyclerView.Adapter<AlarmRecyclerAdapter.Holder>() {

    val mCallback = listener

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        var ampm = view.amOrpm
        var pp = view.timeBox
        var blanck = view.blanckView
        var switch= view.onOffSwitch
        var item = view.alarm_list_cardView

        fun bind(alarm : AlarmTable){
            var hour : String = alarm.hour.toString()

            if (alarm.ampm)
                ampm.text = "오후"
            else
                ampm.text = "오전"

            if(alarm.hour>12){
                hour = (alarm.hour -12).toString()
            }
            if(alarm.hour<1){
                hour = "12"
            }

            if(alarm.min >= 10)
                pp.text = hour + " : " + alarm.min.toString() // 시간을 보기좋게 처리
            else
                pp.text = hour + " : 0" + alarm.min.toString()

            Log.d("스위치초기온오프상태", alarm.onOff.toString())
            switch.setCheckedImmediately(alarm.onOff)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from
                (context).inflate(R.layout.alarm_item, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val idx =  alarms[position].id
        holder.bind(alarms[position])

        Log.d("바인드", "$position" +"\n${alarms.size}")

        if(position == alarms.size-1){
//            val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                100F, context.resources.displayMetrics).toInt()
//
//            holder.blanck.layoutParams.height = height
//            holder.blanck.requestLayout()
        } else {
            holder.blanck.layoutParams.height = 0
            holder.blanck.requestLayout()
        }

//        holder.switch.setOnStateChangeListener { process, state, jtb ->
//            if (state.name == "LEFT")
//                Log.d("알람OFF", position.toString() +  "\n" + state.name)
//            else if(state.name == "RIGHT")
//                Log.d("알람ON", position.toString() +  "\n" + state.name)
//        }

        holder.item.setOnLongClickListener {
            Log.d("롱클릭", "$idx" + "\n" +
                    "${holder.adapterPosition}"+"\n" + "$position" )
            mCallback.sendDeIdx(idx, holder.adapterPosition)

            true
        }

        holder.item.setOnClickListener{
            Log.d("클릭", "$idx" + "\n" +
                    "${holder.adapterPosition}"+"\n" + "$position")
            mCallback.changeData(idx!!, holder.adapterPosition)
        }

        holder.switch.setOnClickListener{
            if(holder.switch.isChecked) {
                Log.d("c", "알람On" + idx.toString())
                mCallback.sendBoard(idx!!.toInt(), true,0,1)
            }
            else {
                Log.d("c", "알람Off" + idx.toString() )
                mCallback.sendBoard(idx!!.toInt(), false,0,1)
            }
        }

    }

    fun change(new : List<AlarmTable>){
        alarms = new
    }


    override fun getItemCount(): Int {
        return alarms.size
    }


}