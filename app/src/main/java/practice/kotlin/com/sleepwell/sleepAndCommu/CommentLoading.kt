package practice.kotlin.com.sleepwell.sleepAndCommu

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import practice.kotlin.com.sleepwell.recycler.CommentItem
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonCommuArray
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList
import java.util.*

class CommentLoading {
    var cnt = 0
    fun refreshCommentList(recycler: RecyclerView) {

        var end = jsonCommuArray!!.length()
        for (i in 0 until (end-1)) {
            addItem(
                jsonCommuArray!!.getJSONObject(i).get("rid") as Int,
                jsonCommuArray!!.getJSONObject(i).get("replyContent").toString(),
                jsonCommuArray!!.getJSONObject(i).get("writter").toString(),
                jsonCommuArray!!.getJSONObject(i).get("likecnt").toString(),
                null,
                 0)

            if(jsonCommuArray!!.getJSONObject(i).optString("대댓글","null")!= "null"){
                getReply(i)
            }

        }

        recycler.adapter?.notifyDataSetChanged()
    }

    private fun getReply(i : Int) {

        var reComment = jsonCommuArray!!.getJSONObject(i).getJSONArray("대댓글")

        val end = reComment.length()

        for(j in 0 until end)
            addItem(
                reComment.getJSONObject(j).get("rid") as Int, reComment.getJSONObject(j).get("reReplyContent").toString(),
                reComment.getJSONObject(j).get("writer").toString(),
                reComment.getJSONObject(j).get("likeCount").toString(),
                reComment.getJSONObject(j).get("rrid") as Int,
                1
            )
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