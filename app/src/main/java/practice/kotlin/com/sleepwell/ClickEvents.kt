package practice.kotlin.com.sleepwell

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commu.*
import practice.kotlin.com.sleepwell.fragment.CommuLoading
import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.statics.commuList

class ClickEvents {
    fun cal(v: View, context : Activity){
        when(v.id){
            R.id.cal_now -> calNow(v,context)
            R.id.cal_future -> calPast(v , context)
            R.id.cal_past -> calFuture(v , context)
            R.id.text_commu -> submit(v,context)
            R.id.refreshButton -> startRefresh()
        }
    }

    private fun startRefresh() {
        ClickEvents().StartThumnailLoading(JsonString.cnt)
    }

    private fun submit(v: View, context: Activity){
        RetrofitService().callBackPost(context.submit_area.text.toString())
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

    fun StartThumnailLoading(check : Int){

        val retrofitService = RetrofitService().callBackGet()
        retrofitService.getTotalUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.subscribe({ it ->
                Log.d("content", it.toString())
                Log.d("message", it.getAsJsonPrimitive("message").asString)
                Log.d("status", it.getAsJsonPrimitive("status").asString)
                Log.d("status", it.getAsJsonPrimitive("data").asString)}) // data는 array라 안됨 */
            .subscribe({
                Log.d("content", it.toString())
                JsonString.jsonArray = it.getAsJsonArray("data")
                if(check == 0) {
                    CommuLoading().refreshList()
                    JsonString.cnt++
                }
                else{
                    commuList.mList.clear()
                    CommuLoading().refreshList()
                }

               /* it.getAsJsonPrimitive("message").asString
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
                    .asJsonObject.get("linkChannel").asString)*/

            })
            {
                Log.e("Error", it.message)
            }

    }

}