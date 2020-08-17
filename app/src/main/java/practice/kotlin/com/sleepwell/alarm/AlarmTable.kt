package practice.kotlin.com.sleepwell.alarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "alarmTable")
data class AlarmTable(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "Time") var time : Long?,
    @ColumnInfo(name = "hour") var hour: Long,
    @ColumnInfo(name = "minute") var min: Long,
    @ColumnInfo(name = "AmPm") var ampm : Boolean,
    @ColumnInfo(name = "OnOff") var onOff: Boolean) {

    constructor() : this(null, null, 0,0, false,false)
}