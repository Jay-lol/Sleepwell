package practice.kotlin.com.sleepwell.sleepAndCommu

import java.text.SimpleDateFormat
import java.util.*

class CalculSleepTime {
    fun calSleep(): String {

        var start = System.currentTimeMillis()

        var today: String
        var result = ""

        val date = Date(start)
        val mFormat = SimpleDateFormat("hh:mm a")

        for (i in 1..6) {
            val cal = Calendar.getInstance()
            cal.setTime(date)
            cal.add(Calendar.MINUTE, 15 + (i * 90))
            today = mFormat.format(cal.getTime())
            if (i % 2 == 0)
                result += today + "\n"
            else
                result += today + "   "
        }


        return result + "위의 시간에 일어나는걸 추천합니다"
    }

    fun calSleepSpecific(hour: Int, minute: Int): String {

        val start = System.currentTimeMillis()

        var today: String
        var result = ""

        val date = Date(start)
        val mFormat = SimpleDateFormat("hh:mm a")

        for (i in 1..6) {
            val cal = Calendar.getInstance()
            cal.setTime(date)
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            cal.add(Calendar.MINUTE, -(15 + (i * 90)))
            today = mFormat.format(cal.getTime())
            if (i % 2 == 0)
                result = today + "   " + result
            else
                result = today + "\n" + result
        }

        return result + "위의 시간에 자는걸 추천합니다"
    }

    fun calSleepFuture(hour: Int, minute: Int): String {

        val start = System.currentTimeMillis()

        var today: String
        var result = ""

        val date = Date(start)
        val mFormat = SimpleDateFormat("hh:mm a")

        for (i in 1..6) {
            val cal = Calendar.getInstance()
            cal.setTime(date)
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            cal.add(Calendar.MINUTE, 15 + (i * 90))
            today = mFormat.format(cal.getTime())
            if (i % 2 == 0)
                result += today + "\n"
            else
                result += today + "   "
        }

        return result + "위의 시간에 일어나는걸 추천합니다"
    }
}