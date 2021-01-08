package practice.kotlin.com.sleepwell.alarm.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarmTable")
    fun getAll() : List<AlarmTable>

    /* import android.arch.persistence.room.OnConflictStrategy.REPLACE */
    @Insert(onConflict = REPLACE)
    fun insert(alarmTable: AlarmTable)

    @Query("DELETE from alarmTable")
    fun deleteAll()

    @Query("DELETE from alarmTable WHERE id = :alarmId")
    fun deleteAlarm(alarmId : Long)

    @Query("UPDATE alarmTable SET OnOff = :onoff WHERE id = :alarmId")
    fun updateOnOff(alarmId : Long, onoff : Boolean)

    @Query("SELECT Time from alarmTable WHERE id = :idx")
    fun getById(idx : Long) : Long?

    @Query("UPDATE alarmTable SET Time = :changedTime , hour = :hourOfDay" +
            ", minute = :minute2, AmPm = :am_pm  WHERE id = :alarmId")
    fun updateTime(
        alarmId: Long,
        changedTime: Long?,
        hourOfDay: Int,
        minute2: Int,
        am_pm: Boolean)
}