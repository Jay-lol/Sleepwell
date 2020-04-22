package practice.kotlin.com.sleepwell

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commu.*
import kotlinx.android.synthetic.main.fragment_commu.view.*
import kotlinx.android.synthetic.main.fragment_commu.view.submit_area

class ClickEvents : Thread(){
    fun cal(v: View, context : Activity){
        when(v.id){
            R.id.cal_now -> calNow(v,context)
            R.id.cal_future -> calPast(v , context)
            R.id.cal_past -> calFuture(v , context)
            R.id.text_commu -> submit(v,context)
        }
    }

    private fun submit(v: View, context: Activity){
//        val retrofitService = RetrofitService().callBackGet(context.submit_area.text.toString())
//        retrofitService.getTotalUser()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            /*.subscribe({ it ->
//                Log.d("content", it.toString())
//                Log.d("userIdx ", it.getAsJsonArray("data").get(0).asJsonObject.get("userIdx").asString)
//                Log.d("name ", it.getAsJsonArray("data").get(0).asJsonObject.get("name").asString)
//                Log.d("part ", it.getAsJsonArray("data").get(0).asJsonObject.get("part").asString)
//                Log.d("profileUrl ", it.getAsJsonArray("data").get(0).asJsonObject.get("profileUrl").asString)
//                Log.d("message", it.getAsJsonPrimitive("message").asString)
//                Log.d("status", it.getAsJsonPrimitive("status").asString)*/
//            .subscribe({
//                Log.d("content", it.toString())
//                Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
//                it.getAsJsonPrimitive("message").asString
////                val test = it.getAsJsonArray("data").get(0).asJsonObject.get("name").asString
////                retrofitService.getUser(test)
////                    .subscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
////                    .subscribe {
////                        Log.d("content", it.toString())
////                        Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
////                    }
//            })
//            {
//                Log.e("Error", it.message)
//            }
//        RetrofitService().callBackPost(context.submit_area.text.toString())
        val retrofitService = RetrofitService().callBackGet()
        retrofitService.getTotalUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.subscribe({ it ->
                Log.d("content", it.toString())
                Log.d("userIdx ", it.getAsJsonArray("data").get(0).asJsonObject.get("userIdx").asString)
                Log.d("name ", it.getAsJsonArray("data").get(0).asJsonObject.get("name").asString)
                Log.d("part ", it.getAsJsonArray("data").get(0).asJsonObject.get("part").asString)
                Log.d("profileUrl ", it.getAsJsonArray("data").get(0).asJsonObject.get("profileUrl").asString)
                Log.d("message", it.getAsJsonPrimitive("message").asString)
                Log.d("status", it.getAsJsonPrimitive("status").asString)*/
            .subscribe({
                Log.d("content", it.toString())
                it.getAsJsonPrimitive("message").asString
                val test = it.getAsJsonArray("data").get(0).asJsonObject.get("name").asString
                retrofitService.getUser(test)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d("content", it.toString())
                        Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
                    }
            })
            {
                Log.e("Error", it.message)
            }
    }


    private fun calNow(v: View, context : Context){
        val showTime = AlertDialog.Builder(context)
            .setTitle("지금 잠들면....")
            .setMessage("7:00AM 에 일어나면 좋습니다")
        showTime.show()
    }
    private fun calFuture(v: View, context : Context){
        val showTime = AlertDialog.Builder(context)
            .setTitle("그때 잠들면....")
            .setMessage("7:00AM 에 일어나면 좋습니다")
        showTime.show()
    }
    private fun calPast(v: View, context : Context){
        val showTime = AlertDialog.Builder(context)
            .setTitle("그때 일어나려면....")
            .setMessage("10:00PM 에 잠들면 좋습니다")
        showTime.show()
    }

    fun startLoading(){

        var jsonObject : JsonObject? = null

        val retrofitService = RetrofitService().callBackGet()
        retrofitService.getTotalUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.subscribe({ it ->
                Log.d("content", it.toString())
                Log.d("message", it.getAsJsonPrimitive("message").asString)
                Log.d("status", it.getAsJsonPrimitive("status").asString)*/
            .subscribe({
                Log.d("content", it.toString())
                jsonObject = it
                it.getAsJsonPrimitive("message").asString
                val test = it.getAsJsonArray("data").get(0).asJsonObject.get("linkUrl").asString
                JsonString.getServerString.add(it.getAsJsonArray("data").get(0)
                    .asJsonObject.get("thumbnailUrl").asString)
                JsonString.getTitleString.add(it.getAsJsonArray("data").get(0)
                    .asJsonObject.get("linkTitle").asString)
                JsonString.getWriterString.add(it.getAsJsonArray("data").get(0)
                    .asJsonObject.get("linkChannel").asString)
                JsonString.getServerString.add(it.getAsJsonArray("data").get(1)
                    .asJsonObject.get("thumbnailUrl").asString)
                JsonString.getTitleString.add(it.getAsJsonArray("data").get(1)
                    .asJsonObject.get("linkTitle").asString)
                JsonString.getWriterString.add(it.getAsJsonArray("data").get(1)
                    .asJsonObject.get("linkChannel").asString)

            })
            {
                Log.e("Error", it.message)
            }

    }

}