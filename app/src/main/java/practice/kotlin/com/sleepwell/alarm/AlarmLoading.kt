import androidx.recyclerview.widget.RecyclerView

//package practice.kotlin.com.sleepwell.alarm
//
//import android.content.Context
//import android.os.AsyncTask
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import androidx.recyclerview.widget.RecyclerView
//import practice.kotlin.com.sleepwell.statics.commuList
//import practice.kotlin.com.sleepwell.statics.commuList.Companion.alarmList
//
//class AlarmLoading(
//    alarmDb: AlarmDB,
//    alarmRecycler: RecyclerView,
//    requireContext: Context) : AsyncTask<Void, Void, Void>() {
//
//    val malarmDb = alarmDb
//    val malarmRecycler = alarmRecycler
//    val mContext = requireContext
//
//    override fun doInBackground(vararg params: Void): Void? {
//        Log.d("in Thread ", "In")
//
//        alarmList = malarmDb.alarmDao().getAll()
//
//        Log.d("Size", commuList.alarmList.size.toString() + "\n")
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            malarmRecycler.adapter =
//                AlarmRecyclerAdapter(mContext, commuList.alarmList)
//            malarmRecycler.adapter?.notifyDataSetChanged()
//        },2000)
//
//        return null
//    }
//}

class AlarmLoading{
    fun refresh(recyclerview : RecyclerView){

        recyclerview.adapter?.notifyDataSetChanged()
    }
}