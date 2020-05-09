package practice.kotlin.com.sleepwell.sleepAndCommu

import android.util.Log
import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.recycler.RecyclerItem
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList

class CommuLoading {

    fun refreshList() {

        var end = JsonString.jsonArray!!.size()

        for (i in end downTo 1) {
            addItem(
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("id").asInt,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("linkUrl").asString,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("thumbnailUrl").asString,

                if(JsonString.jsonArray!!.get(i - 1).asJsonObject.get("writerTitle").asString == "null@title")
                {
                    JsonString.jsonArray!!.get(i - 1).asJsonObject.get("linkTitle").asString
                } else{
                    JsonString.jsonArray!!.get(i - 1).asJsonObject.get("writerTitle").asString
                }
                ,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("linkChannel").asString,
                JsonString.jsonArray!!.get(i-1).asJsonObject.get("writer").asString,
                (JsonString.jsonArray!!.get(i - 1).asJsonObject.get("likeCount").asInt-
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("dislikeCount").asInt)
            )
        }
    }

    fun addItem(id: Int, linkUrl: String, icon: String, title: String, linkChannel: String, writer : String , likeNumber: Int) {

        var item: RecyclerItem? =
            RecyclerItem()

        item?.id = id
        item?.linkUri = linkUrl
        item?.iconUri = icon
        item?.titleStr = title
        item?.writer = writer
        item?.channelStr = linkChannel
        item?.likeNumber = likeNumber

        mList.add(item!!)
    }

}