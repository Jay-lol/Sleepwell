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
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.close_app.*
import kotlinx.android.synthetic.main.fragment_commu.*
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import practice.kotlin.com.sleepwell.recycler.CommentRecycler
import practice.kotlin.com.sleepwell.recycler.RecyclerImageTextAdapter
import practice.kotlin.com.sleepwell.sleepAndCommu.CalculSleepTime
import practice.kotlin.com.sleepwell.sleepAndCommu.CommentLoading
import practice.kotlin.com.sleepwell.sleepAndCommu.CommuLoading
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.cnt
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonArray
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonCommuArray
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.macAddress
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.commentActivityCnt
import practice.kotlin.com.sleepwell.statics.commuList.Companion.commentPosition
import practice.kotlin.com.sleepwell.statics.commuList.Companion.likeOrRecentOrHot
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.position
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

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
            likeOrRecentOrHot = 2
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
    fun submit(writer : String, password: String, writerTitle : String,  linkUrl : String, linkTitle : String, linkChannel : String, context: Activity, recycler: RecyclerView) {

        object : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg params: Void?): String? {

                val result = RetrofitCreator.retrofit
                    .sendBoardInfo(macAddress, linkUrl, linkChannel, linkTitle, writerTitle, writer, password)
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
                if (result != null) {
                    Log.d("submit test", result)
                }
                if (result == "null") {
                    context.progressBar3.visibility=View.GONE
                    context.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    context.toast("짧은 시간내에 많은 게시글 등록은 안됩니다!")
                } else {
                    context.toast("등록 완료!")
                    Log.d("Submit", "제출 완료")
                    mList.clear()
                    startThumnailLoading(0, recycler, 1, context)
                }
            }
        }.execute()
    } // end of submit

    fun startThumnailLoading(check: Int, recycler: RecyclerView?, submitFlag: Int?, context : Activity?) {
        var cnt2 = 0
        if(check==0&&(mList.isEmpty())) {
            Log.d("MLIST삭제요", " dd ")
//            mList.clear()
            position = 0
            cnt = 0
        }

        if(submitFlag==1){
            Log.d("Jay","새로운 영상등록")
//            mList.clear()
            position = 0
            cnt = 0
            cnt2 = 1
        }

        Log.d("보내는 포지션값", position.toString())
        RetrofitCreator.retrofit
            .getTotalUser(position, likeOrRecentOrHot)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("StartThumnailLoading1", it.toString())
                if(!it.get("data").isJsonNull) {
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
                        CommuLoading().refreshList(recycler, submitFlag)
                        if (context != null) {
                            context.progressBar3.visibility = View.GONE
                            context.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }
                        cnt++
                        position = 0
                    } else {
//                    mList.clear()
                        Log.d("포지션값", position.toString())

                        CommuLoading().refreshList(recycler, submitFlag)
                        if (context != null) {
                            context.progressBar3.visibility=View.GONE
                            context.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }
                    }
                } else{
                    jsonArray = JSONArray()
                    position -= 1
                    CommuLoading().refreshList(recycler, submitFlag)
                }
            })
            {
//                mList.clear()
//                jsonArray = JSONArray()
//                position -= 1
//                CommuLoading().refreshList(recycler, null)
                //서버에러
                it.message?.let { it1 -> Log.e("StartThumnailLoading", it1) }
            }
    }

    fun startCommentLoading(
        uid: Int?,
        imageView: ImageView,
        recycler: RecyclerView,
        commentRecyclerProgress: ProgressBar?, cnt : Int) {

        Log.d("commentPosition", commentPosition.toString())
        RetrofitCreator.retrofit
            .getComment(uid, commentPosition)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(commentActivityCnt==cnt){
                Log.d("StartCommentLoading", it.toString())
                if (!it.get("data").isJsonNull) {   // 비어있는 상태에서 댓글첫을때 처리해주기
                    val data = it.getAsJsonPrimitive("data").asString

                    imageView.setImageResource(0)

                    if (commentPosition == 0) {
                        RetrofitCreator.retrofit
                            .getBest(uid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                val best = it.getAsJsonArray("data")
                                Log.d("베스트", it.toString())
                                Log.d("startCommentLoading2 ", data)
                                cList.clear()
                                CommentLoading().getBestCm(best)

                                jsonCommuArray = JSONArray(data)


                                Log.d("dd", jsonCommuArray!!.getJSONObject(0).get("writter").toString())
                                CommentLoading().refreshCommentList(recycler, commentRecyclerProgress)
//                                if (it == null) {
//                                    imageView.setImageResource(R.drawable.no_comment)
//                                } else {
//                                    Log.d("TRUE FALSE", isCommentFirstLoading.toString())
//                                    if (isCommentFirstLoading) {        // 화면 가로로??
//                                        CommentLoading().refreshCommentList(recycler)
//                                        isCommentFirstLoading = !isCommentFirstLoading
//                                        imageView.setImageResource(0)
//                                    } else {
//                                        cList.clear()
//                                        CommentLoading().refreshCommentList(recycler)
//                                        imageView.setImageResource(0)
//                                    }
//
//                                }

                            }
                    } else {

//                jsonA = it.getAsJsonPrimitive("data")
//                rep = jsonA.toString()
//                rep = rep.substring(rep.indexOf("\"") + 1, rep.lastIndexOf("\""))
                        Log.d("startCommentLoading2 ", data)
//              "[{\"likecnt\":\"0\",\"writter\":\"익명\",\"id\":\"232\",\"replyContent\":\" test \",\"rid\":\"5\",\"firecnt\":\"0\"}," +
//                        "{\"likecnt\":0,\"writter\":\"익명\",\"id\":\"232\",\"replyContent\":\" test2 \",\"rid\":\"6\",\"firecnt\":\"0\"}]"
//                rep = rep.replace("\\\"", "\"")
//                rep = rep.replace("\\\"", "\"")
//              "[{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":5,\\\"firecnt\\\":0},{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":6,\\\"firecnt\\\":0},{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":7,\\\"firecnt\\\":0},{\\\"likecnt\\\":0,\\\"writter\\\":\\\"익명\\\",\\\"id\\\":232,\\\"replyContent\\\":\\\" test \\\",\\\"rid\\\":8,\\\"firecnt\\\":0}]\n"

                        jsonCommuArray = JSONArray(data)

                        Log.d("dd", jsonCommuArray!!.getJSONObject(0).get("writter").toString())
                        CommentLoading().refreshCommentList(recycler, commentRecyclerProgress)
//                        if (it == null) {       //화면 가로로될때 중복로딩방지???
//                            imageView.setImageResource(R.drawable.no_comment)
//                        } else {
//                            if (isCommentFirstLoading) {
//                                CommentLoading().refreshCommentList(recycler)
//                                isCommentFirstLoading = !isCommentFirstLoading
//                                imageView.setImageResource(0)
//                            } else {
//                                cList.clear()
//                                CommentLoading().refreshCommentList(recycler)
//                                imageView.setImageResource(0)
//                            }
//
//                        }

                    }
                }else {
                    if(commentPosition==0) {
                        imageView.setImageResource(R.drawable.no_comment)
                        cList.clear()
                        commentPosition -= 1
                        jsonCommuArray = JSONArray()
                        CommentLoading().refreshCommentList(recycler, commentRecyclerProgress)
                        Log.d("Comment", "Comment is Null")
                    }
                    if (commentRecyclerProgress != null) {
                        commentRecyclerProgress.visibility = View.GONE
                    }
                }
            }})
            {
//                imageView.setImageResource(R.drawable.no_comment)
//                cList.clear()
//                jsonCommuArray = JSONArray()
//                CommentLoading().refreshCommentList(recycler)
                // 서버 에러
                it.message?.let { it1 -> Log.e("CommentLoading", it1) }
            }
    }

    fun sendLike(id: Int?, context: Context, holder: RecyclerImageTextAdapter.mViewH) {

        RetrofitCreator.retrofit
            .sendLikeButton(id, macAddress)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("like", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        context.toast("이미 투표하셨습니다!!")
                    } else {
                        holder.likeNumber.text = (holder.likeNumber.text.toString().toInt() + 1).toString()
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

        RetrofitCreator.retrofit
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

        RetrofitCreator.retrofit
            .sendComment( id ,  writer, content, macAddress, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val re = response.body()?.string().toString()
                    var b : JSONObject? = null
                    try {
                        val a = JSONObject(re)
                        b = a
                        Log.d("SEND", a.getInt("status").toString())
                    }catch (e : JSONException){
                        Log.d("ss", e.toString())
                    }

                    //중복 처리
                    if (re == "null") {
                        context?.toast("짧은 시간내에 많은 댓글 등록은 안됩니다!")
                    } else if( b?.getInt("status") == 600){
                        context?.toast("등록할 수 없습니다")
                    } else {
                        context?.toast("등록 완료!")
                        commentPosition = 0
                        startCommentLoading(id, imageView, recycler, null, commentActivityCnt)
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

        RetrofitCreator.retrofit
            .sendReComment(rootid, id, writer, content, macAddress, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    val re = response.body()?.string().toString()
                    var b : JSONObject? = null
                    try {
                        val a = JSONObject(re)
                        b = a
                        Log.d("SEND", a.getInt("status").toString())
                    }catch (e : JSONException){
                        Log.d("ss", e.toString())
                    }

                    Log.d("sendReComment", re)
                    //중복 처리
                    if (re == "null") {
                        context?.toast("짧은 시간내에 많은 댓글 등록은 안됩니다!")
                    } else if( b?.getInt("status") == 600){
                        context?.toast("등록할 수 없습니다")
                    }else {
                        context?.toast("등록 완료!")
                        commentPosition = 0
                        startCommentLoading(rootid, imageView, recycler, null, commentActivityCnt)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("sendReCommentF", t.message.toString())
                }
            })

    }

    fun sendCommentLike(id: Int, context: Context, holder: CommentRecycler.cViewH) {

        RetrofitCreator.retrofit
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

        RetrofitCreator.retrofit
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
        RetrofitCreator.retrofit
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
                        commentPosition = 0
                        startCommentLoading(contentUid, imageView, recycler, null, commentActivityCnt)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DeleteCommentF", t.message.toString())
                }
            })

    }

    fun deleteReComment(contentUid : Int, rrid : Int, password : String, context: Context, recycler: RecyclerView, imageView: ImageView){
        Log.d("Re DELETE", password)
        RetrofitCreator.retrofit
            .deleteReComment(rrid, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("DeleteReComment", response.body().toString())
                    if (response.body().toString() == "null") {
                        Log.d("whats wrong?", response.body().toString())
                        context.toast("비밀번호가 다릅니다")
                    } else {
                        commentPosition = 0
                        startCommentLoading(contentUid, imageView, recycler, null,commentActivityCnt)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DeleteReCommentF", t.message.toString())
                }
            })

    }

    fun fireBoard(rid : Int, context: Context){
        Log.d("FrieBoard", "Fire")
        RetrofitCreator.retrofit
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
        RetrofitCreator.retrofit
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
        RetrofitCreator.retrofit
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

    fun deleteBoard(id : Int, password : String, context: Activity, recycler: RecyclerView, position : Int, idx : Int){
        Log.d("DELETE", macAddress)
        RetrofitCreator.retrofit
            .deleteBoard(id, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("DeleteComment", response.body().toString())
                    //중복 처리
                    if (response.body().toString() == "null") {
                        Log.d("whats wrong?", response.body().toString())
                        context.progressBar3.visibility=View.GONE
                        context.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        context.toast("비밀번호가 다릅니다")
                    } else {
                        context.toast("삭제 완료!")
                        mList.removeAt(idx)
                        recycler.adapter?.notifyItemRemoved(position)

                        context.progressBar3.visibility=View.GONE
                        context.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

//                        startThumnailLoading(cnt, recycler, null)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DeleteCommentF", t.message.toString())
                }
            })

    }

}