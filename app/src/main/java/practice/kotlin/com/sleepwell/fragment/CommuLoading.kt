package practice.kotlin.com.sleepwell.fragment

import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.RecyclerItem
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList

class CommuLoading {

    fun refreshList() {

        var end = JsonString.jsonArray!!.size()

        for (i in 1..end) {
            addItem(
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("linkUrl").asString,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("thumbnailUrl").asString,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("linkTitle").asString,
                JsonString.jsonArray!!.get(i - 1).asJsonObject.get("linkChannel").asString
            )
        }
    }

    fun addItem(linkUrl : String, icon: String, title: String, desc: String) {

        var item : RecyclerItem? = RecyclerItem()

        item?.linkUri = linkUrl
        item?.iconUri = icon
        item?.titleStr = title
        item?.descStr = desc

        mList.add(item!!)
    }
}