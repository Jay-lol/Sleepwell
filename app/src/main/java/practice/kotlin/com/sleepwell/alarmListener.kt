package practice.kotlin.com.sleepwell

interface alarmListener {
    fun onOffalarm(idx : Long, onOff: Boolean)
    fun deleteAlarm(idx : Long , position : Int)
    fun changeAlarmTime(idx: Long, position : Int)
}