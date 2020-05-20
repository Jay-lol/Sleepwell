package practice.kotlin.com.sleepwell.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import org.jetbrains.anko.alert
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.clickBoard
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class AlarmFrag : Fragment(), clickBoard {

    private var alarmDb: AlarmDB? = null

    lateinit var recyclerView : RecyclerView
    lateinit var mAdapter : AlarmRecyclerAdapter
    var alarmList = listOf<AlarmTable>()

    //    private var alarmList = listOf<AlarmTable>()

//    private val recycler_main_screen : RecyclerView by lazy{
//        view?.findViewById(R.id.alarmRecycler) as RecyclerView
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_alarm, container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Inflate the layout for this fragment
        super.onViewCreated(view, savedInstanceState)

//        view.timePicker.setIs24HourView(false)
        val currentHour: Int
        val currentMin: Int

//        AlarmLoading(alarmDb, view.alarmRecycler, requireContext())

        recyclerView = view.alarmRecycler

//        val mLayoutManager = LinearLayoutManager(context)
//        mLayoutManager.reverseLayout = true
//        mLayoutManager.stackFromEnd = true
//        recyclerView.layoutManager = mLayoutManager
        mAdapter = AlarmRecyclerAdapter(requireActivity(), alarmList, this)


        alarmDb = AlarmDB.getInstance(requireActivity())

//        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
//        (recyclerView as LinearLayoutManager).reverseLayout = true;
//        (recyclerView as LinearLayoutManager).stackFromEnd = true;

//        val offsetPx =  TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            100F, context?.resources?.displayMetrics)
//        recyclerView.addItemDecoration(BottomOffsetDecoration(offsetPx.toInt()))

//        recyclerView.adapter = mAdapter

        Thread {
            Log.d("in Thread ", "In")
            alarmList = alarmDb?.alarmDao()?.getAll()!!
//            activity?.runOnUiThread {  mAdapter.notifyDataSetChanged() }
            mAdapter = AlarmRecyclerAdapter(requireActivity(), alarmList, this)
            mAdapter.notifyDataSetChanged()

            Handler(Looper.getMainLooper()).postDelayed( {
//
            Log.d("Size", alarmList.size.toString() + "\n")
//
                recyclerView.adapter = mAdapter
                val mLayoutManager = LinearLayoutManager(context)
                mLayoutManager.reverseLayout = true
                mLayoutManager.stackFromEnd = true
                recyclerView.layoutManager = mLayoutManager
                recyclerView.setHasFixedSize(true)
////                mAdapter.notifyDataSetChanged()
////                activity?.runOnUiThread {  mAdapter.notifyDataSetChanged() }
//
//                Log.d("ㅁㄴ암ㄴㅇ", "ㅁㄴ엄니")
////                view.alarmRecycler.adapter =
////                    AlarmRecyclerAdapter(requireContext(), alarmList,this)
////                requireActivity().runOnUiThread(object : Runnable{
////                    override fun run() {
//
            },500)
        }.start()

//        recycler_main_screen.apply {
//            this.adapter = mAdapter
//        }


//        view.alarmRecycler.adapter =
//            AlarmRecyclerAdapter(requireContext(), alarmList)

//        AlarmLoading().updateAlarm(view.alarmRecycler)

//        aAdapter.notifyDataSetChanged()
//        view.alarmRecycler.adapter = aAdapter
//        view.alarmRecycler.layoutManager = LinearLayoutManager(requireContext())

        // 앞서 설정한 값으로 보여주기
        // 없으면 디폴트 값은 현재시간

        //여기
//        val sharedPreferences = requireContext().getSharedPreferences("daily alarm", Context.MODE_PRIVATE)
//        val millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().timeInMillis)
        val nextNotifyTime: Calendar = GregorianCalendar()
//        nextNotifyTime.timeInMillis = millis
//        val nextDate = nextNotifyTime.time
//        val date_text =
//            SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(nextDate)
        //까지
//        Toast.makeText(requireContext(), "[처음 실행시] 다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT)
//            .show()

        //여기
        // 이전 설정값으로 TimePicker 초기화
        val currentTime = nextNotifyTime.time
        val HourFormat = SimpleDateFormat("HH", Locale.getDefault())
        val MinuteFormat = SimpleDateFormat("mm", Locale.getDefault())
        val pre_hour = HourFormat.format(currentTime).toInt()
        val pre_minute = MinuteFormat.format(currentTime).toInt()
        //까지
//        if (Build.VERSION.SDK_INT >= 23) {
//            view.timePicker.hour = pre_hour
//            view.timePicker.minute = pre_minute
//        } else {
//            view.timePicker.currentHour = pre_hour
//            view.timePicker.currentMinute = pre_minute
//        }


        view.alramButton.setOnClickListener {
            var hour: Int
            var hour_24: Int = 12
            var minute: Int = 0
            var am_pm: Boolean

            val alarmDb = AlarmDB.getInstance(requireContext())

            /* 새로운 cat 객체를 생성, id 이외의 값을 지정 후 DB에 추가 */

            val timePickerDialog =
                TimePickerDialog(
                    context, android.R.style.Theme_DeviceDefault_Dialog_Alert,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute2 ->

                        if (Build.VERSION.SDK_INT >= 23) {
                            hour_24 = hourOfDay
                            minute = minute2
                        } else {
                            hour_24 = 12  // view.currentTime.currentHour
                            minute = 0    // view.timepicker.currentMinute
                        }

                        if (hour_24 > 12) {
                            am_pm = true
                            hour = hour_24 - 12
                        } else {
                            hour = hour_24
                            am_pm = false
                        }

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
//                        val currentDateTime = calendar.time
//                        val date_text =
//                            SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault())
//                                .format(currentDateTime)
//                        Toast.makeText(requireContext(), date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show()

                        Thread{
                            val newAlarm = AlarmTable()
                            newAlarm.hour = hourOfDay.toLong()
                            newAlarm.min = minute2.toLong()
                            newAlarm.ampm = am_pm
                            newAlarm.onOff = true
                            newAlarm.time = calendar.timeInMillis

                            Log.d(
                                "dsa", newAlarm.hour.toString() + "\n" +
                                        newAlarm.min.toString() + "\n" + newAlarm.onOff.toString()
                            )
                            alarmDb?.alarmDao()?.insert(newAlarm)
                            alarmList = alarmDb?.alarmDao()?.getAll()!!
                            Handler(Looper.getMainLooper()).postDelayed( {
                                Log.e("추가", alarmList.size.toString())
                                Log.e("추가 alarmList.size-1", (alarmList.size-1).toString())
//                                view.alarmRecycler.adapter =
//                                    AlarmRecyclerAdapter(requireContext(), alarmList,this)
                                mAdapter.change(alarmList)
                                view.alarmRecycler.adapter?.notifyItemInserted(alarmList.size-1)
                                view.alarmRecycler.scrollToPosition(alarmList.size-1)
                                diaryNotification(alarmList)
                            },0)
                        }.start()


                        //여기
                        //  Preference에 설정한 값 저장
//                        val editor = requireContext().getSharedPreferences("daily alarm", Context.MODE_PRIVATE).edit()
//                        editor.putLong("nextNotifyTime", calendar.timeInMillis)
//                        editor.apply()

                        //까지


//                        val showTime = AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
//                            .setTitle("그때 일어나려면....")
//                            .setMessage(CalculSleepTime().calSleepSpecific(hourOfDay, minute))
//                            .create()
//
//                        showTime.window?.setGravity(Gravity.CENTER)
//                        showTime.show()
                    }
                    , pre_hour, pre_minute, false
                )

            timePickerDialog.window?.setGravity(Gravity.CENTER)
            timePickerDialog.show()

        }

    }


    fun diaryNotification(alarmlists: List<AlarmTable>) {
//        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        Boolean dailyNotify = sharedPref.getBoolean(SettingsActivity.KEY_PREF_DAILY_NOTIFICATION, true);
//        val dailyNotify = true // 무조건 알람을 사용

        val pm = requireContext().packageManager
        val receiver = ComponentName(requireActivity(), DeviceBootReceiver::class.java)

        val alarmIntent = Intent(requireActivity(), AlarmReceiver::class.java)
        var pendingIntent: PendingIntent
        var alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager


        for(i in 0 until alarmlists.size) {
            var time = alarmlists[i].time
            val onOff = alarmlists[i].onOff

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time!!
            // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정


            if (onOff) {
                alarmIntent.putExtra("id", alarmlists[i].id)

                while (calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.DATE, 1)    // date 1을더해 더해준다
                    // 추가작업 : 10일이상 (10번이상 이 와일문을돌려도) 알람시간이 그대로면 업데이트를 해주자
                }

                time = calendar.timeInMillis

//                val currentDateTime = calendar.time
//                val date_text =
//                    SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault())
//                        .format(currentDateTime)
//                Toast.makeText(requireActivity(), date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show()
//                Log.d("알람 재설정" , date_text)

                // 사용자가  알람을 허용했다면
                pendingIntent = PendingIntent.getBroadcast(requireContext(), alarmlists[i].id!!.toInt(), alarmIntent, FLAG_CANCEL_CURRENT)

                alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                Log.d("사용자가 알람을??", "허용")
//            alarmManager.setRepeating(
//                AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                AlarmManager.INTERVAL_DAY, pendingIntent)
//
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, time, pendingIntent)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 각버전에 맞게 호출
                } else {

                }

            } else{
                Log.d("사용자가 알람을??", "허용안함")
                pendingIntent = PendingIntent.getBroadcast(requireContext(), alarmlists[i].id!!.toInt(), alarmIntent, FLAG_CANCEL_CURRENT)
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
        //        else { //Disable Daily Notifications
//            if (PendingIntent.getBroadcast(this, 0, alarmIntent, 0) != null && alarmManager != null) {
//                alarmManager.cancel(pendingIntent);
//                //Toast.makeText(this,"Notifications were disabled",Toast.LENGTH_SHORT).show();
//            }
//            pm.setComponentEnabledSetting(receiver,
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP);
//        }


    override fun sendBoard(idx: Int, bool :Boolean?) {
        val sendTime : Long
        bloop@for (i in 0 until alarmList.size){
            if(alarmList[i].id == idx.toLong())
            {
                sendTime = alarmList[i].time!!
                Thread {
                    Log.d("업데이트 Thread ", "In")

                    alarmDb?.alarmDao()?.updateonoff(idx.toLong(), bool!!)
                    alarmList = alarmDb?.alarmDao()?.getAll()!!

                    Handler(Looper.getMainLooper()).postDelayed({
                        if(bool!!) {
                            Log.d("알람킴","알람킴")
                            alertAlarmTime(sendTime)
                            Log.d("alarmList.onOff값", bool.toString())
                        }
                        else {
                            Log.d("알람끔","알람끔")
                            deleteAlarmTime(sendTime, 0)
                            Log.d("alarmList.onOff값", bool.toString())
                        }
                        diaryNotification(alarmList)
                    }, 0)
                }.start()
                break@bloop
            }
        }


    }

    override fun sendDeIdx(idx: Long?, position : Int) {
        Log.d("ss","ss")

        context?.alert("삭제하시겠습니까?") {
            positiveButton("Yes") {
                Log.d("삭제 버튼", "누름")
                Thread {
                    val sendTime = idx?.let { it1 -> alarmDb?.alarmDao()?.getById(it1) }
                    Log.d("삭제 Thread ", "In")

                    idx?.let { it -> alarmDb?.alarmDao()?.deleteAlarm(it) }
                    alarmList = alarmDb?.alarmDao()?.getAll()!!

                    Handler(Looper.getMainLooper()).postDelayed({
                        Log.d("삭제", alarmList.size.toString() + "\n")
                        var alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val alarmIntent = Intent(requireActivity(), AlarmReceiver::class.java)
                        if(idx!=null){
                            val pendingIntent = PendingIntent.getBroadcast(requireActivity(), idx.toInt(), alarmIntent, FLAG_CANCEL_CURRENT)
                            alarmManager.cancel(pendingIntent)
                        }
                        mAdapter.change(alarmList)
                        mAdapter.notifyItemRemoved(position)
                        sendTime?.let { it1 -> deleteAlarmTime(it1, 1) }
                        diaryNotification(alarmList)
                    }, 0)
                }.start()
            }
            negativeButton("no"){

            }
        }?.show()
    }

    private fun alertAlarmTime(time : Long){

        val calendar  = Calendar.getInstance()
        calendar.timeInMillis = time

        while (calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1)    // date 1을더해 더해준다
            // 추가작업 : 10일이상 (10번이상 이 와일문을돌려도) 알람시간이 그대로면 업데이트를 해주자
        }

        val currentDateTime = calendar.time
        val date_text =
            SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault())
                .format(currentDateTime)
        Toast.makeText(requireActivity(), date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_LONG).show()
    }

    private fun deleteAlarmTime(time : Long, flag : Int){
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time

        while (calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1)    // date 1을더해 더해준다
            // 추가작업 : 10일이상 (10번이상 이 와일문을돌려도) 알람시간이 그대로면 업데이트를 해주자
        }

        val currentDateTime = calendar.time
        val date_text =
            SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault())
                .format(currentDateTime)
        if(flag==0)
            Toast.makeText(requireActivity(), date_text + " 알람을 껏습니다!!", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(requireActivity(), date_text + " 알람을 삭제했습니다!!", Toast.LENGTH_LONG).show()
    }



    override fun onResume() {
        super.onResume()
        Thread {
            Log.d("알람플래그가 떠있는 상태에서 알람액티비티 작동 Thread ", "In")

            alarmList = alarmDb?.alarmDao()?.getAll()!!

            Handler(Looper.getMainLooper()).postDelayed({
                mAdapter.change(alarmList)
                mAdapter.notifyDataSetChanged()
            }, 0)
        }.start()
    }
//    internal class BottomOffsetDecoration(private val mBottomOffset: Int) : ItemDecoration() {
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//            super.getItemOffsets(outRect, view, parent, state)
//            val dataSize = state.itemCount
//            val position = parent.getChildAdapterPosition(view)
//            if (dataSize > 0 && position == dataSize - 1) {
//                outRect.set(0, 0, 0, mBottomOffset)
//            } else {
//                outRect.set(0, 0, 0, 0)
//            }
//        }
//
//    }

}

