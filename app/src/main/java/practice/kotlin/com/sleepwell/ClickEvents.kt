package practice.kotlin.com.sleepwell

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.close_app.*
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import org.json.JSONArray
import practice.kotlin.com.sleepwell.recycler.CommentRecycler
import practice.kotlin.com.sleepwell.recycler.RecyclerImageTextAdapter
import practice.kotlin.com.sleepwell.retrofit.RetrofitCreator
import practice.kotlin.com.sleepwell.sleepAndCommu.CalculSleepTime
import practice.kotlin.com.sleepwell.sleepAndCommu.CommentLoading
import practice.kotlin.com.sleepwell.sleepAndCommu.CommuLoading
import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.cnt
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.isCommentFirstLoading
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonCommuArray
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.macAddress
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonArray
import practice.kotlin.com.sleepwell.statics.commuList.Companion.likeOrRecent
import practice.kotlin.com.sleepwell.statics.commuList.Companion.position

class ClickEvents {
    fun cal(v: View, context: Activity, recycler: RecyclerView?) {
        when (v.id) {
            R.id.cal_now -> calNow(context)
            R.id.cal_Specific -> calSpecific(context)
            R.id.cal_future -> calFuture(context)
        }
    }

    fun backPress(context: Activity) {

        val dlg = Dialog(context)


        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.close_app)

        // 커스텀 다이얼로그를 노출한다.
        dlg.show()
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        dlg.closeOkButton.setOnClickListener {
            context.finishAffinity()
            dlg.dismiss()
        }

        dlg.closeNotOk.setOnClickListener{
            dlg.dismiss()
        }
    }

    private fun calNow(context: Context) {

        val showTime = AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
            .setTitle("지금 잠들면....")
            .setMessage(CalculSleepTime().calSleep())
            .create()

        showTime.window!!.setGravity(Gravity.CENTER)
        showTime.show()
    }

    private fun calSpecific(context: Context) {
        val timePickerDialog =
            TimePickerDialog(
                context, android.R.style.Theme_DeviceDefault_Dialog_Alert,
                OnTimeSetListener { _, hourOfDay, minute ->
                    val showTime = AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                        .setTitle("그때 일어나려면....")
                        .setMessage(CalculSleepTime().calSleepSpecific(hourOfDay, minute))
                        .create()

                    showTime.window?.setGravity(Gravity.CENTER)
                    showTime.show()
                }
                , 12, 0, false
            )
        timePickerDialog.window?.setGravity(Gravity.CENTER)
        timePickerDialog.show()
    }

    private fun calFuture(context: Context) {

        val timePickerDialog =
            TimePickerDialog(
                context, android.R.style.Theme_DeviceDefault_Dialog_Alert,
                OnTimeSetListener { view, hourOfDay, minute ->
                    val showTime = AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                        .setTitle("그때 잠들면....")
                        .setMessage(CalculSleepTime().calSleepFuture(hourOfDay, minute))
                        .create()

                    showTime.window?.setGravity(Gravity.CENTER)
                    showTime.show()
                }
                , 12, 0, false
            )

        timePickerDialog.window?.setGravity(Gravity.CENTER)
        timePickerDialog.show()

    }

    @SuppressLint("StaticFieldLeak")
    fun submit(writer : String, password: String, writerTitle : String,  linkUrl : String, context: Activity, recycler: RecyclerView) {


        object : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg params: Void?): String? {

                val result = RetrofitCreator.defaultRetrofit()
                    .sendBoardInfo(macAddress, linkUrl, writerTitle, writer, password)
                Log.d("무슨값", macAddress +"\n"+ linkUrl +"\n"+ writerTitle +"\n"+ writer +"\n"+ password)
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
                    startThumnailLoading(cnt, recycler, 1)
                }
            }
        }.execute()
    } // end of submit

    fun startThumnailLoading(check: Int, recycler: RecyclerView?, submitFlag: Int?) {

        var rep: String

        if(check==0&&(!mList.isEmpty())) {
            mList.clear()
            position = 0
            cnt = 0
        }

        if(submitFlag==1){
            mList.clear()
            position = 0
            cnt = 0
        }

        Log.d("보내는 포지션값", position.toString())
        RetrofitCreator.defaultRetrofit()
            .getTotalUser(position, likeOrRecent)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("StartThumnailLoading1", it.toString())

//                jsonP = it.getAsJsonPrimitive("data").asString
                val data = it.getAsJsonPrimitive("data").asString
//                Log.d("StartThumnailLoading2", data.substring(9000))
//                rep = rep.substring(rep.indexOf("\"") + 1, rep.lastIndexOf("\""))
//                rep = rep.replace("\\\"", "\"")
//                rep = rep.replace("\\\"", "\"")
//                Log.d("StartThumnailLoading3", data.substring(9300))

                jsonArray = JSONArray(data)

//                Log.d("sdsd", jsonArray.toString().substring(9200))
                Log.d("StartThumnailLoading4", jsonArray.toString())
                if (check == 0) {
                        CommuLoading().refreshList(recycler, null)
                        cnt++
                        position = 0
                } else {
//                    mList.clear()
                    Log.d("포지션값", position.toString())

                    CommuLoading().refreshList(recycler, null)

                }
            })
            {
//                mList.clear()
                jsonArray = JSONArray()
                position -= 1

                    CommuLoading().refreshList(recycler, null)

                Log.e("StartThumnailLoading", it.message)
            }
    }

    fun startCommentLoading(uid: Int?, imageView: ImageView, recycler: RecyclerView) {
        var rep: String
        RetrofitCreator.defaultRetrofit()
            .getComment(uid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("StartCommentLoading", it.toString())
                if(!it.get("data").isJsonNull) {
                    val data = it.getAsJsonPrimitive("data").asString
//                jsonA = it.getAsJsonPrimitive("data")

//                rep = jsonA.toString()
//                rep = rep.substring(rep.indexOf("\"") + 1, rep.lastIndexOf("\""))
                    Log.d("목화1 ", data)
//              "[{\"likecnt\":\"0\",\"writter\":\"익명\",\"id\":\"232\",\"replyContent\":\" test \",\"rid\":\"5\",\"firecnt\":\"0\"}," +
//                        "{\"likecnt\":0,\"writter\":\"익명\",\"id\":\"232\",\"replyContent\":\" test2 \",\"rid\":\"6\",\"firecnt\":\"0\"}]"
//                rep = rep.replace("\\\"", "\"")
//                rep = rep.replace("\\\"", "\"")
//              "[{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":5,\\\"firecnt\\\":0},{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":6,\\\"firecnt\\\":0},{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":7,\\\"firecnt\\\":0},{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":8,\\\"firecnt\\\":0}]\n"

                    jsonCommuArray = JSONArray(data)

                    Log.d("dd", jsonCommuArray!!.getJSONObject(0).get("writter").toString())

                    if (it == null) {
                        imageView.setImageResource(R.drawable.no_comment)
                    } else {
                        if (isCommentFirstLoading) {
                            CommentLoading().refreshCommentList(recycler)
                            isCommentFirstLoading = !isCommentFirstLoading
                            imageView.setImageResource(0)
                        } else {
                            cList.clear()
                            CommentLoading().refreshCommentList(recycler)
                            imageView.setImageResource(0)
                        }

                    }
                }
                else {
                    imageView.setImageResource(R.drawable.no_comment)
                    cList.clear()
                    jsonCommuArray = JSONArray()
                    CommentLoading().refreshCommentList(recycler)
                    Log.d("Comment", "Comment is Null")
                }
            })
            {
                imageView.setImageResource(R.drawable.no_comment)
                cList.clear()
                jsonCommuArray = JSONArray()
                CommentLoading().refreshCommentList(recycler)
                Log.e("CommentLoading", it.message)
            }
    }

    fun sendLike(id: Int?, context: Context, holder: RecyclerImageTextAdapter.mViewH) {

        RetrofitCreator.defaultRetrofit()
            .sendLikeButton(id, macAddress)
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
            .sendDisLikeButton(id, macAddress)
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

    fun sendCommentF(id: Int, writer : String, password: String, content: String, context: Context?, recycler: RecyclerView, imageView: ImageView) {
        Log.d("sendcomment", "$id")

        RetrofitCreator.defaultRetrofit()
            .sendComment( id ,  writer, content, macAddress, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("sendComment", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        context?.toast("짧은 시간내에 많은 댓글 등록은 안됩니다!")
                    } else {
                        context?.toast("등록 완료!")
                        startCommentLoading(id, imageView, recycler)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("sendCommentF", t.message.toString())
                }
            })

    }

    fun sendReCommentF(
        rootid: Int,
        id: Int?, writer : String, password: String,
        content: String,
        context: Context?,
        recycler: RecyclerView,
        imageView: ImageView) {

        RetrofitCreator.defaultRetrofit()
            .sendReComment(rootid, id, writer, content, macAddress, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("sendReComment", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        context?.toast("짧은 시간내에 많은 댓글 등록은 안됩니다!")
                    } else {
                        context?.toast("등록 완료!")
                        startCommentLoading(rootid, imageView, recycler)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("sendReCommentF", t.message.toString())
                }
            })

    }

    fun sendCommentLike(id: Int, context: Context, holder: CommentRecycler.cViewH) {

        RetrofitCreator.defaultRetrofit()
            .sendReplyLike(id, "$macAddress")
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("Commentlike", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        context.toast("이미 투표하셨습니다!!")
                    } else {
                        if (!TextUtils.isEmpty(holder.likecnt.text)) {
                            holder.likecnt.text = (holder.likecnt.text.toString().toInt() + 1).toString()
                        } else {
                            holder.likecnt.text = "1"
                        }
                        context.toast("좋아요!")
                        Log.d("Print", "좋아요")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("sendCommentLike", t.message.toString())
                }
            })
    }

    fun sendReCommentLike(id: Int, context: Context, holder: CommentRecycler.cViewH) {

        RetrofitCreator.defaultRetrofit()
            .sendRereplyLike(id, "$macAddress")
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("ReCommentlike", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        context.toast("이미 투표하셨습니다!!")
                    } else {
                        if (!TextUtils.isEmpty(holder.likecnt.text)) {
                            holder.likecnt.text = (holder.likecnt.text.toString().toInt() + 1).toString()
                        } else {
                            holder.likecnt.text = "1"
                        }
                        context.toast("좋아요!")
                        Log.d("Print", "좋아요")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("ResendCommentLike", t.message.toString())
                }
            })
    }

    fun deleteComment(contentUid : Int, rid : Int, password : String, context: Context, recycler: RecyclerView, imageView: ImageView){
        Log.d("DELETE", password)
        RetrofitCreator.defaultRetrofit()
            .deleteComment(rid, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("DeleteComment", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        Log.d("whats wrong?", response.body().toString())
                        context.toast("비밀번호가 다릅니다")
                    } else {
                        context.toast("삭제 완료!")
                        startCommentLoading(contentUid, imageView, recycler)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DeleteCommentF", t.message.toString())
                }
            })

    }

    fun deleteReComment(contentUid : Int, rrid : Int, password : String, context: Context, recycler: RecyclerView, imageView: ImageView){
        Log.d("Re DELETE", password)
        RetrofitCreator.defaultRetrofit()
            .deleteReComment(rrid, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("DeleteReComment", response.body().toString())
                    if (response.body().toString() == "null") {
                        Log.d("whats wrong?", response.body().toString())
                        context.toast("비밀번호가 다릅니다")
                    } else {
                        startCommentLoading(contentUid, imageView, recycler)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DeleteReCommentF", t.message.toString())
                }
            })

    }

    fun fireBoard(rid : Int, context: Context){
        Log.d("FrieBoard", "Fire")
        RetrofitCreator.defaultRetrofit()
            .fireBoard(rid, macAddress)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("FrieBoard", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        Log.d("whats wrong?", response.body().toString())
                        context.toast("이미 신고하셨습니다..")
                    } else {
                        context.toast("신고 완료!")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("FrieBoard Error", t.message.toString())
                }
            })

    }

    fun fireComment(rid : Int, context: Context){
        Log.d("Fire", "Fire")
        RetrofitCreator.defaultRetrofit()
            .fireComment(rid, macAddress)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("fireComment", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        Log.d("whats wrong?", response.body().toString())
                        context.toast("이미 신고하셨습니다..")
                    } else {
                        context.toast("신고 완료!")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("FrieComment Error", t.message.toString())
                }
            })

    }

    fun fireReComment(rid : Int, context: Context){
        Log.d("Fire Re", "Fire")
        RetrofitCreator.defaultRetrofit()
            .fireReComment(rid, macAddress)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("fireReComment", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        Log.d("whats wrong?", response.body().toString())
                        context.toast("이미 신고하셨습니다..")
                    } else {
                        context.toast("신고 완료!")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("FrieReComment Error", t.message.toString())
                }
            })

    }

    fun deleteBoard(id : Int, password : String, context: Context, recycler: RecyclerView){
        Log.d("DELETE", macAddress)
        RetrofitCreator.defaultRetrofit()
            .deleteBoard(id, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("DeleteComment", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        Log.d("whats wrong?", response.body().toString())
                        context.toast("비밀번호가 다릅니다")
                    } else {
                        context.toast("삭제 완료!")
                        startThumnailLoading(cnt, recycler, null)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DeleteCommentF", t.message.toString())
                }
            })

    }

}