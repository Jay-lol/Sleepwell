package practice.kotlin.com.sleepwell.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.alert
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.adapter.AlarmRecyclerAdapter
import practice.kotlin.com.sleepwell.alarm.*
import practice.kotlin.com.sleepwell.alarm.room.AlarmDB
import practice.kotlin.com.sleepwell.alarm.room.AlarmTable
import practice.kotlin.com.sleepwell.alarmListener
import practice.kotlin.com.sleepwell.databinding.FragmentAlarmBinding
import practice.kotlin.com.sleepwell.extension.showToast
import java.text.SimpleDateFormat
import java.util.*

class AlarmFragment : Fragment(), alarmListener {

    private val TAG: String = "로그 AlarmFragment"

    private val alarmDb: AlarmDB? by lazy {
        AlarmDB.getInstance(requireActivity())
    }

    private lateinit var calendar: Calendar
    private lateinit var binding: FragmentAlarmBinding
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: AlarmRecyclerAdapter
    var alarmList = listOf<AlarmTable>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm, container, false)
        binding.alarmFragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.alarmRecycler

        mAdapter = AlarmRecyclerAdapter(requireActivity(), alarmList, this)

        recyclerView.adapter = mAdapter
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.reverseLayout = true
        mLayoutManager.stackFromEnd = true

        recyclerView.layoutManager = mLayoutManager

        recyclerView.setHasFixedSize(true)

    }

    fun makeAlarm() {
        // 지금 시간으로 TimePicker 초기화
        val nextNotifyTime: Calendar = GregorianCalendar()
        val currentTime = nextNotifyTime.time
        val HourFormat = SimpleDateFormat("HH", Locale.getDefault())
        val MinuteFormat = SimpleDateFormat("mm", Locale.getDefault())
        val defaultHour = HourFormat.format(currentTime).toInt()
        val defaultMinute = MinuteFormat.format(currentTime).toInt()

        var hour_24: Int = 12
        var am_pm: Boolean

        /* 새로운 cat 객체를 생성, id 이외의 값을 지정 후 DB에 추가 */
        val timePickerDialog =
            TimePickerDialog(
                context, android.R.style.Theme_DeviceDefault_Dialog_Alert,
                { _, hourOfDay, minute ->
                    if (Build.VERSION.SDK_INT >= 23) {
                        hour_24 = hourOfDay
                    } else {
                        hour_24 = 12  // view.currentTime.currentHour
                    }

                    am_pm = hour_24 >= 12

                    calendar = Calendar.getInstance()
                    calendar.timeInMillis = System.currentTimeMillis()

                    calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                    calendar[Calendar.MINUTE] = minute
                    calendar[Calendar.SECOND] = 0

                    // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                    calendar = updateCalendar(calendar)
                    // 알람시간을 토스트메세지로 알려줌.
                    alertAlarmTime(calendar.timeInMillis)

                    val job = Thread {
                        val newAlarm = AlarmTable()
                        newAlarm.hour = hourOfDay.toLong()
                        newAlarm.min = minute.toLong()
                        newAlarm.ampm = am_pm
                        newAlarm.onOff = true
                        newAlarm.time = calendar.timeInMillis

                        Log.d(
                            TAG, newAlarm.hour.toString() + "\n" +
                                    newAlarm.min.toString() + "\n" + newAlarm.onOff.toString()
                        )
                        alarmDb?.alarmDao()?.insert(newAlarm)
                        alarmList = alarmDb?.alarmDao()?.getAll()!!
                    }
                    job.start()
                    job.join()
                    Log.d(TAG, "새로운 알람 추가 ID:${alarmList.size}")
                    mAdapter.change(alarmList)
                    binding.alarmRecycler.adapter?.notifyItemInserted(alarmList.size - 1)
                    binding.alarmRecycler.scrollToPosition(alarmList.size - 1)
                    setAlarm(alarmList)
                }, defaultHour, defaultMinute, false
            )
        timePickerDialog.window?.setGravity(Gravity.CENTER)
        timePickerDialog.show()

    }

    fun setAlarm(alarmList: List<AlarmTable>) {
        val pm = requireActivity().applicationContext.packageManager
        val receiver = ComponentName(requireContext(), DeviceBootReceiver::class.java)
        val alarmIntent = Intent(requireActivity(), AlarmBroadcastReceiver::class.java)
        var pendingIntent: PendingIntent
        var alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (i in alarmList.indices) {
            var time = alarmList[i].time
            val onOff = alarmList[i].onOff

            calendar = Calendar.getInstance()
            calendar.timeInMillis = time!!

            if (onOff) {
                alarmIntent.putExtra("id", alarmList[i].id)

                calendar = updateCalendar(calendar)

                time = calendar.timeInMillis

                // 사용자가  알람을 허용했다면
                pendingIntent = PendingIntent.getBroadcast(
                    activity,
                    alarmList[i].id!!.toInt(),
                    alarmIntent,
                    FLAG_CANCEL_CURRENT
                )

                alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

//            alarmManager.setRepeating(
//                AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                AlarmManager.INTERVAL_DAY, pendingIntent)

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    // 각버전에 맞게 호출
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //API 21 이상 API 23미만
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP, time, pendingIntent
                        )
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, time, pendingIntent
                    )
                }

            } else {
                pendingIntent = PendingIntent.getBroadcast(
                    activity,
                    alarmList[i].id!!.toInt(),
                    alarmIntent,
                    FLAG_CANCEL_CURRENT
                )
                alarmManager.cancel(pendingIntent)
            }
        }

        // 부팅 후 실행되는 리시버 사용가능하게 설정
        pm?.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    override fun onOffalarm(idx: Long, onOff: Boolean) {
        var updateTime : Long? = null
        val job = Thread {
            Log.d(TAG, "업데이트 Thread In")
            alarmDb ?: return@Thread
            alarmDb!!.alarmDao().updateOnOff(idx, onOff)
            updateTime = alarmDb!!.alarmDao().getById(idx)
            alarmList = alarmDb!!.alarmDao().getAll()
            Log.d(TAG, "데이터구조 $alarmList")
        }
        job.start()
        job.join()
        updateTime?:return
        if (onOff) alertAlarmTime(updateTime!!)
        else deleteAlarmTime(updateTime!!, 0)
        setAlarm(alarmList)
    }

    override fun deleteAlarm(idx: Long, position: Int) {
        var sendTime : Long? = null
        requireContext().alert("삭제하시겠습니까?") {
            positiveButton("Yes") { dialog->
                Log.d(TAG, "알람 삭제 버튼 누름")
                val job = Thread {
                    alarmDb ?: return@Thread
                    sendTime = alarmDb!!.alarmDao().getById(idx)
                    Log.d("삭제 Thread ", "In")

                    alarmDb?.alarmDao()?.deleteAlarm(idx)
                    alarmList = alarmDb?.alarmDao()?.getAll()!!

                }
                job.start();job.join()
                sendTime?:dialog.dismiss()
                Log.d(TAG, "삭제 -> ${alarmList.size}")
                val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = Intent(requireActivity(), AlarmBroadcastReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    requireActivity(),
                    idx.toInt(),
                    alarmIntent,
                    FLAG_CANCEL_CURRENT
                )
                alarmManager.cancel(pendingIntent)
                mAdapter.change(alarmList)
                mAdapter.notifyItemRemoved(position)
                deleteAlarmTime(sendTime!!, 1)
                setAlarm(alarmList)
            }
            negativeButton("no"){}
        }.show()
    }

    override fun changeAlarmTime(idx: Long, position: Int) {
        Log.d(TAG, "시간 변경 스레드")

        var am_pm: Boolean
        var pre_hour: Long = 12
        var pre_min: Long = 0

        val targetAlarm = alarmList.find { it.id == idx }
        targetAlarm ?: return
        pre_hour = targetAlarm.hour
        pre_min = targetAlarm.min
        Log.d(TAG, "pre_hour: $pre_hour  pre_min: $pre_min")

        val timePickerDialog =
            TimePickerDialog(
                context, android.R.style.Theme_DeviceDefault_Dialog_Alert,
                { _, hourOfDay, minute2 ->
                    Log.d(TAG, "변경 버튼 누름 $hourOfDay")

                    am_pm = hourOfDay >= 12

                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = System.currentTimeMillis()

                    calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                    calendar[Calendar.MINUTE] = minute2
                    calendar[Calendar.SECOND] = 0

                    alertAlarmTime(calendar.timeInMillis)
                    // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                    if (calendar.before(Calendar.getInstance())) {
                        calendar.add(Calendar.DATE, 1)  // date 1을더해 더해준다
                    }

                    val job = Thread {
                        alarmDb?.alarmDao()?.updateOnOff(idx, true)
                        alarmDb?.alarmDao()?.updateTime(idx, calendar.timeInMillis, hourOfDay, minute2, am_pm)
                        Log.d(TAG, "Thread 변경전 테이블구조 $alarmList")
                        alarmList = alarmDb?.alarmDao()?.getAll()!!
                        Log.d(TAG, "Thread 변경후 테이블구조 $alarmList")
                    }
                    job.start()
                    job.join()
                    mAdapter.change(alarmList)
                    mAdapter.notifyItemChanged(position)
                    setAlarm(alarmList)
                }, pre_hour.toInt(), pre_min.toInt(), false
            )

        timePickerDialog.window?.setGravity(Gravity.CENTER)
        timePickerDialog.show()

    }

    private fun alertAlarmTime(time: Long) {
        calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar = updateCalendar(calendar)
        val currentDateTime = calendar.time
        val date =
            SimpleDateFormat("MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault())
                .format(currentDateTime)
        this.showToast("$date 으로 알람이 설정되었습니다!")
    }

    private fun deleteAlarmTime(time: Long, flag: Int) {
        calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar = updateCalendar(calendar)
        val currentDateTime = calendar.time
        val date_text =
            SimpleDateFormat("MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault())
                .format(currentDateTime)
        if (flag == 0) this.showToast("$date_text 알람을 껐습니다!")
        else this.showToast("$date_text 알람을 삭제했습니다!!")
    }

    private fun updateCalendar(calendar: Calendar): Calendar {
        while (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)    // date 1을더해 더해준다
        }
        return calendar
    }

    override fun onResume() {
        super.onResume()
        val job = Thread {
            alarmList = alarmDb?.alarmDao()?.getAll()!!
            Log.d(
                TAG, "onResume loadAlarm\n" +
                        "Alarm Thread ${alarmList.size}개 데이터 구조:\n$alarmList"
            )
        }
        job.start();job.join()
        mAdapter.change(alarmList)
        mAdapter.notifyDataSetChanged()
    }

}

