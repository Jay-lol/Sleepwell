package practice.kotlin.com.sleepwell.sleepAndCommu

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import practice.kotlin.com.sleepwell.recycler.CommentItem
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.isCommentFirstLoading
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonCommuArray
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList

class CommentLoading {
    var cnt = 0

    fun refreshCommentList(
        recycler: RecyclerView,
        commentRecyclerProgress: ProgressBar?) {

        var end = jsonCommuArray!!.length()
        aLoop@for (i in 0 until (end-1)) {
            if(isCommentFirstLoading) {
                addItem(
                    jsonCommuArray!!.getJSONObject(i).get("rid") as Int,
                    jsonCommuArray!!.getJSONObject(i).get("replyContent").toString(),
                    jsonCommuArray!!.getJSONObject(i).get("writter").toString(),
                    jsonCommuArray!!.getJSONObject(i).get("likecnt").toString(),
                    null,
                    0
                )

                if (jsonCommuArray!!.getJSONObject(i).optString("대댓글", "null") != "null") {
                    getReply(i)
                }
            } else {
                Log.d("로딩중지", "로딩중지")
                break@aLoop}
        }
        if (commentRecyclerProgress != null) {
            commentRecyclerProgress.visibility = View.GONE
        }
        if(isCommentFirstLoading)
            recycler.adapter?.notifyDataSetChanged()
    }

    private fun getReply(i : Int) {

        var reComment = jsonCommuArray!!.getJSONObject(i).getJSONArray("대댓글")

        val end = reComment.length()

        bLoop@for(j in 0 until end) {
            if(isCommentFirstLoading) {
            addItem(
                reComment.getJSONObject(j).get("rid") as Int,
                reComment.getJSONObject(j).get("reReplyContent").toString(),
                reComment.getJSONObject(j).get("writer").toString(),
                reComment.getJSONObject(j).get("likeCount").toString(),
                reComment.getJSONObject(j).get("rrid") as Int,
                1
            )} else {
                Log.d("로딩중지", "로딩중지")
                break@bLoop}

        }
    }

    fun getBestCm(best: JsonArray) {
        cLoop@for(i  in 0 until best.size()) {
            if (isCommentFirstLoading) {
                var item = CommentItem()
                item.isBest = true
                item.cWriter = best[i].asJsonObject.get("writer").asString
                item.likeNumber = best[i].asJsonObject.get("likeCount").toString()
                item.replycontent = best[i].asJsonObject.get("content").asString
                if (best[i].asJsonObject.get("replyCheck").asBoolean) {
                    item.rid = best[i].asJsonObject.get("numRid").toString().toInt()
                    item.isRere = false
                } else {
                    item.rrid = best[i].asJsonObject.get("numRid").toString().toInt()
                    item.isRere = true
                }
                Log.d("BEst item ", item.toString())
                cList.add(item)
                Log.d("BEst size ", cList.size.toString())
            }else {
                Log.d("로딩중지", "로딩중지")
                break@cLoop}
        }
    }

    private fun addItem(rid: Int, content: String, writer: String, likecnt: String, rrid : Int? , reply: Int) {

        cnt++   // 게시글의 댓글개수

        var item = CommentItem()
        item.rid = rid
        item.cWriter = writer
        item.replycontent = content
        item.likeNumber = likecnt

        when(reply){
            0 -> item.isRere = false
            1 -> {item.isRere = true
                if (rrid != null) {
                    item.rrid = rrid
                }
            }
        }

        cList.add(item)

    }
}