package practice.kotlin.com.sleepwell.sleepAndCommu

import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.recycler.RecyclerItem
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList

class CommuLoading {

    fun refreshList() {

        var end = JsonString.jsonArray!!.size()

        for (i in 1..end) {
            addItem(
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("id").asInt,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("linkUrl").asString,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("thumbnailUrl").asString,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("linkTitle").asString,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("linkChannel").asString,
                (JsonString.jsonArray!!.get(i - 1).asJsonObject.get("likeCount").asInt-
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("dislikeCount").asInt)
            )
        }
    }

    fun addItem(id: Int, linkUrl: String, icon: String, title: String, linkChannel: String, likeNumber: Int) {

        var item: RecyclerItem? =
            RecyclerItem()

        item?.id = id
        item?.linkUri = linkUrl
        item?.iconUri = icon
        item?.titleStr = title
        item?.channelStr = linkChannel
        item?.likeNumber = likeNumber

        mList.add(item!!)
    }
}