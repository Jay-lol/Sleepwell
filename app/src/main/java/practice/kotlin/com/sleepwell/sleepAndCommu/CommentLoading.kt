package practice.kotlin.com.sleepwell.sleepAndCommu

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_comment.*
import practice.kotlin.com.sleepwell.recycler.CommentItem
import practice.kotlin.com.sleepwell.recycler.RecyclerItem
import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonCommuArray
import practice.kotlin.com.sleepwell.statics.commuList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList

class CommentLoading {

    fun refreshCommuList(recycler: RecyclerView) {
        cList.clear()

        var end = jsonCommuArray!!.length()
        for (i in 0 until end) {
            addItem(
                jsonCommuArray!!.getJSONObject(i).get("rid").toString().toInt(),
                jsonCommuArray!!.getJSONObject(i).get("replyContent").toString(),
                jsonCommuArray!!.getJSONObject(i).get("writter").toString(), (
                        if (jsonCommuArray!!.getJSONObject(i).optString("대댓글", "null") != "null")
                            jsonCommuArray!!.getJSONObject(i).get("대댓글").toString()
                        else
                            "null"
                        )
            )
        }
        recycler.adapter!!.notifyDataSetChanged()
    }

    private fun addItem(id: Int, content: String, writer: String, reply: String) {

        var item: CommentItem? =
            CommentItem()
        item?.commuId = id
        item?.cWriter = writer
        item?.replycontent = content

        Log.d("ddddddsad", reply)
//        item?.iconUri = icon
//        item?.titleStr = title
//        item?.writer = writer
//        item?.channelStr = linkChannel
//        item?.likeNumber = likeNumber
        cList.add(item!!)
    }
}