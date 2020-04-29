package practice.kotlin.com.sleepwell.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import practice.kotlin.com.sleepwell.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class AlarmFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_alarm, container, false)
        view.timePicker.setIs24HourView(true)

        // 앞서 설정한 값으로 보여주기
        // 없으면 디폴트 값은 현재시간
        val sharedPreferences = requireContext().getSharedPreferences("daily alarm", Context.MODE_PRIVATE)
        val millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().timeInMillis)
        val nextNotifyTime: Calendar = GregorianCalendar()
        nextNotifyTime.timeInMillis = millis
        val nextDate = nextNotifyTime.time
        val date_text =
            SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(nextDate)
//        Toast.makeText(requireContext(), "[처음 실행시] 다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT)
//            .show()


        // 이전 설정값으로 TimePicker 초기화
        val currentTime = nextNotifyTime.time
        val HourFormat = SimpleDateFormat("kk", Locale.getDefault())
        val MinuteFormat = SimpleDateFormat("mm", Locale.getDefault())
        val pre_hour = HourFormat.format(currentTime).toInt()
        val pre_minute = MinuteFormat.format(currentTime).toInt()
        if (Build.VERSION.SDK_INT >= 23) {
            view.timePicker.hour = pre_hour
            view.timePicker.minute = pre_minute
        } else {
            view.timePicker.currentHour = pre_hour
            view.timePicker.currentMinute = pre_minute
        }

        view.alramButton.setOnClickListener(View.OnClickListener {
            val hour: Int
            val hour_24: Int
            val minute: Int
            val am_pm: String
            if (Build.VERSION.SDK_INT >= 23) {
                hour_24 = view.timePicker.hour
                minute = view.timePicker.minute
            } else {
                hour_24 = view.timePicker.currentHour
                minute = view.timePicker.currentMinute
            }
            if (hour_24 > 12) {
                am_pm = "PM"
                hour = hour_24 - 12
            } else {
                hour = hour_24
                am_pm = "AM"
            }

            // 현재 지정된 시간으로 알람 시간 설정
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar[Calendar.HOUR_OF_DAY] = hour_24
            calendar[Calendar.MINUTE] = minute
            calendar[Calendar.SECOND] = 0

            // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }
            val currentDateTime = calendar.time
            val date_text =
                SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault())
                    .format(currentDateTime)
            Toast.makeText(requireContext(), date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show()

            //  Preference에 설정한 값 저장
            val editor = requireContext().getSharedPreferences("daily alarm", Context.MODE_PRIVATE).edit()
            editor.putLong("nextNotifyTime", calendar.timeInMillis)
            editor.apply()
            diaryNotification(calendar)
        })
        return view
    }



    fun diaryNotification(calendar: Calendar) {
//        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        Boolean dailyNotify = sharedPref.getBoolean(SettingsActivity.KEY_PREF_DAILY_NOTIFICATION, true);
        val dailyNotify = true // 무조건 알람을 사용
        val pm = requireContext().packageManager
        val receiver = ComponentName(requireContext(), DeviceBootReceiver::class.java)
        val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, 0)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager


        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY, pendingIntent
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(
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
    }
}

