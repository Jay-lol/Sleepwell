package practice.kotlin.com.sleepwell.alarm.room

data class AlarmItem(
    var amPm: String = "0",
    var hour: String = "0",
    var minute: String = "0",
    var onOff: Boolean = true
)