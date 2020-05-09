package practice.kotlin.com.sleepwell

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.submit_dialog.*
import okhttp3.ResponseBody
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import org.json.JSONArray
import practice.kotlin.com.sleepwell.recycler.CommentRecycler
import practice.kotlin.com.sleepwell.recycler.RecyclerImageTextAdapter
import practice.kotlin.com.sleepwell.retrofit.RetrofitCreator
import practice.kotlin.com.sleepwell.sleepAndCommu.CalculSleepTime
import practice.kotlin.com.sleepwell.sleepAndCommu.CommentLoading
import practice.kotlin.com.sleepwell.sleepAndCommu.CommuLoading
import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonCommuArray
import practice.kotlin.com.sleepwell.statics.commuList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ClickEvents {
    fun cal(v: View, context: Activity, dlg: Dialog?) {
        when (v.id) {
            R.id.cal_now -> calNow(context)
            R.id.cal_Specific -> calSpecific(context)
            R.id.cal_future -> calFuture(context)
            R.id.refreshButton -> startRefresh()
            R.id.okButton -> submit(context, dlg)
        }
    }

    fun backPress(context: Activity) {
        context.alert("앱을 종료하시겠습니까??", "종료") {
            yesButton {
                context.finishAffinity()
            }
            noButton {

            }
        }.show()
    }

    private fun startRefresh() {
        ClickEvents().StartThumnailLoading(JsonString.cnt)
    }

    private fun calNow(context: Context) {

        val showTime = AlertDialog.Builder(context)
            .setTitle("지금 잠들면....")
            .setMessage(CalculSleepTime().calSleep())
        showTime.show()
    }

    private fun calSpecific(context: Context) {
        val timePickerDialog =
            TimePickerDialog(
                context, android.R.style.Theme_Holo_Light_Dialog,
                OnTimeSetListener { _, hourOfDay, minute ->
                    val showTime = AlertDialog.Builder(context)
                        .setTitle("그때 일어나려면....")
                        .setMessage(CalculSleepTime().calSleepSpecific(hourOfDay, minute))
                    showTime.show()
                }
                , 12, 0, false
            )
        timePickerDialog.show()
    }

    private fun calFuture(context: Context) {

        val timePickerDialog =
            TimePickerDialog(
                context, android.R.style.Theme_Holo_Light_Dialog,
                OnTimeSetListener { view, hourOfDay, minute ->
                    val showTime = AlertDialog.Builder(context)
                        .setTitle("그때 잠들면....")
                        .setMessage(CalculSleepTime().calSleepFuture(hourOfDay, minute))
                    showTime.show()
                }
                , 12, 0, false
            )
        timePickerDialog.show()

    }

    @SuppressLint("StaticFieldLeak")
    private fun submit(context: Activity, dialog: Dialog?) {

        val linkUrl = dialog?.linkSubmit?.text.toString()

        val title: String
        if (dialog?.titleSubmit?.text.toString() == "") {
            title = "null@title"
        } else {
            title = dialog?.titleSubmit?.text.toString()
        }

        val editor: String
        if (dialog?.writerSubmit?.text.toString() == "") {
            editor = "익명"
        } else {
            editor = dialog?.writerSubmit?.text.toString()
        }

        val password: String?
        if (dialog?.passwordSubmit?.text.toString() == "") {
            password = JsonString.macAddress
        } else {
            password = dialog?.passwordSubmit?.text.toString()
        }

        object : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg params: Void?): String? {

                val result = RetrofitCreator.defaultRetrofit()
                    .sendBoardInfo("${JsonString.macAddress}", "$linkUrl", "$title", editor, "$password")

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
                if (result == "null") {
                    context.toast("짧은 시간내에 많은 게시글 등록은 안됩니다!")
                } else {
                    context.toast("등록 완료!")
                    Log.d("Submit", "제출 완료")
                    startRefresh()
                }
            }
        }.execute()
    } // end of submit

    fun StartThumnailLoading(check: Int) {

        RetrofitCreator.defaultRetrofit()
            .getTotalUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("StartThumnailLoading", it.toString())
                JsonString.jsonArray = it.getAsJsonArray("data")
                Log.d("sdsd", JsonString.jsonArray.toString())
                if (check == 0) {
                    CommuLoading().refreshList()
                    JsonString.cnt++
                } else {
                    commuList.mList.clear()
                    CommuLoading().refreshList()
                }
            })
            {
                Log.e("StartThumnailLoading", it.message)
            }
    }

    fun StartCommentLoading(uid: Int?, imageView: ImageView, recycler : RecyclerView) {
        var rep : String
        RetrofitCreator.defaultRetrofit()
            .getComment(uid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("StartCommentLoading", it.toString())
//                JsonString.cJsonArray = it.getAsJsonArray("data")
                JsonString.jsonA = it.getAsJsonPrimitive("data")
                Log.d("목화 ", JsonString.jsonA.toString())

                rep = JsonString.jsonA.toString()
                rep = rep.substring(rep.indexOf("\"")+1, rep.lastIndexOf("\""))
                Log.d("목화1 ", rep)
//                val test = "[{\"likecnt\":\"0\",\"writter\":\"익명\",\"id\":\"232\",\"replyContent\":\" test \",\"rid\":\"5\",\"firecnt\":\"0\"}," +
//                        "{\"likecnt\":0,\"writter\":\"익명\",\"id\":\"232\",\"replyContent\":\" test2 \",\"rid\":\"6\",\"firecnt\":\"0\"}]"
                rep = rep.replace("\\\"", "\"")
//                val test2 = "[{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":5,\\\"firecnt\\\":0},{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":6,\\\"firecnt\\\":0},{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":7,\\\"firecnt\\\":0},{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":8,\\\"firecnt\\\":0}]\n"

                jsonCommuArray = JSONArray(rep)

//                Log.d("dd", jsonCommuArray!!.getJSONObject(1).get("writter").toString())

                if (it == null) {
                    imageView.setImageResource(R.drawable.no_comment)
                } else {
                    CommentLoading().refreshCommuList(recycler)
                    imageView.setImageResource(0)
                }
            })
            {
                imageView.setImageResource(R.drawable.no_comment)
                Log.e("CommentLoading", it.message)
            }
    }

    fun sendLike(id: Int?, context: Context, holder: RecyclerImageTextAdapter.mViewH) {

        RetrofitCreator.defaultRetrofit()
            .sendLikeButton(id, "${JsonString.macAddress}")
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("like", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        context.toast("이미 투표하셨습니다!!")
                    } else {
                        holder.likeNumber.setText((holder.likeNumber.text.toString().toInt() + 1).toString())
                        context.toast("좋아요!")
                        Log.d("Print", "좋아요")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("sendLike", t.message.toString())
                }
            })
    }

    fun sendDislike(id: Int?, context: Context, holder: RecyclerImageTextAdapter.mViewH) {

        RetrofitCreator.defaultRetrofit()
            .sendDisLikeButton(id, "${JsonString.macAddress}")
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("dislike", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        context.toast("이미 투표하셨습니다!!")
                    } else {
                        holder.likeNumber.setText((holder.likeNumber.text.toString().toInt() - 1).toString())
                        context.toast("싫어요!")
                        Log.d("Print", "싫어요")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("sendDislike", t.message.toString())
                }
            })

    }

    fun sendCommentF(id: Int?, content : String ,context: Context?, recycler: RecyclerView ,imageView: ImageView ) {

        var editor: String? = null
        if (editor == null) {
            editor = "익명"
        } else {
            editor = "사용자"
        }

        var password: String? = null
        if (password == null) {
            password = JsonString.macAddress
        } else {
            password = "1234"
        }
        RetrofitCreator.defaultRetrofit()
            .sendComment(id, editor, content, "${JsonString.macAddress}", password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("sendComment", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        context?.toast("짧은 시간내에 많은 댓글 등록은 안됩니다!")
                    } else {
                        StartCommentLoading(id,imageView,recycler)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("sendCommentF", t.message.toString())
                }
            })

    }

}