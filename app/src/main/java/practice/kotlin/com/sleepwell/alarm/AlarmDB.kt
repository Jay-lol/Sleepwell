package practice.kotlin.com.sleepwell.alarm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlarmTable::class], version = 1)
abstract class AlarmDB : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {
        private var INSTANCE: AlarmDB? = null

        fun getInstance(context: Context): AlarmDB? {
            if (INSTANCE == null) {
                synchronized(AlarmDB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AlarmDB::class.java, "alarmTable")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}