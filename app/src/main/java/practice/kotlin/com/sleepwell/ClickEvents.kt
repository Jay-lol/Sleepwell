package practice.kotlin.com.sleepwell

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commu.*
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import practice.kotlin.com.sleepwell.recycler.LIKE
import practice.kotlin.com.sleepwell.recycler.RecyclerImageTextAdapter
import practice.kotlin.com.sleepwell.retrofit.RetrofitCreator
import practice.kotlin.com.sleepwell.sleepAndCommu.CommuLoading
import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.statics.commuList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ClickEvents {
    fun cal(v: View, context: Activity) {
        when (v.id) {
            R.id.cal_now -> calNow(v, context)
            R.id.cal_future -> calPast(v, context)
            R.id.cal_past -> calFuture(v, context)
            R.id.text_commu -> submit(v, context)
            R.id.refreshButton -> startRefresh()
        }
    }

    private fun startRefresh() {
        ClickEvents().StartThumnailLoading(JsonString.cnt)
    }

    private fun calNow(v: View, context: Context) {
        val showTime = AlertDialog.Builder(context)
            .setTitle("지금 잠들면....")
            .setMessage("7:00AM 에 일어나면 좋습니다")
        showTime.show()
    }

    private fun calFuture(v: View, context: Context) {
        val showTime = AlertDialog.Builder(context)
            .setTitle("그때 잠들면....")
            .setMessage("7:00AM 에 일어나면 좋습니다")
        showTime.show()
    }

    private fun calPast(v: View, context: Context) {
        val showTime = AlertDialog.Builder(context)
            .setTitle("그때 일어나려면....")
            .setMessage("10:00PM 에 잠들면 좋습니다")
        showTime.show()
    }

    @SuppressLint("StaticFieldLeak")
    private fun submit(view: View, context: Activity) {

        val hi = context.submit_area.text.toString()
        val editor = "작성자"
        object : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg params: Void?): String? {

                val result = RetrofitCreator.defaultRetrofit()
                    .sendBoardInfo("${JsonString.macAddress}", "$hi", "영상제목", "$editor")

                try {
                    return result.execute().body().toString()
                } catch (e: IOException) {
                    Log.e("submit error ", e.message.toString())
                }
                return null
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d("submit test", result)
                Log.d("Submit", "제출 완료")
                startRefresh()
            }
        }.execute()
    } // end of submit

    fun StartThumnailLoading(check: Int) {

        val retrofitService = RetrofitCreator.defaultRetrofit()
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
                if (check == 0) {
                    CommuLoading().refreshList()
                    JsonString.cnt++
                } else {
                    commuList.mList.clear()
                    CommuLoading().refreshList()
                }
            })
            {
                Log.e("Error2", it.message)
            }
    }

    fun sendLike(id: Int?, context: Context , holder : RecyclerImageTextAdapter.mViewH): Int { // 중복아니면 0 , 중복이면 1

        var result = 0
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.nodap.xyz/user/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(apiService::class.java)
        val testcall = server.sendLikeButton(id, "${JsonString.macAddress}")
        Log.d("send Ip  :" , "${JsonString.macAddress}")
        testcall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("test2", response?.body().toString())
                //중복 처리
                if (response.body().toString()== "null"){
                    result = 1
                    context.toast("이미 투표하셨습니다!!")
                    holder.likeNumber.setText((holder.likeNumber.text.toString().toInt() - 1).toString())
                }
                else
                    Log.d("Print", "좋아요")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("error", t.message.toString())
            }
        })
        return result
    }

    fun sendDislike(id: Int?, context: Context, holder : RecyclerImageTextAdapter.mViewH): Int {

        var result = 0
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.nodap.xyz/user/dislike/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(apiService::class.java)
        val testcall = server.sendLikeButton(id, "${JsonString.macAddress}")
        testcall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("test2", response?.body().toString())
                //중복 처리
                if (response?.body().toString() == "1")
                    result = 1
                Log.d("Print", "싫어요")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("error", t.message.toString())
            }
        })
        return result
    }

}