package practice.kotlin.com.sleepwell.sleepAndCommu

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_comment.*
import org.json.JSONArray
import practice.kotlin.com.sleepwell.recycler.CommentItem
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonCommuArray
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList

class CommentLoading {
    var cnt = 0
    fun refreshCommuList(recycler: RecyclerView) {

        var end = jsonCommuArray!!.length()
        for (i in 0 until end) {
            addItem(
                jsonCommuArray!!.getJSONObject(i).get("rid").toString().toInt(),
                jsonCommuArray!!.getJSONObject(i).get("replyContent").toString(),
                jsonCommuArray!!.getJSONObject(i).get("writter").toString()
                , 0)

            if(jsonCommuArray!!.getJSONObject(i).optString("대댓글","null")!= "null"){
                getReply(i)
            }

        }
        recycler.adapter?.notifyDataSetChanged()
    }

    private fun getReply(i : Int) {

        var temp = jsonCommuArray!!.getJSONObject(i).get("대댓글").toString()
        var rereply = JSONArray(temp)

        val end = rereply.length()

        for(j in 0 until end)
            addItem(
                rereply.getJSONObject(j).get("rid") as Int, rereply.getJSONObject(j).get("reReplyContent").toString(),
                rereply.getJSONObject(j).get("writer").toString(), 1
            )
    }

    private fun addItem(id: Int, content: String, writer: String, reply: Int) {

        cnt++

        var item = CommentItem()
        item.commuId = id
        item.cWriter = cnt.toString()
        item.replycontent = content

        when(reply){
            0 -> item.isRere = false
            1 -> item.isRere = true
        }

        cList.add(item)
    }
}